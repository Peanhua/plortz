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
import plortz.util.Position;
import plortz.terrain.SoilLayer;
import plortz.terrain.Terrain;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AddSoilLayerTest {

    private double  testdelta;
    private Terrain terrain;
    private Tool    circletool;
    private Tool    rectangletool;
    
    public AddSoilLayerTest() {
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
        terrain = new Terrain(100, 100);
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                terrain.setTile(new Position(x, y), new TestTile(terrain.getTile(x, y)));
            }
        }
        circletool = new AddSoilLayer(SoilLayer.Type.SAND, 1.0, new Position(50, 50), 50);
        rectangletool = new AddSoilLayer(SoilLayer.Type.SAND, 1.0, new Position(25, 25), 50, 50);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void rectangleAffectsTheCorrectArea() {
        rectangletool.apply(terrain);
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if (x >= 25 && x < 25 + 50 && y >= 25 && y < 25 + 50) {
                    assertEquals(SoilLayer.Type.SAND, terrain.getTile(x, y).getTopSoil().getType());
                } else {
                    assertEquals(SoilLayer.Type.CLIFF, terrain.getTile(x, y).getTopSoil().getType());
                }
            }
        }
    }
    
    @Test
    public void rectangleAffectsTheCorrectAmount() {
        rectangletool.apply(terrain);
        int count = 0;
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if (terrain.getTile(x, y).getTopSoil().getType() == SoilLayer.Type.SAND) {
                    count++;
                }
            }
        }
        assertEquals(50 * 50, count);
    }

    @Test
    public void circleAffectsApproximatelyTheCorrectAmount() {
        circletool.apply(terrain);
        int count = 0;
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if (terrain.getTile(x, y).getTopSoil().getType() == SoilLayer.Type.SAND) {
                    count++;
                }
            }
        }
        double real = Math.PI * 50 * 50;
        double diff = (double) count - real;
        assertTrue(Math.abs(diff / real) < 0.01);
    }
}
