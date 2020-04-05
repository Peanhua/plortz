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
import plortz.util.MyArrayList;
import plortz.ui.UserInterface;

/**
 * Command to quit (close) the user interface.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Quit extends Command {

    @Override
    public void execute(UserInterface ui) {
        ui.stop();
    }
    
    @Override
    public String getShortDescription() {
        return "Quits the program.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new MyArrayList<>();
        rv.add("Usage: " + args.get(0));
        return rv;
    }
}
