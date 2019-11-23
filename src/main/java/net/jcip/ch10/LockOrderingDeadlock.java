/*
 * LeftRightDeadlock.java
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link LockOrderingDeadlock} is at risk for deadlock. The {@link #leftRight()} and {@link #rightLeft()} methods each acquire the
 * left and right locks. If one thread calls leftRight and another calls rightLeft, and their actions are interleaved, they will
 * deadlock.
 * <p>
 * The deadlock in {@link LockOrderingDeadlock} came about because the two threads attempted to acquire the same locks in a different
 * order. If they asked for the locks in the same order, there would be no cyclic locking dependency and therefore no deadlock. If
 * you can guarantee that every thread that needs locks L and M at the same time always acquires L and M in the same order, there
 * will be no deadlock.
 */
public class LockOrderingDeadlock {

    public static void main(String[] args) {
        LockOrderingDeadlock lockOrderingDeadlock = new LockOrderingDeadlock();

        Thread leftToRight = new Thread(new RunnableLeftToRight(lockOrderingDeadlock), "LeftToRightThread");
        Thread rightToLeft = new Thread(new RunnableRightToLeft(lockOrderingDeadlock), "RightToLeftThread");

        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        executorService2.submit(leftToRight);

        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        executorService1.submit(rightToLeft);

    }

    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() throws InterruptedException {
        synchronized (left) {
            Thread.sleep(5000);
            synchronized (right) {
                doSomething();
            }
        }
    }

    private void doSomething() {
        System.out.println("doSomething");
    }

    public void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                doSomethingElse();
            }
        }
    }

    private void doSomethingElse() {
        System.out.println("doSomethingElse");
    }
}

class RunnableRightToLeft implements Runnable {

    private final LockOrderingDeadlock lockOrderingDeadlock;

    public RunnableRightToLeft(LockOrderingDeadlock aLockOrderingDeadlock) {
        lockOrderingDeadlock = aLockOrderingDeadlock;
    }

    @Override
    public void run() {
        lockOrderingDeadlock.rightLeft();
    }
}

class RunnableLeftToRight implements Runnable {

    private final LockOrderingDeadlock lockOrderingDeadlock;

    public RunnableLeftToRight(LockOrderingDeadlock aLockOrderingDeadlock) {
        lockOrderingDeadlock = aLockOrderingDeadlock;
    }

    @Override
    public void run() {
        try {
            lockOrderingDeadlock.leftRight();
        } catch (InterruptedException aE) {
            aE.printStackTrace();
        }
    }
}
