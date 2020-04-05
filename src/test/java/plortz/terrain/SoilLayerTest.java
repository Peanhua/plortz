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
package plortz.terrain;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import plortz.util.Vector;
import plortz.util.MyArrayList;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SoilLayerTest {
    
    private SoilLayer layer;
    private double    testdelta;
    
    public SoilLayerTest() {
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
        layer = new SoilLayer(SoilLayer.Type.DIRT, 0.0);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void adjustmentAdjusts() {
        double oldval = layer.getAmount();
        double amount = 1.2345;
        layer.adjustAmount(amount);
        assertEquals(oldval + amount, layer.getAmount(), testdelta);
    }
    
    @Test
    public void allTypesHaveDifferentRGBValues() {
        List<Vector> colors = new MyArrayList<>();
        
        for (SoilLayer.Type type : SoilLayer.Type.values()) {
            layer = new SoilLayer(type, 0.0);
            colors.add(layer.getRGB());
        }
        
        for (int i = 0; i < colors.size(); i++) {
            for (int j = i + 1; j < colors.size(); j++) {
                int matchingvalues = 0;
                for (int k = 0; k < 3; k++) {
                    if (Math.abs(colors.get(i).get(k) - colors.get(j).get(k)) < testdelta) {
                        matchingvalues++;
                    }
                }
                assertTrue(matchingvalues < 3);
            }
        }
    }
}
