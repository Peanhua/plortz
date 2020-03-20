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

import java.util.Random;
import plortz.tool.Tool;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class DiamondSquare extends Command {

    public DiamondSquare(String[] args) {
        super(args);
    }

    @Override
    public void execute(UserInterface ui) {
        if (this.args.length != 2) {
            ui.showError("Incorrect number of arguments.");
            ui.showError("Usage: " + this.args[0] + " <scale>");
            return;
        }
        
        double scale;
        try {
            scale = Double.parseDouble(this.args[1]);
        } catch (Exception e) {
            ui.showError("Failed to parse arguments: " + e.getMessage());
            return;
        }
        
        double[] corners = { 0, 0, 0, 0 };
        Tool ds = new plortz.tool.DiamondSquare(corners, scale, new Random());
        ds.apply(ui.getTerrain());
    }
    
}
