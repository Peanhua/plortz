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
import plortz.collections.MyArrayList;
import plortz.terrain.Position;
import plortz.terrain.SoilLayer;
import plortz.tool.Tool;

/**
 * Command to insert soil at the bottom.
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class InsertSoilAtBottom extends AddSoilLayer {

    @Override
    public String getShortDescription() {
        return "Inserts a layer of soil at the bottom of the terrain.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new MyArrayList<>(String.class);
        rv.add("Usage: " + this.args.get(0) + " <type> <amount> circle <x> <y> <radius>");
        rv.add("       " + this.args.get(0) + " <type> <amount> rect <x> <y> <width> <length>");
        return rv;
    }

    @Override
    protected Tool getCircleTool(SoilLayer.Type soil_type, double amount, Position center, int radius) {
        return new plortz.tool.InsertSoilLayer(0, soil_type, amount, center, radius);
    }
    
    @Override
    protected Tool getRectangleTool(SoilLayer.Type soil_type, double amount, Position center, int width, int length) {
        return new plortz.tool.InsertSoilLayer(0, soil_type, amount, center, width, length);
    }
}
