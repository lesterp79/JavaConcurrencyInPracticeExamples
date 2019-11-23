/*
 * BoundedHashSet.java
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * You can use a Semaphore to turn any collection into a blocking bounded collection, as illustrated by {@link BoundedHashSet}.
 * <p>
 * The semaphore is initialized to the desired maximum size of the collection. The add operation acquires a permit before adding the
 * item into the underlying collection. If the underlying add operation does not actually add anything, it releases the permit
 * immediately.
 * <p>
 * Similarly, a successful remove operation releases a permit, enabling more elements to be added. The underlying Set implementation
 * knows nothing about the bound; this is handled by {@link BoundedHashSet}.
 */
public class BoundedHashSet<T> {
    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }
    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        }
        finally {
            if (!wasAdded)
                sem.release();
        }
    }
    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
            sem.release();
        return wasRemoved;
    }
}
