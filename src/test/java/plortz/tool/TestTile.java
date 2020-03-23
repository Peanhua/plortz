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

import plortz.Tile;
import plortz.TileType;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
class TestTile extends Tile {

    public int altitude_adjust_counter;

    public TestTile(TileType type, double altitude) {
        super(type, altitude);
        this.altitude_adjust_counter = 0;
    }

    public TestTile(Tile tile) {
        super(tile);
        this.altitude_adjust_counter = 0;
    }

    @Override
    public void adjustAltitude(double altitude) {
        super.setAltitude(altitude);
        this.altitude_adjust_counter++;
    }
}
