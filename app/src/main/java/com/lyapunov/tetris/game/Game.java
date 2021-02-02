package com.lyapunov.tetris.game;
import com.lyapunov.tetris.blocks.Shape;
import com.lyapunov.tetris.components.Board;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
    int state;
    Shape currentBlock = null;
    int[] leftTop = new int[2];
    public Game(int state) {
        this.state = state;
    }
    private volatile int blockStatus = 0;

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
                    blockStatus = 0;
                }
                leftTop = Board.getBoard().dropBlock(currentBlock, leftTop[0], leftTop[1]);
                if (leftTop[0] == -1) {
                    currentBlock = null;
                }
            }

        }, 1000, 500);
    }

    /**
     * Generate a new block
     * @return left top coordinate of the block
     */
    public int[] generateNewBlock() {
        currentBlock = BlockGenerator.getBlockGenerator().generateBlock();
        return Board.getBoard().addBlock(currentBlock);
    }

    public void rotateBlock() {
        if (leftTop[0] == 0 || leftTop[0] == -1) {
            return;
        }
        Thread t = new Thread(() -> {
            Board.getBoard().rotateBlock(currentBlock, leftTop[0], leftTop[1], blockStatus);
            if (blockStatus == 3) {
                blockStatus = 0;
            } else {
                blockStatus = blockStatus + 1;
            }
        });
        t.start();
    }

}
