/*
 * CachedFactorizer.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch2;

import java.math.BigInteger;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 *
 * The restructuring of {@link CachedFactorizer} provides a balance between simplicity (synchronizing the entire method) and concurrency
 * (synchronizing the shortest possible code paths) It restructures the servlet to use two separate synchronized blocks, each
 * limited to a short section of code.
 *
 * One guards the check-then-act sequence that tests whether we can just return the cached result, and the other guards updating
 * both the cached number and the cached factors.
 *
 * As a bonus, we’ve reintroduced the hit counter and added a “cache hit” counter as well, updating them within the initial
 * synchronized block. Because these counters constitute shared mutable state as well, we must use synchronization everywhere
 * they are accessed.
 *
 * The portions of code that are outside the synchronized blocks operate exclusively on local (stack-based) variables, which are not
 * shared across threads and therefore do not require synchronization.
 */
@ThreadSafe
public class CachedFactorizer extends FactorizerServlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;

    public synchronized long getHits() { return hits; }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;
        synchronized (this) {
            ++hits;
            if (i.equals(lastNumber)) {
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }
        if (factors == null) {
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }
        encodeIntoResponse(resp, factors);
    }
}