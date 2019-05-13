/*
 * BetterVector.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch4;

import java.util.Vector;
import net.jcip.annotations.ThreadSafe;

/**
 * {@link BetterVector} extends {@link Vector} to add a {@link #putIfAbsent(Object)} method. Extending Vector is straightforward
 * enough, but not all classes expose enough of their state to subclasses to admit this approach.
 */
@ThreadSafe public class BetterVector<E> extends Vector<E> {
    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent)
            add(x);
        return absent;
    }
}