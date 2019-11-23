/*
 * HiddenIterator.java
 *
 * Copyright (c) 2000-2018 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch5;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import net.jcip.annotations.GuardedBy;

/**
 * There is no explicit iteration in HiddenIterator, but the code in bold entails iteration just the same. The string
 * concatenation gets turned by the compiler into a call to StringBuilder.append(Object), which in turn invokes the collection’s
 * toString method—and the implementation of toString in the standard collections iterates the collection and calls toString on each
 * element to produce a nicely formatted representation of the collection’s contents.
 *
 * The {@link #addTenThings()} method could throw {@link java.util.ConcurrentModificationException}, because the collection is being
 * iterated by toString in the process of preparing the debugging message. Of course, the real problem is that HiddenIterator is not
 * thread-safe; the HiddenIterator lock should be acquired before using set in the println call, but debugging and logging code
 * commonly neglect to do this.
 *
 * If HiddenIterator wrapped the HashSet with a {@link java.util.Collections#synchronizedSet(Set)}, encapsulating the
 * synchronization, this sort of error would not occur.
 */
public class HiddenIterator {
    @GuardedBy("this") private final Set<Integer> set = new HashSet<Integer>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++)
            add(r.nextInt());
        System.out.println("DEBUG: added ten elements to " + set);
    }
}