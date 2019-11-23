/*
 * OpenCallsCooperatingObjects.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch10;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * Calling a method with no locks held is called an open call, and classes that rely on open calls are more well-behaved and
 * composable than classes that make calls with locks held.
 * <p>
 * Using open calls to avoid deadlock is analogous to using encapsulation to provide thread safety: while one can certainly
 * construct a thread-safe program without any encapsulation, the thread safety analysis of a program that makes effective use of
 * encapsulation is far easier than that of one that does not.
 * <p>
 * Similarly, the liveness analysis of a program that relies exclusively on open calls is far easier than that of one that does not.
 * Restricting yourself to Java open calls makes it far easier to identify the code paths that acquire multiple locks
 * and therefore to ensure that locks are acquired in a consistent order.
 */
public class OpenCallsCooperatingObjects {

    static final int NUM_TAXIS = 20;
    static final OpenCallsCooperatingObjects.Taxi[] TAXIS = new OpenCallsCooperatingObjects.Taxi[NUM_TAXIS];

    public static void main(String[] args) {
        OpenCallsCooperatingObjects OpenCallsCooperatingObjects = new OpenCallsCooperatingObjects();

        Point location = new Point(1, 1);

        for (int i = 0; i < NUM_TAXIS; i++) {
            TAXIS[i] = OpenCallsCooperatingObjects.new Taxi(location);
        }

        final Set<OpenCallsCooperatingObjects.Taxi> taxis = Arrays.stream(TAXIS).collect(Collectors.toSet());
        OpenCallsCooperatingObjects.Dispatcher dispatcher = OpenCallsCooperatingObjects.new Dispatcher(taxis);
        taxis.stream().forEach(t -> t.setDispatcher(dispatcher));

        OpenCallsCooperatingObjects.GPSThread GPSThread = new OpenCallsCooperatingObjects.GPSThread(dispatcher);
        OpenCallsCooperatingObjects.TaxiThread taxiThread = new OpenCallsCooperatingObjects.TaxiThread(taxis);

        GPSThread.start();
        taxiThread.start();

    }

    private static class GPSThread extends Thread {
        private final OpenCallsCooperatingObjects.Dispatcher dispatcher;

        public GPSThread(OpenCallsCooperatingObjects.Dispatcher aDispatcher) {
            dispatcher = aDispatcher;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                dispatcher.getImage();
            }
        }
    }

    private static class TaxiThread extends Thread {
        private final Set<OpenCallsCooperatingObjects.Taxi> taxis;

        public TaxiThread(Set<OpenCallsCooperatingObjects.Taxi> aTaxis) {
            taxis = aTaxis;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                for (OpenCallsCooperatingObjects.Taxi taxi : taxis) {
                    taxi.setLocation(new Point(1, 1));
                }
            }
        }

    }

    @ThreadSafe class Taxi {
        @GuardedBy("this")
        private Point location, destination;
        private OpenCallsCooperatingObjects.Dispatcher dispatcher;

        public Taxi(Point aDestination) {
            destination = aDestination;
        }

        public void setLocation(Point location) {
            boolean reachedDestination;
            synchronized (this) {
                this.location = location;
                reachedDestination = location.equals(destination);
            }
            if (reachedDestination)
                dispatcher.notifyAvailable(this);
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setDispatcher(OpenCallsCooperatingObjects.Dispatcher aDispatcher) {
            dispatcher = aDispatcher;
        }
    }

    @ThreadSafe class Dispatcher {
        @GuardedBy("this")
        private final Set<OpenCallsCooperatingObjects.Taxi> taxis;
        @GuardedBy("this")
        private final Set<OpenCallsCooperatingObjects.Taxi> availableTaxis;

        public Dispatcher(Set<OpenCallsCooperatingObjects.Taxi> aTaxis) {
            taxis = aTaxis;
            availableTaxis = new HashSet<OpenCallsCooperatingObjects.Taxi>();
        }

        public Dispatcher() {
            taxis = new HashSet<OpenCallsCooperatingObjects.Taxi>();
            availableTaxis = new HashSet<OpenCallsCooperatingObjects.Taxi>();
        }

        public synchronized void notifyAvailable(OpenCallsCooperatingObjects.Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public Image getImage() {
            Set<Taxi> copy;
            synchronized (this) {
                copy = new HashSet<Taxi>(taxis);
            }
            Image image = new Image();
            for (Taxi t : copy)
                image.drawMarker(t.getLocation());
            return image;
        }
    }
}
