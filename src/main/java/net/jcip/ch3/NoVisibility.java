/*
 * NoVisibility.java
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
 * {@link NoVisibility} illustrates what can go wrong when threads share data without synchronization. Two threads, the main thread
 * and the reader thread, access the shared variables ready and number. The main thread starts the reader thread and then sets
 * number to 42 and ready to true. The reader thread spins until it sees ready is true, and then prints out number.
 * <p>
 * While it may seem obvious that NoVisibility will print 42, it is in fact possible that it will print zero, or never terminate at
 * all! Because it does not use adequate synchronization, there is no guarantee that the values of ready and number written by the
 * main thread will be visible to the reader thread.
 * <p>
 * {@link NoVisibility]} could loop forever because the value of ready might never become visible to the reader thread. Even more
 * strangely, NoVisibility could print zero because the write to ready might be made visible to the reader thread before the write
 * to number, a phenomenon known as reordering.
 */
@NotThreadSafe
public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }
}