/*
 * Memoizer.java
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

import java.util.HashMap;
import java.util.Map;
import net.jcip.annotations.GuardedBy;

/**
 * The ExpensiveFunction, which implements Computable, takes a long time to compute its result; we’d like to create a Computable
 * wrapper that remembers the results of previous computations and encapsulates the caching process. (This technique is known as
 * memoization.)
 */
public class SynchronizedMemoizer<A, V> implements Computable<A, V> {
    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<A, V>();
    private final Computable<A, V> c;
    public SynchronizedMemoizer(Computable<A, V> c) {
        this.c = c;
    }
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}