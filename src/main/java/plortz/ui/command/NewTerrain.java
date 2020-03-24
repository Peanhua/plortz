/*
 * Copyright (C) 2020 Joni Yrjana <joniyrjana@gmail.com>
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
package plortz.ui.command;

import plortz.Terrain;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class NewTerrain extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (this.args.length != 3) {
            ui.showError("Incorrect number of arguments.");
            ui.showError("Usage: " + args[0] + " <width> <height>");
            return;
        }
        
        int width = Integer.parseInt(args[1]);
        int height = Integer.parseInt(args[2]);
        
        if (width <= 0) {
            ui.showError("Minimum width is 1.");
            return;
        }
        if (height <= 0) {
            ui.showError("Minimum height is 1.");
            return;
        }
        
        ui.setTerrain(new Terrain(width, height));
    }
    
}
