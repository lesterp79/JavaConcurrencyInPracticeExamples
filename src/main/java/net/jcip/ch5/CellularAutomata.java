/*
 * CellularAutomata.java
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

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * {@link CellularAutomata} demonstrates using a barrier to compute a cellular automata simulation, such as Conway’s Life game
 * (Gardner, 1970).
 * <p>
 * When parallelizing a simulation, it is generally impractical to assign a separate thread to each element (in the case of Life, a
 * cell); this would require too many threads, and the overhead of coordinating them would dwarf the computation. Instead, it makes
 * sense to partition the problem into a number of sub-parts, let each thread solve a sub-part, and then merge the results.
 * <p>
 * {@link CellularAutomata} partitions the board into N-cpu parts, where N-cpu is the number of CPUs available, and assigns each
 * part to a thread. At each step, the worker threads calculate new values for all the cells in their part of the board.
 * <p>
 * When all worker threads have reached the barrier, the barrier action commits the new values to the data model. After  the barrier
 * action runs, the worker threads are released to compute the next step of the calculation, which includes consulting an isDone
 * method to determine whether further iterations are required.`
 */
public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, new Runnable() {
            public void run() {
                mainBoard.commitNewValues();
            }
        });
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++)
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
    }

    private class Worker implements Runnable {
        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        public void run() {
            while (!board.hasConverged()) {
                for (int x = 0; x < board.getMaxX(); x++)
                    for (int y = 0; y < board.getMaxY(); y++)
                        board.setNewValue(x, y, computeValue(x, y));
                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    return;
                } catch (BrokenBarrierException ex) {
                    return;
                }
            }
        }
    }

    private Object computeValue(int aX, int aY) {
        return null;
    }

    public void start() {
        for (int i = 0; i < workers.length; i++)
            new Thread(workers[i]).start();
        mainBoard.waitForConvergence();
    }

    private class Board {

        public void commitNewValues() {
            // update the board with this iteration
        }

        public boolean hasConverged() {
            return false;
        }

        public int getMaxX() {
            return 300;
        }

        public int getMaxY() {
            return 200;
        }

        public Board getSubBoard(int aCount, int aI) {
            return null;
        }

        public void setNewValue(int aX, int aY, Object aComputeValue) {
        }

        public void waitForConvergence() {
        }
    }
}