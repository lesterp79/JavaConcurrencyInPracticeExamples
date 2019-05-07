/*
 * FactorizerServlet.java
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
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class FactorizerServlet implements Servlet {

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }

    protected BigInteger[] factor(BigInteger aI) {
        return new BigInteger[]{BigInteger.ONE};
    }

    protected void encodeIntoResponse(ServletResponse aResp, BigInteger[] aFactors) {
    }

    protected BigInteger extractFromRequest(ServletRequest aReq) {
        return BigInteger.ONE;
    }

    public void init(ServletConfig aServletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }
}
