/*
 * FileCrawler.java
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

/**
 * One type of program that is amenable to decomposition into producers and consumers is an agent that scans local drives for
 * documents and indexes them for later searching, similar to Google Desktop or the Windows Indexing service. see {@link DiskCrawler}.
 *
 * The producer task {@link FileCrawler} searches a file hierarchy for files meeting an indexing criterion and puts their names on
 * the work queue.
 */
public class FileCrawler implements Runnable {
    private final BlockingQueue<IndexableFile> fileQueue;
    private final FileFilter fileFilter;
    private final IndexableFile root;

    public FileCrawler(BlockingQueue<IndexableFile> fileQueue, FileFilter fileFilter, IndexableFile root) {
        this.fileQueue = fileQueue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    public void run() {
        try {
            crawl(root);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void crawl(IndexableFile root) throws InterruptedException {
        File[] entries = root.getFile().listFiles(fileFilter);
        if (entries != null) {
            for (File entry : entries) {
                IndexableFile indexableFile = new IndexableFile(entry);
                if (entry.isDirectory()) {
                    crawl(indexableFile);
                } else if (!alreadyIndexed(indexableFile)) {
                    fileQueue.put(indexableFile);
                }

            }

        }
    }

    private boolean alreadyIndexed(IndexableFile entry) {
        return entry.isIndexed();
    }
}

