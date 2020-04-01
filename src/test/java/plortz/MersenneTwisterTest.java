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
public class MersenneTwisterTest {
    
    private MersenneTwister mt;
    
    public MersenneTwisterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        mt = new MersenneTwister(0);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void returnedDoublesAreInRange() {
        for (int i = 0; i < 100000; i++) {
            double d = mt.nextDouble();
            assertTrue(d >= 0.0);
            assertTrue(d <= 1.0);
        }
    }
    
    @Test
    public void distributionOfBooleansIsApproximately50percent() {
        int trues = 0;
        int falses = 0;
        for (int i = 0; i < 10000000; i++) {
            if (mt.nextBoolean()) {
                trues++;
            } else {
                falses++;
            }
        }
        double ratio = (double) trues / (double) falses;
        assertEquals(1.0, ratio, 0.02);
    }
    
    @Test
    public void nextIntWithBoundReturnsValuesCorrectInRange() {
        int max = 100;
        for (int i = 0; i < 100000; i++) {
            int random = mt.nextInt(max);
            assertTrue(random >= 0);
            assertTrue(random <= max);
        }
    }
    
    @Test
    public void nextLongReturnsValues() {
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += mt.nextLong();
        }
        assertTrue(sum > 0);
    }

    @Test
    public void nextIntReturnsValues() {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += mt.nextInt();
        }
        assertTrue(sum > 0);
    }
}
