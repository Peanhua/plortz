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

/**
 * Generates positions inside a circle.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Circle {

    private final Position       center;
    private final int            radius;
    private final List<Position> positions;
    private final int            processed_size; // The number of elements in processed array is processed_size * processed_size.
    private final boolean[]      processed;      // Holds true for each position already processed, used to avoid adding same position multiple times.
    
    /**
     * Constructor.
     * 
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     */
    public Circle(Position center, int radius) {
        this.center = center;
        this.radius = radius;
        this.positions      = new ArrayList<>();
        this.processed_size = 2 * this.radius + 1; // +1 for the center
        this.processed      = new boolean[this.processed_size * this.processed_size];
        for (int dy = 0; dy <= this.radius; dy++) {
            for (int dx = dy; dx <= this.radius; dx++) {
                if ((dx * dx) + (dy * dy) <= (this.radius * this.radius)) {
                    this.add(+dx, +dy);
                    this.add(+dx, -dy);
                    this.add(-dx, +dy);
                    this.add(-dx, -dy);
                    this.add(+dy, +dx);
                    this.add(+dy, -dx);
                    this.add(-dy, +dx);
                    this.add(-dy, -dx);
                }
            }
        }
    }
    
    /**
     * Return the positions enclosed by the circle.
     * 
     * @return List of positions.
     */
    public List<Position> getPositions() {
        return this.positions;
    }
    
    private void add(int dx, int dy) {
        int pos = (this.radius + dx) + (this.radius + dy) * this.processed_size;
        if (this.processed[pos]) {
            return;
        }
        this.processed[pos] = true;
        this.positions.add(new Position(this.center, dx, dy));
    }
}
