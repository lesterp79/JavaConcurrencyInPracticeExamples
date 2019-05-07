/*
 * ConnectionHolderThreadLocal.java
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Thread-local variables are often used to prevent sharing in designs based on mutable Singletons or global variables. For example,
 * a single-threaded application might maintain a global database connection that is initialized at startup to avoid having to pass
 * a Connection to every method. Since JDBC connections may not be thread-safe, a multithreaded application that uses a global
 * connection without additional coordination is not thread-safe either.
 * By using a ThreadLocal to store the JDBC connection, as in
 * {@link com.sun.media.jfxmedia.locator.ConnectionHolder} , each thread will have its own connection.
 */
public class ConnectionHolder {

    private static final String DB_URL = "jdbc:sql:driver/localdb";

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
        public Connection initialValue() {
            try {
                return DriverManager.getConnection(DB_URL);
            } catch (SQLException aE) {
                throw new IllegalStateException();
            }
        }
    };

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}
