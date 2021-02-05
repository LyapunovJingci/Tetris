package com.lyapunov.tetris.game;

public interface BoardObserver {
    void update();
    void generateNew(int[][] shapeNum);
    void clearRows(int numOfRows, int currentNumOfRows);
}
