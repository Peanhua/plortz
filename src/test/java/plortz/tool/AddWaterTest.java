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

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.util.Position;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.util.MersenneTwister;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddWaterTest {
    
    Terrain terrain;
    double testdelta;
    
    public AddWaterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        terrain = new Terrain(4, 4);
        Random r = new MersenneTwister(0);
        for (Tile t : terrain) {
            t.adjustTopSoilAmount(0.1 * r.nextDouble());
        }
        testdelta = 0.00001;
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void startTileIsWatered() {
        Tool tool = new AddWater(new Position(2, 2), 1);
        tool.apply(terrain);
        assertTrue(terrain.getTile(2, 2).getWater() > 0.0);
    }
    
    @Test
    public void addingWaterToEmptyTerrainFillsItWithWater() {
        Tool tool = new AddWater(new Position(0, 0), 100);
        tool.apply(terrain);
        for (Tile t : terrain) {
            assertTrue(t.getWater() > 0.0);
        }
    }
    
}
