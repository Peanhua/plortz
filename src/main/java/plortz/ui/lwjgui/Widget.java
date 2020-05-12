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
package plortz.ui.lwjgui;

import lwjgui.scene.Node;


/**
 * Abstract base class for all the custom widgets.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class Widget {
    private Node root;
    
    public Widget() {
        this.root = null;
    }
    
    public final Node getRootNode() {
        if (this.root == null) {
            this.root = this.createUserInterface();
        }
        return this.root;
    }
    
    /**
     * Create the user interface nodes to represent this widget.
     * 
     * @return A node containing all the nodes of this widget
     */
    protected abstract Node createUserInterface();
    
    /**
     * Refresh any dynamic content of this widget.
     */
    public abstract void refresh();
}
