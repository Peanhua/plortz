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

import java.util.List;
import plortz.MergeSort;
import plortz.collections.MyArrayList;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.tool.smoothing_filters.SmoothingFilter;

/**
 * Tool to smooth the altitudes of the terrain.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SmoothAltitudes extends Tool {
    
    private double[]              new_amounts;
    private final SmoothingFilter filter;
    
    public SmoothAltitudes(SmoothingFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public void apply(Terrain terrain) {
        this.new_amounts = new double[terrain.getWidth() * terrain.getLength()];
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                this.new_amounts[x + y * terrain.getWidth()] = this.filter.filter(terrain, x, y);
            }
        }
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                terrain.getTile(x, y).setTopSoilAmount(this.new_amounts[x + y * terrain.getWidth()]);
            }
        }
        terrain.zeroBottomSoilLayer();
        terrain.changed();
    }
}
