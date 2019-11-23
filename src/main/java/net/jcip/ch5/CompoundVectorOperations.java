/*
 * CompoundVectorOperations.java
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

import java.util.Vector;
import net.jcip.annotations.ThreadSafe;

/**
 * Compound actions on Vector using client-side locking. Using client -side locking to lock the state of the collection while
 * your intent is not modifying it has an unnecessary impact on concurrency.
 */
@ThreadSafe
public class CompoundVectorOperations {

    public static Object getLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }

    public static void iterate(Vector list) {
        synchronized (list) {
            for (int i = 0; i < list.size(); i++) {
                //doSomething(list.get(i));
            }
        }
    }
}
