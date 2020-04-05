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
import java.util.Random;
import plortz.util.MyArrayList;
import plortz.terrain.Position;
import plortz.terrain.SoilLayer;
import plortz.terrain.Terrain;
import plortz.tool.Tool;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddRandomSoilPatches extends Command {
    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        if (this.args.size() != 4) {
            this.showUsage(ui);
            return;
        }
        int count = Integer.parseInt(this.args.get(1));
        int radius = Integer.parseInt(this.args.get(2));
        double depth = Double.parseDouble(this.args.get(3));
        this.addPatches(ui, count, radius, depth);
    }
    
    private void addPatches(UserInterface ui, int count, int radius, double depth) {
        this.startApplyingTools();
        Terrain terrain = ui.getTerrain();
        Random random = ui.getRandom();
        SoilLayer.Type[] types = SoilLayer.Type.values();
        for (int i = 0; i < count; i++) {
            SoilLayer.Type type = types[random.nextInt(types.length - 1)];
            double r = (double) radius * (0.5 + random.nextDouble());
            Position pos = new Position(random.nextInt(terrain.getWidth() - 1),
                                        random.nextInt(terrain.getLength() - 1));
            Tool tool = new plortz.tool.AddSoilLayer(type, depth, pos, (int) r);
            tool.apply(terrain);
        }
        this.endApplyingTools(ui);
    }
    

    @Override
    public String getShortDescription() {
        return "Add random circle patches of soil.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new MyArrayList<>();
        rv.add("Usage: " + this.args.get(0) + " <count> <average radius> <soil depth>");
        return rv;
    }
}
