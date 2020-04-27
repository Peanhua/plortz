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
import plortz.util.Position;
import plortz.tool.Tool;
import plortz.ui.UserInterface;
import plortz.util.ArrayList;

/**
 * Command to add water, forming rivers and lakes.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddWater extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        if (this.args.size() != 4) {
            ui.showMessage("Incorrect number of arguments.");
            this.showUsage(ui);
            return;
        }
        int x, y;
        double amount;
        try {
            x = Integer.parseInt(this.args.get(1));
            y = Integer.parseInt(this.args.get(2));
            amount = Double.parseDouble(this.args.get(3));
        } catch (Exception e) {
            ui.showMessage("Failed to parse arguments: " + e.getMessage());
            return;
        }
        Tool tool = new plortz.tool.AddWater(new Position(x, y), amount);
        this.applyTool(ui, tool);
    }
    
    @Override
    public String getShortDescription() {
        return "Add water source.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + args.get(0) + " <x> <y> <amount>");
        return rv;
    }
}
