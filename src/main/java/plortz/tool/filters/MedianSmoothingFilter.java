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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;

/**
 * Smoothing filter using median.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class MedianSmoothingFilter extends Filter {
    private final List<Double>      window;
    private final List<Double>      tmp_window;

    public MedianSmoothingFilter(int window_size) {
        super(window_size);
        this.window = new ArrayList<>();
        this.tmp_window = new ArrayList<>();
        // Reserve space for the temporary array required by the sort:
        for (int i = 0; i < window_size * window_size; i++) {
            this.tmp_window.add(0.0);
        }
    }

    @Override
    public double filter(Terrain terrain, int x, int y) {
        window.clear();
        for (int dy = -this.half_window_size; dy <= this.half_window_size; dy++) {
            for (int dx = -this.half_window_size; dx <= this.half_window_size; dx++) {
                Tile t = terrain.getTile(x + dx, y + dy);
                if (t != null) {
                    this.window.add(t.getAltitude(false));
                }
            }
        }
        Collections.sort(this.window);
        return this.window.get(this.window.size() / 2);
    }
}
