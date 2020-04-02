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

import plortz.terrain.Terrain;
import plortz.terrain.Tile;

/**
 * Tool to smooth the altitudes of the terrain.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SmoothAltitudes extends Tool {

    private interface Filter {
        public double filter(Terrain terrain, int x, int y);
    }
    
    private class MedianFilter implements Filter {
        private final int half_window_size;
        private double[]  window;
        
        public MedianFilter(int window_size) {
            if (window_size % 2 == 0) {
                throw new IllegalArgumentException();
            }
            this.half_window_size = window_size / 2;
            this.window = new double[window_size * window_size];
        }
        
        @Override
        public double filter(Terrain terrain, int x, int y) {
            int count = 0;
            double median = 0.0;
            for (int dy = -this.half_window_size; dy <= this.half_window_size; dy++) {
                for (int dx = -this.half_window_size; dx <= this.half_window_size; dx++) {
                    Tile t = terrain.getTile(x + dx, y + dy);
                    if (t != null) {
                        count++;
                        median += t.getAltitude(false);
                    }
                }
            }
            return median / (double) count;
        }
    }
    
    private double[] new_amounts;
    private Filter   filter;
    
    public SmoothAltitudes() {
        this.filter = new MedianFilter(3);
    }
    
    @Override
    public void apply(Terrain terrain) {
        this.new_amounts = new double[terrain.getWidth() * terrain.getHeight()];
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                this.new_amounts[x + y * terrain.getWidth()] = this.filter.filter(terrain, x, y);
            }
        }
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                terrain.getTile(x, y).setTopSoilAmount(this.new_amounts[x + y * terrain.getWidth()]);
            }
        }
        terrain.zeroBottomSoilLayer();
        terrain.changed();
    }
}
