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
public class AStarTest {
    
    protected SearchMaps maps;
    protected PathFinder path_finder;
    
    public AStarTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.maps = new SearchMaps();
        this.path_finder = new AStar();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testScenariosProduceShortestPaths() {
        for (SearchMaps.Map map : maps.maps) {
            PathFinderTestHeuristic h = new PathFinderTestHeuristic(map.map, map.end);
            List<Position> path = path_finder.find(map.start, h);
            if (map.path_length > 0) {
                assertNotNull(path);
                assertEquals(map.path_length, path.size());
            } else {
                assertNull(path);
            }
        }
    }

    @Test
    public void map3PathIsStraightLine() {
        SearchMaps.Map map = maps.maps.get(2);
        PathFinderTestHeuristic h = new PathFinderTestHeuristic(map.map, map.end);
        List<Position> path = path_finder.find(map.start, h);
        assertEquals(new Position(2, 3), path.get(0));
        assertEquals(new Position(3, 3), path.get(1));
        assertEquals(new Position(4, 3), path.get(2));
        assertEquals(new Position(5, 3), path.get(3));
    }
}
