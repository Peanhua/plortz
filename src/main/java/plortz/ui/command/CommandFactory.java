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

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import plortz.util.HashMap;
import plortz.util.ArrayList;

/**
 * Singleton factory to generate command object from given command string.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class CommandFactory {
    private static CommandFactory instance = null;
    
    public static CommandFactory getInstance() {
        if (CommandFactory.instance == null) {
            CommandFactory.instance = new CommandFactory();
        }
        return CommandFactory.instance;
    }
    
    private final HashMap<String, Class> commands;
    
    private CommandFactory() {
        this.commands = new HashMap<>();
        this.commands.put("quit",          Quit.class);
        this.commands.put("dump",          WriteToConsole.class);
        this.commands.put("save",          WriteToTargaFile.class);
        this.commands.put("new",           NewTerrain.class);
        this.commands.put("gauss",         GaussianDistribution.class);
        this.commands.put("ds",            DiamondSquare.class);
        this.commands.put("random",        RandomNoise.class);
        this.commands.put("perlin",        PerlinNoise.class);
        this.commands.put("sheet_erosion", SheetErosion.class);
        this.commands.put("add_soil",      AddSoilLayer.class);
        this.commands.put("insert_soil",   InsertSoilAtBottom.class);
        this.commands.put("help",          Help.class);
        this.commands.put("history",       History.class);
        this.commands.put("!",             ExecuteCommandsFromHistory.class);
        this.commands.put("random_seed",   SetRandomSeed.class);
        this.commands.put("random_soil",   AddRandomSoilPatches.class);
        this.commands.put("smooth",        SmoothAltitudes.class);
        this.commands.put("scale",         ScaleHeights.class);
        this.commands.put("info",          Info.class);
        this.commands.put("water",         AddWater.class);
        this.commands.put("run",           Run.class);
        this.commands.put("#",             Comment.class);
    }
    
    public Command create(String string) {
        List<String> args = this.splitToArgs(string);
        if (args == null || args.isEmpty()) {
            return null;
        }
        Command cmd = this.getCommand(args.get(0));
        cmd.setArgs(args);
        return cmd;
    }
    
    private List<String> splitToArgs(String string) {
        if (string == null) {
            return null;
        }
        ArrayList<String> args = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                args.add(string.substring(start, i));
                start = i + 1;
            }
        }
        args.add(string.substring(start));
        
        // Prune empty args:
        for (int i = args.size() - 1; i >= 0; i--) {
            if (args.get(i).isEmpty()) {
                args.remove(i);
            }
        }
        
        return args;
    }
    
    
    private Command getCommand(String command_string) {
        @SuppressWarnings("unchecked")
        Class<Command> c = this.commands.get(command_string); // unchecked conversion
        if (c == null) {
            @SuppressWarnings("unchecked")
            Class<Command> tmp = (Class) UnknownCommand.class; // unchecked conversion
            c = tmp;
        }

        try {
            Constructor<Command> cons = c.getConstructor();
            Command cmd = cons.newInstance();
            return cmd;
            
        } catch (Exception e) {
            System.out.println("Internal error, failed to instantiate command '" + command_string + "': " + e.getMessage());
            return null;
        }
    }
    
    public Set<String> getCommands() {
        return this.commands.keySet();
    }
}
