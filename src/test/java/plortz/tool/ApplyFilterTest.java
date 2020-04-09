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
package plortz.tool;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.terrain.Terrain;
import plortz.tool.filters.Filter;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class ApplyFilterTest {
    
    Terrain terrain;
    double testdelta;
    
    public ApplyFilterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        terrain = new Terrain(3, 3);
        testdelta = 0.00001;
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void filterIsCalled() {
        
        class MyFilter extends Filter {

            public int call_count;
            
            public MyFilter(int window_size) {
                super(window_size);
                this.call_count = 0;
            }

            @Override
            public double filter(Terrain terrain, int x, int y) {
                this.call_count++;
                return 0.0;
            }
        }
        
        MyFilter filter = new MyFilter(3);
        Tool tool = new ApplyFilter(filter);
        tool.apply(terrain);
        assertTrue(filter.call_count > 0);
    }
    
}
