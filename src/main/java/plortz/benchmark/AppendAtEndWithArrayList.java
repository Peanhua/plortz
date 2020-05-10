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

import java.util.ArrayList;
import java.util.List;

/**
 * Benchmark appending at the end of a list.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class AppendAtEndWithArrayList extends Benchmark {

    private final int       number_count;
    protected List<Integer> list;
    
    public AppendAtEndWithArrayList(int number_count) {
        this.number_count = number_count;
    }

    @Override
    protected void setUp() {
        this.list = new ArrayList<>();
    }        
    
    @Override
    protected void execute() {
        for (int i = 0; i < this.number_count; i++) {
            this.list.add(i);
        }
    }

    @Override
    public String getName() {
        return "Append at end: ArrayList";
    }
    
}
