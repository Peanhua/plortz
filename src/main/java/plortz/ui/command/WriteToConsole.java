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

import java.util.ArrayList;
import java.util.List;
import plortz.io.AsciiWriter;
import plortz.io.Writer;
import plortz.ui.UserInterface;

/**
 * Command to write the terrain to the console.
 * 
 * Note that this does not use the user interface, but writes directly to stdout as the name of the command suggests,
 * might need to be changed later to use the user interface.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class WriteToConsole extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        
        boolean normalize = this.args.size() > 1;
        
        Writer writer = new AsciiWriter(normalize);
        try {
            writer.write(ui.getTerrain(), System.out);
        } catch (Exception e) {
            ui.showMessage(this.args.get(0) + " failed: " + e.getMessage());
        }
    }

    @Override
    public String getShortDescription() {
        return "Dumps the terrain as ascii text to stdout.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + this.args.get(0) + " [normalize]");
        return rv;
    }
}
