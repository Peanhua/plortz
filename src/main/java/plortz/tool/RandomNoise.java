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

import java.util.Random;
import plortz.Terrain;
import plortz.Tile;

/**
 * Adjust altitudes with random values.
 * 
 * Altitude changes are in the range [-scale, +scale].
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class RandomNoise extends Tool {

    private final double scale;
    private final Random random;
    
    public RandomNoise(double scale, Random random) {
        this.scale  = scale;
        this.random = random;
    }
    
    @Override
    public void apply(Terrain terrain) {
        for (Tile t : terrain) {
            double amount = (this.random.nextDouble() * 2.0 - 1.0) * this.scale;
            t.adjustAltitude(amount);
        }
    }
}
