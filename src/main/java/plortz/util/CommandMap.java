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
 * A map between command names and the classes implementing commands.
 * <p>
 * Operates like a hash map, but is not generic.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class CommandMap implements Map<String, Class> {
    
    private class Bucket {
        
        private final List<String> keys;
        private final List<Class>  values;
        
        public Bucket() {
            this.keys   = new MyArrayList<>();
            this.values = new MyArrayList<>();
        }
        
        public boolean containsKey(String key) {
            return this.keys.contains(key);
        }
        
        public boolean contains(Class c) {
            return this.values.contains(c);
        }
        
        public List<Class> getValues() {
            return this.values;
        }
        
        public Class get(String key) {
            int index = this.keys.indexOf(key);
            if (index < 0) {
                return null;
            }
            return this.values.get(index);
        }
        
        public void put(String key, Class value) {
            this.keys.add(key);
            this.values.add(value);
        }
        
        public void remove(String key) {
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
    
    private final Set<String>  keys;
    private final List<Bucket> buckets;

    public CommandMap() {
        this.keys    = new ListSet<>();
        this.buckets = new MyArrayList<>();
        for (int i = 0; i < 20; i++) {
            this.buckets.add(new Bucket());
        }
    }
    
    private int getBucketIndex(String key) {
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
        Class c = (Class) o;
        for (Bucket bucket : this.buckets) {
            if (bucket.contains(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> keySet() {
        return this.keys;
    }

    @Override
    public Collection<Class> values() {
        List<Class> rv = new MyArrayList<>();
        for (Bucket bucket : this.buckets) {
            rv.addAll(bucket.getValues());
        }
        return rv;
    }

    @Override
    public Set<Entry<String, Class>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Class get(Object o) {
        if (o == null) {
            return null;
        }
        final String key = (String) o;
        return this.buckets.get(this.getBucketIndex(key)).get(key);
    }

    @Override
    public Class put(String k, Class v) {
        Class oldval = this.get(k);
        this.buckets.get(this.getBucketIndex(k)).put(k, v);
        return oldval;
    }

    @Override
    public Class remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map<? extends String, ? extends Class> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        for (Bucket bucket : this.buckets) {
            bucket.clear();
        }
    }

    @Override
    public boolean containsKey(Object o) {
        if (o == null) {
            return false;
        }
        final String key = (String) o;
        return this.buckets.get(this.getBucketIndex(key)).containsKey(key);
    }
}
