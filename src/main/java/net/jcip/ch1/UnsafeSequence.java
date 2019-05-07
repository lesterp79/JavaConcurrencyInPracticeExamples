/*
 * UnsafeSequence.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch1;

import net.jcip.annotations.NotThreadSafe;


@NotThreadSafe
public class UnsafeSequence {

    public static void main(String[] args) {

    }

    private int value;
    /** Returns a unique value. */
    public int getNext() {
        return value++;
    }
}
