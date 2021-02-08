package com.lyapunov.tetris.game;

import com.lyapunov.tetris.blocks.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Game {
    Shape currentBlock = null;
    private List<GameObserver> observers = new ArrayList<>();
    private int initialLevel = 1;
    private volatile int totalClearedLines = 0;
    private volatile int level = 1;
    private volatile int score = 0;
    private Timer dropTimer;
    private volatile AtomicInteger blockStatus = new AtomicInteger(0); //record status of block rotation status(0, 1, 2, 3) maybe its better to use enum for future refactor
    private volatile AtomicIntegerArray leftTop = new AtomicIntegerArray(2); //record the coordinate of tht top left corner of the current block
    private Thread rightThread;
    private Thread leftThread;
    private Thread rotateThread;
    private Thread dropThread;
    private Thread fastDropThread;
    private boolean levelUp = false;
    private boolean rightThreadStarted;
    private boolean leftThreadStarted;
    private boolean rotateThreadStarted;
    private boolean dropThreadStarted;
    private boolean fastDropThreadStarted;

    public static Game game = new Game();
    private Game() {
        rightThread = new Thread(() -> {
            leftTop = Board.getBoard().moveBlockRight(currentBlock, leftTop, blockStatus);
            notifyObserversUpdate();
        });
        leftThread = new Thread(() -> {
            leftTop = Board.getBoard().moveBlockLeft(currentBlock, leftTop, blockStatus);
            notifyObserversUpdate();
        });
        dropThread = new Thread(() -> {
            leftTop = Board.getBoard().dropBlock(currentBlock, leftTop, blockStatus);
            notifyObserversUpdate();
        });
        fastDropThread = new Thread(() -> {
            leftTop = Board.getBoard().fastDropBlock(currentBlock, leftTop, blockStatus);
            notifyObserversUpdate();
        });
        rotateThread = new Thread(() -> {
            Board.getBoard().rotateBlock(currentBlock, leftTop, blockStatus);
            if (blockStatus.get() == 3) {
                blockStatus.set(0);
            } else {
                blockStatus.getAndIncrement();
            }
            notifyObserversUpdate();
        });
    }
    public static Game getGame() {
        return game;
    }


    public void setInitialLevel(int initialLevel) {
        this.initialLevel = initialLevel;
        level = initialLevel;
    }

    /**
     * Start a round of new game
     * 1. generate new block
     * 2. drop block
     * Need refactor later
     */
    public void start() {
        notifyObserversClear(0, 0, initialLevel);
        scheduleTimer();
    }

    private void scheduleTimer() {
        dropTimer = new Timer();
        int period = (20 - level) * 50;
        dropTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if (levelUp) {
                    dropTimer.cancel();
                    levelUp = false;
                    scheduleTimer();
                }
                if (currentBlock == null) {
                    leftTop = generateNewBlock();
                    blockStatus.set(0);
                }
                if (leftTop.get(0) == -100) {
                    notifyObserversEnd(score);
                    dropTimer.cancel();
                }
                leftTop = dropBlock();
                if (leftTop.get(0) == -10) {
                    currentBlock = null;
                }
                notifyObserversUpdate();
            }

        }, period / 3, period);
    }

    private void updateGameInfo(int clearedLines) {
        if (clearedLines == 0) {
            return;
        }
        totalClearedLines += clearedLines;
        score += ScoreCounter.getScoreCounter().lineToScore(clearedLines);
        int newLevel = LevelCounter.getLevelCounter().lineToLevel(totalClearedLines);
        if (initialLevel + newLevel > level) {
            levelUp = true;
        }
        level = initialLevel + newLevel;
        notifyObserversClear(totalClearedLines, score, level);
    }

    public void end() {
        levelUp = false;
        dropTimer.cancel();
        totalClearedLines = 0;
        level = initialLevel;
        score = 0;
        currentBlock = null;
        leftThread.interrupt();
        rightThread.interrupt();
        rotateThread.interrupt();
        dropThread.interrupt();
        fastDropThread.interrupt();
        Board.getBoard().clear();
    }

    public int[][] getCurrentBoardMatrix() {
        return Board.getBoard().getBoardMatrix();
    }

    /**
     * Generate a new block
     * @return left top coordinate of the block
     */
    private synchronized AtomicIntegerArray generateNewBlock() {
        updateGameInfo(Board.getBoard().getClearedLines());
        currentBlock = BlockGenerator.getBlockGenerator().generateBlock();
        notifyObserversNew(BlockGenerator.getBlockGenerator().getNextBlock().getShape());
        return Board.getBoard().addBlock(currentBlock);
    }

    /**
     * Drop current block by one unit
     * @return left top coordinate of the block after dropping
     */
    private synchronized AtomicIntegerArray dropBlock() {
        return Board.getBoard().dropBlock(currentBlock, leftTop, blockStatus);
    }

    /**
     * Rotate the current block by 90 degree counter-clockwise
     */
    public synchronized void rotateBlock() {
        if (leftTop.get(0) == 0 || leftTop.get(0) == -1 || leftTop.get(0) > 8) {
            return;
        }
        if (!rotateThreadStarted) {
            rotateThread.start();
            rotateThreadStarted = true;
        } else {
            rotateThread.run();
        }
    }

    /**
     * Move the current block left by one unit
     */
    public synchronized void moveBlockLeft() {
        if (leftTop.get(0) < -2) {
            return;
        }
        if (!leftThreadStarted) {
            leftThread.start();
            leftThreadStarted = true;
        } else {
            leftThread.run();
        }
    }

    /**
     * Move the current block right by one unit
     */
    public synchronized void moveBlockRight() {
        if (leftTop.get(0) < -2) {
            return;
        }
        if (!rightThreadStarted) {
            rightThread.start();
            rightThreadStarted = true;
        } else {
            rightThread.run();
        }

    }


    public synchronized void moveBlockDown() {
        if (leftTop.get(0) < -2 || leftTop.get(1) > 15) {
            return;
        }
        if (!dropThreadStarted) {
            dropThread.start();
            dropThreadStarted = true;
        } else {
            dropThread.run();
        }
    }


    public synchronized void moveBlockDownFast() {
        if (leftTop.get(0) < -2 || leftTop.get(1) > 15) {
            return;
        }
        if (!fastDropThreadStarted) {
            fastDropThread.start();
            fastDropThreadStarted = true;
        } else {
            fastDropThread.run();
        }

    }
    /**
     * Attach observers to the board
     * @param observer MainActivity (updating UI)
     */
    public void attach(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Detach observers
     * @param observer currently no usage
     */
    public void detach(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify observers that the status of the board has changed
     * MainActivity (updating UI)
     */
    protected void notifyObserversUpdate() {
        for(GameObserver observer : observers){
            observer.updateCanvas();
        }
    }

    /**
     * Notify observers that a new block is generating, its time to update the next_block board
     * @param shapeMatrix the shape of next block
     */
    protected void notifyObserversNew(int[][] shapeMatrix) {
        for(GameObserver observer : observers){
            if (shapeMatrix.length == 4) {
                observer.generateNewBlock(shapeMatrix);
                return;
            }
            int[][] matrix = new int[4][4];
            for (int i = 0; i < shapeMatrix.length; i++) {
                System.arraycopy(shapeMatrix[i], 0, matrix[i + 1], 1, shapeMatrix.length);
            }
            observer.generateNewBlock(matrix);
        }
    }

    protected void notifyObserversClear(int totalCleardLines, int score, int level) {
        for (GameObserver observer: observers) {
            observer.updateGameInfo(totalCleardLines, score, level);
        }
    }

    protected void notifyObserversEnd(int finalScore) {
        for (GameObserver observer: observers) {
            observer.gameEnd(finalScore);
        }
    }

}
