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

import java.util.ArrayList;
import plortz.util.Position;
import java.util.List;
import plortz.util.Vector;

/**
 * A single tile in the Terrain.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Tile {
    private final List<SoilLayer> soil_layers;
    private double                water_height; // The depth of the water in this tile, the surface of the water is at surface_level + water_height.
    private final Position        position;
    
    /**
     * Constructor.
     * @param position The position of the tile.
     * @param type     The soil type of the initial bottom layer of the tile.
     * @param amount   The amount of soil in the initial bottom layer.
     */
    public Tile(Position position, SoilLayer.Type type, double amount) {
        this.soil_layers  = new ArrayList<>();
        this.soil_layers.add(new SoilLayer(type, amount));
        this.water_height = -1;
        this.position     = new Position(position);
    }
    
    /**
     * Copy constructor.
     * @param source The source tile to copy from.
     */
    public Tile(Tile source) {
        this.soil_layers = new ArrayList<>();
        for (SoilLayer layer : source.soil_layers) {
            this.soil_layers.add(new SoilLayer(layer));
        }
        this.water_height = source.water_height;
        this.position     = new Position(source.position);
    }
    
    @Override
    public String toString() {
        return "Tile[position=" + this.position + "]";
    }
    
    /**
     * Get the top-most soil layer.
     * @return The top-most soil layer.
     */
    public SoilLayer getTopSoil() {
        return this.soil_layers.get(this.soil_layers.size() - 1);
    }
    
    /**
     * Get the bottom soil layer.
     * @return The bottom soil layer.
     */
    public SoilLayer getBottomSoil() {
        return this.soil_layers.get(0);
    }
    
    /**
     * Return the position in the terrain this tile is in.
     * @return The position in the terrain.
     */
    public Position getPosition() {
        return this.position;
    }
    
    /**
     * Set the position, does not change the terrain.
     * <p>
     * Because the tile has no link to the terrain where it is located,
     * this method does not update the terrain object,
     * and should be called only together when adjusting the terrain simultaneously.
     * @param position The new position of this tile.
     */
    public void setPosition(Position position) {
        this.position.set(position);
    }
    
    /**
     * Calculate the total altitude of this tile.
     * @param with_water If true, the water is included.
     * @return           The total altitude of this tile.
     */
    public double getAltitude(boolean with_water) {
        double alt = 0.0;
        for (SoilLayer layer : this.soil_layers) {
            alt += layer.getAmount();
        }
        if (with_water && this.water_height > 0.0) {
            alt += this.water_height;
        }
        return alt;
    }
    
    /**
     * Set the amount of soil in the top-most soil layer.
     * <p>
     * If the amount is negative, the top-most soil layer is removed.
     * If the amount is negative and the top-most soil layer is also the bottom-most,
     * then the amount of the soil layer will be negative.
     * @param amount The new soil amount.
     */
    public void setTopSoilAmount(double amount) {
        if (amount > 0.0) {
            this.getTopSoil().setAmount(amount);
        } else {
            if (this.soil_layers.size() == 1) {
                this.getTopSoil().setAmount(amount);
            } else {
                this.soil_layers.remove(this.soil_layers.size() - 1);
            }
        }
    }
    
    /**
     * Adjusts the amount of soil in the top-most soil layer, uses setTopSoilAmount().
     * @param change How much to change.
     */
    public void adjustTopSoilAmount(double change) {
        this.setTopSoilAmount(this.getTopSoil().getAmount() + change);
    }
    

    /**
     * Add soil to the top.
     * <p>
     * New layer is created if the current top soil has different type.
     * 
     * @param type   The soil type to add.
     * @param amount The amount of soil to add.
     */
    public void addSoil(SoilLayer.Type type, double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException();
        }
        SoilLayer top = this.getTopSoil();
        if (top.getType() == type) {
            top.adjustAmount(amount);
        } else {
            this.soil_layers.add(new SoilLayer(type, amount));
        }
    }
    
    /**
     * Insert soil.
     * <p>
     * New layer is created if the specified layer has different type.
     * 
     * @param layer  The layer index where to insert.
     * @param type   The soil type to add.
     * @param amount The amount of soil to add.
     */
    public void insertSoil(int layer, SoilLayer.Type type, double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException();
        }
        if (layer < 0 || layer > this.soil_layers.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (layer == this.soil_layers.size()) { // Inserting at top is same as adding
            this.addSoil(type, amount);
            return;
        }
        SoilLayer soil = this.soil_layers.get(layer);
        if (soil.getType() == type) {
            soil.adjustAmount(amount);
        } else {
            soil = new SoilLayer(type, amount);
            this.soil_layers.add(layer, soil);
        }
    }
    
    /**
     * Multiply the soil amount in all soil layers by the given factor.
     * @param factor The factor to multiply with.
     */
    public void scaleSoilLayers(double factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException();
        }
        this.soil_layers.forEach((SoilLayer layer) ->
                layer.setAmount(layer.getAmount() * factor)
        );
    }

    /**
     * Set the water height of this tile.
     * <p>
     * The water height describes how much water is in this tile on top of the surface_level.
     * <p>
     * Zero or negative amounts define a "no water" situation.
     * If the tile contains water, the amount is always greater than 0.0.
     * 
     * @param water_height The new water height.
     */
    public void setWater(double water_height) {
        this.water_height = water_height;
    }
    
    public void adjustWater(double amount) {
        if (this.water_height < 0.0) {
            if (amount > 0.0) {
                this.water_height = amount;
            }
        } else {
            this.water_height += amount;
        }
    }
    
    public double getWater() {
        return this.water_height;
    }
    
    
    /**
     * Return the slope from this to the target.
     * 
     * @param target The target tile.
     * @return The slope, negative values are downhill from a to b, positive values uphill from a to b.
     */
    public double getSlope(Tile target) {
        double altitude_change = target.getAltitude(false) - this.getAltitude(false);
        double distance = this.getDistance(target);
        return altitude_change / distance;
    }

    /**
     * Return the distance to the target tile.
     * 
     * @param target The target tile.
     * @return Distance to the target.
     */
    public double getDistance(Tile target) {
        Vector a = new Vector(this.getPosition());
        Vector b = new Vector(target.getPosition());
        return a.subtract(b).getLength();
    }
}
