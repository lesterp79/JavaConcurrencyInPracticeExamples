/*
 * NumberRange.java
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

import java.util.concurrent.atomic.AtomicInteger;
import net.jcip.annotations.NotThreadSafe;

/**
 * The class {@link NumberRange} uses two AtomicIntegers to manage its state, but imposes an additional constraint- that the first
 * number be less than or equal to the second.
 * <p>
 * {@link NumberRange} is not thread-safe; it does not preserve the invariant that constrains lower and upper. The {@link
 * #setLower(int)} and {@link #setUpper(int)} methods attempt to respect this invariant, but do so poorly. Both {@link
 * #setLower(int)} and {@link #setUpper(int)} are check-then-act sequences, but they do not use sufficient locking to make them
 * atomic.
 * <p>
 * {@link NumberRange} could be made thread-safe by using locking to maintain its invariants, such as guarding {@link #lower} and
 * {@link #upper} with a common lock. It must also avoid publishing {@link #lower}  and {@link #upper} to prevent clients from
 * subverting its invariants.
 */
@NotThreadSafe public class NumberRange {
    // INVARIANT: lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
        // Warning -- unsafe check-then-act
        if (i > upper.get())
            throw new IllegalArgumentException("can’t set lower to " + i + " > upper");
        lower.set(i);
    }

    public void setUpper(int i) {
        // Warning -- unsafe check-then-act
        if (i < lower.get())
            throw new IllegalArgumentException("can’t set upper to " + i + " < lower");
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}