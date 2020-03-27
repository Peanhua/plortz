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
package plortz.ui;

import plortz.Terrain;
import plortz.ui.command.Command;
import plortz.ui.command.CommandFactory;

/**
 * The abstract base class for the user interface implementations.
 * 
 * Each user interface is tied to a terrain object.
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public abstract class UserInterface {
    private boolean              running;
    private final CommandFactory command_factory;
    private Terrain              terrain;
    
    public UserInterface() {
        this.running         = true;
        this.command_factory = CommandFactory.getInstance();
        this.terrain         = null;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void stop() {
        this.running = false;
    }
    
    public Terrain getTerrain() {
        return this.terrain;
    }
    
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
    
    public void tick() {
        if (!this.running) {
            return;
        }
        
        String input = this.getNextCommand();
        Command cmd = this.command_factory.create(input);
        if (cmd != null) {
            cmd.execute(this);
        } else if (input != null && input.length() > 0) {
            this.showError("Unknown command: " + input);
        }
    }
    
    public abstract String getNextCommand();
    public abstract void   showError(String error_message);
}
