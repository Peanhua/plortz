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
import plortz.util.Static2dArray;
import plortz.util.Vector;

/**
 * Perform a smoothing filtering using edge detection to preserve edges.
 * <p>
 * This is a two-pass average filter.
 * In first pass, the edges are detected using Sobel operator.
 * In second pass, the gradient magnitudes from the Sobel operator are used as weights for the center position over the neighboring positions.
 * That is, the center position for the average filter is valued more at or near edges.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Sobel_operator">https://en.wikipedia.org/wiki/Sobel_operator</a>
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class EdgeDetectingSmoothingFilter extends Filter {
    
    private final double[] sobel_x = {
        1, 0, -1,
        2, 0, -2,
        1, 0, -1
    };
    private final double[] sobel_y = {
         1,  2,  1,
         0,  0,  0,
        -1, -2, -1
    };
    private Static2dArray<Double> edge_weights;
    private Vector                minmax;

    public EdgeDetectingSmoothingFilter(int window_size) {
        super(window_size);
        this.setPrefiltering(true);
        this.edge_weights = null;
    }

    /**
     * Fill the edge_weights using Sobel operator with values in [0, 1] where 1 is the most edgy.
     * 
     * @param terrain The terrain.
     * @param x       The current x-coordinate.
     * @param y       The current y-coordinate.
     */
    @Override
    public void preFilter(Terrain terrain, int x, int y) {
        if (this.edge_weights == null) {
            this.edge_weights = new Static2dArray<>(terrain.getWidth(), terrain.getLength(), 0.0);
            this.minmax       = terrain.getAltitudeRange();
        }
        double sx = this.applyConvolutionMatrix(terrain, x, y, this.sobel_x) / 4.0; // 1+2+1 = 4
        double sy = this.applyConvolutionMatrix(terrain, x, y, this.sobel_y) / 4.0;
        double weight = Math.sqrt(sx * sx + sy * sy);
        this.edge_weights.set(x, y, weight);
    }
    
    private double applyConvolutionMatrix(Terrain terrain, int x, int y, double[] matrix) {
        double rv = 0.0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                Tile t = terrain.getTile(x + dx, y + dy);
                if (t != null) {
                    double altitude = t.getAltitude(false);
                    altitude -= this.minmax.getX();
                    altitude /= this.minmax.getY() - this.minmax.getX();
                    rv += altitude * matrix[(dx + 1) + (dy + 1) * 3];
                }
            }
        }
        return rv;
    }
    
    @Override
    public double filter(Terrain terrain, int x, int y) {
        double edge_weight = this.edge_weights.get(x, y);
        double average = 0.0;
        int count = 0;
        for (int dy = -this.half_window_size; dy <= this.half_window_size; dy++) {
            for (int dx = -this.half_window_size; dx <= this.half_window_size; dx++) {
                Tile t = terrain.getTile(x + dx, y + dy);
                if (t != null) {
                    average += t.getAltitude(false);
                    count++;
                }
            }
        }
        average = average / (double) count;
        double center = terrain.getTile(x, y).getAltitude(false);
        return lerp(center, average, edge_weight);
    }
    
    private double lerp(double a, double b, double weight) {
        return a * weight + b * (1.0 - weight);
    }
}
