/*
 * Copyright (C) 2020 Joni Yrjana <joniyrjana@gmail.com>
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
package plortz;

import java.util.Iterator;


/**
 * Container of the terrain data.
 * The terrain is a 2d grid of Tiles.
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class Terrain implements Iterable<Tile> {
    private final int    width;
    private final int    height;
    private final Tile[] tiles;
    
    public Terrain(int width, int height) {
        this.width  = width;
        this.height = height;
        this.tiles  = new Tile[width * height];
        for (int i = 0; i < width * height; i++) {
            this.tiles[i] = new Tile(TileType.DIRT, 0.0);
        }
    }
    
    public Terrain(Terrain source) {
        this.width  = source.width;
        this.height = source.height;
        this.tiles  = new Tile[width * height];
        for (int i = 0; i < width * height; i++) {
            this.tiles[i] = new Tile(source.tiles[i]);
        }
    }

    @Override
    public Iterator<Tile> iterator() {
        return new Iterator<Tile>() {
            private int pos = 0;
            
            @Override
            public boolean hasNext() {
                return pos < width * height;
            }
            
            @Override
            public Tile next() {
                Tile t = tiles[pos];
                pos++;
                return t;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("not supported");
            }
        };
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public Tile getTile(int x, int y) {
        if (!this.isValidTilePosition(x, y)) {
            return null;
        }
        int index = x + y * this.width;
        return this.tiles[index];
    }
    
    public Tile getTile(Position position) {
        if (position == null) {
            return null;
        }
        return this.getTile(position.getX(), position.getY());
    }

    public boolean isValidTilePosition(Position position) {
        if (position == null) {
            return false;
        }
        return this.isValidTilePosition(position.getX(), position.getY());
    }
    
    public boolean isValidTilePosition(int x, int y) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
            return false;
        }
        return true;
    }

    
    /**
     * Normalize altitudes to range 0..1
     */
    public void normalize() {
        double min = this.tiles[0].getAltitude(false);
        double max = this.tiles[0].getAltitude(false);
        
        for (int i = 0; i < this.width * this.height; i++) {
            double alt = this.tiles[i].getAltitude(false);
            min = Math.min(min, alt);
            max = Math.max(max, alt);
        }
        
        final double range = max - min;
        
        if (range > 0.0) {
            for (int i = 0; i < this.width * this.height; i++) {
                double alt = this.tiles[i].getAltitude(false);
                alt -= min;
                alt /= range;
                this.tiles[i].setAltitude(alt);
            }
        }
    }
}
