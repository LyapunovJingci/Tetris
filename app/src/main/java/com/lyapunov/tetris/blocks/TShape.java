package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.T_SHAPE;

public class TShape implements Shape {
    @Override
    public int[][] getShape() {
        int[][] shape = new int[3][3];
        shape[1][0] = T_SHAPE;
        shape[1][1] = T_SHAPE;
        shape[1][2] = T_SHAPE;
        shape[0][1] = T_SHAPE;
        return shape;
    }

    @Override
    public int matrixSize() {
        return 3;
    }
}
