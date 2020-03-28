/*
 * Copyright (C) 2020 Joni Yrjana <joniyrjana@gmail.com>
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
package plortz;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class VectorTest {
    
    private double testdelta;
    
    public VectorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testdelta = 0.00001;
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getLengthReturnsProperValue() {
        Vector v = new Vector(1, 0, 0);
        assertEquals(1, v.getLength(), testdelta);
    }
    
    @Test
    public void normalizeWorks() {
        Vector[] vecs = {
            new Vector(1, 1, 1),
            new Vector(1, 0, 1),
            new Vector(9, 5, -2)
        };
        for (Vector v : vecs) {
            v.normalize();
            assertEquals(1, v.getLength(), testdelta);
        }
    }
    
}
