package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.Z_SHAPE;

public class ZShape implements Shape {
    @Override
    public int[][] getShape() {
        int[][] shape = new int[3][3];
        shape[0][0] = Z_SHAPE;
        shape[0][1] = Z_SHAPE;
        shape[1][1] = Z_SHAPE;
        shape[1][2] = Z_SHAPE;
        return shape;
    }

    @Override
    public int matrixSize() {
        return 3;
    }
}
