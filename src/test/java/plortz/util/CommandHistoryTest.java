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
public class CommandHistoryTest {
    
    private CommandHistory history;
    
    public CommandHistoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        history = new CommandHistory();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void addingWorks() {
        for (int i = 0; i < 10; i++) {
            history.add("test#" + i);
            assertEquals(i + 1, history.get().size());
        }
    }
    
    @Test
    public void previousReturnsPrevious() {
        String a = "eka";
        String b = "toka";
        String c = "kolmas";
        history.add(a);
        history.add(b);
        history.add(c);
        assertTrue(history.previous().equals(c));
        assertTrue(history.previous().equals(b));
        assertTrue(history.previous().equals(a));
    }
    
    @Test
    public void previousReturnsNullIfThereIsNoPrevious() {
        assertNull(history.previous());
        history.add("sdf");
        history.previous();
        assertNull(history.previous());
    }
    
    @Test
    public void nextAndPreviousWork() {
        String a = "eka";
        String b = "toka";
        String c = "kolmas";
        history.add(a);
        history.add(b);
        history.add(c);
        assertTrue(history.previous().equals(c));
        assertNull(history.next());
        assertTrue(history.previous().equals(c));
        assertTrue(history.previous().equals(b));
        assertTrue(history.next().equals(c));
        assertTrue(history.previous().equals(b));
        assertTrue(history.previous().equals(a));
        assertTrue(history.next().equals(b));
        assertTrue(history.next().equals(c));
        assertNull(history.next());
    }
    
    @Test
    public void clearRemovesHistory() {
        history.add("sdf");
        history.add("werwer");
        history.clear();
        assertEquals(0, history.get().size());
    }
    
    @Test
    public void nextReturnsNullAfterClear() {
        history.add("sdf");
        history.add("werwer");
        history.clear();
        assertNull(history.next());
    }

    @Test
    public void previousReturnsNullAfterClear() {
        history.add("sdf");
        history.add("werwer");
        history.clear();
        assertNull(history.previous());
    }
}
