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
package plortz;

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
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class MersenneTwister extends Random {
    private boolean    initialized;
    private final long W;
    private final int  N;
    private final int  M;
    private final long R;
    private final long A;
    private final long U;
    private final long D;
    private final long S;
    private final long B;
    private final long T;
    private final long C;
    private final long L;
    private final long F;
    private long[] MT; // State
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
        this.W = 32;
        this.N = 624;
        this.M = 397;
        this.R = 31;
        this.A = 0x9908B0DFL;
        this.U = 11;
        this.D = 0xFFFFFFFFL;
        this.S = 7;
        this.B = 0x9D2C5680L;
        this.T = 15;
        this.C = 0xEFC60000L;
        this.L = 18;
        
        this.F = 1812433253L;
        
        this.MT = new long[this.N];
        this.index = this.N + 1;
        this.lower_mask = (1L << this.R) - 1L;
        this.upper_mask = ((1L << this.W) - 1L) & (~this.lower_mask);

        this.initialized = true;
        this.setSeed(seed);
    }
    
    @Override
    public final void setSeed(long seed) {
        if (!initialized) { // The super class calls this before our constructor.
            return;
        }
        this.index = this.N;
        this.MT[0] = seed;
        for (int i = 1; i < this.N; i++) {
            this.MT[i] = ((1L << this.W) - 1L) & (this.F * (this.MT[i - 1] ^ (this.MT[i - 1] >> (this.W - 2))) + i);
        }
    }

    @Override
    public double nextDouble() {
        if (this.index >= this.N) {
            this.twist();
        }
        long y = this.MT[this.index];
        y = y ^ ((y >> this.U) & this.D);
        y = y ^ ((y << this.S) & this.B);
        y = y ^ ((y << this.T) & this.C);
        y = y ^ (y >> this.L);
        this.index++;
        
        y = y & ((1L << this.W) - 1L);
        return (double) y / (double) ((1L << this.W) - 1L);
    }
    
    private void twist() {
        for (int i = 0; i < this.N; i++) {
            long x = this.MT[i] & this.upper_mask;
            x += this.MT[(i + 1) % this.N] & this.lower_mask;
            long xA = x >> 1;
            if ((x % 2L) != 0) {
                xA = xA ^ this.A;
            }
            this.MT[i] = this.MT[(i + this.M) % this.N] ^ xA;
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
