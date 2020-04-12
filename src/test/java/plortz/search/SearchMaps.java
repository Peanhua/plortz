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
import plortz.util.ArrayList;
import plortz.util.Position;

/**
 * Maps to use for testing searches.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class SearchMaps {
    
    public class Map {
        public String[] map;
        public Position start;
        public Position end;
        public int      path_length;
        
        public Map(String[] map, Position start, Position end, int path_length) {
            this.map         = map;
            this.start       = start;
            this.end         = end;
            this.path_length = path_length;
        }
    };
    
    public final List<Map> maps;
    
    public SearchMaps() {
        this.maps = new ArrayList<>();
        this.maps.add(new Map(map1, new Position(2, 2), new Position(6, 7),  44));
        this.maps.add(new Map(map2, new Position(2, 2), new Position(3, 2),   2));
        this.maps.add(new Map(map3, new Position(2, 3), new Position(5, 3),   4));
        this.maps.add(new Map(map4, new Position(2, 2), new Position(2, 4),  -1));
        this.maps.add(new Map(map5, new Position(2, 2), new Position(9, 16), 15));
    }

    private final String[] map1 = {
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
    private final String[] map2 = {
        " 1234",
        "1####",
        "2#  #",
        "3####"
    };
    private final String[] map3 = {
        " 123456",
        "1######",
        "2#    #",
        "3#    #",
        "4#    #",
        "5######"
    };
    private final String[] map4 = {
        " 123456",
        "1######",
        "2#    #",
        "3######",
        "4#    #",
        "5######"
    };
    private String[] map5 = {
        " 12345678901234567890",
        "1####################",
        "2#                  #",
        "3# #                #",
        "4# #                #",
        "5# #                #",
        "6# #                #",
        "7# #                #",
        "8# #                #",
        "9#                  #",
        "0#                  #",
        "1#                  #",
        "2#                  #",
        "3#                  #",
        "4#                  #",
        "5#                  #",
        "6#                  #",
        "7####################"
    };
}
