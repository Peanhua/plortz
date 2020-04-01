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
package plortz.observer;

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
public class ObserverTest {
    
    private Subject subject;

    private class MyObserver implements Observer {
        public int callCount = 0;

        @Override
        public void update() {
            this.callCount++;
        }
    };
    
    public ObserverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        subject = new Subject();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void observerIsCalled() {
        MyObserver observer = new MyObserver();
        subject.addObserver(observer);
        subject.notifyObservers();
        assertEquals(1, observer.callCount);
    }

    @Test
    public void removedObserverIsNotCalled() {
        MyObserver observer = new MyObserver();
        subject.addObserver(observer);
        subject.removeObserver(observer);
        subject.notifyObservers();
        assertEquals(0, observer.callCount);
    }
}
