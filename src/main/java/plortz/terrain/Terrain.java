/*
 * Copyright (C) 2020 Joni Yrjana {@literal <joniyrjana@gmail.com>}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package plortz.terrain;

import plortz.util.Position;
import java.security.InvalidParameterException;
import java.util.Iterator;
import plortz.util.Vector;
import plortz.observer.Observer;
import plortz.observer.Subject;
import plortz.util.Static2dArray;


/**
 * Container of the terrain data.
 * <p>
 * The terrain is a 2d grid of Tiles.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Terrain implements Iterable<Tile> {
    private final Static2dArray<Tile> tiles;
    private final Subject             on_change;
    double                            sea_level;
    
    /**
     * Construct a new terrain with the bottom layers soil type given.
     * @param width        Width (x-axis).
     * @param length       Length (y-axis).
     * @param bottom_layer The soil type of the bottom layer.
     */
    public Terrain(int width, int length, SoilLayer.Type bottom_layer) {
        this.tiles  = new Static2dArray<>(width, length);
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                this.tiles.set(x, y, new Tile(new Position(x, y), bottom_layer, 1.0));
            }
        }
        this.on_change = new Subject();
        this.sea_level = -1;
    }
    
    /**
     * Construct a new terrain.
     * @param width  Width (x-axis).
     * @param length Length (y-axis).
     */
    public Terrain(int width, int length) {
        this(width, length, SoilLayer.Type.CLIFF);
    }
    
    /**
     * Copy constructor.
     * @param source The source terrain to copy from.
     */
    public Terrain(Terrain source) {
        this.tiles  = new Static2dArray<>(source.getWidth(), source.getLength());
        for (int i = 0; i < this.tiles.getWidth() * this.tiles.getLength(); i++) {
            this.tiles.set(i, new Tile(source.tiles.get(i)));
        }
        this.on_change = new Subject();
    }

    /**
     * Return an iterator that iterates over all the tiles in the terrain.
     * @return The iterator.
     */
    @Override
    public Iterator<Tile> iterator() {
        return this.tiles.iterator();
    }

    /**
     * Register an observer to be called whenever this terrain object is changed.
     * 
     * @param observer The observer object to be called upon change.
     */
    public void listenOnChange(Observer observer) {
        this.on_change.addObserver(observer);
    }
    
    /**
     * Cause all the onChange listeners to be called.
     */
    public void changed() {
        this.on_change.notifyObservers();
    }
    
    public int getWidth() {
        return this.tiles.getWidth();
    }
    
    public int getLength() {
        return this.tiles.getLength();
    }
    
    public Tile getTile(int x, int y) {
        if (!this.tiles.isValidPosition(x, y)) {
            return null;
        }
        return this.tiles.get(x, y);
    }
    
    public Tile getTile(Position position) {
        if (!this.tiles.isValidPosition(position)) {
            return null;
        }
        return this.tiles.get(position);
    }
    
    public double getSeaLevel() {
        return this.sea_level;
    }
    
    public void setSeaLevel(double sea_level) {
        // If the new sea level is lower, remove all current sea first:
        if (sea_level < this.sea_level) {
            for (var tile : this.tiles) {
                if (tile.getAltitude(true) <= this.sea_level) {
                    tile.setWater(-1);
                }
            }
        }
        this.sea_level = sea_level;
        if (this.sea_level > 0.0) {
            for (var tile : this.tiles) {
                final double water = this.sea_level - tile.getAltitude(false);
                if (water > 0.0) {
                    tile.setWater(water);
                }
            }
        }
    }
    
    /**
     * Replace the tile at the given position.
     * @param position The position of the tile.
     * @param tile     The new tile for the given position.
     */
    public void setTile(Position position, Tile tile) {
        if (tile == null || !this.tiles.isValidPosition(position)) {
            throw new InvalidParameterException();
        }
        this.tiles.set(position, tile);
        tile.setPosition(position);
        this.changed();
    }

    /**
     * Checks whether the given position is valid for this terrain.
     * @param position The position to check.
     * @return         True if the position is valid for this terrain.
     */
    public boolean isValidTilePosition(Position position) {
        return this.tiles.isValidPosition(position);
    }
    
    /**
     * Checks whether the given position is valid for this terrain.
     * @param x The x-axis of the position.
     * @param y The y-axis of the position.
     * @return  True if the position is valid for this terrain.
     */
    public boolean isValidTilePosition(int x, int y) {
        return this.tiles.isValidPosition(x, y);
    }

    
    /**
     * Returns a vector containing the minimum and maximum altitude of the terrain.
     * 
     * @return Vector whose X -component contains the minimum altitude, and Y contains the maximum.
     */
    public Vector getAltitudeRange() {
        double min = this.tiles.get(0).getAltitude(false);
        double max = this.tiles.get(0).getAltitude(false);
        
        for (int i = 0; i < this.tiles.getWidth() * this.tiles.getLength(); i++) {
            double alt = this.tiles.get(i).getAltitude(false);
            if (alt < min) {
                min = alt;
            }
            if (alt > max) {
                max = alt;
            }
        }
        
        return new Vector(min, max);
    }
    

    /**
     * Raise the bottom soil layer so that it doesn't contain negative amounts.
     * <p>
     * Certain tools require/assume that every soil layer has a positive amount,
     * but the changes made to the terrain will occasionally invalidate this.
     * 
     * This method fixes the situation by adding to the bottom layer of all tiles.
     * 
     * All tools that have the potential to adjust the soil amounts with a negative amount must call this afterwards.
     */
    public void zeroBottomSoilLayer() {
        double minamount = 0.0;
        for (int i = 0; i < this.tiles.getWidth() * this.tiles.getLength(); i++) {
            double amt = this.tiles.get(i).getBottomSoil().getAmount();
            if (amt < minamount) {
                minamount = amt;
            }
        }
        if (minamount < 0.0) {
            minamount = -minamount;
            for (int i = 0; i < this.tiles.getWidth() * this.tiles.getLength(); i++) {
                this.tiles.get(i).getBottomSoil().adjustAmount(minamount);
            }
        }
    }
}
