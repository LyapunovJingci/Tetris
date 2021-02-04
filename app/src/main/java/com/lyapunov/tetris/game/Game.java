package com.lyapunov.tetris.game;
import com.lyapunov.tetris.blocks.Shape;
import com.lyapunov.tetris.components.Board;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Game {
    int state;
    Shape currentBlock = null;

    private volatile AtomicInteger blockStatus = new AtomicInteger(0); //record status of block rotation status(0, 1, 2, 3) maybe its better to use enum for future refactor
    private volatile AtomicIntegerArray leftTop = new AtomicIntegerArray(2); //record the coordinate of tht top left corner of the current block

    private Thread rightThread;
    private Thread leftThread;
    private Thread rotateThread;
    private boolean rightThreadStarted;
    private boolean leftThreadStarted;
    private boolean rotateThreadStarted;
    public Game(int state) {
        this.state = state;
        rightThread = new Thread(() -> {
            leftTop = Board.getBoard().moveBlockRight(currentBlock, leftTop.get(0), leftTop.get(1), blockStatus.get());
        });

        leftThread = new Thread(() -> {
            leftTop = Board.getBoard().moveBlockLeft(currentBlock, leftTop.get(0), leftTop.get(1), blockStatus.get());
        });

        rotateThread = new Thread(() -> {
            Board.getBoard().rotateBlock(currentBlock, leftTop.get(0), leftTop.get(1), blockStatus.get());
            if (blockStatus.get() == 3) {
                blockStatus.set(0);
            } else {
                blockStatus.getAndIncrement();
            }
        });
    }

    /**
     * Start a round of new game
     * 1. generate new block
     * 2. drop block
     * Need refactor later
     */
    public void start() {
        Timer thread = new Timer();
        thread.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if (currentBlock == null) {
                    leftTop = generateNewBlock();
                    blockStatus.set(0);
                }
                leftTop = Board.getBoard().dropBlock(currentBlock, leftTop.get(0), leftTop.get(1), blockStatus.get());
                if (leftTop.get(0) == -10) {
                    currentBlock = null;
                }
            }

        }, 1000, 500);
    }

    /**
     * Generate a new block
     * @return left top coordinate of the block
     */
    public AtomicIntegerArray generateNewBlock() {
        currentBlock = BlockGenerator.getBlockGenerator().generateBlock();
        return Board.getBoard().addBlock(currentBlock);
    }

    /**
     * Rotate the current block by 90 degree counter-clockwise
     */
    public void rotateBlock() {
        if (leftTop.get(0) == 0 || leftTop.get(0) == -1 || leftTop.get(0) > 8) {
            return;
        }
        if (!rotateThreadStarted) {
            rotateThread.start();
            rotateThreadStarted = true;
        }
        rotateThread.run();
    }

    /**
     * Move the current block left by one unit
     */
    public void moveBlockLeft() {
        if (leftTop.get(0) < -2) {
            return;
        }
        if (!leftThreadStarted) {
            leftThread.start();
            leftThreadStarted = true;
        }
        leftThread.run();
    }

    /**
     * Move the current block right by one unit
     */
    public void moveBlockRight() {
        if (leftTop.get(0) < -2) {
            return;
        }
        if (!rightThreadStarted) {
            rightThread.start();
            rightThreadStarted = true;
        }
        rightThread.run();


    }
}
