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
import plortz.Vector;
import plortz.collections.MyArrayList;
import plortz.terrain.Terrain;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Info extends Command {

    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }
        Terrain terrain = ui.getTerrain();
        ui.showMessage("Info:");
        ui.showMessage(" terrain dimensions: " + terrain.getWidth() + "x" + terrain.getLength());
        Vector minmax = terrain.getAltitudeRange();
        ui.showMessage(" lowest point: " + minmax.getX());
        ui.showMessage(" highest point: " + minmax.getY());
    }

    @Override
    public String getShortDescription() {
        return "Shows information.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new MyArrayList<>(String.class);
        rv.add("Usage: " + args.get(0));
        return rv;
    }
}
