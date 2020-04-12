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
 * Elevates terrain to create a mountain/hill.
 * <p>
 * For each tile within range, calculate the change in altitude based on distance to the center using Gaussian distribution.
 * <p>
 * Altitude changes are in the range [0, vertical_scale].
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class GaussianDistribution extends Tool {
    private final int    center_x;
    private final int    center_y;
    private final double variance;
    private final double gauss_factor;
    private final double horizontal_scale;
    private final double vertical_scale;
    // Temporary working variables:
    private boolean[]    processed;      // Holds true for each tile already processed, used to avoid processing same tile multiple times.
    private int          processed_size; // The number of elements in processed array is processed_size * processed_size.

    
    public GaussianDistribution(int x, int y, double variance, double horizontal_scale, double vertical_scale) {
        this.center_x         = x;
        this.center_y         = y;
        this.variance         = variance;
        this.gauss_factor     = 1.0 / (variance * Math.sqrt(2.0 * Math.PI));
        this.horizontal_scale = horizontal_scale;
        this.vertical_scale   = vertical_scale;
    }
    
    
    @Override
    public void apply(Terrain terrain) {
        int radius = calculateRadius(0.00001);

        // Make sure the circle drawing routine adjusts each tile only once using a 2d boolean array:
        this.processed_size = 2 * radius + 1; // +1 for the center
        this.processed      = new boolean[this.processed_size * this.processed_size];
        
        // Process the circle, divided into 8 identical sections:
        for (int dy = 0; dy <= radius; dy++) {
            for (int dx = dy; dx <= radius; dx++) {
                double distance = Math.sqrt(dx * dx + dy * dy) / this.horizontal_scale;
                double change = this.getAltitudeChangeForDistance(distance);
                if (change > 0.00001) {
                    double scaled_change = change * vertical_scale;
                    this.adjustAltitude(terrain, radius, +dx, +dy, scaled_change);
                    this.adjustAltitude(terrain, radius, +dx, -dy, scaled_change);
                    this.adjustAltitude(terrain, radius, -dx, +dy, scaled_change);
                    this.adjustAltitude(terrain, radius, -dx, -dy, scaled_change);
                    this.adjustAltitude(terrain, radius, +dy, +dx, scaled_change);
                    this.adjustAltitude(terrain, radius, +dy, -dx, scaled_change);
                    this.adjustAltitude(terrain, radius, -dy, +dx, scaled_change);
                    this.adjustAltitude(terrain, radius, -dy, -dx, scaled_change);
                }
            }
        }
        
        if (this.vertical_scale < 0.0) {
            terrain.zeroBottomSoilLayer();
        }
        terrain.changed();
    }
    
    private double getAltitudeChangeForDistance(double distance) {
        return this.gauss_factor * Math.exp((-0.5) * Math.pow(distance / this.variance, 2.0));
    }


    private void adjustAltitude(Terrain terrain, int radius, int dx, int dy, double change) {
        int pos = (radius + dx) + (radius + dy) * processed_size;
        if (this.processed[pos]) {
            return;
        }
        this.processed[pos] = true;
        Tile t = terrain.getTile(this.center_x + dx, this.center_y + dy);
        if (t != null) {
            t.adjustTopSoilAmount(this.vertical_scale * change);
        }
    }
    
    
    /**
     * Find the radius of the circle containing the affected tiles.
     * 
     * @param altitude_change_threshold When the change goes below this threshold value, the radius has been found.
     */
    private int calculateRadius(double altitude_change_threshold) {
        Integer radius = null;
        for (int i = 0; radius == null; i++) {
            double distance = Math.sqrt(i * i) / this.horizontal_scale;
            double change = this.getAltitudeChangeForDistance(distance);
            if (change < altitude_change_threshold) {
                radius = i;
            }
        }
        return radius;
    }
}
