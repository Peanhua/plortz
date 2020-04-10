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
package plortz.util;

import java.util.Iterator;
import plortz.terrain.Position;

/**
 * Statically allocated 2d array.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 * @param <E> The array element type.
 */
public class Static2dArray<E> implements Iterable<E> {
    
    private final Object[] array;
    private final int      width;
    private final int      length;
    
    public Static2dArray(int width, int length) {
        this.width  = width;
        this.length = length;
        this.array  = new Object[width * length];
    }

    public Static2dArray(int width, int length, E default_value) {
        this(width, length);
        for (int i = 0; i < width * length; i++) {
            this.array[i] = default_value;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int pos = 0;
            
            @Override
            public boolean hasNext() {
                return pos < width * length;
            }
            
            @SuppressWarnings({"unchecked"})
            @Override
            public E next() {
                E t = (E) array[pos]; // unchecked cast
                pos++;
                return t;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("not supported");
            }
        };
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.length;
    }

    public boolean isValidPosition(Position position) {
        if (position == null) {
            return false;
        }
        return this.isValidPosition(position.getX(), position.getY());
    }

    
    @SuppressWarnings({"unchecked"})
    public E get(int x, int y) {
        return (E) this.array[x + y * this.width]; // unchecked cast
    }

    public E get(Position position) {
        return this.get(position.getX(), position.getY());
    }
    
    @SuppressWarnings({"unchecked"})
    public E get(int index) {
        return (E) this.array[index]; // unchecked cast
    }
    
    
    public int getWidth() {
        return this.width;
    }
    
    public int getLength() {
        return this.length;
    }

    
    public void set(int x, int y, E value) {
        if (!this.isValidPosition(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        this.array[x + y * this.width] = value;
    }

    public void set(Position position, E value) {
        this.set(position.getX(), position.getY(), value);
    }
    
    public void set(int index, E value) {
        this.array[index] = value;
    }
}
