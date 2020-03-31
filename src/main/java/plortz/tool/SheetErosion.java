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
package plortz.tool;

import java.util.List;
import java.util.Random;
import plortz.Position;
import plortz.Terrain;
import plortz.Tile;
import plortz.collections.MyArrayList;

/**
 * Rolls loose land mass downhill.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SheetErosion extends Tool {

    private final Random   random;
    private Tile[]         tiles;  // All the tiles sorted by altitude (highest first).
    private boolean[]      moving; // True for tiles that are rolling (decides whether to use static of kinetic friction).
    private List<Position> neighbor_offsets; // Randomized offsets to the neighbor tiles

    public SheetErosion(Random random) {
        this.random = random;
    }
    
    @Override
    public void apply(Terrain terrain) {
        this.setupNeighborOffsets(terrain);
        this.setupTiles(terrain);
        this.moving = new boolean[terrain.getWidth() * terrain.getHeight()];
        
        for (Tile tile : this.tiles) {
            Tile neighbor = this.getLowestNeighborTile(terrain, tile);
            if (neighbor != null) {
                this.erode(terrain, tile, neighbor);
            }
        }
    }
    
    private void setupTiles(Terrain terrain) {
        this.tiles = new Tile[terrain.getWidth() * terrain.getHeight()];
        int i = 0;
        for (Tile tile : terrain) {
            this.tiles[i++] = tile;
        }
        
        this.sortTilesByAltitude(0, this.tiles.length - 1);
    }
    
    private void setupNeighborOffsets(Terrain terrain) {
        this.neighbor_offsets = new MyArrayList<>(Position.class);
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (y == 0 && x == 0) {
                    continue;
                }
                this.neighbor_offsets.add(new Position(x, y));
            }
        }
        // Shuffle them:
        for (int i = 0; i < this.neighbor_offsets.size(); i++) {
            int swap_with = this.random.nextInt(this.neighbor_offsets.size());
            if (i != swap_with) {
                Position tmp = this.neighbor_offsets.get(i);
                this.neighbor_offsets.set(i, this.neighbor_offsets.get(swap_with));
                this.neighbor_offsets.set(swap_with, tmp);
            }
        }
    }
    
    /**
     * Sort this.tiles by altitude, highest altitude first.
     * 
     * Uses quicksort algorithm.
     * The recursion goes too deep (resulting in stack overflow) with empty terrain -> switch to merge sort
     * 
     * @param start
     * @param end 
     */
    private void sortTilesByAltitude(int start, int end) {
        if (start >= end) {
            return;
        }
        int middle = this.sortTilesByAltitudeSplit(start, end);
        this.sortTilesByAltitude(start, middle - 1);
        this.sortTilesByAltitude(middle + 1, end);
    }
    
    private int sortTilesByAltitudeSplit(int start, int end) {
        int middle = start;
        for (int i = start + 1; i <= end; i++) {
            if (this.tiles[i].getAltitude(false) > this.tiles[start].getAltitude(false)) {
                middle++;
                Tile tmp = this.tiles[i];
                this.tiles[i] = this.tiles[middle];
                this.tiles[middle] = tmp;
            }
        }
        Tile tmp = this.tiles[start];
        this.tiles[start] = this.tiles[middle];
        this.tiles[middle] = tmp;
        return middle;
    }
    
    
    private void erode(Terrain terrain, Tile source, Tile destination) {
        double angle_of_repose = source.getType().getAngleOfRepose(this.moving[source.getPosition().getX() + source.getPosition().getY() * terrain.getWidth()]);
        double slope = angle_of_repose * Math.PI / 180.0; // to radians
        slope = Math.tan(slope); // to slope
        
        double distance = source.getDistance(destination);
        
        // slope = altitude_change / distance
        // -> slope * distance = altitude_change
        double max_altitude_change = slope * distance;

        double target_height = destination.getAltitude(false) + max_altitude_change;

        double amount = source.getAltitude(false) - target_height;
        amount *= 0.5; // half the amount because both source and destination will change in altitude
        if (amount < 0.01) {
            return;
        }
        
        destination.adjustAltitude(amount);
        destination.setType(source.getType());

        source.adjustAltitude(-amount);
        // todo: expose (ie. set source.type) what was under the stuff that just rolled to the destination
        
        this.moving[destination.getPosition().getX() + destination.getPosition().getY() * terrain.getWidth()] = true;
    }
    
    
    private Tile getLowestNeighborTile(Terrain terrain, Tile tile) {
        Tile lowest = null;
        int x = tile.getPosition().getX();
        int y = tile.getPosition().getY();
        for (Position offset : this.neighbor_offsets) {
            Tile tmp = terrain.getTile(x + offset.getX(), y + offset.getY());
            if(tmp == null) {
                continue;
            }
            if (tmp.getAltitude(false) < tile.getAltitude(false)) {
                if (lowest == null || tmp.getAltitude(false) < lowest.getAltitude(false)) {
                    // todo: change to get the tile where the slope is the most steep (downhill)?
                    lowest = tmp;
                }
            }
        }
        
        return lowest;
    }
}
