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

    public void start() {
        Timer thread = new Timer();
        thread.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if (currentBlock == null) {
                    leftTop = generateNewBlock();
                }
                leftTop = Board.getBoard().dropBlock(currentBlock, leftTop[0], leftTop[1]);
            }

        }, 500, 500);
    }

    public int[] generateNewBlock() {
        currentBlock = BlockGenerator.getBlockGenertor().generateBlock();
        return Board.getBoard().addBlock(currentBlock);
    }



    public void newBlock() {
        Timer thread = new Timer();
        thread.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Board.getBoard().addBlock(BlockGenerator.getBlockGenertor().generateBlock());
            }

        }, 500, 50000);
    }
}
