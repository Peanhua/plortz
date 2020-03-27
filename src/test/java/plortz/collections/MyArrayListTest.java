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
package plortz.collections;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class MyArrayListTest {
    
    private MyArrayList<String>  string_list;
    private MyArrayList<Integer> int_list;
    
    public MyArrayListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        string_list = new MyArrayList<>(String.class);
        int_list = new MyArrayList<>(Integer.class);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void sizeReturns0ForEmpty() {
        assertEquals(0, string_list.size());
        assertEquals(0, int_list.size());
    }
    
    @Test
    public void addIncreasesSize() {
        int oldsize;
        
        for (int i = 0; i < 9999; i++) {
            oldsize = string_list.size();
            string_list.add("sdfsdf");
            assertEquals(oldsize + 1, string_list.size());

            oldsize = int_list.size();
            int_list.add(432);
            assertEquals(oldsize + 1, int_list.size());
        }
    }
    
    @Test
    public void getReturnsAddedElements() {
        int count = 9999;
        String sprefix = "element#";
        int    iprefix = 42;
        
        for (int i = 0; i < count; i++) {
            string_list.add(sprefix + i);
            int_list.add(iprefix + i);
        }
        
        for (int i = 0; i < count; i++) {
            assertTrue(string_list.get(i).equals(sprefix + i));
            assertNotNull(int_list.get(i));
            assertEquals(iprefix + i, (int) int_list.get(i));
        }
    }
    
    @Test
    public void sizeReturns0AfterClear() {
        for (int i = 0; i < 123; i++) {
            string_list.add("sdfsfddsf");
            int_list.add(i + 42);
        }
        string_list.clear();
        int_list.clear();
        assertEquals(0, string_list.size());
        assertEquals(0, int_list.size());
    }
    
    @Test
    public void getWithInvalidIndexThrowsException() {
        boolean exception_thrown;
        
        exception_thrown = false;
        try {
            string_list.get(-1);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);

        exception_thrown = false;
        try {
            int_list.get(-1);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);

        exception_thrown = false;
        try {
            string_list.get(string_list.size() + 1);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);

        exception_thrown = false;
        try {
            int_list.get(int_list.size() + 1);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
    }
    
    @Test
    public void canAddLargeAmountOfElements() {
        int large_amount = 4 * 1024 * 1024;
        for (int i = 0; i < large_amount; i++) {
            string_list.add("sdfsdf" + i);
            int_list.add(42 + i);
        }
        assertEquals(large_amount, string_list.size());
        assertEquals(large_amount, int_list.size());
    }
    
    @Test
    public void isEmptyReturnsTrueWhenEmpty() {
        assertTrue(int_list.isEmpty());
        assertTrue(string_list.isEmpty());
    }
    
    @Test
    public void isEmptyReturnsFalseWhenNotEmpty() {
        string_list.add("sdfsfd");
        assertFalse(string_list.isEmpty());
        for (int i = 0; i < 324; i++) {
            int_list.add(42 + i);
        }
        assertFalse(int_list.isEmpty());
    }
}
