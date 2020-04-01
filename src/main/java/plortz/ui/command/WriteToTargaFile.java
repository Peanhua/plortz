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

import java.io.RandomAccessFile;
import java.util.List;
import plortz.collections.MyArrayList;
import plortz.terrain.Terrain;
import plortz.io.TargaWriter;
import plortz.io.Writer;
import plortz.ui.UserInterface;

/**
 * Command to write the terrain to a TGA file.
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class WriteToTargaFile extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (this.args.size() != 2) {
            ui.showMessage("Incorrect number of arguments.");
            this.showUsage(ui);
            return;
        }
        
        RandomAccessFile fp;
        try {
            fp = new RandomAccessFile(this.args.get(1), "rw");
            fp.setLength(0);
        } catch (Exception e) {
            ui.showMessage("Failed to create file '" + this.args.get(1) + "': " + e.getMessage());
            return;
        }
    
        Writer writer = new TargaWriter(true);
        try {
            writer.write(ui.getTerrain(), fp);
            fp.close();
        } catch (Exception e) {
            ui.showMessage("Failed to save to file '" + this.args.get(1) + "': " + e.getMessage());
        }
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new MyArrayList<>(String.class);
        rv.add("Usage: " + this.args.get(0) + " <filename>");
        return rv;
    }
}
