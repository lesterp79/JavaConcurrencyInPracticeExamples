/*
 * LIstHelperClient.java
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
import net.jcip.annotations.ThreadSafe;

/**
 * The documentation for the synchronized wrapper classes states, albeit obliquely, states that they support client-side locking, by
 * using the intrinsic lock for wrapper collection (not the wrapped collection). This code code that uses client-side locking
 * correctly.
 */
@ThreadSafe public class ListHelperClient<E> {
    public final List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public boolean putIfAbsent(E x) {
        synchronized (list) {
            boolean absent = !list.contains(x);
            if (absent)
                list.add(x);
            return absent;
        }
    }
}