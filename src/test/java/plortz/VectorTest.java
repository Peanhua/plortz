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
    
    @Test
    public void subtractingWorks() {
        Vector[] vecs = {
            new Vector(0, 0, 0), new Vector(1, 1, 1), new Vector(-1, -1, -1),
            new Vector(1, 1, 1), new Vector(0, 0, 0), new Vector(1, 1, 1),
            new Vector(1, 2, 3), new Vector(2, 2, 2), new Vector(-1, 0, 1)
        };
        for (int i = 0; i < vecs.length; i += 3) {
            Vector result = vecs[i + 0].subtract(vecs[i + 1]);
            Vector expected = vecs[i + 2];
            checkVectorEquality(expected, result);
        }
    }
    
    private void checkVectorEquality(Vector expected, Vector result) {
        assertEquals(expected.getDimensions(), result.getDimensions());
        for (int j = 0; j < result.getDimensions(); j++) {
            assertEquals(expected.get(j), result.get(j), testdelta);
        }
    }        
    
    @Test
    public void copyConstructorCreatesIdenticalVector() {
        Vector src = new Vector(1, 2, 3);
        Vector copy = new Vector(src);
        checkVectorEquality(src, copy);
    }
    
    @Test
    public void copyConstructorDoesDeepCopy() {
        double x = 1;
        double y = 2;
        double z = 3;
        Vector src = new Vector(x, y, z);
        Vector copy = new Vector(src);
        src.setX(0);
        src.setY(0);
        src.setZ(0);
        assertEquals(x, copy.getX(), testdelta);
        assertEquals(y, copy.getY(), testdelta);
        assertEquals(z, copy.getZ(), testdelta);
    }
    
    @Test
    public void getThrowsExceptionOnInvalidDimension() {
        Vector v = new Vector(1, 2);
        boolean exception_thrown;
        
        exception_thrown = false;
        try {
            v.get(-1);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);

        exception_thrown = false;
        try {
            v.get(2);
        } catch (Exception e) {
            exception_thrown = true;
        }
        assertTrue(exception_thrown);
    }
    
    @Test
    public void multiplyWorks() {
        Vector v = new Vector(2.0, 3.0, 4.0);
        Vector m = v.multiply(10.0);
        assertEquals(20.0, m.getX(), testdelta);
        assertEquals(30.0, m.getY(), testdelta);
        assertEquals(40.0, m.getZ(), testdelta);
    }
    
    @Test
    public void settingFromOtherVectorWorks() {
        Vector src = new Vector(2.0, 3.0, 4.0);
        Vector dst = new Vector(0, 0, 0);
        dst.set(src);
        assertEquals(src.getX(), dst.getX(), testdelta);
        assertEquals(src.getY(), dst.getY(), testdelta);
        assertEquals(src.getZ(), dst.getZ(), testdelta);
    }
    
    @Test
    public void settingFromOtherVectorDoesNotChangeTheSource() {
        Vector src = new Vector(2.0, 3.0, 4.0);
        Vector dst = new Vector(0, 0, 0);
        dst.set(src);
        assertEquals(2.0, src.getX(), testdelta);
        assertEquals(3.0, src.getY(), testdelta);
        assertEquals(4.0, src.getZ(), testdelta);
    }
}
