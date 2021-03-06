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
package plortz.terrain;

import java.util.ArrayList;
import plortz.util.Position;
import java.util.Iterator;

/**
 * A list containing only valid positions for the given terrain.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class ValidPositionList implements Iterable<Position> {
    private final Terrain               terrain;
    private final ArrayList<Position> positions;
    
    public ValidPositionList(Terrain terrain) {
        this.terrain   = terrain;
        this.positions = new ArrayList<>();
    }
    
    public void clear() {
        this.positions.clear();
    }
    
    public int size() {
        return this.positions.size();
    }
    
    /**
     * Adds the given position to the list, if the position is not valid for the terrain, it is silently discarded.
     * @param position The position to add.
     */
    public void add(Position position) {
        if (position == null) {
            return;
        }
        if (this.terrain.isValidTilePosition(position)) {
            this.positions.add(position);
        }
    }
    
    /**
     * Adds the given position to the list, if the position is not valid for the terrain, it is silently discarded.
     * @param x The coordinate in x-axis of the position to add.
     * @param y The coordinate in y-axis of the position to add.
     */
    public void add(int x, int y) {
        this.add(new Position(x, y));
    }

    @Override
    public Iterator<Position> iterator() {
        return new Iterator<Position>() {
            private int pos = 0;
            
            @Override
            public boolean hasNext() {
                return pos < positions.size();
            }
            
            @Override
            public Position next() {
                Position p = positions.get(pos);
                pos++;
                return p;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("not supported");
            }
        };
    }
}
