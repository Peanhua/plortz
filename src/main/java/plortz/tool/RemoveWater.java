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
 * Removes water from the given area.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class RemoveWater extends Tool {

    @Override
    public void apply(Terrain terrain) {
        for (Tile tile : terrain) {
            tile.setWater(-1);
        }
        terrain.changed();
    }
}
