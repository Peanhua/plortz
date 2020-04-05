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
package plortz.tool;

import plortz.terrain.Position;
import plortz.terrain.SoilLayer;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;

/**
 * Tool to insert new layers of soil at the bottom of the tile.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class InsertSoilLayer extends AddSoilLayer {
    
    private final int layer;
    
    /**
     * Constructor for rectangle area.
     * 
     * @param layer     The layer index to insert at.
     * @param soil_type The type of the soil to add.
     * @param amount    The amount (vertical).
     * @param center    Center of the rectangle.
     * @param width     Width of the rectangle.
     * @param length    Length of the rectangle.
     */
    public InsertSoilLayer(int layer, SoilLayer.Type soil_type, double amount, Position center, int width, int length) {
        super(soil_type, amount, center, width, length);
        this.layer = layer;
    }

    /**
     * Constructor for circle area.
     * 
     * @param layer     The layer index to insert at.
     * @param soil_type The type of the soil to add.
     * @param amount    The amount (vertical).
     * @param center    Center of the circle.
     * @param radius    Radius of the circle.
     */
    public InsertSoilLayer(int layer, SoilLayer.Type soil_type, double amount, Position center, int radius) {
        super(soil_type, amount, center, radius);
        this.layer = layer;
    }
    
    @Override
    protected void processTile(Terrain terrain, int x, int y) {
        Tile t = terrain.getTile(x, y);
        if (t != null) {
            t.insertSoil(this.layer, this.soil_type, this.amount);
        }
    }
}
