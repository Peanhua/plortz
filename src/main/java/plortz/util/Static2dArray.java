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
    
    /**
     * Constructor.
     * 
     * @param width  The width of the 2d array.
     * @param length The length of the 2d array.
     */
    public Static2dArray(int width, int length) {
        this.width  = width;
        this.length = length;
        this.array  = new Object[width * length];
    }

    /**
     * Constructor with default value.
     * 
     * @param width         The width of the 2d array.
     * @param length        The length of the 2d array.
     * @param default_value The default value, used to initialize the array.
     */
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
            
            @Override
            public E next() {
                @SuppressWarnings({"unchecked"})
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

    /**
     * Check if the given position is valid for this array.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return  True if the given position is valid.
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.length;
    }

    /**
     * Check if the given position is valid for this array.
     * 
     * @param position The position to check.
     * @return  True if the given position is valid.
     */
    public boolean isValidPosition(Position position) {
        if (position == null) {
            return false;
        }
        return this.isValidPosition(position.getX(), position.getY());
    }

    
    /**
     * Return the element at the given position.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return  The element.
     */
    public E get(int x, int y) {
        @SuppressWarnings({"unchecked"})
        E e = (E) this.array[x + y * this.width]; // unchecked cast
        return e;
    }

    /**
     * Return the element at the given position.
     * 
     * @param position The position.
     * @return         The element.
     */
    public E get(Position position) {
        return this.get(position.getX(), position.getY());
    }
    
    /**
     * Return the element at the given index, does not check the validity of the index.
     * 
     * @param index The element index.
     * @return      The element.
     */
    public E get(int index) {
        @SuppressWarnings({"unchecked"})
        E e = (E) this.array[index]; // unchecked cast
        return e;
    }
    
    
    public int getWidth() {
        return this.width;
    }
    
    public int getLength() {
        return this.length;
    }

    /**
     * Set the element for the given position.
     * 
     * @param x     The x coordinate.
     * @param y     The y coordinate.
     * @param value The new element for the position.
     */
    public void set(int x, int y, E value) {
        if (!this.isValidPosition(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        this.array[x + y * this.width] = value;
    }

    /**
     * Set the element for the given position.
     * 
     * @param position The position of the element.
     * @param value    The new element for the position.
     */
    public void set(Position position, E value) {
        this.set(position.getX(), position.getY(), value);
    }
    
    /**
     * Set the element for the given index, does not check the validity of the index.
     * 
     * @param index The index of the element.
     * @param value The new element.
     */
    public void set(int index, E value) {
        this.array[index] = value;
    }
    
    @Override
    public String toString() {
        String rv = "";
        for (int y = 0; y < this.length; y++) {
            for (int x = 0; x < this.width; x++) {
                rv += this.array[x + y * this.width] + ", ";
            }
            rv += "\n";
        }
        return rv;
    }
}
