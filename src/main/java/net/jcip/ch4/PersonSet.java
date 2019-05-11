/*
 * PersonSet.java
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

import java.util.HashSet;
import java.util.Set;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * {@link PersonSet} illustrates how confinement and locking can work together to make a class thread-safe even when its component
 * state variables are not. The state of {@link PersonSet} is managed by a {@link HashSet}, which is not thread-safe. But because
 * {@link #mySet} is private and not allowed to escape, the {@link HashSet} is confined to the {@link PersonSet}. The only code
 * paths that can access {@link #mySet} are {@link #addPerson(Person)} and containsPerson, and each of these acquires the lock on
 * the PersonSet. All its state is guarded by its intrinsic lock, making {@link PersonSet} thread-safe.
 * <p>
 * Note: This example makes no assumptions about the thread-safety of {@link Person}, but if it is mutable, additional
 * synchronization will be needed when accessing a {@link Person} retrieved from a {@link PersonSet}. The most reliable way to do
 * this would be to make {@link Person} thread-safe; less reliable would be to guard the {@link Person} objects with a lock and
 * ensure that all clients follow the protocol of acquiring the appropriate lock before accessing the {@link Person}.
 */
@ThreadSafe public class PersonSet {
    @GuardedBy("this")
    private final Set<Person> mySet = new HashSet<Person>();

    public synchronized void addPerson(Person p) {
        mySet.add(p);
    }

    public synchronized boolean containsPerson(Person p) {
        return mySet.contains(p);
    }
}