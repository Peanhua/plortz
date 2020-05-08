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

/**
 * Generic merge sorter.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 * @param <E> The element type.
 */
public class MergeSort<E> {
    
    /**
     * Compare function used when sorting with MergeSort.
     * 
     * @param <E> The type of the compared objects.
     */
    public interface Comparer<E> {
        /**
         * Compare if object a is less than object b.
         * 
         * @param a The first object.
         * @param b The second object.
         * @return  True if the first object is less than the second object.
         */
        public boolean compare(E a, E b);
    }
    
    private Comparer<E> comparer;
    private List<E>     data;
    private List<E>     tmp;
    
    /**
     * Sorts the given data using merge sort.
     * 
     * @param data     The source and destination list of data.
     * @param tmp      Temporary list with at least data.size() elements allocated.
     * @param comparer Comparer function object, return true for "a less than b".
     */
    public void sort(List<E> data, List<E> tmp, Comparer<E> comparer) {
        if (tmp.size() < data.size()) {
            throw new IllegalArgumentException();
        }
        this.data     = data;
        this.tmp      = tmp;
        this.comparer = comparer;
        this.mergesort(0, this.data.size() - 1);
    }
    
    private void mergesort(int start, int end) {
        if (start == end) {
            return;
        }
        int middle = (start + end) / 2;
        this.mergesort(start, middle);
        this.mergesort(middle + 1, end);
        
        this.partition(start, middle, middle + 1, end);
    }
    
    private void partition(int start1, int end1, int start2, int end2) {
        int pos1 = start1;
        int pos2 = start2;
        for (int i = start1; i <= end2; i++) {
            if (pos2 > end2 || (pos1 <= end1 && this.comparer.compare(this.data.get(pos1), this.data.get(pos2)))) {
                this.tmp.set(i, this.data.get(pos1));
                pos1++;
            } else {
                this.tmp.set(i, this.data.get(pos2));
                pos2++;
            }
        }
        for (int i = start1; i <= end2; i++) {
            this.data.set(i, this.tmp.get(i));
        }
    }    
}
