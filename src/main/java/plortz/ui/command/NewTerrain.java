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
import plortz.terrain.Terrain;
import plortz.ui.UserInterface;

/**
 * Command to create new terrain, the previous terrain is destroyed.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class NewTerrain extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (this.args.size() != 3) {
            ui.showMessage("Incorrect number of arguments.");
            this.showUsage(ui);
            return;
        }
        
        int width  = Integer.parseInt(args.get(1));
        int length = Integer.parseInt(args.get(2));
        
        if (width <= 0) {
            ui.showMessage("Minimum width is 1.");
            return;
        }
        if (length <= 0) {
            ui.showMessage("Minimum length is 1.");
            return;
        }
        
        this.startApplyingTools();
        ui.setTerrain(new Terrain(width, length));
        this.endApplyingTools(ui);
    }

    @Override
    public String getShortDescription() {
        return "Destroys the current terrain and creates a new terrain.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + args.get(0) + " <width> <length>");
        return rv;
    }
}
