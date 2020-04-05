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
public class MergeSortTest {
    
    private MergeSort<Integer> sorter;
    private List<Integer>      data;
    private List<Integer>      tmp;
    private Random             random;
    
    public MergeSortTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        sorter = new MergeSort<>();
        random = new MersenneTwister(42);
    }
    
    private void setupData(int count) {
        data = new MyArrayList<>();
        tmp = new MyArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add(random.nextInt());
            tmp.add(0);
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void sortingWorks() {
        setupData(999);
        sorter.sort(data, tmp, (Integer a, Integer b) -> a < b);
        for (int i = 1; i < data.size(); i++) {
            assertTrue(data.get(i - 1) <= data.get(i));
        }
    }
    
}
