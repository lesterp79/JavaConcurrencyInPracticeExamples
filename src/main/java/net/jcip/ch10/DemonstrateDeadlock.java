/*
 * DemonstrateDeadlock.java
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

import java.util.Random;

public class DemonstrateDeadlock {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;

    public static void main(String[] args) {
        final Random rnd = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(new DollarAmount(100000));
        }



        class TransferThread extends Thread {

            private final DynamicLockOrderingDeadLock dynamicLockOrderingDeadLock;

            public TransferThread(DynamicLockOrderingDeadLock aDynamicLockOrderingDeadLock) {
                dynamicLockOrderingDeadLock = aDynamicLockOrderingDeadLock;
            }

            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
                    DollarAmount amount = new DollarAmount(rnd.nextInt(1000));
                    try {
                        dynamicLockOrderingDeadLock.transferMoney(accounts[fromAcct], accounts[toAcct], amount);
                    } catch (DynamicLockOrderingDeadLock.InsufficientFundsException aE) {
                    }
                }
            }
        }

        DynamicLockOrderingDeadLock dynamicLockOrderingDeadLock = new DynamicLockOrderingDeadLock();

        for (int i = 0; i < NUM_THREADS; i++)
            new TransferThread(dynamicLockOrderingDeadLock).start();
    }
}
