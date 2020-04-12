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

import plortz.util.Position;
import plortz.terrain.Tile;
import plortz.terrain.SoilLayer;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class TestTile extends Tile {

    public int altitude_adjust_counter;

    public TestTile(Position pos, SoilLayer.Type type, double altitude) {
        super(pos, type, altitude);
        this.altitude_adjust_counter = 0;
    }

    public TestTile(Tile tile) {
        super(tile);
        this.altitude_adjust_counter = 0;
    }

    @Override
    public void adjustTopSoilAmount(double altitude) {
        super.adjustTopSoilAmount(altitude);
        this.altitude_adjust_counter++;
    }
}
