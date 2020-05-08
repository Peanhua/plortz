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
 * Command to set the altitude that defines the sea level.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SetSeaLevel extends Command {

    @Override
    public String getShortDescription() {
        return "Set the altitude that defines the sea level. Anything below the sea level will contain water up to the sea level.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + this.args.get(0) + " <sea_level>");
        return rv;
    }

    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        if (this.args.size() != 2) {
            ui.showMessage("Incorrect number of arguments.");
            this.showUsage(ui);
            return;
        }
        double sea_level;
        try {
            sea_level = Double.parseDouble(this.args.get(1));
        } catch (Exception e) {
            ui.showMessage("Failed to parse arguments: " + e.getMessage());
            return;
        }
        Tool tool = new plortz.tool.SetSeaLevel(sea_level);
        this.applyTool(ui, tool);
    }
}
