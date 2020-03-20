/*
 * Copyright (C) 2020 Joni Yrjana <joniyrjana@gmail.com>
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

import plortz.Terrain;
import plortz.Tile;

/**
 * Elevates terrain to create a mountain/hill.
 * 
 * For each tile within range, calculate the increase in altitude based on distance to the center.
 * 
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class GaussianDistribution extends Tool {
    private final double center_x;
    private final double center_y;
    private final double variance;
    private final double horizontal_scale;
    private final double vertical_scale;

    public GaussianDistribution(double x, double y, double variance, double horizontal_scale, double vertical_scale) {
        this.center_x         = x;
        this.center_y         = y;
        this.variance         = variance;
        this.horizontal_scale = horizontal_scale;
        this.vertical_scale   = vertical_scale;
    }
    
    @Override
    public void apply(Terrain terrain) {
        // todo: do a circle, calculate only 1/8th of the circle, fill from inside, stop when change reaches 0
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = terrain.getTile(x, y);
                double delta_x = center_x - x;
                double delta_y = center_y - y;
                double distance = Math.sqrt(delta_x * delta_x + delta_y * delta_y) / (double) horizontal_scale;
                double change = (1.0 / (variance * Math.sqrt(2.0 * Math.PI))) * Math.exp((-0.5) * Math.pow(distance / variance, 2.0));
                t.adjustAltitude(vertical_scale * change);
            }
        }
    }
}
