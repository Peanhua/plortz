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

import java.io.ByteArrayOutputStream;
import plortz.Terrain;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class TargaWriter extends Writer {
    @Override
    protected byte[] getBytes(Terrain terrain) {
        if(terrain.getWidth() > 0xffff || terrain.getHeight() > 0xffff)
            throw new IllegalArgumentException("Targa file can not exceed the size of 65535 pixels.");
        
        byte[] header = new byte[18];
        header[0] = 0; // Image ID length
        header[1] = 0; // Color map type, 0 = no color map
        header[2] = 3; // Image type, 3 = uncompressed grayscale image
        header[8] = 0; // x-origin (2 bytes)
        header[9] = 0;
        header[10] = 0; // y-origin
        header[11] = 0;
        header[12] = (byte) (terrain.getWidth() & 0xff); // Image width, least significant byte
        header[13] = (byte) (terrain.getWidth() >> 8);
        header[14] = (byte) (terrain.getHeight() & 0xff); // Image height
        header[15] = (byte) (terrain.getHeight() >> 8);
        header[16] = 8; // Pixel depth
        header[17] = 0; // Image descriptor
        
        byte[] image = new byte[terrain.getWidth() * terrain.getHeight()];
        for(int y = 0; y < terrain.getHeight(); y++)
            for(int x = 0; x < terrain.getWidth(); x++)
                image[x + (terrain.getHeight() - y - 1) * terrain.getWidth()] = (byte) (terrain.getTile(x, y).getAltitude(false) * 255.0);

        // todo: get rid of bs
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bs.writeBytes(header);
        bs.writeBytes(image);
        return bs.toByteArray();
    }
}
