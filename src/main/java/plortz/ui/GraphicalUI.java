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
package plortz.ui;

import plortz.terrain.Terrain;

/**
 * User interface with JavaFX.
 * 
 * Uses consoles terrain.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class GraphicalUI extends UserInterface {
    
    private final UserInterface console;

    public GraphicalUI(UserInterface console) {
        super();
        this.console = console;
    }
    
    public void run() {
        plortz.ui.javafx.Main gui = new plortz.ui.javafx.Main();
        gui.run(this);
    }
    
    @Override
    public void setTerrain(Terrain terrain) {
        this.console.setTerrain(terrain);
    }
    
    @Override
    public Terrain getTerrain() {
        return this.console.getTerrain();
    }
    
    @Override
    public String getNextCommand() {
        return null;
    }

    @Override
    public void showError(String error_message) {
    }
}
