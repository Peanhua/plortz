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
package plortz.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Keeps history of issued commands.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class CommandHistory {
    
    private final List<String> history;
    private int                cursor;
    
    public CommandHistory() {
        this.history = new ArrayList<>();
        this.cursor  = 0;
    }
    
    /**
     * Add a command to the end of the history, updates the current position in history to the end.
     * 
     * @param command The command to add.
     */
    public void add(String command) {
        this.history.add(command);
        this.cursor = this.history.size();
    }
    
    /**
     * Return the full history.
     * 
     * @return All the commands from the history.
     */
    public List<String> get() {
        return this.history;
    }
    
    /**
     * Clear the history, removing all commands from it.
     */
    public void clear() {
        this.history.clear();
        this.cursor = 0;
    }

    /**
     * Return the previous command, updates the current position in the history.
     * 
     * @return Previous command, or null if the beginning of history is reached.
     */
    public String previous() {
        if (this.cursor == 0) {
            return null;
        }
        this.cursor--;
        return this.history.get(this.cursor);
    }
    
    /**
     * Returns the next command, updates the current position in the history.
     * 
     * @return Next command, or null if the end of the history is reached.
     */
    public String next() {
        if (this.cursor >= this.history.size() - 1) {
            this.cursor = this.history.size();
            return null;
        }
        this.cursor++;
        String rv = this.history.get(this.cursor);
        return rv;
    }
    
    /**
     * Save the history to a file.
     * <p>
     * Destroys the current contents of the file (does not append).
     * 
     * @param filename     The name of the file to write to.
     * @throws IOException Exception thrown on failure.
     */
    public void save(String filename) throws IOException {
        PrintStream file = new PrintStream(filename);
        this.history.forEach((line) -> {
            file.println(line);
        });
    }
    
    /**
     * Add entries from the file to the history.
     * <p>
     * The entries are appended to the end of the history.
     * 
     * @param filename               The name of the file to read from.
     * @throws FileNotFoundException Exception thrown on failure.
     */
    public void load(String filename) throws FileNotFoundException {
        Scanner s = new Scanner(new File(filename));
        while (s.hasNextLine()) {
            String line = s.nextLine();
            this.add(line);
        }
    }
}
