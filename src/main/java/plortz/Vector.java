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

/**
 * A generic vector class.
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class Vector {
    private final double[] coords;
    
    public Vector(double[] coords) {
        this.coords = new double[coords.length];
        for (int i = 0; i < this.coords.length; i++) {
            this.coords[i] = coords[i];
        }
    }

    public Vector(double x, double y) {
        this.coords = new double[2];
        this.coords[0] = x;
        this.coords[1] = y;
    }
    
    public Vector(double x, double y, double z) {
        this.coords = new double[3];
        this.coords[0] = x;
        this.coords[1] = y;
        this.coords[2] = z;
    }
    
    public Vector(Vector source) {
        this.coords = new double[source.coords.length];
        for (int i = 0; i < this.coords.length; i++) {
            this.coords[i] = source.coords[i];
        }
    }
    
    public double getX() {
        return this.coords[0];
    }
    
    public double getY() {
        if (this.coords.length < 2) {
            throw new IndexOutOfBoundsException();
        }
        return this.coords[1];
    }
    
    public double getZ() {
        if (this.coords.length < 3) {
            throw new IndexOutOfBoundsException();
        }
        return this.coords[2];
    }

    public double getW() {
        if (this.coords.length < 4) {
            throw new IndexOutOfBoundsException();
        }
        return this.coords[3];
    }
    
    /**
     * Return a new vector = this - rhs
     * @param rhs 
     * @return New vector equal to this - rhs
     */
    public Vector subtract(Vector rhs) {
        double[] nvalues = new double[this.coords.length];
        for (int i = 0; i < nvalues.length; i++) {
            nvalues[i] = this.coords[i] - rhs.coords[i];
        }
        return new Vector(nvalues);
    }
    
    /**
     * Normalize to unit vector.
     */
    public void normalize() {
        double len = this.getLength();
        double multiplier = 1.0 / len;
        for (int i = 0; i < this.coords.length; i++) {
            this.coords[i] *= multiplier;
        }
    }
    
    /**
     * Return the vector length.
     * 
     * @return The length.
     */
    public double getLength() {
        double dp = this.getDotProduct(this);
        return Math.sqrt(dp);
    }
    
    /**
     * Calculate the dot product of this vector with some other vector.
     * 
     * @param other The other vector of the dot product.
     * @return The dot product of this and the other.
     */
    public double getDotProduct(Vector other) {
        double dp = 0.0;
        for (int i = 0; i < this.coords.length; i++) {
            dp += this.coords[i] * other.coords[i];
        }
        return dp;
    }
    
    @Override
    public String toString() {
        String rv = "(";
        for (int i = 0; i < this.coords.length; i++) {
            if (i > 0) {
                rv += ",";
            }
            rv += this.coords[i];
        }
        return rv + ")";
    }
}
