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
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.util.MersenneTwister;
import plortz.util.Vector;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class ScaleHeightsTest {
    
    private Terrain terrain;
    private double  testdelta;
    
    public ScaleHeightsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        terrain = new Terrain(100, 100);
        testdelta = 0.00001;
        Random r = new MersenneTwister(0);
        for (Tile t : terrain) {
            t.adjustTopSoilAmount(r.nextDouble());
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void scalingChangesSoilAmountsCorrectly() {
        Vector original = terrain.getAltitudeRange();
        double factor = 1.53;
        Tool tool = new ScaleHeights(factor);
        tool.apply(terrain);
        Vector changed = terrain.getAltitudeRange();
        assertEquals(original.multiply(factor).getX(), changed.getX(), testdelta);
        assertEquals(original.multiply(factor).getY(), changed.getY(), testdelta);
    }
    
}
