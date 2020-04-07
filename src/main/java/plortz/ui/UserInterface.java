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
package plortz.ui;

import java.util.Random;
import plortz.util.CommandHistory;
import plortz.util.MersenneTwister;
import plortz.observer.Observer;
import plortz.observer.Subject;
import plortz.terrain.Terrain;

/**
 * The abstract base class for the user interface implementations.
 * 
 * Each user interface is tied to a terrain object.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class UserInterface {
    private boolean              running;
    private final CommandHistory command_history;
    private final String         command_history_filename;
    private Terrain              terrain;
    private final Subject        on_terrain_change;
    private final Subject        on_message;
    private String               message; // Current message
    private final Random         random_generator;
    private boolean              output_timing;
    
    public UserInterface() {
        this.running           = true;
        this.command_history   = new CommandHistory();
        this.command_history_filename = ".plortz_history";
        this.terrain           = null;
        this.on_terrain_change = new Subject();
        this.on_message        = new Subject();
        this.random_generator  = new MersenneTwister();
        this.output_timing     = false;
        
        try {
            this.command_history.load(this.command_history_filename);
        } catch (Exception e) {
            // Silently ignored.
        }
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public abstract void run();
    
    public void stop() {
        this.running = false;
        try {
            this.command_history.save(this.command_history_filename);
        } catch (Exception e) {
            System.err.println("Failed to write history to \"" + this.command_history_filename + "\": " + e.getMessage());
        }
    }
    
    public final Terrain getTerrain() {
        return this.terrain;
    }
    
    public final void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        this.on_terrain_change.notifyObservers();
    }
    
    /**
     * Register an observer to be called whenever the terrain reference is changed.
     * 
     * @param observer The observer object to be called upon change.
     */
    public final void listenOnTerrainChange(Observer observer) {
        this.on_terrain_change.addObserver(observer);
    }
    
    public final void listenOnMessage(Observer observer) {
        this.on_message.addObserver(observer);
    }

    public final void showMessage(String message) {
        this.message = message;
        this.on_message.notifyObservers();
    }
    
    public final String getMessage() {
        return this.message;
    }
    
    public CommandHistory getCommandHistory() {
        return this.command_history;
    }
    
    public Random getRandom() {
        return this.random_generator;
    }
    
    public void setOutputTiming(boolean enabled) {
        this.output_timing = enabled;
    }
    
    public boolean getOutputTiming() {
        return this.output_timing;
    }
}
