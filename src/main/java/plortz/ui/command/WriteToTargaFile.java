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
import java.util.ArrayList;
import java.util.List;
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
        if (!this.requireTerrain(ui)) {
            return;
        }
        
        if (this.args.size() != 3) {
            ui.showMessage("Incorrect number of arguments.");
            this.showUsage(ui);
            return;
        }
        
        String type = this.args.get(1);
        if (!type.equals("heightmap") && !type.equals("color") && !type.equals("soil")) {
            ui.showMessage("Incorrect argument #1 for <data>, expected 'heightmap' or 'color', but got '" + type + "'");
            return;
        }
        boolean color   = false;
        boolean heights = false;
        switch (type) {
            case "heightmap":
                heights = true;
                color   = false;
                break;
            case "color":
                heights = true;
                color   = true;
                break;
            case "soil":
                heights = false;
                color   = true;
                break;
        }
        
        String filename = this.args.get(2);
        
        RandomAccessFile fp;
        try {
            fp = new RandomAccessFile(filename, "rw");
            fp.setLength(0);
        } catch (Exception e) {
            ui.showMessage("Failed to create file '" + filename + "': " + e.getMessage());
            return;
        }
        
        Writer writer = new TargaWriter(true, heights, color);
        try {
            writer.write(ui.getTerrain(), fp);
            fp.close();
        } catch (Exception e) {
            ui.showMessage("Failed to save to file '" + filename + "': " + e.getMessage());
        }
    }

    @Override
    public String getShortDescription() {
        return "Saves the current terrain as 8-bit grayscale or 24-bit color image in Targa file format.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + this.args.get(0) + " <data> <filename>");
        rv.add("Where <data> is one of: heightmap, soil, color");
        rv.add(" heightmap = save the altitudes in grayscale");
        rv.add(" soil = save the soil types in color");
        rv.add(" color = save the altitudes and the soil types in color");
        return rv;
    }
}
