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
package plortz.tool;

import java.util.List;
import plortz.terrain.Position;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.util.AStar;
import plortz.util.ArrayList;
import plortz.util.PathFinder;
import plortz.util.PathFinder.Heuristic;

/**
 * Tool to add water to a location and carve river with the amount of water stored at the end in a pond/lake/sea.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddWater extends Tool {

    private final Position water_source_position;
    private final double   water_source_amount;
    
    public AddWater(Position position, double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException();
        }
        this.water_source_position = position;
        this.water_source_amount   = amount;
    }
    
    @Override
    public void apply(Terrain terrain) {
        // The Plan:
        //   First add water as a river while there is dry land.
        //   For the dry land turned into water, keep the total altitude the same: decrease dry land as much as water is added.
        //   If another water is hit before the water is exhausted, follow the existing water as long as it goes downhill.
        //   Once the bottom is reached, start expanding the water:
        //     Find the area covered with the current altitude (water included) and find the location where the water expands next (= where the altitude is lowest around the water).
        //     Add water until at the same level as the expand location or until the given water amount is exhausted.
        //       If there is water remaining to be spread, start the process over from the expand location.
        // Postprocess:
        //   Decrease the dry land portion for all tiles that were previously dry, but now have water.
        //   Go through from start and make sure the altitude with water never goes up by decreasing altitude of the later tiles?
        // Mark rivers and lakes/ponds?
        //   Could be used to quickly find the next pond/lake downstream from any point in a river.
        //   Could be used for erosion, to erode the rivers.
        this.addRiverOnDryLand(terrain, this.water_source_position, this.water_source_amount);
        terrain.zeroBottomSoilLayer();
        terrain.changed();
    }

    /**
     * Add water as river while there is dry land.
     * 
     * @param terrain
     * @param position
     * @param water_amount 
     */
    private void addRiverOnDryLand(Terrain terrain, Position position, double water_amount) {
        Heuristic heuristic = new Heuristic() {
            @Override
            public double estimateCost(Position previous, Position current) {
                // The lower the altitude, the better:
                double cost = -terrain.getTile(current).getAltitude(false);
                if (previous != null) {
                    // Also favor shorter distance:
                    int dx = previous.getX() - current.getX();
                    int dy = previous.getY() - current.getY();
                    if (dx == 0 || dy == 0) {
                        cost += 1.0;
                    } else {
                        cost += 1.414;
                    }
                }
                return cost;
            }

            @Override
            public boolean isValidNextDestination(Position from, Position to) {
                if (!terrain.isValidTilePosition(to)) {
                    return false;
                }
                return terrain.getTile(from).getAltitude(false) >= terrain.getTile(to).getAltitude(false);
            }

            @Override
            public boolean isAtDestination(Position position) {
                // Stop when existing water is reached:
                Tile curtile = terrain.getTile(position);
                if (curtile.getWater() > 0.0) {
                    return true;
                }
                // Also stop if all the neighbors are higher:
                double curalt = curtile.getAltitude(false);
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dx == 0 && dy == 0) {
                            continue;
                        }
                        Tile neighbor = terrain.getTile(new Position(position, dx, dy));
                        if (neighbor != null && neighbor.getAltitude(false) < curalt) {
                            return false;
                        }
                    }
                }
                return true;
            }
        };
        
        PathFinder pather = new AStar();
        List<Position> path = pather.find(position, heuristic);
        if (path == null) {
            return;
        }
        for (Position pos : path) {
            Tile tile = terrain.getTile(pos);
            if (tile.getWater() > 0.0) {
                continue;
            }
            double altitude = tile.getAltitude(true);
            terrain.getTile(pos).adjustWater(0.01);
            this.carveTile(tile, altitude);
        }
    }
    
    private void carveTile(Tile tile, double target_water_altitude) {
        while (tile.getAltitude(true) > target_water_altitude) {
            tile.adjustTopSoilAmount(target_water_altitude - tile.getAltitude(true));
        }
        // todo: adjust the neighbor tiles
    }
}
