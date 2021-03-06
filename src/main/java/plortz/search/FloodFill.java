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
package plortz.search;

import java.util.ArrayList;
import java.util.List;
import plortz.util.Position;
import plortz.util.Static2dArray;

/**
 * Performs a flood fill discovering the filled area and the borders around it.
 * <p>
 * Does not fill diagonal positions, but the resulting borders do include diagonal positions.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class FloodFill {
    
    /**
     * Callback functions used with FloodFill.
     */
    public interface FloodFillCallback {
        /**
         * Check whether the given position should be filled.
         * 
         * @param position The position to check.
         * @return         True if the given position should be filled.
         */
        public boolean isPositionIn(Position position);
        
        /**
         * Called when a position is filled.
         * <p>
         * This will be called at most one time per position.
         * @param position The position to fill.
         */
        public void fill(Position position);
    }
    
    private Static2dArray<Boolean> filled;
    private Static2dArray<Boolean> borders;
    private List<Position>         queue;
    private int                    width;
    private int                    length;
    
    private boolean checkPosition(Position position, FloodFillCallback callback) {
        if (!this.filled.isValidPosition(position)) {
            return false;
        }
        return callback.isPositionIn(position);
    }
    
    /**
     * Return a list of positions that are considered as borders for the filled area.
     * <p>
     * Contains diagonal positions, for example:
     * <pre>
     *  #####
     *  #bbb#
     *  #bfb#
     *  #bbb#
     *  #####
     * </pre>
     * The "b"'s are returned,
     * the "f"'s are the filled positions,
     * and the "#"'s are the not filled positions.
     * 
     * @return The positions of the border elements.
     */
    public List<Position> getBorders() {
        List<Position> rv = new ArrayList<>();
        for (int y = 0; y < this.length; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.borders.get(x, y) != null) {
                    if (this.filled.get(x, y) == null) {
                        rv.add(new Position(x, y));
                    }
                }
            }
        }
        return rv;
    }
    
    /**
     * Flood fill area inside a 2d rectangle space, does not extend in diagonal directions.
     * <p>
     * Implementation details:
     * <ul>
     * <li>Uses scanline fill.
     * <li>Uses a queue to find the next scanline to scan.
     * <li>When processing a scanline, lines above and below are checked if they can be filled.
     * <li>The newly found fillable areas are then added to the queue.
     * </ul>
     * 
     * @see <a href="https://en.wikipedia.org/wiki/Flood_fill#Scanline_fill">https://en.wikipedia.org/wiki/Flood_fill#Scanline_fill</a>
     * @param width    Width of the area.
     * @param length   Length of the area.
     * @param start    The starting position.
     * @param callback The callback object.
     */
    public void fill(int width, int length, Position start, FloodFillCallback callback) {
        this.width   = width;
        this.length  = length;
        this.filled  = new Static2dArray<>(width, length);
        this.borders = new Static2dArray<>(width, length);
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
            
            if (this.filled.get(curpos) == null) {
                this.filled.set(curpos, true);
                this.setBorders(curpos);
                callback.fill(curpos);
            }

            final var above = this.checkPosition(new Position(start, dx, -1), callback);
            final var below = this.checkPosition(new Position(start, dx,  1), callback);

            if (!above_filling && above) {
                final var above_pos = new Position(start, dx, -1);
                if (this.filled.get(above_pos) == null) {
                    this.queue.add(above_pos);
                }
            }
            if (!below_filling && below) {
                final var below_pos = new Position(start, dx, 1);
                if (this.filled.get(below_pos) == null) {
                    this.queue.add(below_pos);
                }
            }

            above_filling = above;
            below_filling = below;
        }
    }

    /**
     * Set the tiles around the given position as borders.
     * Will be later used in getBorders().
     * 
     * @param around The filled position whose borders needs to be set.
     */
    private void setBorders(Position around) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                if (dx != 0 && dy != 0) { // Skip diagonal
                    continue;
                }
                final int x = around.getX() + dx;
                final int y = around.getY() + dy;
                if (!this.borders.isValidPosition(x, y)) {
                    continue;
                }
                this.borders.set(x, y, true);
            }
        }
    }
}
