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

import java.util.Map;
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
public class HashMapTest {
    
    private Map<String, Integer> map;
    
    public HashMapTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        map = new HashMap<>();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void addingIncreasesSize() {
        int oldsize = map.size();
        map.put("eka", 435);
        assertEquals(oldsize + 1, map.size());
    }
    
    @Test
    public void addingMultipleWithSameKeyDoesNotIncreaseSize() {
        String k = "sdf";
        map.put(k, 42);
        int oldsize = map.size();
        for (int i = 0; i < 32; i++) {
            map.put(k, i);
            assertEquals(oldsize, map.size());
        }
    }
    
    @Test
    public void addedItemCanBeFetched() {
        String k = "sdfsdf";
        Integer v = 42;
        for (int i = 0; i < 10; i++) {
            map.put("sdfsfd#" + i, i);
        }
        map.put(k, v);
        for (int i = 0; i < 10; i++) {
            map.put("cvjxljv#" + i, i * 10);
        }
        assertEquals(v, map.get(k));
    }

    @Test
    public void containsValueReturnsTrueForExistingItem() {
        String k = "sdfsdf";
        Integer v = 42;
        for (int i = 0; i < 10; i++) {
            map.put("sdfsfd#" + i, i);
        }
        map.put(k, v);
        for (int i = 0; i < 10; i++) {
            map.put("cvjxljv#" + i, i * 10);
        }
        assertTrue(map.containsValue(v));
    }

    @Test
    public void containsValueReturnsFalseForNonExistingItem() {
        String k = "sdfsdf";
        Integer v = 42;
        for (int i = 0; i < 10; i++) {
            map.put("sdfsfd#" + i, i);
        }
        for (int i = 0; i < 10; i++) {
            map.put("cvjxljv#" + i, i * 10);
        }
        assertFalse(map.containsValue(v));
    }

    @Test
    public void containsKeyReturnsTrueForExistingItem() {
        String k = "sdfsdf";
        Integer v = 42;
        for (int i = 0; i < 10; i++) {
            map.put("sdfsfd#" + i, i);
        }
        map.put(k, v);
        for (int i = 0; i < 10; i++) {
            map.put("cvjxljv#" + i, i * 10);
        }
        assertTrue(map.containsKey(k));
    }

    @Test
    public void containsKeyReturnsFalseForNonExistingItem() {
        String k = "sdfsdf";
        Integer v = 42;
        for (int i = 0; i < 10; i++) {
            map.put("sdfsfd#" + i, i);
        }
        for (int i = 0; i < 10; i++) {
            map.put("cvjxljv#" + i, i * 10);
        }
        assertFalse(map.containsKey(k));
    }
    
    @Test
    public void valuesReturnsTheCorrectNumberOfItems() {
        assertEquals(0, map.values().size());
        int count = 432;
        for (int i= 0; i < count; i++) {
            map.put("sdfsdf#" + i, i);
        }
        assertEquals(count, map.values().size());
    }
    
    @Test
    public void keySetReturnsEmptyListAfterClear() {
        int count = 432;
        for (int i= 0; i < count; i++) {
            map.put("sdfsdf#" + i, i);
        }
        map.clear();
        assertEquals(0, map.keySet().size());
    }
    
    @Test
    public void removingDecreasesSize() {
        int count = 100;
        for (int i= 0; i < count; i++) {
            map.put("sdf#" + i, i);
        }
        int oldsize = map.size();
        map.remove("sdf#" + (count / 2));
        assertEquals(oldsize - 1, map.size());
    }
    
    @Test
    public void removingNonExistingItemDoesNotChangeSize() {
        int count = 100;
        for (int i= 0; i < count; i++) {
            map.put("sdf#" + i, i);
        }
        int oldsize = map.size();
        map.remove("sdfskljfsljfsldf");
        assertEquals(oldsize, map.size());
    }
}
