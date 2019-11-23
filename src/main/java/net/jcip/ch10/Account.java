/*
 * Account.java
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

public class Account {
    DollarAmount balance;

    public Account(DollarAmount aBalance) {
        balance = aBalance;
    }

    public DollarAmount getBalance() {
        return balance;
    }

    public void debit(DollarAmount aAmount) {
        balance.inc(aAmount);
    }

    public void credit(DollarAmount aAmount) {
        balance.dec(aAmount);
    }
}