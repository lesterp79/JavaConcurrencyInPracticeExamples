/*
 * DynamicLockOrderingDeadLock.java
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

/**
 * How can {@link DynamicLockOrderingDeadLock} deadlock? It may appear as if all the threads acquire their locks in the same order,
 * but in fact the lock order depends on the order of arguments passed to {@link #transferMoney(Account, Account, DollarAmount)},
 * and these in turn might depend on external inputs. Deadlock can occur if two threads call transferMoney at the same time, one
 * transferring from X to Y, and the other doing the opposite:
 * <p>
 * A: transferMoney(myAccount, yourAccount, 10); B: transferMoney(yourAccount, myAccount, 20);
 * <p>
 * With unlucky timing, A will acquire the lock on myAccount and wait for the lock on yourAccount, while B is holding the lock on
 * yourAccount and waiting for the lock on myAccount.
 */
public class DynamicLockOrderingDeadLock {

    public void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount) throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }
    class InsufficientFundsException extends Exception {
    }
}
