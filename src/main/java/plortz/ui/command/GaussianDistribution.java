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

import plortz.tool.Tool;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class GaussianDistribution extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (ui.getTerrain() == null) {
            ui.showError("No terrain.");
            return;
        }
        
        if (this.args.length != 6) {
            ui.showError("Incorrect number of arguments.");
            ui.showError("Usage: " + this.args[0] + " <x> <y> <var> <h> <v>");
            ui.showError("  <x> <y> the center coordinates");
            ui.showError("  <var>   variance");
            ui.showError("  <h>     horizontal scale");
            ui.showError("  <v>     vertical scale");
            return;
        }
        
        int x, y;
        double var, h, v;
        try {
            x   = Integer.parseInt(args[1]);
            y   = Integer.parseInt(args[2]);
            var = Double.parseDouble(args[3]);
            h   = Double.parseDouble(args[4]);
            v   = Double.parseDouble(args[5]);
        } catch (Exception e) {
            ui.showError(this.args[0] + ": Failed to parse arguments: " + e.getMessage());
            return;
        }
        
        Tool tool = new plortz.tool.GaussianDistribution(x, y, var, h, v);
        tool.apply(ui.getTerrain());
    }
    
}
