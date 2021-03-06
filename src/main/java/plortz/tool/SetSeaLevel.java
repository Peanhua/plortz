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

/**
 * Tool to set the altitude that defines the sea level.
 * Anything below the sea level will contain water up to the sea level.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SetSeaLevel extends Tool {
    private final double level;

    public SetSeaLevel(double level) {
        this.level = level;
    }
    
    @Override
    public void apply(Terrain terrain) {
        terrain.setSeaLevel(this.level);
        terrain.changed();
    }
    
}
