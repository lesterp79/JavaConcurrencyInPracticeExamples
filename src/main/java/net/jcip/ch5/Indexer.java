/*
 * Indexer.java
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

import java.util.concurrent.BlockingQueue;

/**
 * One type of program that is amenable to decomposition into producers and consumers is an agent that scans local drives for
 * documents and indexes them for later searching, similar to Google Desktop or the Windows Indexing service. see {@link DiskCrawler}.
 *
 * The consumer {@link Indexer} shows the consumer task that takes file names from the queue and indexes them.
 */
public class Indexer implements Runnable {
    private final BlockingQueue<IndexableFile> queue;

    public Indexer(BlockingQueue<IndexableFile> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true)
                indexFile(queue.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(IndexableFile take) {
        // actual code to index the file
        take.setIndexed(true);
    }
}