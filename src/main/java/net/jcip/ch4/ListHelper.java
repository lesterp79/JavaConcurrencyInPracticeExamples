/*
 * ListHelper.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.jcip.annotations.NotThreadSafe;

/**
 * This class shows a failed attempt to create a helper class with an atomic put-if-absent operation for operating on a thread-safe
 * List.
 * <p>
 * Why wouldn’t this work? After all, putIfAbsent is synchronized, right? The problem is that it synchronizes on the wrong lock.
 * Whatever lock the List uses to guard its state, it sure isn’t the lock on the ListHelper. ListHelper provides only the illusion
 *  * of synchronization; the various list operations, while all synchronized, use different locks, which means that putIfAbsent is not
 *  * atomic relative to other operations on the List. So there is no guarantee that another thread won’t modify the list while
 *  * putIfAbsent is executing.
 */
@NotThreadSafe public class ListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !list.contains(x);
        if (absent)
            list.add(x);
        return absent;
    }
}