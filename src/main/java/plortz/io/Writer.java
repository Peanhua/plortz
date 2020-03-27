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

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import plortz.Terrain;

/**
 * Base class for writing terrain to file.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class Writer {
    public void write(Terrain terrain, OutputStream output) throws IOException {
        output.write(this.getBytes(terrain));
    }
    
    public void write(Terrain terrain, RandomAccessFile file) throws IOException {
        file.write(this.getBytes(terrain));
    }

    protected abstract byte[] getBytes(Terrain terrain);
}
