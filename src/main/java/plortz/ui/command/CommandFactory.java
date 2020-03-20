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
    
    private CommandFactory() {
    }
    
    public Command create(String string) {
        if (string == null) {
            return null;
        }
        
        String[] args = string.split(" ");
        if (args.length == 0) {
            return null;
        }

        // todo: use a hashmap to map these
        if (args[0].equals("quit")) {
            return new Quit(args);
        } else if (args[0].equals("dump")) {
            return new WriteToConsole(args);
        } else if (args[0].equals("save")) {
            return new WriteToTargaFile(args);
        } else if (args[0].equals("new")) {
            return new NewTerrain(args);
        } else if (args[0].equals("gauss")) {
            return new GaussianDistribution(args);
        } else if (args[0].equals("ds")) {
            return new DiamondSquare(args);
        } else if (args[0].equals("random")) {
            return new RandomNoise(args);
        }
        
        return null;
    }
}
