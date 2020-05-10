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

import java.util.ArrayList;
import java.util.List;
import plortz.util.Position;
import plortz.terrain.SoilLayer;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.util.Circle;

/**
 * Tool to add new layers of soil on top of existing.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddSoilLayer extends Tool {

    private enum AreaType {
        Circle,
        Rectangle
    };
    
    private final AreaType area_type;
    private final Position center;
    private final int      width;
    private final int      length;
    protected final SoilLayer.Type soil_type;
    protected final double         amount;
    
    /**
     * Constructor for rectangle area.
     * 
     * @param soil_type The type of the soil to add.
     * @param amount    The amount (vertical).
     * @param topleft   The top-left coordinates of the rectangle.
     * @param width     Width of the rectangle.
     * @param length    Length of the rectangle.
     */
    public AddSoilLayer(SoilLayer.Type soil_type, double amount, Position topleft, int width, int length) {
        this.soil_type = soil_type;
        this.amount    = amount;
        this.area_type = AreaType.Rectangle;
        this.center    = new Position(topleft);
        this.width     = width;
        this.length    = length;
    }

    /**
     * Constructor for circle area.
     * 
     * @param soil_type The type of the soil to add.
     * @param amount    The amount (vertical).
     * @param center    Center of the circle.
     * @param radius    Radius of the circle.
     */
    public AddSoilLayer(SoilLayer.Type soil_type, double amount, Position center, int radius) {
        this.soil_type = soil_type;
        this.amount    = amount;
        this.area_type = AreaType.Circle;
        this.center    = new Position(center);
        this.width     = radius;
        this.length    = 0;
    }
    
    @Override
    public void apply(Terrain terrain) {
        for (Position pos : this.getPositions()) {
            this.processTile(terrain, pos);
        }
        terrain.changed();
    }

    private List<Position> getPositions() {
        List<Position> positions = null;
        switch (this.area_type) {
            case Rectangle:
                positions = new ArrayList<>();
                for (int dy = 0; dy < this.length; dy++) {
                    for (int dx = 0; dx < this.width; dx++) {
                        positions.add(new Position(this.center, dx, dy));
                    }
                }
                break;
            case Circle:
                Circle circle = new Circle(this.center, this.width);
                positions = circle.getPositions();
                break;
        }
        return positions;
    }
    
    protected void processTile(Terrain terrain, Position position) {
        Tile t = terrain.getTile(position);
        if (t != null) {
            t.addSoil(this.soil_type, this.amount);
        }
    }
}
