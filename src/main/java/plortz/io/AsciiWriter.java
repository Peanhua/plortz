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
package plortz.io;

import java.io.OutputStream;
import java.io.PrintStream;
import plortz.Terrain;

/**
 * Writes ascii representation of the terrain.
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class AsciiWriter extends Writer {

    @Override
    public boolean write(Terrain terrain, OutputStream output) {
        PrintStream ps = (PrintStream) output;
        if(ps == null)
            return false;
        ps.println(terrain.getTile(0, 0));
        ps.println(terrain.getTile(0, 0).getAltitude(false));
        for(int y = 0; y < terrain.getHeight(); y++) {
            for(int x = 0; x < terrain.getWidth(); x++) {
                ps.printf("%4.2f ", terrain.getTile(x, y).getAltitude(false));
            }
            ps.println();
        }
        return true;
    }
}
