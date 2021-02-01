package com.lyapunov.tetris.components;

public interface BoardObserver {
    void update();
    void generateNew(int[][] shapeNum);
}
