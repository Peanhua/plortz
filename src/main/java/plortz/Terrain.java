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
package plortz;

import java.util.Iterator;
import java.util.function.Consumer;


/**
 * Container of the terrain data.
 * The terrain is a 2d grid of Tiles.
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class Terrain implements Iterable<Tile> {
    private final int    width;
    private final int    height;
    private final Tile[] tiles;
    
    public Terrain(int width, int height) {
        this.width  = width;
        this.height = height;
        this.tiles  = new Tile[width * height];
    }

    @Override
    public Iterator<Tile> iterator() {
        return new Iterator<Tile>() {
            private int pos = 0;
            
            @Override
            public boolean hasNext() {
                return pos < width * height;
            }
            
            @Override
            public Tile next() {
                Tile t = tiles[pos];
                pos++;
                return t;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("not supported");
            }
        };
    }

}
