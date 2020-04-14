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
import javafx.scene.input.KeyEvent;
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
    /**
     * The user interface this widget is part of.
     */
    protected UserInterface user_interface;
    
    /**
     * The container node for this terrain view.
     */
    protected BorderPane container;
    
    /**
     * The width of the container.
     */
    protected int width;
    
    /**
     * The height of the container.
     */
    protected int height;
    
    /**
     * True if this terrain view is active.
     * <p>
     * The refresh() should use this to determine whether to actually update the contents or not.
     */
    protected boolean active;
    
    /**
     * Construct a new terrain view object, creates the container.
     * 
     * @param ui The user interface this terrain view is part of.
     */
    public TerrainView(UserInterface ui) {
        super();
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
    protected Node createUserInterface() {
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

    /**
     * Called when the containing node is resized.
     */
    protected void onResized() {
        this.width  = (int) this.container.getWidth();
        this.height = (int) this.container.getHeight();
    }

    /**
     * Return the color of the given tile.
     * 
     * @param tile     The tile.
     * @param altitude Normalized altitude of the tile, in range [0, 1].
     * @return         The ARGB value.
     */
    protected int getTileARGB(Tile tile, double altitude) {
        Vector rgb;
        if (tile.getWater() > 0.0) {
            rgb = new Vector(0, 0, 1);
        } else {
            rgb = tile.getTopSoil().getRGB();
        }
        int r = (int) (rgb.getX() * 255.0 * altitude);
        int g = (int) (rgb.getY() * 255.0 * altitude);
        int b = (int) (rgb.getZ() * 255.0 * altitude);
        int argb = (0xff << 24) | (r << 16) | (g << 8) | b;
        return argb;
    }
    
    /**
     * Sets whether this view is active or not.
     * <p>
     * Calls refresh() when setting to active.
     * 
     * @param active True if this view is active.
     */
    public void setActive(boolean active) {
        boolean previously = this.active;
        this.active = active;
        if (!previously && active) {
            this.refresh();
        }
    }
    
    public void onKeyPressed(KeyEvent event) {
    }

    public void onKeyReleased(KeyEvent event) {
    }
}
