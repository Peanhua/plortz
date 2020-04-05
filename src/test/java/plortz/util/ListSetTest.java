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
package plortz.util;

import java.util.Set;
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
public class ListSetTest {
    
    private Set<Integer> intlist;
    
    public ListSetTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        intlist = new ListSet<>();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void addingIncreasesSize() {
        int size = intlist.size();
        intlist.add(3);
        size++;
        assertEquals(size, intlist.size());
        intlist.add(9);
        size++;
        assertEquals(size, intlist.size());
        
        for (int i = 0; i < 999; i++) {
            intlist.add(100 + i);
            size++;
            assertEquals(size, intlist.size());
        }
    }
    
    @Test
    public void addingExistingDoesNotIncreaseSize() {
        int count = 23;
        intlist.add(42);
        int oldsize = intlist.size();
        for (int i = 0; i < count; i++) {
            intlist.add(42);
        }
        assertEquals(oldsize, intlist.size());
    }
    
    @Test
    public void containsReturnsTrueForExistingItem() {
        intlist.add(42);
        assertTrue(intlist.contains(42));
    }
    
    @Test
    public void clearSetsSizeToZero() {
        intlist.add(42);
        intlist.clear();
        assertEquals(0, intlist.size());
    }
    
    @Test
    public void removingDecreasesSize() {
        int count = 32;
        int start = 42;
        for (int i = 0; i < count; i++) {
            intlist.add(start + i);
        }
        count = intlist.size();
        intlist.remove(start + count / 2);
        assertEquals(count - 1, intlist.size());
    }
    
    @Test
    public void itemCanNotBeFoundAfterRemovingIt() {
        int count = 32;
        int start = 42;
        for (int i = 0; i < count; i++) {
            intlist.add(start + i);
        }
        int itemtoremove = start + count / 2;
        intlist.remove(itemtoremove);
        assertFalse(intlist.contains(itemtoremove));
    }
    
    @Test
    public void isEmptyReturnsTrueForEmptyList() {
        assertTrue(intlist.isEmpty());
    }
    
    @Test
    public void isEmptyReturnsFalseForListWithItems() {
        int count = 32;
        int start = 42;
        for (int i = 0; i < count; i++) {
            intlist.add(start + i);
        }
        assertFalse(intlist.isEmpty());
    }
}
