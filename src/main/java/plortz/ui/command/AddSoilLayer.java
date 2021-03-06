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

import java.util.ArrayList;
import java.util.List;
import plortz.util.Position;
import plortz.terrain.SoilLayer;
import plortz.tool.Tool;
import plortz.ui.UserInterface;

/**
 * Command to add soil on top of an area.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddSoilLayer extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        
        if (this.args.size() != 7 && this.args.size() != 8) {
            ui.showMessage("Incorrect number of arguments.");
            this.showUsage(ui);
            return;
        }
        
        SoilLayer.Type soil_type = this.parseSoilTypeArg(ui);
        if (soil_type == null) {
            return;
        }
        
        double amount = Double.parseDouble(this.args.get(2));
        
        Position center = new Position(Integer.parseInt(this.args.get(4)), Integer.parseInt(this.args.get(5)));
        
        if (this.args.size() == 7) {
            this.circle(ui, soil_type, amount, center);
        } else if (this.args.size() == 8) {
            this.rectangle(ui, soil_type, amount, center);
        }
    }
    
    @Override
    public String getShortDescription() {
        return "Adds a layer of soil on top of the terrain.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new ArrayList<>();
        rv.add("Usage: " + this.args.get(0) + " <type> <amount> circle <x> <y> <radius>");
        rv.add("       " + this.args.get(0) + " <type> <amount> rect <topleft_x> <topleft_y> <width> <length>");
        return rv;
    }
    
    private SoilLayer.Type parseSoilTypeArg(UserInterface ui) {
        SoilLayer.Type soil_type = null;
        for (SoilLayer.Type t : SoilLayer.Type.values()) {
            if (t.name().toLowerCase().equals(this.args.get(1))) {
                soil_type = t;
                break;
            }
        }
        if (soil_type == null) {
            String validtypes = "";
            for (SoilLayer.Type t : SoilLayer.Type.values()) {
                if (validtypes.length() > 0) {
                    validtypes += ", ";
                }
                validtypes += t.name();
            }
            ui.showMessage("Invalid soil type '" + this.args.get(1) + "'. Valid values are: " + validtypes);
        }
        return soil_type;
    }        
    
    private void circle(UserInterface ui, SoilLayer.Type soil_type, double amount, Position center) {
        if (!this.args.get(3).equals("circle")) {
            ui.showMessage("Error while parsing arguments, expected \"circle\", but got \"" + this.args.get(3) + "\".");
            this.showUsage(ui);
            return;
        }
        int radius = Integer.parseInt(this.args.get(6));
        
        Tool tool = this.getCircleTool(soil_type, amount, center, radius);
        this.applyTool(ui, tool);
    }
    
    protected Tool getCircleTool(SoilLayer.Type soil_type, double amount, Position center, int radius) {
        return new plortz.tool.AddSoilLayer(soil_type, amount, center, radius);
    }

    private void rectangle(UserInterface ui, SoilLayer.Type soil_type, double amount, Position center) {
        if (!this.args.get(3).equals("rect")) {
            ui.showMessage("Error while parsing arguments, expected \"rect\", but got \"" + this.args.get(3) + "\".");
            this.showUsage(ui);
            return;
        }

        int width  = Integer.parseInt(this.args.get(6));
        int length = Integer.parseInt(this.args.get(7));
        
        Tool tool = getRectangleTool(soil_type, amount, center, width, length);
        this.applyTool(ui, tool);
    }
    
    protected Tool getRectangleTool(SoilLayer.Type soil_type, double amount, Position center, int width, int length) {
        return new plortz.tool.AddSoilLayer(soil_type, amount, center, width, length);
    }
}
