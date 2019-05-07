/*
 * UnsafeCachingFactorizer.java
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
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jcip.annotations.NotThreadSafe;

/**
 * Even though the atomic references are individually thread-safe, UnsafeCachingFactorizer has race conditions that could make it
 * produce the wrong answer.
 */
@NotThreadSafe public class UnsafeCachingFactorizer extends FactorizerServlet {

    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get()))
            // a check-then-act race condition might happen if a thread blocks here and then acts on the stale value for the
            // last number cached, while the last numbers cached and last factors were changed by another thread, potentially
            // returning incorrect results.
            encodeIntoResponse(resp, lastFactors.get());
        else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(resp, factors);
        }
    }
}