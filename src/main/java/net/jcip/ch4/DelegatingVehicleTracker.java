/*
 * DelegatingVehicleTracker.java
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.jcip.annotations.ThreadSafe;

/**
 * {@link DelegatingVehicleTracker} does not use any explicit synchronization; all access to state is managed by {@link
 * ConcurrentHashMap}, and all the keys and values of the Map are immutable.
 * <p>
 * If we had used the original {@link MutablePoint} class instead of {@link Point}, we would be breaking encapsulation by letting
 * getLocations publish a reference to mutable state that is not thread-safe.
 * <p>
 * Notice that we’ve changed the behavior of the vehicle tracker class slightly; while the monitor version returned a snapshot of
 * the locations, the delegating version returns an unmodifiable but “live” view of the vehicle locations. This means that if thread
 * A calls {@link #getLocations()} and thread B later modifies the location of some of the points, those changes are reflected in
 * the Map returned to thread A. As we remarked earlier, this can be a benefit (more up-to-date data) or a liability (potentially
 * inconsistent view of the fleet), depending on your requirements. If an unchanging view of the fleet is required, {@link
 * #getLocations()} could instead return a shallow copy of the locations map.
 * <p>
 * Since the contents of the Map are immutable, only the structure of the Map, not the contents, must be copied, as shown {@link
 * #getLocationsSnapShot()} (which returns a plain HashMap, since getLocations did not promise to return a thread-safe Map)
 */
@ThreadSafe public class DelegatingVehicleTracker {
    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<String, Point>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, new Point(x, y)) == null)
            throw new IllegalArgumentException("invalid vehicle name: " + id);
    }

    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    public Map<String, Point> getLocationsSnapShot() {
        return Collections.unmodifiableMap(new HashMap<String, Point>(locations));
    }
}
