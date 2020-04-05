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
package plortz.ui.command;

import java.util.List;
import plortz.util.MyArrayList;
import plortz.tool.Tool;
import plortz.ui.UserInterface;

/**
 * Command to execute the GaussianDistribution tool over the terrain.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class GaussianDistribution extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        
        if (this.args.size() != 6) {
            ui.showMessage("Incorrect number of arguments.");
            this.showUsage(ui);
            return;
        }
        
        int x, y;
        double var, h, v;
        try {
            x   = Integer.parseInt(args.get(1));
            y   = Integer.parseInt(args.get(2));
            var = Double.parseDouble(args.get(3));
            h   = Double.parseDouble(args.get(4));
            v   = Double.parseDouble(args.get(5));
        } catch (Exception e) {
            ui.showMessage(this.args.get(0) + ": Failed to parse arguments: " + e.getMessage());
            return;
        }
        
        Tool tool = new plortz.tool.GaussianDistribution(x, y, var, h, v);
        this.applyTool(ui, tool);
    }
    
    @Override
    public String getShortDescription() {
        return "Elevates terrain to create a mountain/hill.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new MyArrayList<>();
        rv.add("Usage: " + this.args.get(0) + " <x> <y> <var> <h> <v>");
        rv.add("  <x> <y> the center coordinates");
        rv.add("  <var>   variance");
        rv.add("  <h>     horizontal scale");
        rv.add("  <v>     vertical scale");
        return rv;
    }
}
