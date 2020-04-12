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
    /**
     * Estimate the cost from the current position to the destination.
     * <p>
     * Not used by all path finding algorithms.
     * May not over-estimate.
     * 
     * @param previous The position where the current node was reached from.
     * @param current  The current position from where to estimate the cost.
     * @return         The cost, relative to the other costs (absolute value does not matter).
     */
    public abstract double estimateCost(Position previous, Position current);

    /**
     * Check if it is valid to move to the given position.
     * 
     * @param from The position where we're coming from.
     * @param to   The target position.
     * @return     True if it is possible to move from "from" to "to".
     */
    public abstract boolean isValidNextDestination(Position from, Position to);

    /**
     * Check if the given position is at the destination.
     * 
     * @param position The position to check.
     * @return         True if the given position is at the destination.
     */
    public abstract boolean isAtDestination(Position position);

    /**
     * Return all possible neighbors for the given position.
     * 
     * @param current The position whose neighbors are returned.
     * @return        The neighbors of the current position.
     */
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
