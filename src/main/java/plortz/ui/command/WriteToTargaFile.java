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

import java.io.RandomAccessFile;
import plortz.Terrain;
import plortz.io.TargaWriter;
import plortz.io.Writer;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class WriteToTargaFile extends Command {

    public WriteToTargaFile(String[] args) {
        super(args);
    }

    @Override
    public void execute(UserInterface ui) {
        if (this.args.length != 2) {
            ui.showError("Incorrect number of arguments.");
            ui.showError("Usage: " + this.args[0] + " <filename>");
            return;
        }
        
        RandomAccessFile fp;
        try {
            fp = new RandomAccessFile(this.args[1], "rw");
        } catch (Exception e) {
            ui.showError("Failed to create file '" + this.args[1] + "': " + e.getMessage());
            return;
        }
    
        Terrain terrain = new Terrain(ui.getTerrain());
        terrain.normalize();
    
        Writer writer = new TargaWriter(true);
        try {
            writer.write(terrain, fp);
        } catch (Exception e) {
            ui.showError("Failed to save to file '" + this.args[1] + "': " + e.getMessage());
        }
    }
}
