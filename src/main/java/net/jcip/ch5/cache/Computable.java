/*
 * Computable.java
 *
 * Copyright (c) 2000-2019 MotionPoint Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * MotionPoint Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with MotionPoint.
 */
package net.jcip.ch5.cache;

/**
 * The Computable<A,V> interface describes a function with input of type A and result of type V.
 * @param <A> the input type
 * @param <V> the result type
 */
public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}