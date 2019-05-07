/*
 * SynchronizedInteger.java
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

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * We can make {@link MutableInteger} thread safe by synchronizing the getter and setter as shown in {@link SynchronizedInteger}.
 * Synchronizing only the setter would not be sufficient: threads calling get would still be able to see stale values.
 *
 * Notice however that this object it's STILL susceptible to a race condition. if the getter and setters are used in a check-then-act
 * scenario ib a multithreaded environment, a program may behave based on a "stale" value, not because of visibility issues, but because
 * CPU interleaving may cause a thread to "lose" an update by another one. For example, consider this code that tries to reset
 * value once it reaches 10:
 *
 * SynchronizedInteger syncInt = new SynchronizedInteger();
 * if (syncInt.get() == 10) {
 *    syncInt.set(0);
 * }
 */
@ThreadSafe public class SynchronizedInteger {

    @GuardedBy("this")
    private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value) {
        this.value = value;
    }
}