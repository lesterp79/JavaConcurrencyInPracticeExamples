/*
 * Point.java
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

import net.jcip.annotations.Immutable;

/**
 * Point is thread-safe because it is immutable. Immutable values can be freely
 * shared and published, so we no longer need to copy the locations when returning
 * them.
 */
@Immutable public class Point {
    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}