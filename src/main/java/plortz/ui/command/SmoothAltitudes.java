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
import plortz.tool.Tool;
import plortz.tool.filters.AverageSmoothingFilter;
import plortz.tool.filters.MedianSmoothingFilter;
import plortz.tool.filters.SmoothingFilter;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SmoothAltitudes extends Command {
    @Override
    public void execute(UserInterface ui) {
        if (!this.requireTerrain(ui)) {
            return;
        }

        if (this.args.size() != 1 && this.args.size() != 3) {
            this.showUsage(ui);
            return;
        }
        
        SmoothingFilter filter = null;
        if (this.args.size() == 1) {
            filter = new MedianSmoothingFilter(3);
        } else if (this.args.size() == 3) {
            int window_size = Integer.parseInt(this.args.get(2));
            if (this.args.get(1).equals("median")) {
                filter = new MedianSmoothingFilter(window_size);
            } else if (this.args.get(1).equals("average")) {
                filter = new AverageSmoothingFilter(window_size);
            } else {
                ui.showMessage("Unknown filter type: " + this.args.get(1));
                return;
            }
        }
        Tool tool = new plortz.tool.ApplyFilter(filter);
        tool.apply(ui.getTerrain());
    }

    @Override
    public String getShortDescription() {
        return "Smooth the altitudes to remove irregularities.";
    }

    @Override
    public List<String> getUsage() {
        List<String> rv = new MyArrayList<>(String.class);
        rv.add("Usage: " + this.args.get(0) + " [filter window_size] ");
        rv.add("Where [filter] is one of: median, average");
        rv.add("And [window_size] is an uneven integer number, minimum 3.");
        rv.add("Defaults: filter=median, window_size=3");
        return rv;
    }
}
