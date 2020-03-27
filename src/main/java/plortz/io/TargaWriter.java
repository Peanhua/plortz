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
 * Writes a Truevision TGA image file of the terrain.
 * 
 * https://en.wikipedia.org/wiki/Truevision_TGA
 * http://www.paulbourke.net/dataformats/tga/
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class TargaWriter extends Writer {
    private final boolean compressed;
    
    public TargaWriter(boolean compress) {
        this.compressed = compress;
    }
    
    @Override
    protected byte[] getBytes(Terrain terrain) {
        if (terrain.getWidth() > 0xffff || terrain.getHeight() > 0xffff) {
            throw new IllegalArgumentException("Targa file can not exceed the size of 65535 pixels.");
        }

        byte[] header = this.getHeader(terrain);
        byte[] body;
        if (this.compressed) {
            body = this.getCompressedBody(terrain);
        } else {
            body = this.getBody(terrain);
        }
        
        // todo: get rid of bs
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bs.writeBytes(header);
        bs.writeBytes(body);

        return bs.toByteArray();
    }

    
    private byte[] getHeader(Terrain terrain) {
        byte[] header = new byte[18];
        header[0] = 0; // Image ID length
        header[1] = 0; // Color map type, 0 = no color map
        header[2] = (byte) (this.compressed ? 11 : 3); // Image type, 3 = uncompressed grayscale image, 11 = compressed grayscale image
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
        return header;
    }

    
    private byte getImageByte(Terrain terrain, int x, int y) {
        return (byte) (terrain.getTile(x, y).getAltitude(false) * 255.0);
    }
    
    
    /**
     * Returns the terrain in uncompressed format.
     * 
     * @param terrain
     * @return
     */
    private byte[] getBody(Terrain terrain) {
        byte[] image = new byte[terrain.getWidth() * terrain.getHeight()];
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                image[x + (terrain.getHeight() - y - 1) * terrain.getWidth()] = this.getImageByte(terrain, x, y);
            }
        }
        return image;
    }
    

    /**
     * Returns the terrain in RLE encoded format.
     * 
     * Following v2.0 TGA specification and limiting RLE packets to scanline boundaries.
     * 
     * @param terrain
     * @return 
     */
    private byte[] getCompressedBody(Terrain terrain) {
        byte[] tmp = new byte[terrain.getWidth() * terrain.getHeight() * 2]; // Workspace, reserve some extra in case compression yields very bad result
        int pos = 0;
        for (int y = 0; y < terrain.getHeight(); y++) {
            int x = 0;
            while (x < terrain.getWidth()) {
                int count = this.countNextRlePacketSizeOfSameBytes(terrain, x, y);
                if (count > 1) { // RLE encoded
                    tmp[pos++] = (byte) (128 + (count - 1));
                    tmp[pos++] = this.getImageByte(terrain, x, y);
                    x += count;
                } else { // Raw
                    count = this.countNextRlePacketSizeOfDifferentBytes(terrain, x, y);
                    tmp[pos++] = (byte) (count - 1);
                    for (int i = 0; i < count; i++) {
                        tmp[pos++] = this.getImageByte(terrain, x, y);
                        x++;
                    }
                }
            }
        }
        
        // todo: avoid copying
        byte[] image = new byte[pos];
        for (int i = 0; i < pos; i++) {
            image[i] = tmp[i];
        }
        return image;
    }

    private int countNextRlePacketSizeOfSameBytes(Terrain terrain, int start_x, int y) {
        final byte b = this.getImageByte(terrain, start_x, y);
        int count = 1;
        for (int x = start_x + 1; x < terrain.getWidth() && this.getImageByte(terrain, x, y) == b && count < 128; x++) {
            count++;
        }
        return count;
    }

    private int countNextRlePacketSizeOfDifferentBytes(Terrain terrain, int start_x, int y) {
        byte b = this.getImageByte(terrain, start_x, y);
        int count = 1;
        for (int x = start_x + 1; x < terrain.getWidth() && this.getImageByte(terrain, x, y) != b && count < 128; x++) {
            count++;
            b = this.getImageByte(terrain, x, y);
        }
        return count;
    }
}
