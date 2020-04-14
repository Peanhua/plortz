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
package plortz.ui.javafx;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import plortz.util.Vector;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.ui.UserInterface;

/**
 * A widget showing the current terrain as an 2d image.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class TerrainView2d extends TerrainView {

    private Canvas          canvas;
    private GraphicsContext graphics_context;
    
    public TerrainView2d(UserInterface ui) {
        super(ui);
        this.canvas           = null;
        this.graphics_context = null;
    }
    
    @Override
    protected Node createUserInterface() {
        this.canvas           = null;
        this.graphics_context = null;
        return super.createUserInterface();
    }

    @Override
    protected void onResized() {
        super.onResized();
        this.canvas = new Canvas(this.width, this.height);
        this.graphics_context = canvas.getGraphicsContext2D();
        this.container.setCenter(this.canvas);
        this.refresh();
    }


    @Override
    public void refresh() {
        if (!this.active || this.width == 0 || this.canvas == null) {
            return;
        }
        this.graphics_context.setFill(Color.BLACK);
        this.graphics_context.fillRect(0, 0, this.width, this.height);

        Terrain terrain = this.user_interface.getTerrain();
        if (terrain == null) {
            return;
        }
        
        Vector size = this.zoomToFit(new Vector(terrain.getWidth(), terrain.getLength()), new Vector(this.width, this.height));
        double x = this.width  / 2.0 - size.getX() / 2.0;
        double y = this.height / 2.0 - size.getY() / 2.0;
        this.graphics_context.drawImage(this.getImage(terrain), x, y, size.getX(), size.getY());
    }
    
    /**
     * Grow or shrink the given values keeping the aspect ratio.
     * 
     * @param values Values to grow/shrink, same object will be returned.
     * @param max    The maximum values.
     * @return       The new values, same object as the given parameter 'values'.
     */
    private Vector zoomToFit(Vector values, Vector max) {
        if (values.getX() < max.getX()) {
            values = values.multiply(max.getX() / values.getX());
        }
        if (values.getY() < max.getY()) {
            values = values.multiply(max.getY() / values.getY());
        }
        if (values.getX() > max.getX()) {
            values = values.multiply(max.getX() / values.getX());
        }
        if (values.getY() > max.getY()) {
            values = values.multiply(max.getY() / values.getY());
        }
        return values;
    }
    
    private Image getImage(Terrain terrain) {
        WritableImage image = new WritableImage(terrain.getWidth(), terrain.getLength());

        PixelWriter pw = image.getPixelWriter();
        Vector minmax = terrain.getAltitudeRange();
        
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile tile = terrain.getTile(x, y);
                double altitude = 0.1 + this.getNormalizedAltitude(tile, minmax) * 0.9;
                pw.setArgb(x, y, this.getTileARGB(tile, altitude));
            }
        }
        
        return image;
    }
    
    private double getNormalizedAltitude(Tile tile, Vector minmax) {
        double altitude = tile.getAltitude(true);
        altitude -= minmax.getX();
        altitude /= (minmax.getY() - minmax.getX());
        if (Double.isNaN(altitude)) {
            altitude = 0.0;
        }
        return altitude;
    }
}
