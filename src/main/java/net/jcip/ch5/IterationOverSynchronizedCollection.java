/*
 * IterationOverSynchronizedCollection.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.jcip.annotations.NotThreadSafe;

/**
 * Iterating a collection with the for-each loop syntax. Internally, javac generates code that uses an Iterator, repeatedly calling
 * hasNext and next to iterate the List. Just as with iterating the Vector, the way to prevent ConcurrentModificationException is to
 * hold the collection lock for the duration of the iteration
 */
@NotThreadSafe
public class IterationOverSynchronizedCollection {

    public void iterate() {
        List<Widget> widgetList = Collections.synchronizedList(new ArrayList<Widget>());
        // May throw ConcurrentModificationException
        for (Widget w : widgetList) {
            // doSomething(w);
        }
    }

    public static class Widget {
    }
}
