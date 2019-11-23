/*
 * IndexableFile.java
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

public class IndexableFile {
    private final File file;

    private boolean indexed;

    public IndexableFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public boolean isIndexed() {
        // actual code to find out if the file is indexed
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        // actual code to index the file
        this.indexed = indexed;
    }
}
