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
import plortz.ui.UserInterface;

/**
 * Command to execute given commands from the command history.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class ExecuteCommandsFromHistory extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (this.args.size() != 2 && this.args.size() != 3) {
            this.showUsage(ui);
            return;
        }
        
        int start = Integer.parseInt(this.args.get(1));
        int end = start;
        if (this.args.size() == 3) {
            end = Integer.parseInt(this.args.get(2));
        }
        
        start--;
        end--;
        List<String> history = ui.getCommandHistory().get();
        
        if (history.isEmpty()) {
            ui.showMessage("No history.");
            return;
        }
        
        if (start < 0 || start >= history.size() || end < start || end >= history.size()) {
            ui.showMessage(this.args.get(0) + ": Invalid range " + (start + 1) + "-" + (end + 1) + ", valid range is 1-" + (history.size()) + ".");
            return;
        }
        this.executeHistoryCommands(ui, start, end);
    }
    
    private void executeHistoryCommands(UserInterface ui, int start, int end) {
        List<String> commands = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            String input = ui.getCommandHistory().get().get(i);
            commands.add(input);
        }
        this.executeOtherCommands(ui, commands);
    }

    @Override
    public String getShortDescription() {
        return "Execute commands from the command history.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + args.get(0) + " <index>");
        rv.add("       " + args.get(0) + " <start> <end>");
        return rv;
    }
}
