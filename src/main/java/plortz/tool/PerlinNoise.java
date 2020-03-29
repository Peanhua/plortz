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
package plortz.tool;

import java.util.Random;
import plortz.Terrain;
import plortz.Vector;

/**
 * Adjusts the altitudes using perlin noise.
 * 
 * Altitude changes are in range [-scale, scale].
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class PerlinNoise extends Tool {

    private final double scale;
    private final Random random;
    private final double density;
    
    private int      gradient_width;
    private int      gradient_height;
    private Vector[] gradients;
    
    public PerlinNoise(double scale, double density, Random random) {
        this.scale   = scale;
        this.density = density;
        this.random  = random;
    }
    
    @Override
    public void apply(Terrain terrain) {
        this.gradient_width  = (int) ((double) terrain.getWidth() * this.density);
        this.gradient_height = (int) ((double) terrain.getHeight() * this.density);
        
        this.gradients = new Vector[gradient_width * gradient_height];
        for (int i = 0; i < this.gradients.length; i++) {
            this.gradients[i] = this.generateRandomUnitVector();
        }
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                // Candidate is the location of the current tile in the gradient space.
                // The maximum x and y coordinates are one less than the gradient size because there must always be a gradient around all sides of the candidate point.
                Vector candidate = new Vector((double) x / (double) (terrain.getWidth())  * (gradient_width  - 1),
                                              (double) y / (double) (terrain.getHeight()) * (gradient_height - 1));
                int cx = (int) candidate.getX();
                int cy = (int) candidate.getY();
                
                double topleft     = this.getGradientDotProduct(cx + 0, cy + 0, candidate);
                double topright    = this.getGradientDotProduct(cx + 1, cy + 0, candidate);
                double bottomleft  = this.getGradientDotProduct(cx + 0, cy + 1, candidate);
                double bottomright = this.getGradientDotProduct(cx + 1, cy + 1, candidate);
                
                double lerpweight_x = this.smootherstep(0, 1, candidate.getX() - (double) cx);
                double lerpweight_y = this.smootherstep(0, 1, candidate.getY() - (double) cy);

                double top    = this.lerp(topleft,    topright,    lerpweight_x);
                double bottom = this.lerp(bottomleft, bottomright, lerpweight_x);
                double result = this.lerp(top, bottom, lerpweight_y);
                
                terrain.getTile(x, y).adjustAltitude((result * 2.0 - 1.0) * this.scale);
            }
        }
    }
    
    
    private double getGradientDotProduct(int gradient_x, int gradient_y, Vector for_point) {
        Vector gradient = this.gradients[gradient_x + gradient_y * this.gradient_width];
        Vector offset   = for_point.subtract(new Vector(gradient_x, gradient_y));
        return gradient.getDotProduct(offset);
    }
    

    private double lerp(double a, double b, double weight) {
        return (1.0 - weight) * a + weight * b;
    }

    
    private Vector generateRandomUnitVector() {
        double[] coords = {
            this.random.nextDouble() * 2.0 - 1.0,
            this.random.nextDouble() * 2.0 - 1.0
        };
        Vector v = new Vector(coords);
        v.normalize();
        return v;
    }
    

    /**
     * Smoothstep function to turn linear values "/" to "S" -curve.
     * 
     * https://en.wikipedia.org/wiki/Smoothstep
     * This is the Ken Perlins variation suggestion.
     * 
     * @param edge0 The minimum value.
     * @param edge1 The maximum value.
     * @param x     The value to transform.
     * @return Value x transformed by an "S" -curve between edge0 and edge1.
     */
    private double smootherstep(double edge0, double edge1, double x) {
        // Scale, and clamp x to 0..1 range
        x = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
        // Evaluate polynomial
        return x * x * x * (x * (x * 6 - 15) + 10);
    }

    private double clamp(double x, double lowerlimit, double upperlimit) {
        if (x < lowerlimit) {
            x = lowerlimit;
        }
        if (x > upperlimit) {
            x = upperlimit;
        }
        return x;
    }
}
