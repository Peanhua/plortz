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
 * Filters are used to adjust the terrain altitudes.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class Filter {
    protected final int half_window_size;
    private boolean     prefilter;

    /**
     * The size of the window to operate on, must be a non-even number.
     * @param window_size The size of the window.
     */
    public Filter(int window_size) {
        if (window_size % 2 == 0) {
            throw new IllegalArgumentException();
        }
        this.half_window_size = window_size / 2;
        this.prefilter        = false;
    }
    
    /**
     * Enable/disable prefiltering process.
     * @param enabled True to enable prefiltering process.
     */
    protected final void setPrefiltering(boolean enabled) {
        this.prefilter = enabled;
    }
    
    /**
     * Return whether the pre-filtering is enabled or not.
     * 
     * @return True if the pre-filtering is enabled.
     */
    public boolean isPrefiltering() {
        return this.prefilter;
    }
    
    /**
     * Perform the pre-filtering operation.
     * <p>
     * Pre-filtering is performed before the actual filtering,
     * no changes to the terrain are made in pre-filtering process.
     * 
     * @param terrain The terrain.
     * @param x       The x-coordinate of the current position.
     * @param y       The y-coordinate of the current position.
     */
    public void preFilter(Terrain terrain, int x, int y) {
    }

    /**
     * Perform the filtering operation.
     * 
     * @param terrain The terrain.
     * @param x       The x-coordinate of the current position.
     * @param y       The y-coordinate of the current position.
     * @return        The new altitude value.
     */
    public abstract double filter(Terrain terrain, int x, int y);
}
