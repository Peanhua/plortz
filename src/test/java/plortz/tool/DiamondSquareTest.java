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

import java.security.InvalidParameterException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.util.Position;
import plortz.terrain.Terrain;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class DiamondSquareTest {
    
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
        for (int y = 0; y < terrain.getLength(); y++) {
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
        if (x == 0 && y == terrain.getLength() - 1) {
            return true;
        }
        if (x == terrain.getWidth() - 1 && y == terrain.getLength() - 1) {
            return true;
        }
        return false;
    }
        

    @Test
    public void allTilesAreAltered() {
        tool.apply(terrain);
        for (int y = 0; y < terrain.getLength(); y++) {
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
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if (!isCorner(terrain, x, y)) {
                    TestTile tt = (TestTile) terrain.getTile(x, y);
                    assertNotNull(tt);
                    assertEquals(1, tt.altitude_adjust_counter);
                }
            }
        }
    }
    
    @Test
    public void invalidTerrainSizeThrowException() {
        int[] sizes = {
            1, 1,
            2, 2,
            3, 2,
            10, 9,
            9, 10,
            8, 8,
            1024, 1024
        };
        for (int i = 0; i < sizes.length; i += 2) {
            boolean thrown = false;
            Terrain t = new Terrain(sizes[i + 0], sizes[i + 1]);
            try {
                tool.apply(t);
            } catch (InvalidParameterException e) {
                thrown = true;
            }
            assertTrue(thrown);
        }
    }
}
