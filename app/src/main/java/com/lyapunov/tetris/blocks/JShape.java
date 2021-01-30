package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.J_SHAPE;

public class JShape implements Shape {
    @Override
    public int[][] getShape() {
        int[][] shape = new int[3][3];
        shape[0][1] = J_SHAPE;
        shape[1][1] = J_SHAPE;
        shape[2][1] = J_SHAPE;
        shape[2][0] = J_SHAPE;
        return shape;
    }

    @Override
    public int matrixSize() {
        return 3;
    }
}
