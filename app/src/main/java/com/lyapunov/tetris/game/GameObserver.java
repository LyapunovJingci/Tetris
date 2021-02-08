package com.lyapunov.tetris.game;

public interface GameObserver {
    void updateCanvas();
    void generateNewBlock(int[][] shapeNum);
    void updateGameInfo(int totalClearedLines, int score, int level);
    void gameEnd(int finalScore);
}
