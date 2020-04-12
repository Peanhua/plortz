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

import plortz.util.Vector;

/**
 * The type of the tile, describes static properties of the type.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SoilLayer {
    
    /**
     * Enum listing all the possible soil types.
     */
    public enum Type {
        DIRT,
        SAND,
        CLIFF;
    };
    
    private final Type type;
    private double     amount;
    
    public SoilLayer(Type type, double amount) {
        this.type   = type;
        this.amount = amount;
    }
    
    /**
     * Copy constructor.
     * 
     * @param source The source SoilLayer to copy from.
     */
    public SoilLayer(SoilLayer source) {
        this.type   = source.type;
        this.amount = source.amount;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public void adjustAmount(double adjustment) {
        this.amount += adjustment;
    }
    
    /**
     * Return the angle of repose.
     * <p>
     * https://en.wikipedia.org/wiki/Angle_of_repose
     * 
     * @param kinetic True if the soil layer is currently moving, false if the soil layer is static and not moving.
     * @return        The angle of repose, in degrees.
     */
    public double getAngleOfRepose(boolean kinetic) {
        double aor = 0.0;
        switch (this.type) {
            case DIRT:
                aor = 40;
                break;
            case SAND:
                aor = 34;
                break;
            case CLIFF:
                aor = 91;
                break;
        }
        if (kinetic && this.type != Type.CLIFF) {
            aor *= 0.75;
        }
        return aor;
    }
    
    /**
     * Return a unique color for the soil layers type.
     * 
     * @return A vector containing the red, green, and blue values in [0, 1].
     */
    public Vector getRGB() {
        switch (this.type) {
            case DIRT:  return new Vector(0.495, 0.325, 0.080);
            case SAND:  return new Vector(1.000, 0.795, 0.291);
            case CLIFF: return new Vector(0.600, 0.600, 0.600);
            default:    return new Vector(1.000, 1.000, 1.000);
        }
    }
}
