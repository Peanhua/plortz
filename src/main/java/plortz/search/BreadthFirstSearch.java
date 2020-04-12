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

import java.util.List;
import plortz.util.ArrayList;
import plortz.util.Position;

/**
 * Breadth-first path finding algorithm.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class BreadthFirstSearch extends PathFinder {

    private class State {
        private final State    previous;
        private final Position position;
        
        public State(State previous, Position position) {
            this.previous = previous;
            this.position = position;
        }
        
        public State getPrevious() {
            return this.previous;
        }
        
        public Position getPosition() {
            return this.position;
        }
    }
    
    
    @Override
    public List<Position> find(Position start, PathFinderHeuristic heuristic) {
        State destination = this.findPath(start, heuristic);
        if (destination == null) {
            return null;
        }
        List<Position> path = new ArrayList<>();
        while (destination != null) {
            path.add(0, destination.getPosition());
            destination = destination.getPrevious();
        }
        return path;
    }

    private State findPath(Position start, PathFinderHeuristic heuristic) {
        List<State>    queue   = new ArrayList<>();
        List<Position> visited = new ArrayList<>();
        
        queue.add(new State(null, start));
        while (!queue.isEmpty()) {
            State node = queue.remove(0);
            Position nodepos = node.getPosition();
            if (heuristic.isAtDestination(nodepos)) {
                return node;
            }
            
            if (!visited.contains(nodepos)) {
                visited.add(nodepos);
                for (Position pos : heuristic.getNeighbors(nodepos)) {
                    queue.add(new State(node, pos));
                }
            }
        }
        return null;
    }
}
