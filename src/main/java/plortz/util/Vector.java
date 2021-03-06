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
package plortz.util;

/**
 * Generic vector class.
 * <p>
 * Can handle arbitrary number of dimensions.
 * The dimensions are named (in order): x, y, z, w.
 * For more than 4 dimensions, the remaining dimensions are not named.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Vector {
    private final double[] coords;
    
    /**
     * Construct a vector from arbitrary number of dimensions.
     * 
     * @param coords The coordinates for each dimension.
     */
    public Vector(double[] coords) {
        this.coords = new double[coords.length];
        for (int i = 0; i < this.coords.length; i++) {
            this.coords[i] = coords[i];
        }
    }
    
    /**
     * Construct a vector from a Position.
     * 
     * @param position The source position.
     */
    public Vector(Position position) {
        this(position.getX(), position.getY());
    }

    /**
     * Constructor for 2d vector.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Vector(double x, double y) {
        this.coords = new double[2];
        this.coords[0] = x;
        this.coords[1] = y;
    }
    
    /**
     * Constructor for 3d vector.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */
    public Vector(double x, double y, double z) {
        this.coords = new double[3];
        this.coords[0] = x;
        this.coords[1] = y;
        this.coords[2] = z;
    }

    /**
     * Copy constructor.
     * 
     * @param source The vector to copy from.
     */
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
    
    public double get(int dimension) {
        if (dimension < 0 || dimension >= this.coords.length) {
            throw new IndexOutOfBoundsException();
        }
        return this.coords[dimension];
    }
    
    /**
     * Returns the number of dimensions this vector has.
     * 
     * @return The number of dimensions.
     */
    public int getDimensions() {
        return this.coords.length;
    }
    
    /**
     * Sets his vector using another vector.
     * The number of dimensions in the source must be equal to this vector.
     * 
     * @param source The vector to copy from.
     */
    public void set(Vector source) {
        if (this.coords.length != source.coords.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < this.coords.length; i++) {
            this.coords[i] = source.coords[i];
        }
    }
    
    public void setX(double x) {
        this.coords[0] = x;
    }
    
    public void setY(double y) {
        if (this.coords.length < 2) {
            throw new IndexOutOfBoundsException();
        }
        this.coords[1] = y;
    }

    public void setZ(double z) {
        if (this.coords.length < 3) {
            throw new IndexOutOfBoundsException();
        }
        this.coords[2] = z;
    }

    public void setW(double w) {
        if (this.coords.length < 4) {
            throw new IndexOutOfBoundsException();
        }
        this.coords[3] = w;
    }
    
    
    /**
     * Return a new vector = this - rhs
     * @param rhs The right hand side vector to subtract from this.
     * @return    New vector equal to this - rhs
     */
    public Vector subtract(Vector rhs) {
        double[] nvalues = new double[this.coords.length];
        for (int i = 0; i < nvalues.length; i++) {
            nvalues[i] = this.coords[i] - rhs.coords[i];
        }
        return new Vector(nvalues);
    }
    
    /**
     * Return a new vector = this * factor
     * @param factor The scalar factor.
     * @return       New vector equal to this * factor.
     */
    public Vector multiply(double factor) {
        double[] nvalues = new double[this.coords.length];
        for (int i = 0; i < nvalues.length; i++) {
            nvalues[i] = this.coords[i] * factor;
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
