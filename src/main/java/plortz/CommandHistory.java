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
package plortz;

import java.util.List;
import plortz.collections.MyArrayList;

/**
 * Keeps history of issued commands.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class CommandHistory {
    
    private final List<String> history;
    private int                cursor;
    
    public CommandHistory() {
        this.history = new MyArrayList<>(String.class);
        this.cursor  = 0;
    }
    
    public void add(String command) {
        this.history.add(command);
        this.cursor = this.history.size();
    }
    
    public List<String> get() {
        return this.history;
    }
    
    public void clear() {
        this.history.clear();
        this.cursor = 0;
    }

    public String previous() {
        if (this.cursor == 0) {
            return null;
        }
        this.cursor--;
        return this.history.get(this.cursor);
    }
    
    public String next() {
        if (this.cursor >= this.history.size() - 1) {
            this.cursor = this.history.size();
            return null;
        }
        this.cursor++;
        String rv = this.history.get(this.cursor);
        return rv;
    }
}
