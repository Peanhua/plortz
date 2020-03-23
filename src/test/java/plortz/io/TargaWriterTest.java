/*
 * Copyright (C) 2020 Joni Yrjana <joniyrjana@gmail.com>
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
package plortz.io;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.Terrain;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class TargaWriterTest {
    
    private Terrain[] empty_terrains;
    private Terrain   constructed_terrain;
    private Writer    writer;
    
    public TargaWriterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        empty_terrains = new Terrain[4];
        empty_terrains[0] = new Terrain(20, 20);
        empty_terrains[1] = new Terrain(10, 20);
        empty_terrains[2] = new Terrain(20, 10);
        empty_terrains[3] = new Terrain(3, 5);
        this.constructed_terrain = new Terrain(10, 10);
        for (int y = 2; y < 8; y++) {
            this.constructed_terrain.getTile(5, y).setAltitude(1.0);
            this.constructed_terrain.getTile(1 + y, y).setAltitude(1.0);
            this.constructed_terrain.getTile(9 - y, y).setAltitude(1.0);
        }
        writer = new TargaWriter();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getBytesReturnMoreThanHeader() {
        for (int i = 0; i < empty_terrains.length; i++) {
            assertTrue(writer.getBytes(empty_terrains[i]).length > 18);
        }
        assertTrue(writer.getBytes(constructed_terrain).length > 18);
    }
    
    @Test
    public void getBytesReturnCorrectNumberOfBytes() {
        for (int i = 0; i < empty_terrains.length; i++) {
            assertEquals(18 + empty_terrains[i].getWidth() * empty_terrains[i].getHeight(), writer.getBytes(empty_terrains[i]).length);
        }
        assertEquals(18 + constructed_terrain.getWidth() * constructed_terrain.getHeight(), writer.getBytes(constructed_terrain).length);
    }
}
