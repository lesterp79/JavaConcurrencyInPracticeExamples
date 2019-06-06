/*
 * Preloader.java
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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Preloader creates a {@link FutureTask} that describes the task of loading product information from a database and a thread in
 * which the computation will be performed. It provides a {@link #start()} method to start the thread, since it is inadvisable to
 * start a thread from a constructor or static initializer. When the program later needs the ProductInfo, it can call get, which
 * returns the loaded data if it is ready, or waits for the load to complete if not.
 * <p>
 * Tasks described by {@link Callable} can throw checked and unchecked exceptions, and any code can throw an Error. Whatever the
 * task code may throw, it is wrapped in an ExecutionException and rethrown from {@link Future#get()}
 */
public class Preloader {

    private final FutureTask<ProductInfo> future = new FutureTask<>(new Callable<ProductInfo>() {
        public ProductInfo call() throws DataLoadException {
            return loadProductInfo();
        }

        private ProductInfo loadProductInfo() throws DataLoadException {
            // loads product info from storage
            return new ProductInfo(/*loading info*/);
        }
    });
    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public ProductInfo get() throws DataLoadException, InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException)
                throw (DataLoadException) cause;
            else
                throw launderThrowable(cause);
        }
    }

    /**
     * If the Throwable is an Error, throw it; if it is a RuntimeException return it, otherwise throw IllegalStateException
     */
    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked", t);
    }

    private class ProductInfo {
        public ProductInfo() throws DataLoadException {
            // some product info
        }
    }

    private class DataLoadException extends Exception {
        public DataLoadException(Throwable aCause) {
            super(aCause);
        }
    }
}
