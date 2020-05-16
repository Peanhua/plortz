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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class CameraTest {
    
    private float testdelta;
    
    public CameraTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testdelta = 0.0001f;
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void initialPositionIsAtOrigin() {
        Camera c = new Camera();
        assertEquals(0, c.getPosition().x(), testdelta);
        assertEquals(0, c.getPosition().y(), testdelta);
        assertEquals(0, c.getPosition().z(), testdelta);
    }
    
    @Test
    public void initialForwardFacesPositiveYAxis() {
        Camera c = new Camera();
        assertEquals(0, c.getForwardVector().x(), testdelta);
        assertEquals(1, c.getForwardVector().y(), testdelta);
        assertEquals(0, c.getForwardVector().z(), testdelta);
    }
    
    @Test
    public void testMovingForward() {
        Camera c = new Camera();
        c.moveForward(5);
        assertEquals(0, c.getPosition().x(), testdelta);
        assertEquals(5, c.getPosition().y(), testdelta);
        assertEquals(0, c.getPosition().z(), testdelta);
    }

    @Test
    public void testMovingRight() {
        Camera c = new Camera();
        c.moveRight(5);
        assertEquals(5, c.getPosition().x(), testdelta);
        assertEquals(0, c.getPosition().y(), testdelta);
        assertEquals(0, c.getPosition().z(), testdelta);
    }
    
    @Test
    public void testRotatingYawAndThenMovingForward() {
        Camera c = new Camera();
        c.rotateYaw(90);
        c.moveForward(5);
        assertEquals(5, c.getPosition().x(), testdelta);
        assertEquals(0, c.getPosition().y(), testdelta);
        assertEquals(0, c.getPosition().z(), testdelta);
    }

    @Test
    public void testRotatingYawTwiceAndThenMovingForward() {
        Camera c = new Camera();
        c.rotateYaw(45);
        c.rotateYaw(45);
        c.moveForward(5);
        assertEquals(5, c.getPosition().x(), testdelta);
        assertEquals(0, c.getPosition().y(), testdelta);
        assertEquals(0, c.getPosition().z(), testdelta);
    }
    
    @Test
    public void testRotatingPitchAndThenMovingForward() {
        Camera c = new Camera();
        c.rotatePitch(90);
        c.moveForward(5);
        assertEquals(0, c.getPosition().x(), testdelta);
        assertEquals(0, c.getPosition().y(), testdelta);
        assertEquals(5, c.getPosition().z(), testdelta);
    }

    @Test
    public void testRotatingYawAndPitchAndThenMovingForward() {
        Camera c = new Camera();
        c.rotateYaw(90);
        c.rotatePitch(90);
        c.moveForward(5);
        assertEquals(0, c.getPosition().x(), testdelta);
        assertEquals(0, c.getPosition().y(), testdelta);
        assertEquals(5, c.getPosition().z(), testdelta);
    }
}
