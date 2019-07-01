/*
 * CoperatingObjectsDeadlock.java
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

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.jcip.annotations.GuardedBy;

/**
 * Consider the cooperating classes in {@link CooperatingObjectsDeadlock}, which might be used in a taxicab dispatching application.
 * {@link Taxi} represents an individual taxi with a location and a destination; {@link Dispatcher} represents a fleet of taxis.
 * <p>
 * While no method explicitly acquires two locks, callers of setLocation and getImage can acquire two locks just the same. If a
 * thread calls setLocation in response to an update from a GPS receiver, it first updates the taxi’s location and then checks to
 * see if it has reached its destination. If it has, it informs the dispatcher that it needs a new destination. Since both
 * setLocation and notifyAvailable are synchronized, the thread calling setLocation acquires the Taxi lock and then the Dispatcher
 * lock. Similarly, a thread calling getImage acquires the Dispatcher lock and then each Taxi lock (one at at time).
 * <p>
 * Just as in LeftRightDeadlock, two locks are acquired by two threads in different orders, risking deadlock. It was easy to spot
 * the deadlock possibility in LeftRightDeadlock or transferMoney by looking for methods that acquire two locks. Spotting the
 * deadlock possibility in Taxi and Dispatcher is a little harder: the warning sign is that an alien method is being called while
 * holding a lock.
 * <p>
 * Invoking an alien method with a lock held is asking for liveness trouble. The alien method might acquire other locks (risking
 * deadlock) or block for an unexpectedly long time, stalling other threads that need the lock you hold.
 */
public class CooperatingObjectsDeadlock {

    static final int NUM_TAXIS = 20;
    static final Taxi[] TAXIS = new Taxi[NUM_TAXIS];

    public static void main(String[] args) {
        CooperatingObjectsDeadlock cooperatingObjectsDeadlock = new CooperatingObjectsDeadlock();

        Point location = new Point(1, 1);

        for (int i = 0; i < NUM_TAXIS; i++) {
            TAXIS[i] = cooperatingObjectsDeadlock.new Taxi(location);
        }

        final Set<Taxi> taxis = Arrays.stream(TAXIS).collect(Collectors.toSet());
        Dispatcher dispatcher = cooperatingObjectsDeadlock.new Dispatcher(taxis);
        taxis.stream().forEach(t -> t.setDispatcher(dispatcher));

        GPSThread GPSThread = new GPSThread(dispatcher);
        TaxiThread taxiThread = new TaxiThread(taxis);

        GPSThread.start();
        taxiThread.start();

    }

    private static class GPSThread extends Thread {
        private final Dispatcher dispatcher;

        public GPSThread(Dispatcher aDispatcher) {
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
        private final Set<Taxi> taxis;

        public TaxiThread(Set<Taxi> aTaxis) {
            taxis = aTaxis;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                for (Taxi taxi : taxis) {
                    taxi.setLocation(new Point(1, 1));
                }
            }
        }

    }

    class Taxi {
        @GuardedBy("this")
        private Point location, destination;
        private Dispatcher dispatcher;

        public Taxi(Point aDestination) {
            destination = aDestination;
        }

        public synchronized void setLocation(Point location) {
            this.location = location;
            if (location.equals(destination))
                dispatcher.notifyAvailable(this);
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setDispatcher(Dispatcher aDispatcher) {
            dispatcher = aDispatcher;
        }
    }

    class Dispatcher {
        @GuardedBy("this")
        private final Set<Taxi> taxis;
        @GuardedBy("this")
        private final Set<Taxi> availableTaxis;

        public Dispatcher(Set<Taxi> aTaxis) {
            taxis = aTaxis;
            availableTaxis = new HashSet<Taxi>();
        }

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t : taxis)
                image.drawMarker(t.getLocation());
            return image;
        }
    }
}
