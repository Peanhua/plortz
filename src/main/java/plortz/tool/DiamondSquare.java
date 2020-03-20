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
package plortz.tool;

import java.security.InvalidParameterException;
import java.util.Random;
import plortz.Position;
import plortz.Terrain;
import plortz.Tile;
import plortz.ValidPositionList;

/**
 * Adjusts the altitudes using the diamond-square algorithm.
 * 
 * Altitude changes are in the range [-scale, +scale].
 *
 * https://en.wikipedia.org/wiki/Diamond-square_algorithm
 * 
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class DiamondSquare extends Tool {

    private final double   scale;
    private Random         random;

    /**
     * 
     * @param scale          Scaling factor.
     * @param random         Random number generator.
     */
    public DiamondSquare(double scale, Random random) {
        this.scale  = scale;
        this.random = random;
    }

    
    @Override
    public void apply(Terrain terrain) {
        if (terrain.getWidth() != terrain.getHeight() || !this.checkSize(terrain.getWidth())) {
            throw new InvalidParameterException("Invalid terrain dimensions (must be 2^n+1)");
        }
        
        for (int distance = terrain.getWidth() - 1; distance > 1; distance /= 2) {
            diamondStep(terrain, distance);
            squareStep(terrain, distance);
        }
    }
    
    
    private void diamondStep(Terrain terrain, int distance) {
        ValidPositionList positions = new ValidPositionList(terrain);
        Position middle = new Position(0, 0);
        for (int y = 0; y <= terrain.getHeight() - distance; y += distance) {
            for (int x = 0; x <= terrain.getWidth() - distance; x += distance) {
                positions.clear();
                positions.add(x,            y);
                positions.add(x + distance, y);
                positions.add(x,            y + distance);
                positions.add(x + distance, y + distance);
                middle.set(x + distance / 2, y + distance / 2);
                Tile tile = terrain.getTile(middle);
                tile.adjustAltitude(this.getAltitudeChange(terrain, positions));
            }
        }
    }

    
    private void squareStep(Terrain terrain, int distance) {
        ValidPositionList positions = new ValidPositionList(terrain);
        Position middle = new Position(0, 0);
        for (int y = 0; y < terrain.getHeight(); y += distance) {
            for (int x = 0; x < terrain.getWidth(); x += distance) {
                positions.clear();
                // The middle is centered to the right of x, y:
                middle.set(x + distance / 2, y);
                if (terrain.isValidTilePosition(middle)) {
                    positions.add(x,                y);
                    positions.add(x + distance / 2, y - distance / 2);
                    positions.add(x + distance,     y);
                    positions.add(x + distance / 2, y + distance / 2);
                    Tile tile = terrain.getTile(middle);
                    tile.adjustAltitude(this.getAltitudeChange(terrain, positions));
                }
                // The middle is centered to below of x, y:
                middle.set(x, y + distance / 2);
                if (terrain.isValidTilePosition(middle)) {
                    positions.add(x,                y);
                    positions.add(x - distance / 2, y + distance / 2);
                    positions.add(x + distance / 2, y + distance / 2);
                    positions.add(x,                y + distance);
                    Tile tile = terrain.getTile(middle);
                    tile.adjustAltitude(this.getAltitudeChange(terrain, positions));
                }
            }
        }
    }

    
    private double getAltitudeChange(Terrain terrain, ValidPositionList positions) {
        double average = 0.0;
        for (Position p : positions) {
            average += terrain.getTile(p).getAltitude(false);
        }
        average /= (double) positions.size();
        
        return average + (this.random.nextDouble() * 2.0 - 1.0) * this.scale;
    }
    
    
    /**
     * Checks that the size is 2^n+1
     * 
     * @param size
     * @return true if the size is valid
     */
    private boolean checkSize(int size) {
        if (size % 2 != 1) {
            return false;
        }
        
        size -= 1;
        int tmp = 1;
        while (tmp < size) {
            tmp = tmp * 2;
        }
        if (tmp > size) {
            return false;
        }

        return true;
    }
}
