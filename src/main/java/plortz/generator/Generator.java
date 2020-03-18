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
package plortz.generator;

import java.util.HashMap;
import plortz.Terrain;

/**
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public abstract class Generator {
    private final Terrain terrain;
    private final HashMap<String, Integer> int_parameters;
    private final HashMap<String, Double>  double_parameters;
    private final HashMap<String, String>  string_parameters;
    
    public Generator(int width, int height) {
        this.terrain           = new Terrain(width, height);
        this.int_parameters    = new HashMap<>();
        this.double_parameters = new HashMap<>();
        this.string_parameters = new HashMap<>();
    }
    
    public Terrain getTerrain() {
        return this.terrain;
    }
    
    public Integer getIntParameter(String type) {
        return this.int_parameters.get(type);
    }
    
    public Double getDoubleParameter(String type) {
        return this.double_parameters.get(type);
    }
    
    public String getStringParameter(String type) {
        return this.string_parameters.get(type);
    }

    /**
     * Set integer parameter.
     * 
     * @param type Name of the parameter.
     * @param value 
     */
    public void setParameter(String type, int value) {
        this.int_parameters.put(type, value);
    }

    /**
     * Set double parameter.
     * 
     * @param type Name of the parameter.
     * @param value 
     */
    public void setParameter(String type, double value) {
        this.double_parameters.put(type, value);
    }
    
    /**
     * Set string parameter.
     * 
     * @param type Name of the parameter.
     * @param value 
     */
    public void setParameter(String type, String value) {
        this.string_parameters.put(type, value);
    }
    
    /**
     * Run the generator.
     */
    public abstract void run();
}
