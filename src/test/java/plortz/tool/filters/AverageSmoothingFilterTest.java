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
package plortz.tool.filters;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.util.Position;
import plortz.terrain.SoilLayer;
import plortz.terrain.Terrain;
import plortz.tool.TestTile;
import plortz.util.MersenneTwister;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AverageSmoothingFilterTest {
    
    private   double  testdelta;
    private   Terrain terrain;
    private   Terrain rough_terrain;
    protected Filter  filter;
    
    public AverageSmoothingFilterTest() {
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
        terrain = new Terrain(20, 20, SoilLayer.Type.SAND);
        filter = new AverageSmoothingFilter(3);
        for (int y = 0; y < terrain.getLength(); y++) {
            int oddeven = y % 2;
            for (int x = 0; x < terrain.getWidth(); x++) {
                terrain.getTile(x, y).adjustTopSoilAmount(((x + oddeven) % 2) * 10);
            }
        }
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                terrain.setTile(new Position(x, y), new TestTile(terrain.getTile(x, y)));
            }
        }
        rough_terrain = new Terrain(100, 100, SoilLayer.Type.SAND);
        Random r = new MersenneTwister(0);
        for (int y = 0; y < rough_terrain.getLength(); y++) {
            for (int x = 0; x < rough_terrain.getWidth(); x++) {
                rough_terrain.setTile(new Position(x, y), new TestTile(rough_terrain.getTile(x, y)));
                rough_terrain.getTile(x, y).adjustTopSoilAmount(r.nextDouble() * 10.0);
            }
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void differentValueIsReturned() {
        if (filter.isPrefiltering()) {
            for (int y = 0; y < terrain.getLength(); y++) {
                for (int x = 0; x < terrain.getWidth(); x++) {
                    filter.preFilter(terrain, x, y);
                }
            }
        }
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                double original = terrain.getTile(x, y).getAltitude(false);
                double result = filter.filter(terrain, x, y);
                assertNotEquals(original, result, testdelta);
            }
        }
    }
    
    @Test
    public void differentValuesForRoughTerrain() {
        int diffcount = 0;
        if (filter.isPrefiltering()) {
            for (int y = 0; y < rough_terrain.getLength(); y++) {
                for (int x = 0; x < rough_terrain.getWidth(); x++) {
                    filter.preFilter(rough_terrain, x, y);
                }
            }
        }
        for (int y = 0; y < rough_terrain.getLength(); y++) {
            for (int x = 0; x < rough_terrain.getWidth(); x++) {
                double original = rough_terrain.getTile(x, y).getAltitude(false);
                double result = filter.filter(rough_terrain, x, y);
                if (Math.abs(original - result) > testdelta) {
                    diffcount++;
                }
            }
        }
        // At least 50% must come out different.
        assertTrue(diffcount > rough_terrain.getWidth() * rough_terrain.getLength() / 2);
    }
    
    private double calcSum() {
        double sum = 0.0;
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                sum += terrain.getTile(x, y).getAltitude(false);
            }
        }
        return sum;
    }
    
    @Test
    public void tilesAreNotAdjusted() {
        if (filter.isPrefiltering()) {
            for (int y = 0; y < terrain.getLength(); y++) {
                for (int x = 0; x < terrain.getWidth(); x++) {
                    filter.preFilter(terrain, x, y);
                }
            }
        }
        double original = calcSum();
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                filter.filter(terrain, x, y);
            }
        }
        double afterfilter = calcSum();
        assertEquals(original, afterfilter, testdelta);
    }
}
