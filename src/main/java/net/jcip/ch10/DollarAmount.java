/*
 * DollarAmount.java
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

public class DollarAmount implements Comparable<DollarAmount> {
    public double getAmount() {
        return amount;
    }

    private double amount;

    public DollarAmount(double aAmount) {
        amount = aAmount;
    }

    @Override
    public int compareTo(DollarAmount o) {
        if (o == null) {
            throw new IllegalArgumentException("Null arg");
        }

        double diff = this.amount - o.getAmount();

        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public void inc(DollarAmount aAmount) {
        this.amount += aAmount.getAmount();
    }

    public void dec(DollarAmount aAmount) {
        this.amount -= aAmount.getAmount();
    }
}
