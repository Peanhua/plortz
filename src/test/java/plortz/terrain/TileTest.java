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

import plortz.util.Position;
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
public class TileTest {
    
    private double testdelta;
    private Tile dirt;
    private Tile sand;
    private Tile sand_underwater;
    
    public TileTest() {
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
        Position pos = new Position(0, 0);
        dirt = new Tile(pos, SoilLayer.Type.DIRT, 0.0);
        sand = new Tile(pos, SoilLayer.Type.SAND, 0.5);
        sand_underwater = new Tile(pos, SoilLayer.Type.SAND, 0.2);
        sand_underwater.setWater(0.1);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getAltitudeWorksWithoutWater() {
        assertEquals(0.0, dirt.getAltitude(false), testdelta);
        assertEquals(0.0, dirt.getAltitude(true), testdelta);
        assertEquals(0.5, sand.getAltitude(false), testdelta);
        assertEquals(0.5, sand.getAltitude(true), testdelta);
    }
    
    @Test
    public void getAltitudeWorksWithWater() {
        assertEquals(0.2, sand_underwater.getAltitude(false), testdelta);
        assertEquals(0.3, sand_underwater.getAltitude(true), testdelta);
    }
    
    @Test
    public void addingDifferentTypeOfSoilCreatesNewLayer() {
        dirt.addSoil(SoilLayer.Type.SAND, 1.0);
        assertEquals(SoilLayer.Type.DIRT, dirt.getBottomSoil().getType());
        assertEquals(SoilLayer.Type.SAND, dirt.getTopSoil().getType());
    }

    @Test
    public void addingSameTypeOfSoilAddsToExisting() {
        double original = dirt.getTopSoil().getAmount();
        dirt.addSoil(SoilLayer.Type.DIRT, 1.0);
        assertEquals(SoilLayer.Type.DIRT, dirt.getBottomSoil().getType());
        assertEquals(SoilLayer.Type.DIRT, dirt.getTopSoil().getType());
        assertTrue(original < dirt.getTopSoil().getAmount());
    }
    
    @Test
    public void removingAllTopSoilExposesLayerBelow() {
        dirt.addSoil(SoilLayer.Type.SAND, 1.0);
        dirt.adjustTopSoilAmount(-2.0);
        assertEquals(SoilLayer.Type.DIRT, dirt.getTopSoil().getType());
    }
    
    @Test
    public void insertingSoilLayerIncreasesTheTotalHeight() {
        double orig = dirt.getAltitude(false);
        dirt.insertSoil(0, SoilLayer.Type.SAND, 1.0);
        assertTrue(dirt.getAltitude(false) > orig);
    }
    
    @Test
    public void insertingAtBottomChangesBottom() {
        dirt.insertSoil(0, SoilLayer.Type.SAND, 1.0);
        assertEquals(SoilLayer.Type.SAND, dirt.getBottomSoil().getType());
    }

    @Test
    public void insertingAtTopDoesNotChangeBottom() {
        dirt.insertSoil(1, SoilLayer.Type.SAND, 1.0);
        assertEquals(SoilLayer.Type.DIRT, dirt.getBottomSoil().getType());
    }
    
    @Test
    public void insertingSameTypeDoesNotAddNewLayer() {
        dirt.insertSoil(0, SoilLayer.Type.DIRT, 1.0);
        assertEquals(dirt.getBottomSoil(), dirt.getTopSoil());
    }
    
    @Test
    public void scalingChangesAltitudeProperly() {
        double orig = sand.getAltitude(false);
        sand.scaleSoilLayers(2.0);
        assertTrue(orig < sand.getAltitude(false));
    }
    
    @Test
    public void scalingWithIllegalValueThrowsException() {
        boolean exception_thrown;
        exception_thrown = false;
        try {
            sand.scaleSoilLayers(0.0);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
        
        exception_thrown = false;
        try {
            sand.scaleSoilLayers(-1.0);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
    }
}
