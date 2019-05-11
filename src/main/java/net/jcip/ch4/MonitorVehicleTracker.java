/*
 * MonitorVehicleTracker.java
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * {@link MonitorVehicleTracker} shows an implementation of the vehicle tracker using the Java monitor pattern that uses {@link
 * MutablePoint} for representing the vehicle locations. Even though {@link MutablePoint} is not thread-safe, the tracker class is.
 * Neither the map nor any of the mutable points it contains is ever published. When we need to return vehicle locations to callers,
 * the appropriate values are copied using either the {@link MutablePoint} copy constructor or {@link #deepCopy(Map)}, which creates
 * a new {@link Map} whose values are copies of the keys and values from the old Map.
 * <p>
 * This implementation maintains thread safety in part by copying mutable data before returning it to the client. This is usually
 * not a performance issue, but could become one if the set of vehicles is very large.
 * <p>
 * Another consequence of copying the data on each call to {@link #getLocations()} is that the contents of the returned collection
 * do not change even if the underlying locations change. Whether this is good or bad depends on your requirements. It could be a
 * benefit if there are internal consistency requirements on the location set, in which case returning a consistent snapshot is
 * critical, or a drawback if callers require up-to-date information for each vehicle and therefore need to refresh their snapshot
 * more often.
 */
@ThreadSafe public class MonitorVehicleTracker {
    @GuardedBy("this")
    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint loc = locations.get(id);
        if (loc == null)
            throw new IllegalArgumentException("No such ID: " + id);
        loc.x = x;
        loc.y = y;
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();
        for (String id : m.keySet())
            result.put(id, new MutablePoint(m.get(id)));
        return Collections.unmodifiableMap(result);
    }
}