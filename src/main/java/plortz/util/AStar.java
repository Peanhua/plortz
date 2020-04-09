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
import plortz.terrain.Position;

/**
 * A-Star path finding algorithm.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AStar extends PathFinder {

    private class PathComponent {
        public PathComponent source;
        public Position      position;
        public double        cost;
        
        public PathComponent(PathComponent source, Position position, double cost) {
            this.source      = source;
            this.position    = position;
            this.cost        = cost;
        }
    }
    private List<PathComponent> open;
    private List<PathComponent> closed;
    
    @Override
    public List<Position> find(Position start, PathFinderHeuristic heuristic) {
        this.open   = new ArrayList<>(); // todo: use priority queue
        this.closed = new ArrayList<>(); // todo: use a fast set (hashed)
        
        this.open.add(new PathComponent(null, start, heuristic.estimateCost(null, start)));
        PathComponent last = null;
        while (this.open.size() > 0) {
            // Find the one with shortest path to the destination:
            PathComponent current = null;
            for (PathComponent pc : this.open) {
                if (current == null || pc.cost < current.cost) {
                    current = pc;
                }
            }
            // Mark current as closed:
            this.open.remove(current);
            this.closed.add(current);
            // Check if the path was found:
            if (heuristic.isAtDestination(current.position)) {
                last = current;
                break;
            }
            // Add neighboring positions:
            for (Position neighbor_pos : heuristic.getNeighbors(current.position)) {
                // Search if this neighbor is already in one of the lists:
                PathComponent existing = null;
                boolean existing_is_closed = false;
                for (int i = 0; existing == null && i < this.open.size(); i++) {
                    if (this.open.get(i).position.equals(neighbor_pos)) {
                        existing = this.open.get(i);
                    }
                }
                for (int i = 0; existing == null && i < this.closed.size(); i++) {
                    if (this.closed.get(i).position.equals(neighbor_pos)) {
                        existing = this.closed.get(i);
                        existing_is_closed = true;
                    }
                }
                // Either update the existing or add a new path component:
                double neighbor_cost = heuristic.estimateCost(current.position, neighbor_pos);
                if (existing != null) {
                    // Some path to the neighbor already exists.
                    if (neighbor_cost < existing.cost) {
                        // This current new path is faster, so use it instead:
                        existing.source = current;
                        existing.cost   = neighbor_cost;
                        if (existing_is_closed) {
                            // When dealing with non-consistent heuristic, the node needs to be re-added to the open list:
                            this.closed.remove(existing);
                            this.open.add(existing);
                        }
                    }
                } else {
                    // No path exists to the neighbor, add it:
                    this.open.add(new PathComponent(current, neighbor_pos, neighbor_cost));
                }
            }
        }
        
        if (last == null) {
            // No path.
            return null;
        }
        
        // Return list of positions from start to end:
        List<Position> path = new ArrayList<>();
        while (last != null) {
            path.add(0, last.position);
            last = last.source;
        }
        // todo: reverse the path smarter or create a variation of ArrayList where inserting at the beginning is fast
        return path;
    }
}
