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
import plortz.tool.Tool;
import plortz.ui.UserInterface;
import plortz.util.ArrayList;

/**
 * A command to remove water from the terrain.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class RemoveWater extends Command {
    @Override
    public String getShortDescription() {
        return "Remove all water from the given area.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + this.args.get(0) + " all");
        /*
        todo: add the same options as in AddSoilLayer:
        rv.add("       " + this.args.get(0) + " circle <x> <y> <radius>");
        rv.add("       " + this.args.get(0) + " rect <x> <y> <width> <length>");
        */
        return rv;
    }

    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        if (this.args.size() != 2) {
            this.showUsage(ui);
            return;
        }
        if (!this.args.get(1).equals("all")) {
            ui.showMessage("Incorrect argument #1: '" + this.args.get(1) + "'");
            this.showUsage(ui);
            return;
        }
        
        Tool tool = new plortz.tool.RemoveWater();
        this.applyTool(ui, tool);
    }
}
