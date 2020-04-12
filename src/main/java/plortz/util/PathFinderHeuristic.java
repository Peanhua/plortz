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
package plortz.util;

import java.util.List;

/**
 * The callback object passed to the path finding algorithms.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class PathFinderHeuristic {
    public abstract double  estimateCost(Position previous, Position current);

    public abstract boolean isValidNextDestination(Position from, Position to);

    public abstract boolean isAtDestination(Position position);

    public List<Position> getNeighbors(Position current) {
        List<Position> neighbors = new ArrayList<>();
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                Position pos = new Position(current, dx, dy);
                if (this.isValidNextDestination(current, pos)) {
                    neighbors.add(pos);
                }
            }
        }
        return neighbors;
    }
}
