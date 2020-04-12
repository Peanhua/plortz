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
import plortz.terrain.Tile;
import plortz.util.Vector;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class InsertSoilLayerTest {
    
    Terrain terrain;
    double testdelta;
    Position center;
    
    public InsertSoilLayerTest() {
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
        center = new Position(terrain.getWidth() / 2, terrain.getLength() / 2);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void rectInsertAtBottomRaisesAltitudes() {
        double[] originals = new double[terrain.getWidth() * terrain.getLength()];
        for (Tile t : terrain) {
            originals[t.getPosition().getX() + t.getPosition().getY() * terrain.getWidth()] = t.getAltitude(true);
        }
        double amount = 2.0;
        Tool tool = new InsertSoilLayer(0, SoilLayer.Type.DIRT, amount, center, terrain.getWidth(), terrain.getLength());
        tool.apply(terrain);
        for (Tile t : terrain) {
            assertEquals(amount + originals[t.getPosition().getX() + t.getPosition().getY() * terrain.getWidth()], t.getAltitude(true), testdelta);
        }
    }
    
    @Test
    public void rectInsertAtBottomChangesBottom() {
        for (SoilLayer.Type type : SoilLayer.Type.values()) {
            Tool tool = new InsertSoilLayer(0, type, 1.0, center, terrain.getWidth(), terrain.getLength());
            tool.apply(terrain);
            assertEquals(type, terrain.getTile(0, 0).getBottomSoil().getType());
        }
    }
    
}
