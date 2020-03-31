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

import plortz.Terrain;
import plortz.Tile;
import plortz.Vector;

/**
 * Rolls loose land mass downhill.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SheetErosion extends Tool {

    private Tile[]    tiles;  // All the tiles sorted by altitude (highest first).
    private boolean[] moving; // True for tiles that are rolling (decides whether to use static of kinetic friction).
    
    private int count;
    private int iteration;
    
    @Override
    public void apply(Terrain terrain) {
        
        for (int y = -10; y <= 10; y++) {
            for (int x = -10; x <= 10; x++) {
                terrain.getTile(x + terrain.getWidth() / 2, y + terrain.getHeight() / 2).setAltitude(10.0);
            }
        }
        
        iteration = 0;
        while((iteration == 0 || count > 0) && iteration < 2) {
            count = 0;
            iteration++;
        
        this.tiles = new Tile[terrain.getWidth() * terrain.getHeight()];
        int i = 0;
        for (Tile tile : terrain) {
            this.tiles[i++] = tile;
        }
        this.sortTilesByAltitude(0, tiles.length - 1);
        
        this.moving = new boolean[terrain.getWidth() * terrain.getHeight()];
        
        for (Tile tile : this.tiles) {
            Tile neighbor = this.getLowestNeighborTile(terrain, tile);
            if (neighbor != null) {
                this.erode(terrain, tile, neighbor);
            }
        }
            System.out.println("iteration=" + iteration + ": " + count);
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
    
    
    private boolean shouldErode(Terrain terrain, Tile source, Tile destination) {
        /*
        * From https://en.wikipedia.org/wiki/Angle_of_repose
        * Angle of repose is in degrees.
        */
        double angle_of_repose = source.getType().getAngleOfRepose(this.moving[source.getPosition().getX() + source.getPosition().getY() * terrain.getWidth()]);
        
        double slope = terrain.getSlope(source, destination);
        slope = Math.atan(slope); // Convert to radians
        slope = slope * 180.0 / Math.PI; // Convert to degrees
        
        System.out.print("should(aor=" + angle_of_repose + ",slope=" + slope);
        System.out.print(")");

        return angle_of_repose < -slope; // Negate slope, because downhill slope is below 0 and angle_of_repose is a positive number.
    }
    
    
    private void erode(Terrain terrain, Tile source, Tile destination) {
        /*if (!this.shouldErode(terrain, source, destination)) {
        //    return;
        }*/
        
        double angle_of_repose = source.getType().getAngleOfRepose(this.moving[source.getPosition().getX() + source.getPosition().getY() * terrain.getWidth()]);
        double slope = angle_of_repose * Math.PI / 180.0; // to radians
        slope = Math.tan(slope); // to slope
        
        Vector va = new Vector(source.getPosition());
        Vector vb = new Vector(destination.getPosition());
        double distance = Math.abs((va.subtract(vb)).getLength());
        
        // slope = altitude_change / distance
        // -> slope * distance = altitude_change
        double max_altitude_change = slope * distance;

        double target_height = destination.getAltitude(false) + max_altitude_change;
        double amount = source.getAltitude(false) - target_height;
        amount *= 0.5; // half the amount because both source and destination will change in altitude
        if (amount < 0.01) {
            return;
        }
        
        System.out.print("iteration=" + iteration + " ");
        System.out.print(source.getPosition() + ":" + destination.getPosition() + " ");
        System.out.print("old slope=" + terrain.getSlope(source, destination) + ": ");
        System.out.print(this.shouldErode(terrain, source, destination) + ": aor=" + angle_of_repose + "->" + slope + ": amount=" + amount);
        System.out.print("  heights=" + source.getAltitude(false) + "->" + destination.getAltitude(false) + ": target=" + target_height);
        count++;
        destination.adjustAltitude(amount);
        destination.setType(source.getType());

        source.adjustAltitude(-amount);
        // todo: expose (ie. set source.type) what was under the stuff that just rolled to the destination
        
        this.moving[destination.getPosition().getX() + destination.getPosition().getY() * terrain.getWidth()] = true;
        
        System.out.print(" new slope=" + terrain.getSlope(source, destination));
        System.out.print("  new heights=" + source.getAltitude(false) + "->" + destination.getAltitude(false));
        
        System.out.println("");
    }
    
    
    private Tile getLowestNeighborTile(Terrain terrain, Tile tile) {
        Tile lowest = null;
        int x = tile.getPosition().getX();
        int y = tile.getPosition().getY();
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                Tile tmp = terrain.getTile(x + dx, y + dy);
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
        }
        
        return lowest;
    }
}
