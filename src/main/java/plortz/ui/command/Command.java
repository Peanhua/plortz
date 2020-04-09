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

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import plortz.tool.Tool;
import plortz.ui.UserInterface;

/**
 * The abstract base class for all the commands.
 * 
 * Commands are created by the user interfaces to perform some action.
 * The action is often an operation performed by one or more tools.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class Command {
    protected List<String> args;
    private Instant        start_time;
    
    public void setArgs(List<String> args) {
        this.args = args;
    }
    
    public void showUsage(UserInterface ui) {
        this.getUsage().forEach(s -> ui.showMessage(s));
    }
    
    protected boolean requireTerrain(UserInterface ui) {
        if (ui.getTerrain() == null) {
            ui.showMessage("Error: No terrain.");
            ui.showMessage("The command " + this.args.get(0) + " requires a terrain, use the command \"new\" to create it.");
            return false;
        }
        return true;
    }
    
    protected void applyTool(UserInterface ui, Tool tool) {
        this.startApplyingTools();
        tool.apply(ui.getTerrain());
        this.endApplyingTools(ui);
    }
    
    protected void startApplyingTools() {
        this.start_time = Instant.now();
    }
    
    protected void endApplyingTools(UserInterface ui) {
        if (ui.getOutputTiming()) {
            Instant end_time = Instant.now();
            Duration duration = Duration.between(start_time, end_time);
            ui.showMessage("Execution time: " + duration);
        }
    }
        
    protected void executeOtherCommands(UserInterface ui, List<String> commands) {
        CommandFactory cf = CommandFactory.getInstance();
        for (String input : commands) {
            ui.showMessage("> " + input);
            Command cmd = cf.create(input);
            if (cmd != null) {
                cmd.execute(ui);
            } else if (input != null && input.length() > 0) {
                ui.showMessage("Unknown command: " + input);
            }
        }
    }
    
    public abstract void execute(UserInterface ui);
    public abstract String getShortDescription();
    public abstract List<String> getUsage();
}
