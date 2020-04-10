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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A set implementation using hashing.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 * @param <E>
 */
public class HashSet<E> implements Set<E> {
    
    private class Bucket<E> {
        private List<E> values;
        
        public Bucket() {
            this.values = new ArrayList<>();
        }
        
        public int size() {
            return this.values.size();
        }
        
        public boolean contains(E e) {
            return this.values.contains(e);
        }
        
        public void add(E e) {
            this.values.add(e);
        }
        
        public void remove(E e) {
            this.values.remove(e);
        }
        
        public void clear() {
            this.values.clear();
        }
        
        public Iterator<E> iterator() {
            return this.values.iterator();
        }
    }
    
    List<Bucket<E>> buckets;
    
    public HashSet() {
        this.buckets = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            this.buckets.add(new Bucket<>());
        }
    }

    @Override
    public int size() {
        int total = 0;
        for (Bucket<E> b : this.buckets) {
            total += b.size();
        }
        return total;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        @SuppressWarnings({"unchecked"})
        E e = (E) o; // unchecked cast
        Bucket<E> bucket = this.getBucket(e);
        return bucket.contains(e);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int         bucket_pos      = 0;
            private Iterator<E> bucket_iterator = null;
                
            @Override
            public boolean hasNext() {
                this.forwardUntilNext();
                return this.bucket_iterator.hasNext();
            }
            
            private void forwardUntilNext() {
                if (this.bucket_iterator == null) {
                    this.bucket_iterator = buckets.get(this.bucket_pos).iterator();
                }
                if (!this.bucket_iterator.hasNext()) {
                    while (this.bucket_pos < buckets.size() - 1) {
                        this.bucket_pos++;
                        this.bucket_iterator = buckets.get(this.bucket_pos).iterator();
                        if (this.bucket_iterator.hasNext()) {
                            break;
                        }
                    }
                }
            }
            
            @Override
            public E next() {
                this.forwardUntilNext();
                return this.bucket_iterator.next();
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("not supported");
            }
        };
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
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        Bucket<E> bucket = this.getBucket(e);
        if (bucket.contains(e)) {
            return false;
        }
        bucket.add(e);
        return true;
    }
    
    private Bucket<E> getBucket(E e) {
        int hashcode = Math.abs(e.hashCode());
        return this.buckets.get(hashcode % this.buckets.size());
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        @SuppressWarnings({"unchecked"})
        E e = (E) o; // unchecked cast
        Bucket<E> bucket = this.getBucket(e);
        if (!bucket.contains(e)) {
            return false;
        }
        bucket.remove(e);
        return true;
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
    public boolean retainAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        for (Bucket<E> b : this.buckets) {
            b.clear();
        }
    }
    
}
