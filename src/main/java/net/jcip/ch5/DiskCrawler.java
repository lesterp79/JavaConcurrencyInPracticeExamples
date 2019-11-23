/*
 * DiskCrawler.java
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

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * One type of program that is amenable to decomposition into producers and consumers is an agent that scans local drives for
 * documents and indexes them for later searching, similar to Google Desktop or the Windows Indexing service.
 *
 * The producer task {@link FileCrawler} searches a file hierarchy for files meeting an indexing criterion and puts their names on the work queue;
 *
 * The consumer {@link Indexer} shows the consumer task that takes file names from the queue and indexes them.
 *
 * {@link DiskCrawler} starts several crawlers and indexers, each in their own thread. As written, the consumer threads never exit, which prevents the
 * program from terminating; While this example uses explicitly managed threads, many producer-consumer designs can be expressed
 * using the {@link java.util.concurrent.Executor} task execution framework, which itself uses the producer-consumer pattern
 */
public class DiskCrawler {
    private static final int BOUND = 50;
    private static final int N_CPU_CORES = 4;

    public static void startIndexing(IndexableFile[] roots) {
        BlockingQueue<IndexableFile> queue = new LinkedBlockingQueue<>(BOUND);
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return true;
            }
        };
        for (IndexableFile root : roots)
            // a thread by root (I/O bound task)
            new Thread(new FileCrawler(queue, filter, root)).start();
        for (int i = 0; i < N_CPU_CORES; i++)
            // a thread by cpu core (assume 4) (CPU bound task)
            new Thread(new Indexer(queue)).start();
    }
}
