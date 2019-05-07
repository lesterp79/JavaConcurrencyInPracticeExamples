/*
 * UnsafeListener.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch3;

import java.util.EventListener;
import javafx.event.Event;

/**
 * When the inner {@link EventListener} instance is published by calling the "alien" method
 * {@link EventSource#registerListener(EventListener)}, the implicit "this" reference escapes from an unfinished constructor,
 * which results on publishing an objct that is not properly constructed.
 */
public class UnsafeListener {
    public UnsafeListener(EventSource source) {
        EventListener listener = new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }

            private void doSomething(Event aE) {
            }
        };
        source.registerListener(listener);
    }
}
