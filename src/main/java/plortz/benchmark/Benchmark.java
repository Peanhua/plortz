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
package plortz.benchmark;

import java.util.List;
import plortz.util.ArrayList;
import plortz.util.MergeSort;

/**
 * Abstract base class for performance tests.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public abstract class Benchmark {
    
    public static List<Benchmark> getAllTests() {
        ArrayList<Benchmark> tests = new ArrayList<>();
        final int addcount = 100000;
        tests.add(new AppendAtEndWithArrayList(addcount));
        tests.add(new AppendAtEndWithFastInsertList(addcount));
        tests.add(new InsertAtStartWithArrayList(addcount));
        tests.add(new InsertAtStartWithFastInsertList(addcount));
        return tests;
    }        
    
    /**
     * Run this test for the given number of times and return the shortest execution time.
     * 
     * @param warm_ups   Number of warm-up times to run the test.
     * @param iterations Number of times to run the test.
     * @return The shortest execution time.
     */
    public final long run(int warm_ups, int iterations) {
        // Warm-up:
        for (int i = 0; i < warm_ups; i++) {
            this.setUp();
            this.execute();
        }
        
        // Run the actual test:
        List<Long> times = new ArrayList<>();
        List<Long> tmp = new ArrayList<>(); // for mergesort
        for (int i = 0; i < iterations; i++) {
            // Try to run garbage collection between executions:
            System.gc();
            try {
                Thread.sleep(50L);
            } catch (Exception e) {
            }

            this.setUp();
            
            var t_start = System.nanoTime();
            this.execute();
            var t_end = System.nanoTime();
            
            times.add(t_end - t_start);
            tmp.add(0L);
        }

        // Return the shortest time:
        MergeSort<Long> sorter = new MergeSort<>();
        sorter.sort(times, tmp, (a, b) -> a < b);
        return times.get(0);
    }

    /**
     * Prepare for a test run.
     */
    protected abstract void setUp();

    /**
     * Run one test.
     */
    protected abstract void execute();
    
    /**
     * Return user-friendly name for this test.
     * @return The name of this test.
     */
    public abstract String getName();
}
