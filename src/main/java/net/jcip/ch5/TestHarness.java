/*
 * TestHarness.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch5;

import java.util.concurrent.CountDownLatch;

/**
 * TestHarness illustrates two common uses for latches. TestHarness creates a number of threads that run a given task concurrently.
 * It uses two latches, a “starting gate” and an “ending gate”. The starting gate is initialized with a count of one; the ending
 * gate is initialized with a count equal to the number of worker threads.
 * <p>
 * The first thing each worker thread does is wait on the starting gate; this ensures that none of them starts working until they
 * all are ready to start. The last thing each does is count down on the ending gate; this allows the master thread to wait
 * efficiently until the last of the worker threads has finished, so it can calculate the elapsed time.
 */
public class TestHarness {

    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {

        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        // all threads block on the #startGate until countdown (initially 1) reaches 0 (signal from master
                        // thread)
                        // (waiting until gate is open)
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            // all threads count down on #endGate, even when interrupted (or any other exception), so the master
                            // thread waiting on #endGate can proceed when countdown (initially nThreads) reaches 0.
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }
        long start = System.nanoTime();
        // countdown on #startGate (initially 1) unblocks all threads waiting on it at the same time (open the gate)
        startGate.countDown();
        // waits on the #endGate until the working threads close it by counting down on finalizing execution.
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }
}