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
package plortz.terrain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class TerrainTest {

    private Terrain terrain;
    
    public TerrainTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        terrain = new Terrain(2, 5);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testIteration() {
        int count = 0;
        for (Tile t : terrain)
            count++;
        assertEquals(10, count);
    }
    
    @Test
    public void basicGettersWork() {
        assertEquals(2, terrain.getWidth());
        assertEquals(5, terrain.getHeight());
    }
    
    @Test
    public void getTileReturnsTile() {
        assertNotNull(terrain.getTile(0, 0));
    }
    
    @Test
    public void getTileWithPositionReturnsTile() {
        assertNotNull(terrain.getTile(new Position(0, 0)));
    }
    
    @Test
    public void getTileForIncorrectCoordinatesReturnsNull() {
        assertNull(terrain.getTile(-1, 0));
        assertNull(terrain.getTile(0, -1));
        assertNull(terrain.getTile(99, 0));
        assertNull(terrain.getTile(0, 99));
    }

    @Test
    public void getTileForIncorrectPositionReturnsNull() {
        assertNull(terrain.getTile(null));
        assertNull(terrain.getTile(new Position(-1, 0)));
        assertNull(terrain.getTile(new Position(0, -1)));
        assertNull(terrain.getTile(new Position(99, 0)));
        assertNull(terrain.getTile(new Position(0, 99)));
    }
    
    @Test
    public void isValidTilePositionWorksForValidPositions() {
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                assertTrue(terrain.isValidTilePosition(new Position(x, y)));
            }
        }
    }
    
    @Test
    public void isValidTilePositionReturnsFalseForInvalidPositions() {
        assertFalse(terrain.isValidTilePosition(null));
        assertFalse(terrain.isValidTilePosition(new Position(-1, 0)));
        assertFalse(terrain.isValidTilePosition(new Position(0, -1)));
        assertFalse(terrain.isValidTilePosition(new Position(99, 0)));
        assertFalse(terrain.isValidTilePosition(new Position(0, 99)));
    }
    
    @Test
    public void copyConstructorDoesDeepCopy() {
        Terrain t = new Terrain(terrain);
        assertNotEquals(terrain.getTile(0, 0), t.getTile(0, 0));
        t.getTile(0, 0).setTopSoilAmount(1.0);
        assertTrue(t.getTile(0, 0).getAltitude(false) - terrain.getTile(0, 0).getAltitude(false) > 0.1);
    }
    
    @Test
    public void setTileChangesTheTile() {
        Position pos = new Position(0, 1);
        Tile t = new Tile(pos, SoilLayer.Type.SAND, 5);
        terrain.setTile(pos, t);
        assertEquals(t, terrain.getTile(pos));
    }

    @Test
    public void setTileWithIncorrectParametersThrowsException() {
        Position pos = new Position(0, 1);
        Tile t = new Tile(pos, SoilLayer.Type.SAND, 5);
        Position invpos = new Position(-1, -1);
        boolean exception_thrown;
        
        exception_thrown = false;
        try {
            terrain.setTile(null, null);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);

        exception_thrown = false;
        try {
            terrain.setTile(null, t);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
        
        exception_thrown = false;
        try {
            terrain.setTile(pos, null);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
        
        exception_thrown = false;
        try {
            terrain.setTile(invpos, t);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
    }
    
    @Test
    public void zeroBottomLayerDoesNotLeaveNegativeAmounts() {
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                terrain.getTile(x, y).getBottomSoil().setAmount(-(x + 1)/(y + 1));
            }
        }
        terrain.zeroBottomSoilLayer();
        for (Tile t : terrain) {
            // paranoidly false test here so there is no need to test for equality with 0.0
            assertFalse(t.getBottomSoil().getAmount() < 0.0);
        }
    }
}
