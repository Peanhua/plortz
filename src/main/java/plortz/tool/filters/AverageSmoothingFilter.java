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
package plortz.tool.filters;

import plortz.terrain.Terrain;
import plortz.terrain.Tile;

/**
 * Smoothing filter using averaging.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AverageSmoothingFilter extends Filter {
    private final double[] window;

    public AverageSmoothingFilter(int window_size) {
        super(window_size);
        this.window = new double[window_size * window_size];
    }

    @Override
    public double filter(Terrain terrain, int x, int y) {
        int count = 0;
        double average = 0.0;
        for (int dy = -this.half_window_size; dy <= this.half_window_size; dy++) {
            for (int dx = -this.half_window_size; dx <= this.half_window_size; dx++) {
                Tile t = terrain.getTile(x + dx, y + dy);
                if (t != null) {
                    count++;
                    average += t.getAltitude(false);
                }
            }
        }
        return average / (double) count;
    }
}
