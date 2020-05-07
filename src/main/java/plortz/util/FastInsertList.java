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

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;


/**
 * A list with fast insert at the start.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 * @param <E> The element type.
 */
public class FastInsertList<E> implements List<E> {
    
    /**
     * The actual data.
     * <p>
     * The indices of first and last elements are kept track of separately.
     * 
     * Keeps empty space before first element and after last element at all times,
     * wasting some memory in exchange for performance and simplicity.
     */
    private Object[] data;
    
    /**
     * First points to the index where the first element is.
     */
    private int first;
    /**
     * Last points to the index where the next appended element will be put.
     */
    private int last;

    public FastInsertList() {
        this.data  = null;
        this.first = 0;
        this.last  = 0;
        this.allocate(256);
    }
    
    /**
     * Reserve more space for the data, and copy the current contents to the new space.
     * 
     * @param target_size The new amount of space.
     */
    private void allocate(int target_size) {
        if (target_size < 0) {
            throw new OutOfMemoryError("current_size=" + this.data.length + ", target_size=" + target_size);
        }
        Object[] old = this.data;
        this.data = new Object[target_size];
        final int new_first = target_size / 2 - this.size();
        for (int i = 0; i < this.size(); i++) {
            this.data[new_first + i] = old[this.first + i];
        }
        this.last  = new_first + this.size();
        this.first = new_first;
    }
    
    @Override
    public boolean add(E e) {
        if (this.last == this.data.length) {
            this.allocate(this.data.length * 2);
        }
        this.data[this.last] = e;
        this.last++;
        return true;
    }

    @Override
    public int size() {
        return this.last - this.first;
    }

    @Override
    public boolean isEmpty() {
        return this.first == this.last;
    }

    @Override
    public void clear() {
        this.first = this.data.length / 2;
        this.last  = this.first;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>(this);
    }

    @Override
    public E get(int i) {
        if (i < 0 || i >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        final int pos = this.first + i;
        @SuppressWarnings({"unchecked"})
        E e = (E) this.data[pos]; // unchecked cast
        return e;
    }

    @Override
    public E set(int i, E e) {
        if (i < 0 || i >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        final int pos = this.first + i;
        @SuppressWarnings({"unchecked"})
        E previous = (E) this.data[pos]; // unchecked cast
        this.data[pos] = e;
        return previous;
    }

    @Override
    public void add(int i, E e) {
        if (i < 0 || i > this.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (i == 0) { // Special fast insert at the beginning
            if (this.first == 0) {
                this.allocate(this.data.length * 2);
            }
            this.first--;
            this.data[this.first] = e;
            
        } else if (i == this.size()) { // Special fast append at the end
            if (this.last == this.data.length) {
                this.allocate(this.data.length * 2);
            }
            this.data[this.last] = e;
            this.last++;
            
        } else { // General slow insert in the middle, not implemented
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public E remove(int i) {
        if (i < 0 || i >= this.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (i == 0) { // Special fast remove from the beginning
            @SuppressWarnings({"unchecked"})
            E removed = (E) this.data[this.first]; // unchecked cast
            this.first++;
            return removed;
            
        } else if (i == this.size() - 1) { // Special fast remove from the end
            @SuppressWarnings({"unchecked"})
            E removed = (E) this.data[this.last - 1]; // unchecked cast
            this.last--;
            return removed;
            
        } else { // General slow remove from the middle
            @SuppressWarnings({"unchecked"})
            E removed = (E) this.data[this.first + i]; // unchecked cast
            this.last--;
            for (int j = i; j < this.size(); j++) {
                final int pos = this.first + j;
                this.data[pos] = this.data[pos + 1];
            }
            return removed;
        }
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < this.size(); i++) {
            final int pos = this.first + i;
            if (o == null && this.data[pos] == null) {
                return i;
            }
            if (o != null && this.data[pos].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = false;
        for (int i = 0; removed == false && i < this.size(); i++) {
            final int pos = this.first + i;
            if (o == null ? this.data[pos] == null : o.equals(this.data[pos])) {
                removed = true;
                this.remove(i);
            }
        }
        return removed;
    }

    

    public class Iterator<E> implements java.util.Iterator<E> {
        private final FastInsertList<E> list;
        private int                           pos;
        
        public Iterator(FastInsertList<E> list) {
            this.list = list;
            this.pos  = list.first;
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.list.last;
        }

        @Override
        public E next() {
            @SuppressWarnings({"unchecked"})
            E rv = (E) this.list.data[this.pos]; // unchecked cast
            this.pos++;
            return rv;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(Collection<? extends E> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<E> subList(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
