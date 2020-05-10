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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import plortz.ui.UserInterface;

/**
 * Command to run a script file.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Run extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (this.args.size() != 2) {
            this.showUsage(ui);
            return;
        }
        
        String filename = this.args.get(1);
        List<String> commands = new ArrayList<>();
        Scanner s = null;
        try {
            s = new Scanner(new File(filename));
        } catch (Exception e) {
            ui.showMessage("Failed to open '" + filename + "' for reading: " + e.getMessage());
            return;
        }
        while (s.hasNextLine()) {
            String line = s.nextLine();
            commands.add(line);
        }
        this.startApplyingTools();
        this.executeOtherCommands(ui, commands);
        this.endApplyingTools(ui);
    }
    

    @Override
    public String getShortDescription() {
        return "Execute commands from a script file.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + args.get(0) + " <filename>");
        return rv;
    }
}
