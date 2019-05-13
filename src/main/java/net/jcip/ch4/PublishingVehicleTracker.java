/*
 * PublishingVehicleTracker.java
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.jcip.annotations.ThreadSafe;

/**
 * {@link PublishingVehicleTracker} derives its thread safety from delegation to an underlying {@link ConcurrentHashMap}, but this
 * time the contents of the Map are thread-safe mutable points rather than immutable ones.
 * <p>
 * The {@link #getLocations()} method returns an unmodifiable copy of the underlying Map. Callers cannot add or remove vehicles, but
 * could change the location of one of the vehicles by mutating the {@link SafePoint} values in the returned Map.
 * <p>
 * Again, the “live” nature of the Map may be a benefit or a drawback, depending on the requirements. {@link
 * PublishingVehicleTracker} is thread-safe, but would not be so if it imposed any additional constraints on the valid values for
 * vehicle locations. If it needed to be able to “veto” changes to vehicle locations or to take action when a location changes, the
 * approach taken by {@link PublishingVehicleTracker-} would not be appropriate.
 */
@ThreadSafe public class PublishingVehicleTracker {
    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    public PublishingVehicleTracker(Map<String, SafePoint> locations) {
        this.locations = new ConcurrentHashMap<String, SafePoint>(locations);
        this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
    }

    public Map<String, SafePoint> getLocations() {
        return unmodifiableMap;
    }

    public SafePoint getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (!locations.containsKey(id))
            throw new IllegalArgumentException("invalid vehicle name: " + id);
        locations.get(id).set(x, y);
    }
}