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

import java.util.List;
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
public class FastInsertListTest {
    protected List<String>  string_list;
    protected List<Integer> int_list;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        string_list = new FastInsertList<>();
        int_list = new FastInsertList<>();
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
    
    @Test
    public void setChangesTheElement() {
        for (int i = 10; i < 100; i++) {
            int_list.add(i);
        }
        int_list.set(20, 777);
        assertEquals(777, (int) int_list.get(20));
    }
    
    @Test
    public void invalidIndexToSetThrowsException() {
        boolean exception_thrown = false;
        try {
            int_list.set(-4, 3);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
    }

    @Test
    public void setReturnsPrevious() {
        int_list.add(42);
        int_list.add(44);
        int_list.add(46);
        int previous = int_list.set(1, 7);
        assertEquals(44, previous);
    }
    
    @Test
    public void iteratorLoopsThroughAllItems() {
        int count = 10;
        int total = 0;
        for (int i = 0; i < count; i++) {
            int value = 3 * i + 8;
            int_list.add(value);
            total += value;
        }
        int iterator_total = 0;
        for (int i : int_list) {
            iterator_total += i;
        }
        assertEquals(total, iterator_total);
    }
    
    @Test
    public void removeWorks() {
        int count = 7;
        for (int i = 0; i < count; i++) {
            int_list.add(42 + i);
        }
        
        int_list.remove(1);
        assertEquals(42, (int) int_list.get(0));
        assertEquals(44, (int) int_list.get(1));
    }
    
    @Test
    public void removingIllegalIndexThrowsException() {
        boolean exception_thrown;
        
        exception_thrown = false;
        try {
            int_list.remove(-1);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);

        exception_thrown = false;
        try {
            int_list.remove(1);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
    }
    
    @Test
    public void removingObjectWorks() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(c);
        string_list.remove(b);
        assertEquals(2, string_list.size());
        assertTrue(string_list.get(0).equals(a));
        assertTrue(string_list.get(1).equals(c));
    }

    @Test
    public void removingNullObjectWorks() {
        String a = "eka";
        String b = null;
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(c);
        string_list.remove(null);
        assertEquals(2, string_list.size());
        assertTrue(string_list.get(0).equals(a));
        assertTrue(string_list.get(1).equals(c));
    }
    
    @Test
    public void removingNonExistingObjectDoesNotChangeTheList() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(c);
        string_list.remove(b);
        assertEquals(2, string_list.size());
        assertTrue(string_list.get(0).equals(a));
        assertTrue(string_list.get(1).equals(c));
    }

    @Test
    public void removingNullObjectDoesNotChangeTheListWhenItDoesNotContainNull() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(c);
        string_list.remove(null);
        assertEquals(3, string_list.size());
        assertTrue(string_list.get(0).equals(a));
        assertTrue(string_list.get(1).equals(b));
        assertTrue(string_list.get(2).equals(c));
    }
    
    @Test
    public void insertingAtStartWorks() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(0, c);
        assertTrue(string_list.get(0).equals(c));
        assertTrue(string_list.get(1).equals(a));
        assertTrue(string_list.get(2).equals(b));
    }

    @Test
    public void insertingAtEndWorks() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(2, c);
        assertTrue(string_list.get(0).equals(a));
        assertTrue(string_list.get(1).equals(b));
        assertTrue(string_list.get(2).equals(c));
    }
    
    @Test
    public void insertingAtNegativeIndexThrowsException() {
        boolean exception_thrown = false;
        try {
            string_list.add(-1, "eka");
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);

        exception_thrown = false;
        try {
            string_list.add(-999999, "eka");
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
    }

    @Test
    public void insertingPastTheSizeThrowsException() {
        boolean exception_thrown = false;
        try {
            string_list.add(string_list.size() + 1, "eka");
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);

        exception_thrown = false;
        try {
            string_list.add(string_list.size() + 99999, "eka");
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
    }

    
    @Test
    public void indexOfReturnsCorrectValue() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(c);
        
        assertEquals(0, string_list.indexOf("eka"));
        assertEquals(1, string_list.indexOf("toka"));
        assertEquals(2, string_list.indexOf("kolkko"));
    }
    
    @Test
    public void indexOfReturnsNegativeForNonExistingValue() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(c);
        assertTrue(string_list.indexOf("nelkku") < 0);
    }

    @Test
    public void removingFirstElementWorks() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(c);
        string_list.remove(0);
        assertEquals(b, string_list.get(0));
        string_list.remove(0);
        assertEquals(c, string_list.get(0));
        string_list.remove(0);
        assertTrue(string_list.isEmpty());
    }

    @Test
    public void removingLastElementWorks() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(c);
        string_list.remove(2);
        string_list.remove(1);
        string_list.remove(0);
        assertTrue(string_list.isEmpty());
    }
    
    @Test
    public void indexOfNullFindsNullElement() {
        String a = "eka";
        String b = "toka";
        String c = "kolkko";
        string_list.add(a);
        string_list.add(b);
        string_list.add(null);
        string_list.add(c);
        assertEquals(2, string_list.indexOf(null));
    }
}
