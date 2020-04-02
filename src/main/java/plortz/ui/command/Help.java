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
import plortz.collections.MyArrayList;
import plortz.ui.UserInterface;

/**
 * Command to show help on commands.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Help extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (this.args.size() == 1) {
            this.helpCommandList(ui);
        } else {
            this.helpCommand(ui, this.args.get(1));
        }
    }

    @Override
    public String getShortDescription() {
        return "Help on commands.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new MyArrayList<>(String.class);
        rv.add("Usage: " + this.args.get(0) + " [command]");
        return rv;
    }
    
    private void helpCommandList(UserInterface ui) {
        CommandFactory cf = CommandFactory.getInstance();
        ui.showMessage("Commands:");
        cf.getCommands().forEach(s -> ui.showMessage("  " + s));
        ui.showMessage("Try: help <command>");
    }
    
    private void helpCommand(UserInterface ui, String command) {
        CommandFactory cf = CommandFactory.getInstance();
        Command cmd = cf.create(command);
        ui.showMessage(cmd.getShortDescription());
        cmd.showUsage(ui);
    }
}
