package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.L_SHAPE;

public class LShape implements Shape {
    @Override
    public int[][] getShape() {
        int[][] shape = new int[3][3];
        shape[0][0] = L_SHAPE;
        shape[1][0] = L_SHAPE;
        shape[2][0] = L_SHAPE;
        shape[2][1] = L_SHAPE;
        return shape;
    }

    @Override
    public int matrixSize() {
        return 3;
    }
}
