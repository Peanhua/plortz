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
import java.util.Map;
import java.util.Set;

/**
 * Map using hash table.
 * <p>
 * Implements only the methods required by this application.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 * @param <K> The element type for keys.
 * @param <V> The element type for values.
 */
public class HashMap<K, V> implements Map<K, V> {
    
    private class Bucket<K, V> {
        
        private final List<K> keys;
        private final List<V> values;
        
        public Bucket() {
            this.keys   = new ArrayList<>();
            this.values = new ArrayList<>();
        }
        
        public boolean containsKey(K key) {
            return this.keys.contains(key);
        }
        
        public boolean containsValue(V v) {
            return this.values.contains(v);
        }
        
        public List<V> getValues() {
            return this.values;
        }
        
        public V get(K key) {
            int index = this.keys.indexOf(key);
            if (index < 0) {
                return null;
            }
            return this.values.get(index);
        }
        
        /**
         * Puts an item into the bucket.
         * @param key   The key.
         * @param value The item.
         * @return      True if the key was already in the bucket.
         */
        public boolean put(K key, V value) {
            if (key == null) {
                throw new NullPointerException();
            }
            int index = this.keys.indexOf(key);
            if (index >= 0) {
                this.values.set(index, value);
                return true;
            }
            this.keys.add(key);
            this.values.add(value);
            return false;
        }
        
        /**
         * Removes an item from the bucket.
         * 
         * @param key The item to be removed.
         */
        public void removeByKey(K key) {
            int index = this.keys.indexOf(key);
            if (index < 0) {
                return;
            }
            this.keys.remove(index);
            this.values.remove(index);
        }
        
        public void clear() {
            this.keys.clear();
            this.values.clear();
        }
    }
    
    private final Set<K>             keys;
    private final List<Bucket<K, V>> buckets;

    public HashMap() {
        this.keys    = new HashSet<>();
        this.buckets = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            this.buckets.add(new Bucket<>());
        }
    }
    
    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % this.buckets.size();
    }
    
    @Override
    public int size() {
        return this.keys.size();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsValue(Object o) {
        @SuppressWarnings({"unchecked"})
        V c = (V) o; // unchecked cast
        for (Bucket<K, V> bucket : this.buckets) {
            if (bucket.containsValue(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<K> keySet() {
        return this.keys;
    }

    @Override
    public Collection<V> values() {
        List<V> rv = new ArrayList<>();
        for (Bucket<K, V> bucket : this.buckets) {
            rv.addAll(bucket.getValues());
        }
        return rv;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V get(Object o) {
        if (o == null) {
            return null;
        }
        @SuppressWarnings({"unchecked"})
        final K key = (K) o; // unchecked cast
        return this.buckets.get(this.getBucketIndex(key)).get(key);
    }

    @Override
    public V put(K k, V v) {
        V oldval = this.get(k);
        if (!this.buckets.get(this.getBucketIndex(k)).put(k, v)) {
            this.keys.add(k);
        }
        return oldval;
    }

    @Override
    public V remove(Object o) {
        if (o == null) {
            return null;
        }
        @SuppressWarnings({"unchecked"})
        final K key = (K) o; // unchecked cast
        if (!this.keys.contains(key)) {
            return null;
        }
        Bucket<K, V> bucket = this.buckets.get(this.getBucketIndex(key));
        V oldval = bucket.get(key);
        bucket.removeByKey(key);
        this.keys.remove(key);
        return oldval;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        for (Bucket<K, V> bucket : this.buckets) {
            bucket.clear();
        }
        this.keys.clear();
    }

    @Override
    public boolean containsKey(Object o) {
        if (o == null) {
            return false;
        }
        @SuppressWarnings({"unchecked"})
        final K key = (K) o; // unchecked cast
        return this.buckets.get(this.getBucketIndex(key)).containsKey(key);
    }
}
