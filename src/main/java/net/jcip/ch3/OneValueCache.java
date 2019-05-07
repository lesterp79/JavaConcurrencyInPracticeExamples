/*
 * OneValueCache.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch3;

import java.math.BigInteger;
import java.util.Arrays;
import net.jcip.annotations.Immutable;

/**
 * * The {@link net.jcip.ch3.VolatileCachedFactorizer} performs two operations that must be atomic: updating the cached result and
 *  conditionally fetching the cached factors if the cached number matches the requested number. Whenever a group of related data
 *  items must be acted on atomically, consider creating an immutable holder class for them, such as {@link OneValueCache]}.
 *
 *
 */
@Immutable
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;
    public OneValueCache(BigInteger i,
                    BigInteger[] factors) {
        lastNumber = i;
        lastFactors = Arrays.copyOf(factors, factors.length);
    }
    public BigInteger[] getFactors(BigInteger i) {
        if (lastNumber == null || !lastNumber.equals(i))
            return null;
        else
            return Arrays.copyOf(lastFactors, lastFactors.length);
    }

    public static void main(String[] args) {
        System.out.println("new BigInteger(null) = " + new OneValueCache(null, null));

    }
}