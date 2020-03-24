/*
 * Copyright (C) 2020 Joni Yrjana <joniyrjana@gmail.com>
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
import java.util.HashMap;

/**
 * Singleton factory to generate command object from given command string.
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
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
        this.commands.put("quit",   Quit.class);
        this.commands.put("dump",   WriteToConsole.class);
        this.commands.put("save",   WriteToTargaFile.class);
        this.commands.put("new",    NewTerrain.class);
        this.commands.put("gauss",  GaussianDistribution.class);
        this.commands.put("ds",     DiamondSquare.class);
        this.commands.put("random", RandomNoise.class);
    }
    
    public Command create(String string) {
        if (string == null) {
            return null;
        }
        
        String[] args = string.split(" ");
        if (args.length == 0) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Class<Command> c = this.commands.get(args[0]); // unchecked conversion
        if (c == null) {
            return null;
        }
        
        try {
            Constructor<Command> cons = c.getConstructor();
            Command cmd = cons.newInstance();
            cmd.setArgs(args);
            return cmd;
            
        } catch (Exception e) {
            System.out.println("Internal error, failed to instantiate command '" + args[0] + "': " + e.getMessage());
            return null;
        }
    }
}
