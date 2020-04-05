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
import javafx.scene.layout.BorderPane;
import plortz.terrain.Tile;
import plortz.ui.UserInterface;
import plortz.util.Vector;

/**
 * Abstract base class for the different terrain view widgets.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class TerrainView extends Widget {
    protected UserInterface   user_interface;
    protected BorderPane      container;
    protected int             width;
    protected int             height;
    protected boolean         active;
    
    public TerrainView(UserInterface ui) {
        this.user_interface   = ui;
        this.container        = null;
        this.width            = 0;
        this.height           = 0;
        this.active           = false;
        ui.listenOnTerrainChange(() -> {
            this.refresh();
            this.user_interface.getTerrain().listenOnChange(() -> {
                this.refresh();
            });
        });
    }

    @Override
    public Node createUserInterface() {
        this.container = new BorderPane();
        this.container.setMinWidth(0);
        this.container.setMinHeight(0);
        this.container.setMaxWidth(Double.MAX_VALUE);
        this.container.setMaxHeight(Double.MAX_VALUE);

        this.width  = 0;
        this.height = 0;
        
        this.container.widthProperty().addListener((o) -> this.onResized());
        this.container.heightProperty().addListener((o) -> this.onResized());

        return this.container;
    }

    protected void onResized() {
        this.width  = (int) this.container.getWidth();
        this.height = (int) this.container.getHeight();
    }

    protected int getTileARGB(Tile tile, double altitude) {
        Vector rgb = tile.getTopSoil().getRGB();
        int r = (int) (rgb.getX() * 255.0 * altitude);
        int g = (int) (rgb.getY() * 255.0 * altitude);
        int b = (int) (rgb.getZ() * 255.0 * altitude);
        int argb = (0xff << 24) | (r << 16) | (g << 8) | b;
        return argb;
    }
    
    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            this.refresh();
        }
    }
}
