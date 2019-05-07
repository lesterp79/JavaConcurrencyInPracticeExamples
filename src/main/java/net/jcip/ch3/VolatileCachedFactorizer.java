/*
 * VolatileCachedFactorizer.java
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
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jcip.annotations.ThreadSafe;
import net.jcip.ch2.FactorizerServlet;

/**
 * In {@link net.jcip.ch2.UnsafeCachingFactorizer}, we tried to use two AtomicReferences to store the last number and last factors,
 * but this was not thread-safe because we could not fetch or update the two related values atomically.
 *
 *  Using volatile variables for these values would not be thread-safe for the same reason. However, immutable objects can sometimes
 *  provide a weak form of atomicity.
 *
 * The factoring servlet performs two operations that must be atomic: updating the cached result and conditionally fetching the
 * cached factors if the cached number matches the requested number. Whenever a group of related data items must be acted on
 * atomically, consider creating an immutable holder class for them, such as {@link OneValueCache]}.
 *
 * Race conditions in accessing or updating multiple related variables can be eliminated by using an immutable object to hold all
 * the variables. With a mutable holder object, you would have to use locking to ensure atomicity; with an immutable one, once a
 * thread acquires a reference to it, it need never worry about another thread modifying its state.
 *
 * Note that this ONLY works because {@link #cache} is immutable AND its only accessed ONCE in the code path.
 *
 */
@ThreadSafe public class VolatileCachedFactorizer extends FactorizerServlet {
    private volatile OneValueCache cache = new OneValueCache(BigInteger.ONE, new BigInteger[]{BigInteger.ONE});

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }
        encodeIntoResponse(resp, factors);
    }
}