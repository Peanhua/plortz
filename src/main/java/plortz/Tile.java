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
 * A single tile in the Terrain.
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class Tile {
    private TileType type;
    private double   surface_level; // The altitude of the surface for this tile.
    private double   water_height; // The depth of the water in this tile, the surface of the water is at surface_level + water_height.
    
    public Tile(TileType type, double altitude) {
        this.type          = type;
        this.surface_level = altitude;
        this.water_height  = -1;
    }
    
    public Tile(Tile source) {
        this.type          = source.type;
        this.surface_level = source.surface_level;
        this.water_height  = source.water_height;
    }
    
    @Override
    public String toString() {
        return "Tile[type=" + this.type + ", surface_level=" + this.surface_level + ", water_height=" + this.water_height + "]";
    }
    
    
    public double getAltitude(boolean with_water) {
        double alt = this.surface_level;
        if (with_water && this.water_height > 0.0) {
            alt += this.water_height;
        }
        return alt;
    }
    
    public void setAltitude(double altitude) {
        this.surface_level = altitude;
    }
    
    public void adjustAltitude(double change) {
        this.surface_level += change;
    }

    /**
     * Set the water height of this tile.
     * The water height describes how much water is in this tile on top of the surface_level.
     * 
     * @param water_height The new water height.
     */
    public void setWater(double water_height) {
        this.water_height = water_height;
    }
    
    /**
     * Return the slope from this tile to the other.
     * 
     * @param other The target tile.
     * @return The slope, negative values are downhill, positive values uphill.
     */
    public double getSlope(Tile other) {
        return 0.0;
    }
}
