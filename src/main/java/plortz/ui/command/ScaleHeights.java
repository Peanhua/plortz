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
import plortz.util.ArrayList;
import plortz.tool.Tool;
import plortz.ui.UserInterface;

/**
 * A command to scale all the soil layers by a factor.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class ScaleHeights extends Command {
    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        
        if (this.args.size() != 2) {
            this.showUsage(ui);
            return;
        }
        
        double factor = Double.parseDouble(this.args.get(1));
        if (factor <= 0.0) {
            ui.showMessage("Error, the factor must be greater than 0.");
            return;
        }
        
        Tool tool = new plortz.tool.ScaleHeights(factor);
        this.applyTool(ui, tool);
    }

    @Override
    public String getShortDescription() {
        return "Scale all heights by a factor.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + args.get(0) + " <factor>");
        return rv;
    }
}
