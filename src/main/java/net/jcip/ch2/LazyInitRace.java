/*
 * LazyInitRace.java
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

import net.jcip.annotations.NotThreadSafe;

/**
 * Race condition in lazy initialization. Don’t do this.
 */
@NotThreadSafe public class LazyInitRace {
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        // this observation may become stale...
        if (instance == null)
            //...if execution of another thread is interleaved here (or when a thread is still instantiating ExpensiveObject),
            // ... a race condition will occcur, which may cause even two threads getting different instances of the object
            instance = new ExpensiveObject();
        return instance;
    }

    private class ExpensiveObject {
    }
}