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
import plortz.util.Vector;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;

/**
 * Writes a Truevision TGA image file of the terrain.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Truevision_TGA">https://en.wikipedia.org/wiki/Truevision_TGA</a>
 * @see <a href="http://www.paulbourke.net/dataformats/tga/">http://www.paulbourke.net/dataformats/tga/</a>
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class TargaWriter extends Writer {
    private final boolean compressed;
    private final boolean heights;
    private final boolean colors;
    private Vector        minmax;
    
    /**
     * Constructor.
     * 
     * @param compress If true, the Targa file will be compressed.
     * @param heights  If true, the altitude affects the brightness of each pixel.
     * @param colors   If true, the bits per pixel is 24, and each pixel is the altitude colored by the top-most soil type.
     *                 If false, the bits per pixel is 8, and each pixel is the altitude.
     */
    public TargaWriter(boolean compress, boolean heights, boolean colors) {
        this.compressed = compress;
        this.heights    = heights;
        this.colors     = colors;
        this.minmax     = new Vector(0, 1);
    }
    
    @Override
    protected byte[] getBytes(Terrain terrain) {
        if (terrain.getWidth() > 0xffff || terrain.getLength() > 0xffff) {
            throw new IllegalArgumentException("Targa file can not exceed the size of 65535 pixels.");
        }

        this.minmax = terrain.getAltitudeRange();

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
        header[2] = (byte) ((this.colors ? 2 : 3) + (this.compressed ? 8 : 0)); // Image type, 2 = uncompressed rgb image, 3 = uncompressed grayscale image, 10 = compressed rgb image, 11 = compressed grayscale image
        header[8] = 0; // x-origin (2 bytes)
        header[9] = 0;
        header[10] = 0; // y-origin
        header[11] = 0;
        header[12] = (byte) (terrain.getWidth() & 0xff); // Image width, least significant byte
        header[13] = (byte) (terrain.getWidth() >> 8);
        header[14] = (byte) (terrain.getLength() & 0xff); // Image height
        header[15] = (byte) (terrain.getLength() >> 8);
        header[16] = (byte) (this.colors ? 24 : 8); // Pixel depth
        header[17] = 0; // Image descriptor
        return header;
    }

    
    private double getAltitude(Terrain terrain, int x, int y) {
        if (!this.heights) {
            return 1.0;
        }
        double altitude = terrain.getTile(x, y).getAltitude(true);
        altitude -= this.minmax.getX();
        altitude /= (this.minmax.getY() - this.minmax.getX());
        return altitude;
    }
        
    private int getImagePixel(Terrain terrain, int x, int y) {
        if (this.colors) {
            return this.getImageRGB(terrain, x, y);
        } else {
            return this.getImageGray(terrain, x, y);
        }
    }

    private byte getImageGray(Terrain terrain, int x, int y) {
        return (byte) (this.getAltitude(terrain, x, y) * 255.0);
    }
    
    private int getImageRGB(Terrain terrain, int x, int y) {
        Vector rgb;
        Tile tile = terrain.getTile(x, y);
        if (tile.getWater() > 0.0) {
            rgb = new Vector(0, 0, 1);
        } else {
            rgb = tile.getTopSoil().getRGB();
        }
        double altitude = 0.1 + 0.9 * this.getAltitude(terrain, x, y);
        rgb = rgb.multiply(altitude * 255.0);
        int r = (int) rgb.getX();
        int g = (int) rgb.getY();
        int b = (int) rgb.getZ();
        return (r << 16) | (g << 8) | b;
    }
    
    
    /**
     * Returns the terrain in uncompressed format.
     * 
     * @param terrain
     * @return
     */
    private byte[] getBody(Terrain terrain) {
        byte[] image = new byte[terrain.getWidth() * terrain.getLength() * (this.colors ? 3 : 1)];
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if (this.colors) {
                    int color = this.getImageRGB(terrain, x, y);
                    image[x * 3 + 0 + (terrain.getLength() - y - 1) * terrain.getWidth() * 3] = (byte) ((color >>  0) & 0xff); // blue
                    image[x * 3 + 1 + (terrain.getLength() - y - 1) * terrain.getWidth() * 3] = (byte) ((color >>  8) & 0xff); // green
                    image[x * 3 + 2 + (terrain.getLength() - y - 1) * terrain.getWidth() * 3] = (byte) ((color >> 16) & 0xff); // red
                } else {
                    image[x + (terrain.getLength() - y - 1) * terrain.getWidth()] = this.getImageGray(terrain, x, y);
                }
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
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        for (int y = 0; y < terrain.getLength(); y++) {
            int x = 0;
            while (x < terrain.getWidth()) {
                int count = this.countNextRlePacketSizeOfSameBytes(terrain, x, y);
                if (count > 1) {
                    this.addDataRLE(terrain, x, y, count, tmp);
                } else {
                    count = this.countNextRlePacketSizeOfDifferentBytes(terrain, x, y);
                    this.addDataRAW(terrain, x, y, count, tmp);
                }
                x += count;
            }
        }
        return tmp.toByteArray();
    }
    
    private void addDataRLE(Terrain terrain, int x, int y, int count, ByteArrayOutputStream output) {
        output.write((byte) (128 + (count - 1)));
        if (this.colors) {
            int color = this.getImageRGB(terrain, x, y);
            output.write((byte) ((color >>  0) & 0xff)); // blue
            output.write((byte) ((color >>  8) & 0xff)); // green
            output.write((byte) ((color >> 16) & 0xff)); // red
        } else {
            output.write(this.getImageGray(terrain, x, y));
        }
    }

    private void addDataRAW(Terrain terrain, int x, int y, int count, ByteArrayOutputStream output) {
        output.write((byte) (count - 1));
        for (int i = 0; i < count; i++) {
            if (this.colors) {
                int color = this.getImageRGB(terrain, x, y);
                output.write((byte) ((color >>  0) & 0xff)); // blue
                output.write((byte) ((color >>  8) & 0xff)); // green
                output.write((byte) ((color >> 16) & 0xff)); // red
            } else {
                output.write(this.getImageGray(terrain, x, y));
            }
        }
    }
    
    private int countNextRlePacketSizeOfSameBytes(Terrain terrain, int start_x, int y) {
        int count = 1;
        final int p = this.getImagePixel(terrain, start_x, y);
        for (int x = start_x + 1; x < terrain.getWidth() && count < 128; x++) {
            if (this.getImagePixel(terrain, x, y) != p) {
                break;
            }
            count++;
        }
        return count;
    }

    private int countNextRlePacketSizeOfDifferentBytes(Terrain terrain, int start_x, int y) {
        int count = 1;
        int p = this.getImagePixel(terrain, start_x, y);
        for (int x = start_x + 1; x < terrain.getWidth() && count < 128; x++) {
            int cur = this.getImagePixel(terrain, x, y);
            if (cur == p) {
                break;
            }
            count++;
            p = cur;
        }
        return count;
    }
}
