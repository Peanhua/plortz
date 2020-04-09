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
package plortz.util;

import java.util.List;
import plortz.terrain.Position;

/**
 * Performs a flood fill discovering the filled area and the borders around it.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class FloodFill {
    
    public interface FloodFillCallback {
        public boolean isPositionIn(Position position);
        public void    fill(Position position);
    }
    
    private Static2dArray<Boolean> filled;
    private List<Position>         queue;
    private int                    width;
    private int                    length;
    
    private boolean checkPosition(Position position, FloodFillCallback callback) {
        if (!this.filled.isValidPosition(position)) {
            return false;
        }
        return callback.isPositionIn(position);
    }
    
    public List<Position> getBorders() {
        List<Position> borders = new ArrayList<>();
        for (int y = 0; y < this.length; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.filled.get(x, y)) {
                    continue;
                }
                if (this.isBorder(x, y)) {
                    borders.add(new Position(x, y));
                }
            }
        }
        return borders;
    }
    
    private boolean isBorder(int x, int y) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                if (dx != 0 && dy != 0) { // Skip diagonal
                    continue;
                }
                if (!this.filled.isValidPosition(x + dx, y + dy)) {
                    continue;
                }
                if (this.filled.get(x + dx, y + dy)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Flood fill area inside a 2d rectangle space.
     * <p>
     * Uses scanline fill: https://en.wikipedia.org/wiki/Flood_fill#Scanline_fill
     * Uses a queue to find the next scanline to scan.
     * When processing a scanline, lines above and below are checked if they can be filled.
     * The newly found fillable areas are then added to the queue.
     * 
     * @param width    Width of the area.
     * @param length   Length of the area.
     * @param start    The starting position.
     * @param callback The callback object.
     */
    public void fill(int width, int length, Position start, FloodFillCallback callback) {
        this.width   = width;
        this.length  = length;
        this.filled  = new Static2dArray<>(width, length, false);
        this.queue   = new ArrayList<>();
        
        this.queue.add(start);
        while (!this.queue.isEmpty()) {
            Position current = this.queue.get(this.queue.size() - 1);
            this.queue.remove(this.queue.size() - 1);
            // Fill this scanline to the left:
            this.fillScanlineHalf(current, -1, callback);
            // Fill this scanline to the right:
            this.fillScanlineHalf(current,  1, callback);
        }
    }
    
    /**
     * Fill half of the given scanline, either left or right from the given starting position.
     * 
     * @param start     Starting position.
     * @param direction Direction, either -1 for left, or +1 for right.
     * @param callback  The callback object.
     */
    private void fillScanlineHalf(Position start, int direction, FloodFillCallback callback) {
        boolean above_filling = false;
        boolean below_filling = false;
        for (int x = 0; x < this.width; x++) {
            int dx = x * direction;
            Position curpos = new Position(start, dx, 0);

            if (!this.checkPosition(curpos, callback)) {
                break;
            }
            
            if (!this.filled.get(curpos)) {
                this.filled.set(curpos, true);
                callback.fill(curpos);
            }

            boolean above = this.checkPosition(new Position(start, dx, -1), callback);
            boolean below = this.checkPosition(new Position(start, dx,  1), callback);

            if (!above_filling && above) {
                Position above_pos = new Position(start, dx, -1);
                if (!this.filled.get(above_pos)) {
                    this.queue.add(above_pos);
                }
            }
            if (!below_filling && below) {
                Position below_pos = new Position(start, dx, 1);
                if (!this.filled.get(below_pos)) {
                    this.queue.add(below_pos);
                }
            }

            above_filling = above;
            below_filling = below;
        }
    }
}
