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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * ArrayList replacement.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 * @param <E> The type of the element.
 */
public class MyArrayList<E> implements List<E> {
    
    private E[]            array;
    private int            allocated_size;
    private int            used_size;
    private final Class<E> element_class;
    
    private class Iterator<E> implements java.util.Iterator<E> {
        private final MyArrayList<E> list;
        private int                  pos;
        
        public Iterator(MyArrayList<E> list) {
            this.list = list;
            this.pos  = 0;
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.list.used_size;
        }

        @Override
        public E next() {
            E rv = this.list.get(this.pos);
            this.pos++;
            return rv;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    
    public MyArrayList(Class<E> c) {
        this.allocated_size = 0;
        this.used_size      = 0;
        this.element_class  = c;
        this.allocate(16);
    }
    
    @SuppressWarnings({"unchecked"})
    private void allocate(int amount) {
        E[] old = this.array;
        this.array = (E[]) Array.newInstance(this.element_class, amount); // unchecked cast
        this.allocated_size = amount;
        for (int i = 0; i < this.used_size; i++) {
            this.array[i] = old[i];
        }
    }

    @Override
    public int size() {
        return this.used_size;
    }

    @Override
    public boolean add(E e) {
        if (this.used_size >= this.allocated_size) {
            this.allocate(this.allocated_size * 2);
            if (this.used_size >= this.allocated_size) {
                throw new OutOfMemoryError();
            }
        }
        this.array[this.used_size] = e;
        this.used_size++;
        return true;
    }

    @Override
    public void clear() {
        this.used_size = 0;
    }

    @Override
    public E get(int i) {
        if (i < 0 || i >= this.used_size) {
            throw new IndexOutOfBoundsException();
        }
        return this.array[i];
    }

    @Override
    public boolean isEmpty() {
        return this.used_size == 0;
    }
    
    @Override
    public E set(int i, E e) {
        if (i < 0 || i >= this.used_size) {
            throw new IndexOutOfBoundsException();
        }
        E previous = this.array[i];
        this.array[i] = e;
        return previous;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>(this);
    }
    
    @Override
    public E remove(int i) {
        if (i < 0 || i >= this.used_size) {
            throw new IndexOutOfBoundsException();
        }
        E removed = this.array[i];
        for (int j = i; j < this.used_size - 1; j++) {
            this.array[j] = this.array[j + 1];
        }
        this.used_size--;
        return removed;
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = false;
        for (int i = 0; removed == false && i < this.used_size; i++) {
            if (o == null ? this.array[i] == null : o.equals(this.array[i])) {
                removed = true;
                this.remove(i);
            }
        }
        return removed;
    }

    @Override
    public void add(int i, E e) {
        if (i < 0 || i > this.used_size) {
            throw new IndexOutOfBoundsException();
        }
        if (this.used_size >= this.allocated_size) {
            this.allocate(this.allocated_size * 2);
            if (this.used_size >= this.allocated_size) {
                throw new OutOfMemoryError();
            }
        }
        for (int j = this.used_size; j > i; j--) {
            this.array[j] = this.array[j - 1];
        }
        this.array[i] = e;
        this.used_size++;
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
    public int indexOf(Object o) {
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
