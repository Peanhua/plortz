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

/**
 * Abstract base class for filters.
 * <p>
 * Smoothing filters are used to smooth the terrain altitudes.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class Filter {
    protected final int half_window_size;

    public Filter(int window_size) {
        if (window_size % 2 == 0) {
            throw new IllegalArgumentException();
        }
        this.half_window_size = window_size / 2;
    }

    public abstract double filter(Terrain terrain, int x, int y);
}
