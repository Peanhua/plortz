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

import java.util.Arrays;
import java.util.Random;
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
public class PriorityQueueTest {
    
    private PriorityQueue<Integer> pq;
    private Integer[] testdata;
    private Integer[] sorted;
    
    public PriorityQueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        pq = new PriorityQueue<>();
        int count = 23465;
        testdata = new Integer[count];
        sorted = new Integer[count];
        Random r = new MersenneTwister(0);
        for (int i = 0; i < count; i++) {
            int n = r.nextInt();
            testdata[i] = n;
            pq.add(n);
        }
        sorted = Arrays.copyOf(testdata, testdata.length);
        Arrays.sort(sorted);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void peekReturnsTheSmallest() {
        assertEquals(sorted[0], pq.peek());
    }
    
    @Test
    public void peekReturnsNullWithEmptyPriorityQueue() {
        PriorityQueue<Integer> empty = new PriorityQueue<>();
        assertNull(empty.peek());
    }
    
    @Test
    public void pollingReturnsTheSmallest() {
        assertEquals(sorted[0], pq.poll());
    }
    
    @Test
    public void pollReturnsNullWithEmptyPriorityQueue() {
        PriorityQueue<Integer> empty = new PriorityQueue<>();
        assertNull(empty.poll());
    }
    
    @Test
    public void subsequentPollReturnsElementsInCorrectOrder() {
        for (Integer i : sorted) {
            assertEquals(i, pq.poll());
        }
    }
    
    @Test
    public void pollAfterClearReturnsNull() {
        pq.clear();
        assertNull(pq.poll());
    }
    
    @Test
    public void sizeReturnsZeroAfterClear() {
        pq.clear();
        assertEquals(0, pq.size());
    }
    
    @Test
    public void addingSmallestAndThenPollingReturnsTheAdded() {
        Integer value = sorted[0] - 1;
        pq.add(value);
        assertEquals(value, pq.poll());
    }
    
}
