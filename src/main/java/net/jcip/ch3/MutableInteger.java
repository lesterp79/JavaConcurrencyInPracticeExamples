/*
 * MutableInteger.java
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

import net.jcip.annotations.NotThreadSafe;

/**
 * {@link MutableInteger} is not thread-safe because the value field is accessed from both get and set without synchronization.
 * Among other hazards, it is susceptible to stale values: if one thread calls set, other threads calling get may or may not see
 * that update.
 * <p>
 * Note that it does NOT matter that {@link #value} is a private field. As long as its containing object is used to share state by
 * multiple threads, its value can be stale.
 */
@NotThreadSafe
public class MutableInteger {
    private int value;

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }
}