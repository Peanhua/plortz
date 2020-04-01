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
 * Tool to add new layers of soil on top of existing.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddSoilLayer extends Tool {

    private enum AreaType {
        Circle,
        Rectangle
    };
    
    private final AreaType       area_type;
    private final Position       center;
    private final int            width;
    private final int            height;
    private final SoilLayer.Type soil_type;
    private final double         amount;
    private boolean[] processed;      // Holds true for each tile already processed, used to avoid processing same tile multiple times.
    private int       processed_size; // The number of elements in processed array is processed_size * processed_size.
    
    /**
     * Constructor for rectangle area.
     * 
     * @param soil_type
     * @param amount
     * @param center
     * @param width
     * @param height 
     */
    public AddSoilLayer(SoilLayer.Type soil_type, double amount, Position center, int width, int height) {
        this.soil_type = soil_type;
        this.amount    = amount;
        this.area_type = AreaType.Rectangle;
        this.center    = new Position(center);
        this.width     = width;
        this.height    = height;
    }

    /**
     * Constructor for circle area.
     * 
     * @param soil_type
     * @param amount
     * @param center
     * @param radius 
     */
    public AddSoilLayer(SoilLayer.Type soil_type, double amount, Position center, int radius) {
        this.soil_type = soil_type;
        this.amount    = amount;
        this.area_type = AreaType.Circle;
        this.center    = new Position(center);
        this.width     = radius;
        this.height    = 0;
    }
    
    @Override
    public void apply(Terrain terrain) {
        switch (this.area_type) {
            case Rectangle:
                this.addRectangleLayer(terrain);
                break;
            case Circle:
                this.addCircleLayer(terrain);
                break;
        }
        terrain.changed();
    }
    
    private void addCircleLayer(Terrain terrain) {
        int radius = this.width;
        this.processed_size = 2 * radius + 1; // +1 for the center
        this.processed      = new boolean[this.processed_size * this.processed_size];
        for (int dy = 0; dy <= radius; dy++) {
            for (int dx = dy; dx <= radius; dx++) {
                if ((dx * dx) + (dy * dy) <= (radius * radius)) {
                    this.circlePlot(terrain, +dx, +dy);
                    this.circlePlot(terrain, +dx, -dy);
                    this.circlePlot(terrain, -dx, +dy);
                    this.circlePlot(terrain, -dx, -dy);
                    this.circlePlot(terrain, +dy, +dx);
                    this.circlePlot(terrain, +dy, -dx);
                    this.circlePlot(terrain, -dy, +dx);
                    this.circlePlot(terrain, -dy, -dx);
                }
            }
        }
    }
    
    private void circlePlot(Terrain terrain, int dx, int dy) {
        int radius = this.width;
        int pos = (radius + dx) + (radius + dy) * processed_size;
        if (this.processed[pos]) {
            return;
        }
        this.processed[pos] = true;
        Tile t = terrain.getTile(this.center.getX() + dx, this.center.getY() + dy);
        if (t != null) {
            t.addSoil(this.soil_type, this.amount);
        }
    }
    
    private void addRectangleLayer(Terrain terrain) {
        for (int dy = 0; dy < this.height; dy++) {
            int y = this.center.getY() + dy - this.height / 2;
            for (int dx = 0; dx < this.width; dx++) {
                int x = this.center.getX() + dx - this.width / 2;
                Tile t = terrain.getTile(x, y);
                if (t != null) {
                    t.addSoil(this.soil_type, this.amount);
                }
            }
        }
    }
}
