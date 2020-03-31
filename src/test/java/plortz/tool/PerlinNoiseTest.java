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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.terrain.Position;
import plortz.terrain.Terrain;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class PerlinNoiseTest {
    
    private double  testdelta;
    private Terrain terrain;
    private Tool    tool;
    
    public PerlinNoiseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testdelta = 0.00001;
        this.terrain = new Terrain(100, 100);
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                terrain.setTile(new Position(x, y), new TestTile(terrain.getTile(x, y)));
            }
        }
        tool = new PerlinNoise(1.0, 0.2, new TestRandom());
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void allTilesAreAltered() {
        tool.apply(terrain);
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                TestTile tt = (TestTile) terrain.getTile(x, y);
                assertNotNull(tt);
                assertTrue(tt.altitude_adjust_counter > 0);
            }
        }
    }

    @Test
    public void allTilesAreAlteredOnlyOnce() {
        tool.apply(terrain);
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                TestTile tt = (TestTile) terrain.getTile(x, y);
                assertNotNull(tt);
                assertEquals(1, tt.altitude_adjust_counter);
            }
        }
    }
    
}
