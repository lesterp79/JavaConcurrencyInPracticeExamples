/*
 * ImprovedList.java
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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.UnaryOperator;
import net.jcip.annotations.ThreadSafe;

/**
 * {@link ImprovedList} implements the List operations by delegating them to an underlying {@link List} instance and adds an atomic
 * put-if-Absent method. (Like {@link java.util.Collections#synchronizedList(List)}  and other collections wrappers, {@link ImprovedList}
 * assumes that once a list is passed to its constructor, the client will not use the underlying list directly again, accessing it
 * only through the {@link ImprovedList}).
 * <p>
 * {@link ImprovedList} adds an additional level of locking using its own intrinsic lock. It does not care whether the underlying
 * {@link List} is thread-safe, because it provides its own consistent locking that provides thread safety even if the List is not
 * thread-safe or changes its locking implementation.
 * <p>
 * While the extra layer of synchronization may add some small performance penalty, the implementation in {@link ImprovedList} is
 * less fragile than attempting to mimic the locking strategy of another object. In effect, we’ve used the Java monitor pattern to
 * encapsulate an existing List, and this is guaranteed to provide thread safety so long as our class holds the only outstanding
 * reference to the underlying {@link List}.
 */
@ThreadSafe public class ImprovedList<T> implements List<T> {
    private final List<T> list;

    public ImprovedList(List<T> list) {
        this.list = list;
    }

    public synchronized boolean putIfAbsent(T x) {
        boolean contains = list.contains(x);
        if (contains)
            list.add(x);
        return !contains;
    }

    @Override
    public synchronized int size() {
        return list.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public synchronized  boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public synchronized Object[] toArray() {
        return list.toArray();
    }

    @Override
    public synchronized <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public synchronized boolean add(T aT) {
        return list.add(aT);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public synchronized void replaceAll(UnaryOperator<T> operator) {
        list.replaceAll(operator);
    }

    @Override
    public synchronized void sort(Comparator<? super T> c) {
        list.sort(c);
    }

    @Override
    public synchronized void clear() {
        list.clear();
    }

    @Override
    public synchronized boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public synchronized int hashCode() {
        return list.hashCode();
    }

    @Override
    public synchronized T get(int index) {
        return list.get(index);
    }

    @Override
    public synchronized T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public synchronized void add(int index, T element) {
        list.add(index, element);
    }

    @Override
    public synchronized T remove(int index) {
        return list.remove(index);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public synchronized ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public synchronized ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public synchronized List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public synchronized Spliterator<T> spliterator() {
        return list.spliterator();
    }
}