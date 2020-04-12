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

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import plortz.util.Position;
import static org.junit.Assert.*;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class BreadthFirstSearchTest {
    
    public BreadthFirstSearchTest() {
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

    private String[] map1 = {
        " 12345678901234567890",
        "1####################",
        "2#  ###      ##     #",
        "3##   # #### #  ### #",
        "4#### # #      ## # #",
        "5###  # # #### ## # #",
        "6### ## # ####    # #",
        "7### #  #      ## # #",
        "8### ############## #",
        "9###                #",
        "0####################"
    };
    @Test
    public void testScenariosProduceShortestPathsMap1() {
        PathFinderTestHeuristic h = new PathFinderTestHeuristic(map1, new Position(6, 7));
        PathFinder search = new BreadthFirstSearch();
        List<Position> path = search.find(new Position(2, 2), h);
        assertNotNull(path);
        assertEquals(44, path.size());
    }
    
    private String[] map2 = {
        " 1234",
        "1####",
        "2#  #",
        "3####"
    };
    @Test
    public void testScenariosProduceShortestPathsMap2() {
        PathFinderTestHeuristic h = new PathFinderTestHeuristic(map2, new Position(3, 2));
        PathFinder search = new BreadthFirstSearch();
        List<Position> path = search.find(new Position(2, 2), h);
        assertNotNull(path);
        assertEquals(2, path.size());
    }

    private String[] map3 = {
        " 123456",
        "1######",
        "2#    #",
        "3#    #",
        "4#    #",
        "5######"
    };
    @Test
    public void testScenariosProduceShortestPathsMap3() {
        PathFinderTestHeuristic h = new PathFinderTestHeuristic(map3, new Position(5, 3));
        PathFinder search = new BreadthFirstSearch();
        List<Position> path = search.find(new Position(2, 3), h);
        assertNotNull(path);
        assertEquals(4, path.size());
        assertEquals(new Position(2, 3), path.get(0));
        assertEquals(new Position(5, 3), path.get(3));
    }
    
}
