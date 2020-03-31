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
import plortz.terrain.Terrain;
import plortz.terrain.Tile;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class GaussianDistributionTest {
    
    private double               testdelta;
    private Terrain              terrain;
    private GaussianDistribution gauss;
    
    public GaussianDistributionTest() {
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
        terrain = new Terrain(10, 10);
        gauss = new GaussianDistribution(5, 5, 0.4, 3, 1);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void centerIsElevated() {
        gauss.apply(terrain);
        assertTrue(terrain.getTile(5, 5).getAltitude(false) > 0.1);
    }

    @Test
    public void centerIsHighest() {
        gauss.apply(terrain);
        double center = terrain.getTile(5, 5).getAltitude(false);
        for (Tile t : terrain) {
            assertTrue(t.getAltitude(false) <= center);
        }
    }
    
    @Test
    public void heightsLookOk() {
        // These values were taken when the code seems to work, the values have not been verified with some other implementation:
        double[] heights = {
            0.000000, 0.000001, 0.000007, 0.000042, 0.000120, 0.000169, 0.000120, 0.000042, 0.000007, 0.000001, 
            0.000001, 0.000015, 0.000169, 0.000961, 0.002725, 0.003856, 0.002725, 0.000961, 0.000169, 0.000015, 
            0.000007, 0.000169, 0.001925, 0.010927, 0.030966, 0.043821, 0.030966, 0.010927, 0.001925, 0.000169, 
            0.000042, 0.000961, 0.010927, 0.062012, 0.175738, 0.248693, 0.175738, 0.062012, 0.010927, 0.000961, 
            0.000120, 0.002725, 0.030966, 0.175738, 0.498031, 0.704780, 0.498031, 0.175738, 0.030966, 0.002725, 
            0.000169, 0.003856, 0.043821, 0.248693, 0.704780, 0.997356, 0.704780, 0.248693, 0.043821, 0.003856, 
            0.000120, 0.002725, 0.030966, 0.175738, 0.498031, 0.704780, 0.498031, 0.175738, 0.030966, 0.002725, 
            0.000042, 0.000961, 0.010927, 0.062012, 0.175738, 0.248693, 0.175738, 0.062012, 0.010927, 0.000961, 
            0.000007, 0.000169, 0.001925, 0.010927, 0.030966, 0.043821, 0.030966, 0.010927, 0.001925, 0.000169, 
            0.000001, 0.000015, 0.000169, 0.000961, 0.002725, 0.003856, 0.002725, 0.000961, 0.000169, 0.000015 
        };
        gauss.apply(terrain);
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                assertEquals(heights[x + y * terrain.getWidth()], terrain.getTile(x, y).getAltitude(false), testdelta);
            }
        }
    }

}
