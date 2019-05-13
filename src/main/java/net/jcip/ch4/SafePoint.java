/*
 * SafePoint.java
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

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * {@link SafePoint} provides a getter that retrieves both the x and y values at once by returning a two-element array.
 * <p>
 * If we provided separate getters for x and y, then the values could change between the time one coordinate is retrieved and the
 * other, resulting in a caller seeing an inconsistent value: an (x, y) location where the vehicle never was.
 * <p>
 * Using {@link SafePoint}, we can construct a vehicle tracker that publishes the underlying mutable state without undermining
 * thread safety, as shown in the PublishingVehicleTracker.
 */
@ThreadSafe public class SafePoint {
    @GuardedBy("this")
    private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint p) {
        this(p.get());
    }

    public SafePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public synchronized int[] get() {
        return new int[]{x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}