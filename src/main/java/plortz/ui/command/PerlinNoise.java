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

import plortz.MersenneTwister;
import plortz.tool.Tool;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class PerlinNoise extends Command {

    @Override
    public void execute(UserInterface ui) {
        double scale   = 1.0;
        double density = 0.5;
        if (this.args.size() >= 2) {
            scale = Double.parseDouble(this.args.get(1));
        }
        if (this.args.size() >= 3) {
            density = Double.parseDouble(this.args.get(2));
        }
        Tool perlin = new plortz.tool.PerlinNoise(scale, density, new MersenneTwister(0));
        perlin.apply(ui.getTerrain());
    }
}
