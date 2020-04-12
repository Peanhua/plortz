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
 * Abstract base class for path finding algorithms.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class PathFinder {
    /**
     * Search for a path from the given start position using the callbacks provided.
     * 
     * @param start     The start position.
     * @param heuristic The callback object.
     * @return          The resulting path, or null if no path was found.
     */
    public abstract List<Position> find(Position start, PathFinderHeuristic heuristic);
}
