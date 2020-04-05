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

import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Mersenne Twister based random number generator.
 * 
 * Follows the pseudocode from https://en.wikipedia.org/wiki/Mersenne_Twister
 * Variant MT19937
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class MersenneTwister extends Random {
    private boolean    initialized;
    private final long w;
    private final int  n;
    private final int  m;
    private final long r;
    private final long a;
    private final long u;
    private final long d;
    private final long s;
    private final long b;
    private final long t;
    private final long c;
    private final long l;
    private final long f;
    private long[] mt; // State
    private int    index;
    private final long lower_mask;
    private final long upper_mask;
    
    public MersenneTwister() {
        this(System.currentTimeMillis());
    }
    
    public MersenneTwister(int seed) {
        this((long) seed);
    }
    
    public MersenneTwister(long seed) {
        // Coefficients for MT19937:
        this.w = 32;
        this.n = 624;
        this.m = 397;
        this.r = 31;
        this.a = 0x9908B0DFL;
        this.u = 11;
        this.d = 0xFFFFFFFFL;
        this.s = 7;
        this.b = 0x9D2C5680L;
        this.t = 15;
        this.c = 0xEFC60000L;
        this.l = 18;
        
        this.f = 1812433253L;
        
        this.mt = new long[this.n];
        this.index = this.n + 1;
        this.lower_mask = (1L << this.r) - 1L;
        this.upper_mask = ((1L << this.w) - 1L) & (~this.lower_mask);

        this.initialized = true;
        this.setSeed(seed);
    }
    
    @Override
    public final void setSeed(long seed) {
        if (!initialized) { // The super class calls this before our constructor.
            return;
        }
        this.index = this.n;
        this.mt[0] = seed;
        for (int i = 1; i < this.n; i++) {
            this.mt[i] = ((1L << this.w) - 1L) & (this.f * (this.mt[i - 1] ^ (this.mt[i - 1] >> (this.w - 2))) + i);
        }
    }

    @Override
    public double nextDouble() {
        if (this.index >= this.n) {
            this.twist();
        }
        long y = this.mt[this.index];
        y = y ^ ((y >> this.u) & this.d);
        y = y ^ ((y << this.s) & this.b);
        y = y ^ ((y << this.t) & this.c);
        y = y ^ (y >> this.l);
        this.index++;
        
        y = y & ((1L << this.w) - 1L);
        return (double) y / (double) ((1L << this.w) - 1L);
    }
    
    private void twist() {
        for (int i = 0; i < this.n; i++) {
            long x = this.mt[i] & this.upper_mask;
            x += this.mt[(i + 1) % this.n] & this.lower_mask;
            long xA = x >> 1;
            if ((x % 2L) != 0) {
                xA = xA ^ this.a;
            }
            this.mt[i] = this.mt[(i + this.m) % this.n] ^ xA;
        }
        this.index = 0;
    }

    
    @Override
    public float nextFloat() {
        return (float) this.nextDouble();
    }

    @Override
    public boolean nextBoolean() {
        return this.nextDouble() < 0.5;
    }

    @Override
    public long nextLong() {
        return (long) (this.nextDouble() * Long.MAX_VALUE);
    }

    @Override
    public int nextInt(int bound) {
        return (int) (this.nextDouble() * bound);
    }

    @Override
    public int nextInt() {
        return (int) (this.nextDouble() * Integer.MAX_VALUE);
    }

    
    // The remaining methods are not yet implemented:
    @Override
    public DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DoubleStream doubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DoubleStream doubles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DoubleStream doubles(long streamSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LongStream longs(long randomNumberOrigin, long randomNumberBound) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LongStream longs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LongStream longs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LongStream longs(long streamSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IntStream ints(int randomNumberOrigin, int randomNumberBound) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IntStream ints(long streamSize, int randomNumberOrigin, int randomNumberBound) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IntStream ints() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IntStream ints(long streamSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized double nextGaussian() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void nextBytes(byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int next(int bits) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
