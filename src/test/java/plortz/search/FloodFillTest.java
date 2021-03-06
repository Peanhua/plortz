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
package plortz.search;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import plortz.util.Position;
import plortz.util.Static2dArray;
import static org.junit.Assert.*;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class FloodFillTest {
    
    private class FFCallback implements FloodFill.FloodFillCallback {

        private final Static2dArray<Character> map;
        public final List<Position>            filled;
        
        
        public FFCallback(Static2dArray<Character> map) {
            this.map    = map;
            this.filled = new ArrayList<>();
        }
        
        @Override
        public boolean isPositionIn(Position position) {
            return this.map.get(position) == ' ';
        }

        @Override
        public void fill(Position position) {
            this.filled.add(position);
        }
    }
    
    public FloodFillTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    private Static2dArray<Character> mapToArr(String[] map) {
        Static2dArray<Character> rv = new Static2dArray<>(map[0].length(), map.length);
        
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length(); x++) {
                rv.set(x, y, map[y].charAt(x));
            }
        }
        
        return rv;
    }
    
    private void testMap(String[] source_map, Position start, boolean debugprint) {
        Static2dArray<Character> map = mapToArr(source_map);
        FloodFill ff = new FloodFill();
        FFCallback cb = new FFCallback(map);
        ff.fill(map.getWidth(), map.getLength(), start, cb);
        if (debugprint) {
            for (int y = 0; y < map.getLength(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    if (cb.filled.contains(new Position(x, y))) {
                        System.out.print("*");
                    } else {
                        System.out.print(map.get(x, y));
                    }
                }
                System.out.println("");
            }
            for (Position pos : cb.filled) {
                System.out.println(pos);
            }
        }
        int target_fill_count = 0;
        int border_count = 0;
        for (int y = 0; y < map.getLength(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                boolean was_empty = map.get(x, y) == ' ';
                boolean was_filled = cb.filled.contains(new Position(x, y));
                if (was_empty) {
                    assertTrue(was_filled);
                    target_fill_count++;
                } else {
                    assertFalse(was_filled);
                }
                if (map.get(x, y) == 'x') {
                    border_count++;
                }
            }
        }
        assertEquals(target_fill_count, cb.filled.size());
        assertEquals(border_count, ff.getBorders().size());
    }

    String[] map1 = {
        "01234",
        "1dxxd",
        "2x  x",
        "3x  x",
        "4x  x",
        "5dxxd"
    };
    @Test
    public void testMap1() {
        testMap(map1, new Position(2, 3), false);
    }
    
    String[] map2 = {
        "0123456789",
        "1dxxxxxxxd",
        "2x       x",
        "3x       x",
        "4x       x",
        "5dxxxxxxxd"
    };
    @Test
    public void testMap2() {
        testMap(map2, new Position(5, 3), false);
    }

    String[] map3 = {
        "0123456789",
        "1dxxxxxxxd",
        "2x       x",
        "3x x  xxxd",
        "4x x     x",
        "5dxdxxxxxd"
    };
    @Test
    public void testMap3() {
        testMap(map3, new Position(2, 2), false);
    }

    String[] map4 = {
        ".....................................................................",
        "...................dxxxxxxxxxxxxxxxxxxxxxd...........................",
        "...............dxxxx                     xxxxd.......................",
        "...........dxxxx                             xxxxxxxd................",
        "........dxxx                                        xxxxd............",
        "......dxx                                               xd...........",
        ".....dx                                                  xd..........",
        "....dx                                                    xxd........",
        "...dx                                                       xd.......",
        ".dxx           xxxxxx                xxxxxxxxx               xd......",
        ".x            xd....dx              xd.......dx               xd.....",
        ".x           xd......dx            xd.........x                xd....",
        ".x           x........x            xd........dx                 xd...",
        "dx           xd......dx             xd......dx                   xd..",
        "x             xxxxxxxx               xxxxxxxx                     xd.",
        "x                                                                  xd",
        "x                                                                   x",
        "x                      x      xx                                    x",
        "x                      x        x          x x x x                  x",
        "x                      x      xx           x x x x                  x",
        "x                       xxxxxx             xxdxdxx                  x",
        "x                                          x x x x                  x",
        "dx                                         x x x x                 xd",
        ".dx                                                               xd.",
        "..dx                                                             xd..",
        "...dxx                                                         xxd...",
        ".....dx                                                       xd.....",
        "......dx               xxxxxxxxxxxxxxxxx                     xd......",
        ".......dxx          xxx                 xx                  xd.......",
        ".........dx        x                      x                xd........",
        "..........dx      x                        x              xd.........",
        "...........x      x                       x              xd..........",
        "...........dx      xxxxxxxxx xxxxxxxxxxxxx              xd...........",
        "............x                                          xd............",
        "............dx                                         x.............",
        ".............x                                        xd.............",
        ".............dxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxd..............",
        "..............x        x         x                   x...............",
        "..............x        x         x       x           x...............",
        "..............x        x         x       x           x...............",
        "..............x                  x       x           x...............",
        "..............x        x         x       x           x...............",
        "..............x        x         x       x           x...............",
        "..............x        x         x       x           x...............",
        "..............x        x                 x           x...............",
        "..............dxxxxxxxxdxxxxxxxxxxxxxxxxxdxxxxxxxxxxxd..............."
    };
    @Test
    public void testMap4() {
        testMap(map4, new Position(10, 20), false);
    }

}
