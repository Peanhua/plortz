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
    
    public void setArgs(List<String> args) {
        this.args = args;
    }
    
    public void showUsage(UserInterface ui) {
        this.getUsage().forEach(s -> ui.showMessage(s));
    }
    
    public abstract void execute(UserInterface ui);
    public abstract String getShortDescription();
    public abstract List<String> getUsage();
}
