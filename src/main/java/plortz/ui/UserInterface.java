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
 * <p>
 * Each user interface is associated to a terrain object.
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
    
    /**
     * Return true if this user interface is active.
     * @return True if this user interface is active.
     */
    public boolean isRunning() {
        return this.running;
    }
    
    /**
     * Start the execution of this user interface.
     */
    public abstract void run();
    
    /**
     * Stop this user interface.
     */
    public void stop() {
        this.running = false;
        try {
            this.command_history.save(this.command_history_filename);
        } catch (Exception e) {
            System.err.println("Failed to write history to \"" + this.command_history_filename + "\": " + e.getMessage());
        }
    }
    
    /**
     * Return the terrain object associated with this user interface.
     * @return The terrain object.
     */
    public final Terrain getTerrain() {
        return this.terrain;
    }
    
    /**
     * Set the terrain object associated with this user interface.
     * @param terrain The terrain object.
     */
    public final void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        this.on_terrain_change.notifyObservers();
    }
    
    /**
     * Register an observer to be called whenever the terrain reference is changed.
     * <p>
     * Note that this is different than listening on changes on the terrain itself.
     * 
     * @param observer The observer object to be called upon change.
     */
    public final void listenOnTerrainChange(Observer observer) {
        this.on_terrain_change.addObserver(observer);
    }
    
    /**
     * Register an observer to be called whenever a message for the user is sent.
     * @param observer The observer object to be called upon a new message.
     */
    public final void listenOnMessage(Observer observer) {
        this.on_message.addObserver(observer);
    }

    /**
     * Send a message for the user to see.
     * @param message The message.
     */
    public final void showMessage(String message) {
        this.message = message;
        this.on_message.notifyObservers();
    }
    
    /**
     * Return the current/last message sent to the user.
     * @return The message.
     */
    public final String getMessage() {
        return this.message;
    }
    
    /**
     * Return the command history object for this user interface.
     * @return The command history object.
     */
    public CommandHistory getCommandHistory() {
        return this.command_history;
    }
    
    /**
     * Return a random generator associated with this user interface.
     * @return The random generator object.
     */
    public Random getRandom() {
        return this.random_generator;
    }
    
    /**
     * Enable/disable timing the command execution.
     * @param enabled True to enable.
     */
    public void setOutputTiming(boolean enabled) {
        this.output_timing = enabled;
    }
    
    /**
     * Return whether the timing of command execution is enabled or not.
     * @return True if the timing is enabled.
     */
    public boolean getOutputTiming() {
        return this.output_timing;
    }
}
