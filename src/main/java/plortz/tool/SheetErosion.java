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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import plortz.util.Position;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;

/**
 * Rolls loose land mass downhill.
 * <p>
 * Performs sheet erosion on the top-most soil using the angle of repose.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Sheet_erosion">https://en.wikipedia.org/wiki/Sheet_erosion</a>
 * @see <a href="https://en.wikipedia.org/wiki/Angle_of_repose">https://en.wikipedia.org/wiki/Angle_of_repose</a>
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SheetErosion extends Tool {

    private final Random   random;
    private Tile[]         tiles;  // All the tiles sorted by altitude (highest first).
    private boolean[]      moving; // True for tiles that are rolling (decides whether to use static of kinetic friction).
    private List<Position> neighbor_offsets; // Randomized offsets to the neighbor tiles
    private Tile[]         tmp_tiles;

    public SheetErosion(Random random) {
        this.random = random;
    }
    
    @Override
    public void apply(Terrain terrain) {
        this.setupNeighborOffsets(terrain);
        this.setupTiles(terrain);
        this.moving = new boolean[terrain.getWidth() * terrain.getLength()];
        
        for (Tile tile : this.tiles) {
            Tile neighbor = this.getLowestNeighborTile(terrain, tile);
            if (neighbor != null) {
                this.erode(terrain, tile, neighbor);
            }
        }
        terrain.zeroBottomSoilLayer();
        terrain.changed();
    }
    
    private void setupTiles(Terrain terrain) {
        this.tiles = new Tile[terrain.getWidth() * terrain.getLength()];
        this.tmp_tiles = new Tile[terrain.getWidth() * terrain.getLength()];
        int i = 0;
        for (Tile tile : terrain) {
            this.tiles[i++] = tile;
        }
        
        this.sortTilesByAltitude(0, this.tiles.length - 1);
    }
    
    private void setupNeighborOffsets(Terrain terrain) {
        this.neighbor_offsets = new ArrayList<>();
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
     * Uses merge sort algorithm.
     * 
     * @param start
     * @param end 
     */
    private void sortTilesByAltitude(int start, int end) {
        if (start == end) {
            return;
        }
        int middle = (start + end) / 2;
        this.sortTilesByAltitude(start, middle);
        this.sortTilesByAltitude(middle + 1, end);
        
        this.sortTilesByAltitudeMerge(start, middle, middle + 1, end);
    }
    
    private void sortTilesByAltitudeMerge(int start1, int end1, int start2, int end2) {
        int pos1 = start1;
        int pos2 = start2;
        for (int i = start1; i <= end2; i++) {
            if (pos2 > end2 || (pos1 <= end1 && this.tiles[pos1].getAltitude(false) > this.tiles[pos2].getAltitude(false))) {
                this.tmp_tiles[i] = this.tiles[pos1];
                pos1++;
            } else {
                this.tmp_tiles[i] = this.tiles[pos2];
                pos2++;
            }
        }
        for (int i = start1; i <= end2; i++) {
            this.tiles[i] = this.tmp_tiles[i];
        }
    }
    
    
    
    private void erode(Terrain terrain, Tile source, Tile destination) {
        double angle_of_repose = source.getTopSoil().getAngleOfRepose(this.moving[source.getPosition().getX() + source.getPosition().getY() * terrain.getWidth()]);
        if (angle_of_repose > 90.0) {
            return;
        }
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

        double available_amount = source.getTopSoil().getAmount();
        if (amount > available_amount) {
            amount = available_amount;
        }

        destination.addSoil(source.getTopSoil().getType(), amount);
        source.adjustTopSoilAmount(-amount);
        
        this.moving[destination.getPosition().getX() + destination.getPosition().getY() * terrain.getWidth()] = true;
    }
    
    
    private Tile getLowestNeighborTile(Terrain terrain, Tile tile) {
        Tile lowest = null;
        int x = tile.getPosition().getX();
        int y = tile.getPosition().getY();
        for (Position offset : this.neighbor_offsets) {
            Tile tmp = terrain.getTile(x + offset.getX(), y + offset.getY());
            if (tmp == null) {
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
