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
import plortz.util.HashMap;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class PositionTest {
    
    private Position position;
    
    public PositionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        position = new Position(3, 5);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void gettersWork() {
        assertEquals(3, position.getX());
        assertEquals(5, position.getY());
    }
    
    @Test
    public void setterWorks() {
        position.set(9, 12);
        assertEquals(9, position.getX());
        assertEquals(12, position.getY());
    }
    
    @Test
    public void equalWorksBetweenTwoDifferentObjects() {
        Position a = new Position(5, 3);
        Position b = new Position(5, 3);
        assertTrue(a.equals(b));
    }
    
    @Test
    public void hashCodeGivesDistinctValuesMostOfTheTime() {
        HashMap<Integer, Integer> counts = new HashMap<>();
        int count = 256;
        for (int y = 0; y < count; y++) {
            for (int x = 0; x < count; x++) {
                Position pos = new Position(x, y);
                int hc = pos.hashCode();
                int c = counts.getOrDefault(hc, 0) + 1;
                counts.put(hc, c);
            }
        }
        for (Integer key : counts.keySet()) {
            assertTrue(counts.get(key) < 10);
        }
    }
}
