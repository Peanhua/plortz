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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.terrain.Position;
import plortz.terrain.SoilLayer;
import plortz.terrain.Terrain;
import plortz.tool.TestTile;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class MedianSmoothingFilterTest {

    private double  testdelta;
    private Terrain terrain;
    private Filter  filter;
    
    public MedianSmoothingFilterTest() {
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
        filter = new MedianSmoothingFilter(3);
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
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void differentValueIsReturned() {
        int count = 0;
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                double original = terrain.getTile(x, y).getAltitude(false);
                double result = filter.filter(terrain, x, y);
                if (Math.abs(original - result) > testdelta) {
                    count++;
                }
            }
        }
        assertTrue(count > terrain.getWidth());
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
        double original = calcSum();
        filter.filter(terrain, 10, 10);
        double afterfilter = calcSum();
        assertEquals(original, afterfilter, testdelta);
    }
}
