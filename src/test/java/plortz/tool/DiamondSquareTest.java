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

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.Position;
import plortz.Terrain;
import plortz.Tile;
import plortz.TileType;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class DiamondSquareTest {
    
    private class TestRandom extends Random {
        @Override
        public double nextDouble() {
            return 1.0;
        }
    }
    
    private class TestTile extends Tile {
        
        public int altitude_adjust_counter;

        public TestTile(TileType type, double altitude) {
            super(type, altitude);
            this.altitude_adjust_counter = 0;
        }
        
        public TestTile(Tile tile) {
            super(tile);
            this.altitude_adjust_counter = 0;
        }
        
        @Override
        public void adjustAltitude(double altitude) {
            super.setAltitude(altitude);
            this.altitude_adjust_counter++;
        }
    }
    
    private double        testdelta;
    private Terrain       terrain;
    private DiamondSquare tool;
    
    public DiamondSquareTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testdelta = 0.000001;
        terrain = new Terrain(9, 9);
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                terrain.setTile(new Position(x, y), new TestTile(terrain.getTile(x, y)));
            }
        }
        tool = new DiamondSquare(1, new TestRandom());
    }
    
    @After
    public void tearDown() {
    }
    
    private boolean isCorner(Terrain terrain, int x, int y) {
        if (x == 0 && y == 0) {
            return true;
        }
        if (x == terrain.getWidth() - 1 && y == 0) {
            return true;
        }
        if (x == 0 && y == terrain.getHeight() - 1) {
            return true;
        }
        if (x == terrain.getWidth() - 1 && y == terrain.getHeight() - 1) {
            return true;
        }
        return false;
    }
        

    @Test
    public void allTilesAreAltered() {
        tool.apply(terrain);
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if (!isCorner(terrain, x, y)) {
                    assertTrue(terrain.getTile(x, y).getAltitude(false) > testdelta);
                }
            }
        }
    }

    @Test
    public void allTilesAreAlteredOnlyOnce() {
        tool.apply(terrain);
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if (!isCorner(terrain, x, y)) {
                    TestTile tt = (TestTile) terrain.getTile(x, y);
                    assertNotNull(tt);
                    assertEquals(1, tt.altitude_adjust_counter);
                }
            }
        }
    }
}
