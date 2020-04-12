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
package plortz.search;

import plortz.util.Position;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class PathFinderTestHeuristic extends PathFinderHeuristic {

    private String[] map;
    private Position destination;

    public PathFinderTestHeuristic(String[] map, Position destination) {
        this.map         = map;
        this.destination = destination;
    }

    @Override
    public double estimateCost(Position previous, Position current) {
        double dx = current.getX() - this.destination.getX();
        double dy = current.getY() - this.destination.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean isValidNextDestination(Position from, Position to) {
        if (to.getX() < 0 || to.getX() >= map[0].length()) {
            return false;
        }
        if (to.getY() < 0 || to.getY() >= map.length) {
            return false;
        }
        if (map[to.getY()].charAt(to.getX()) != ' ') {
            return false;
        }
        return true;
    }

    @Override
    public boolean isAtDestination(Position position) {
        return position.equals(this.destination);
    }
}
