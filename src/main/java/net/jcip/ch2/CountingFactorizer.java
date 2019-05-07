/*
 * CountingFactorizer.java
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
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jcip.annotations.ThreadSafe;

/**
 * The java.util.concurrent.atomic package contains atomic variable classes
 * for effecting atomic state transitions on numbers and object references. By replacing
 * the long counter with an AtomicLong, we ensure that all actions that access
 * the counter state are atomic. Because the state of the servlet is the state of the
 * counter and the counter is thread-safe, our servlet is once again thread-safe.
 */
@ThreadSafe
public class CountingFactorizer extends FactorizerServlet {

    private final AtomicLong count = new AtomicLong(0);

    public long getCount() { return count.get(); }

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(resp, factors);
    }
}