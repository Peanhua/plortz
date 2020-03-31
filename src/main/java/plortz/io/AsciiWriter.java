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
package plortz.io;

import java.io.ByteArrayOutputStream;
import plortz.terrain.Terrain;

/**
 * Writes ascii representation of the terrain.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AsciiWriter extends Writer {

    @Override
    protected byte[] getBytes(Terrain terrain) {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        byte[] newline = { '\n' };
        byte[] buf = new byte[128];
        
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                String s = String.format("%4.2f ", terrain.getTile(x, y).getAltitude(false));
                char[] chars = s.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    buf[i] = (byte) chars[i];
                }
                bs.write(buf, 0, chars.length);
            }
            bs.writeBytes(newline);
        }
        
        return bs.toByteArray();
    }
}
