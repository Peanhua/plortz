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
package plortz;

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
public class ValidPositionListTest {
    
    private Terrain           terrain;
    private ValidPositionList list;
    
    public ValidPositionListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        terrain = new Terrain(10, 10);
        list = new ValidPositionList(terrain);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void listIsInitiallyEmpty() {
        assertEquals(0, list.size());
    }
    
    @Test
    public void addValidIncreasesSize() {
        list.add(new Position(0, 0));
        list.add(1, 1);
        assertEquals(2, list.size());
    }
    
    @Test
    public void addingInvalidDoesNotIncreaseSize() {
        list.add(null);
        list.add(new Position(-1, -1));
        list.add(-1, -1);
        assertEquals(0, list.size());
    }
    
    @Test
    public void clearSetsSizeToZero() {
        list.add(new Position(0, 0));
        list.clear();
        assertEquals(0, list.size());
    }
    
    @Test
    public void iteratorGoesThroughCorrectNumberOfPositions() {
        int[] positions = {
            0, 0,
            1, 0,
            5, 5
        };
        for (int i = 0; i < positions.length; i += 2) {
            list.add(new Position(positions[i + 0], positions[i + 1]));
        }
        int count = 0;
        for (Position p : list) {
            count++;
        }
        assertEquals(positions.length / 2, count);
    }
}
