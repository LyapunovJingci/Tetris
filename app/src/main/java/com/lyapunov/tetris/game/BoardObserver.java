package com.lyapunov.tetris.game;

public interface BoardObserver {
    void updateCanvas();
    void generateNew(int[][] shapeNum);
    void updateGameInfo(int totalClearedLines, int score, int level);
    void gameEnd();
    void gameRestart();
}
