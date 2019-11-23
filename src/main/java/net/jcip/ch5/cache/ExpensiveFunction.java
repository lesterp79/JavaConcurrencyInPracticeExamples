/*
 * ExpensiveFunction.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch5.cache;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import net.jcip.annotations.GuardedBy;

/**
 * The ExpensiveFunction, which implements {@link Computable}, takes a long time to compute.
 */
public class ExpensiveFunction implements Computable<String, BigInteger> {
    public BigInteger compute(String arg) {
        // after deep thought...
        return new BigInteger(arg);
    }
}
