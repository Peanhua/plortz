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

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * Priority queue implemented using binary heap.
 * <p>
 * Element at index 0 is not used, the root is at the index 1.
 * <p>
 * Implements only the methods required by this application.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 * @param <E> The element type.
 */
public class PriorityQueue<E> extends AbstractQueue<E> {

    private final ArrayList<E> data;
    
    public PriorityQueue() {
        this.data = new ArrayList<>();
        this.data.add(null); // Not used
    }
    
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
        return this.data.size() - 1;
    }
    
    @Override
    public void clear() {
        this.data.clear();
        this.data.add(null);
    }

    @Override
    public boolean offer(E e) {
        this.data.add(e);
        this.upHeap();
        return true;
    }

    @Override
    public E poll() {
        if (this.data.size() <= 1) {
            return null;
        }
        E rv = this.data.get(1);
        if (this.data.size() > 2) {
            // Move the last element to the root:
            int last = this.data.size() - 1;
            this.data.set(1, this.data.get(last));
            this.data.remove(last);
            this.downHeap();
        } else {
            this.data.remove(1);
        }
        return rv;
    }

    @Override
    public E peek() {
        if (this.data.size() <= 1) {
            return null;
        }
        return this.data.get(1);
    }
    
    /**
     * Up-heap, aka re-order so that the heap is correct after adding a new last node.
     */
    private void upHeap() {
        Integer current = this.data.size() - 1;
        while (current != null) {
            Integer parent = this.getParentNode(current);
            if (parent != null) {
                @SuppressWarnings("unchecked")
                Comparable<E> cc = (Comparable<E>) this.data.get(current); // unchecked cast
                if (cc.compareTo(this.data.get(parent)) < 0) {
                    this.swap(parent, current);
                }
            }
            current = parent;
        }
    }

    /**
     * Down-heap, aka re-order so that the heap is correct after removing the root node.
     */
    private void downHeap() {
        Integer current = 1;
        while (true) {
            // Compare the smaller child to the current and swap if the child is smaller:
            Integer child = this.getSmallerChild(current);
            if (child == null) {
                // Reached the bottom, the heap is correct:
                break;
            }
            @SuppressWarnings("unchecked")
            Comparable<E> cc = (Comparable<E>) this.data.get(current); // unchecked cast
            if (cc.compareTo(this.data.get(child)) > 0) {
                this.swap(current, child);
                current = child;
                continue;
            }
            // Current is smaller than the children, the heap is correct:
            break;
        }
    }
    
    private void swap(int a, int b) {
        E tmp = this.data.get(a);
        this.data.set(a, this.data.get(b));
        this.data.set(b, tmp);
    }
    
    private Integer getSmallerChild(Integer parent) {
        Integer left_node = this.getLeftChildNode(parent);
        if (left_node == null) {
            return null;
        }
        Integer right_node = this.getRightChildNode(parent);
        if (right_node == null) {
            return left_node;
        }
        Integer child_node = left_node;
        @SuppressWarnings("unchecked")
        Comparable<E> cleft = (Comparable<E>) this.data.get(left_node); // unchecked cast
        if (cleft.compareTo(this.data.get(right_node)) > 0) {
            child_node = right_node;
        }
        return child_node;
    }
    
    private Integer getLeftChildNode(Integer node) {
        Integer rv = node * 2;
        if (rv >= this.data.size()) {
            return null;
        }
        return rv;
    }

    private Integer getRightChildNode(Integer node) {
        Integer rv = node * 2 + 1;
        if (rv >= this.data.size()) {
            return null;
        }
        return rv;
    }
    
    private Integer getParentNode(Integer node) {
        if (node == 1) {
            return null;
        }
        return node / 2;
    }
}
