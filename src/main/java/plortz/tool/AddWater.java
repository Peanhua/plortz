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
import plortz.util.FloodFill;
import plortz.util.PathFinder;
import plortz.util.PathFinder.Heuristic;
import plortz.util.Static2dArray;

/**
 * Tool to add water to a location and carve river with the amount of water stored at the end in a pond/lake/sea.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddWater extends Tool {

    private final Position water_source_position;
    private final double   water_source_amount;
    private List<Position> new_water;
    
    public AddWater(Position position, double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException();
        }
        this.water_source_position = position;
        this.water_source_amount   = amount;
        this.new_water = new ArrayList<>();
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
        /*
        System.out.println("New water:");
        for (Position pos : new_water) {
            Tile t = terrain.getTile(pos);
            System.out.println("  " + pos + ": altitude=" + t.getAltitude(true) + ", water=" + t.getWater());
        }
        */
    }


    private abstract class AddWaterPathFinderHeuristic implements Heuristic {
        
        protected final Terrain terrain;
        
        public AddWaterPathFinderHeuristic(Terrain terrain) {
            this.terrain = terrain;
        }
        
        @Override
        public double estimateCost(Position previous, Position current) {
            // The lower the altitude, the better:
            double cost = -this.terrain.getTile(current).getAltitude(false);
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
    }
    
    
    /**
     * Add water as river while there is dry land.
     * 
     * @param terrain
     * @param position
     * @param water_amount 
     * @return             The last position of the river.
     */
    private void addRiverOnDryLand(Terrain terrain, Position position, double water_amount) {
        
        class AddRiverOnDryLandPathFinderHeuristic extends AddWaterPathFinderHeuristic {

            public AddRiverOnDryLandPathFinderHeuristic(Terrain terrain) {
                super(terrain);
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
        }
        // Find path for the river:
        PathFinder pather = new AStar();
        Heuristic heuristic = new AddRiverOnDryLandPathFinderHeuristic(terrain);
        List<Position> path = pather.find(position, heuristic);
        if (path == null) {
            return;
        }
        // If the last tile in the path contains water, follow it:
        Position lastpos = path.get(path.size() - 1);
        boolean follow_river = terrain.getTile(lastpos).getWater() > 0.0;
        // But first add water to the new river portion:
        for (Position pos : path) {
            Tile tile = terrain.getTile(pos);
            if (tile.getWater() > 0.0) {
                continue;
            }
            double altitude = tile.getAltitude(true);
            terrain.getTile(pos).adjustWater(0.01);
            this.carveTile(tile, altitude);
            //System.out.println("new river at " + pos + " -> water depth = " + tile.getWater());
            if (!new_water.contains(pos))
                new_water.add(pos);
        }
        if (follow_river) {
            lastpos = this.followExistingRiver(terrain, lastpos);
        }
        if (lastpos != null) {
            this.expandWater(terrain, lastpos, water_amount);
        }
    }
    
    /**
     * Follow the river from start until it levels out or ends.
     * 
     * @param terrain
     * @param start
     * @return        The last position of the river.
     */
    private Position followExistingRiver(Terrain terrain, Position start) {
        
        class FollowExistingRiverPathFinderHeuristic extends AddWaterPathFinderHeuristic {

            public FollowExistingRiverPathFinderHeuristic(Terrain terrain) {
                super(terrain);
            }

            @Override
            public boolean isValidNextDestination(Position from, Position to) {
                if (!terrain.isValidTilePosition(to)) {
                    return false;
                }
                Tile totile = terrain.getTile(to);
                if (totile.getWater() < 0.0) {
                    return false;
                }
                return terrain.getTile(from).getAltitude(false) >= totile.getAltitude(false);
            }

            @Override
            public boolean isAtDestination(Position position) {
                // Stop when there is no neighbor water at lower altitude:
                Tile curtile = terrain.getTile(position);
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
        }
        
        // Follow the river:
        PathFinder pather = new AStar();
        Heuristic heuristic = new FollowExistingRiverPathFinderHeuristic(terrain);
        List<Position> path = pather.find(start, heuristic);
        if (path == null) {
            return null;
        }
        /*
        System.out.println("Followed river: ");
        for (Position pos : path) {
            System.out.println("  " + pos);
        }
*/
        
        return path.get(path.size() - 1);
    }


    private void expandWater(Terrain terrain, Position start, double water_amount) {
        class LakeFinder implements FloodFill.FloodFillCallback {

            private final Terrain terrain;
            private final double  water_level;
            
            public List<Position> filled;
            
            public LakeFinder(Terrain terrain, double water_level) {
                this.terrain     = terrain;
                this.water_level = water_level;
                this.filled      = new ArrayList<>();
            }
            
            @Override
            public boolean isPositionIn(Position position) {
                Tile tile = this.terrain.getTile(position);
                if (tile == null) {
                    return false;
                }
                if (tile.getWater() < 0.0) {
                    return false;
                }
                return Math.abs(tile.getAltitude(true) - water_level) < 0.001;
            }

            @Override
            public void fill(Position position) {
                this.filled.add(position);
            }
        }
        
        LakeFinder lakefinder = new LakeFinder(terrain, terrain.getTile(start).getAltitude(true));
        FloodFill ff = new FloodFill();
        ff.fill(terrain.getWidth(), terrain.getLength(), start, lakefinder);
        //System.out.println("lake size=" + lakefinder.filled.size());
        for (Position pos : lakefinder.filled) {
            //System.out.println(pos + " altitude=" + terrain.getTile(pos).getAltitude(true) + ", water=" + terrain.getTile(pos).getWater());
        }

        Position floodpos = null; // The location from where to start flooding out if there is extra water.
        for (Position pos : ff.getBorders()) {
            if (floodpos == null || terrain.getTile(pos).getAltitude(false) < terrain.getTile(floodpos).getAltitude(false)) {
                floodpos = pos;
            }
        }
        //System.out.println("floodpos=" + floodpos + ", altitude=" + terrain.getTile(floodpos).getAltitude(true) + ", water=" + terrain.getTile(floodpos).getWater());

        double amount_per_tile = water_amount / (double) lakefinder.filled.size();
        double max_amount_per_tile = terrain.getTile(floodpos).getAltitude(true) - terrain.getTile(start).getAltitude(true);
        if (amount_per_tile > max_amount_per_tile) {
            amount_per_tile = max_amount_per_tile;
        }
        //System.out.println("water amount per tile = " + amount_per_tile);
        if (amount_per_tile > 0.0) {
            // Fill the lake:
            for (Position pos : lakefinder.filled) {
                //System.out.print("fill " + pos + " alt=" + terrain.getTile(pos).getAltitude(true) + ", water=" + terrain.getTile(pos).getWater());
                terrain.getTile(pos).adjustWater(amount_per_tile);
                //System.out.println(" -> altitude=" + terrain.getTile(pos).getAltitude(true) + ", water=" + terrain.getTile(pos).getWater());
                water_amount -= amount_per_tile;
            }
        } else {
            // Fill the floodpos with water because it is lower than the lake:
            double amount = -amount_per_tile;
            terrain.getTile(floodpos).adjustWater(amount);
            water_amount -= amount;
            //System.out.println("filled floodpos: new altitude=" + terrain.getTile(floodpos).getAltitude(true) + ", water=" + terrain.getTile(floodpos).getWater());
        }
        // If there is extra water, start a new river from the floodpos:
        if (water_amount > 0.0001) {
            //System.out.println("start new river at " + floodpos + ", water_amount=" + water_amount);
            this.addRiverOnDryLand(terrain, floodpos, water_amount);
        }
    }
    
    private void carveTile(Tile tile, double target_water_altitude) {
        while (tile.getAltitude(true) > target_water_altitude) {
            tile.adjustTopSoilAmount(target_water_altitude - tile.getAltitude(true));
        }
        // todo: adjust the neighbor tiles
    }
}
