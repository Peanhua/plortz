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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import plortz.Vector;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class TerrainView2d extends Widget {

    private UserInterface   ui;
    private Pane            container;
    private Canvas          canvas;
    private GraphicsContext graphics_context;
    private int             width;
    private int             height;
    
    public TerrainView2d(UserInterface ui) {
        this.ui               = ui;
        this.container        = null;
        this.canvas           = null;
        this.graphics_context = null;
        this.width            = 0;
        this.height           = 0;
        
        ui.listenOnTerrainChange(() -> {
            this.refresh();
            this.ui.getTerrain().listenOnChange(() -> {
                this.refresh();
            });
        });
    }
    
    @Override
    public Node createUserInterface() {
        this.container = new Pane();
        this.container.setMaxWidth(Double.MAX_VALUE);
        this.container.setMaxHeight(Double.MAX_VALUE);

        this.width            = 0;
        this.height           = 0;
        this.canvas           = null;
        this.graphics_context = null;
        
        this.container.heightProperty().addListener((o) -> {
            this.onResized();
        });

        return this.container;
    }
    
    private void onResized() {
        this.width  = (int) this.container.getWidth();
        this.height = (int) this.container.getHeight();
        
        if (this.canvas != null) {
            this.container.getChildren().remove(this.canvas);
        }
        
        this.canvas = new Canvas(this.width, this.height);
        this.graphics_context = canvas.getGraphicsContext2D();

        this.container.getChildren().add(this.canvas);
        
        this.refresh();
    }


    @Override
    public void refresh() {
        if (this.width == 0 || this.canvas == null) {
            return;
        }
        this.graphics_context.setFill(Color.BLACK);
        this.graphics_context.fillRect(0, 0, this.width, this.height);

        Terrain terrain = this.ui.getTerrain();
        if (terrain == null) {
            return;
        }
        this.graphics_context.drawImage(this.getImage(terrain), 0, 0, this.width, this.height);
    }
    
    private Image getImage(Terrain terrain) {
        WritableImage image = new WritableImage(terrain.getWidth(), terrain.getHeight());

        PixelWriter pw = image.getPixelWriter();
        Vector minmax = terrain.getAltitudeRange();
        
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile tile = terrain.getTile(x, y);
                
                double altitude = tile.getAltitude(true);
                altitude -= minmax.getX();
                altitude /= (minmax.getY() - minmax.getX());
                
                Vector rgb = tile.getTopSoil().getRGB();
                int r = (int) (rgb.getX() * 255.0 * altitude);
                int g = (int) (rgb.getY() * 255.0 * altitude);
                int b = (int) (rgb.getZ() * 255.0 * altitude);
                int argb = (0xff << 24) | (r << 16) | (g << 8) | b;
                pw.setArgb(x, y, argb);
            }
        }
        
        return image;
    }
}
