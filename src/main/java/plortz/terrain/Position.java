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
package plortz.terrain;

/**
 * Simple class to wrap x and y location into an object for code readability.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Position(Position source) {
        this.x = source.x;
        this.y = source.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(Position source) {
        this.x = source.x;
        this.y = source.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "(x=" + this.x + ",y=" + this.y + ")";
    }
}
