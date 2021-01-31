package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.S_SHAPE;

public class SShape implements Shape {
    @Override
    public int[][] getShape() {
        int[][] shape = new int[3][3];
        shape[1][0] = S_SHAPE;
        shape[0][1] = S_SHAPE;
        shape[1][1] = S_SHAPE;
        shape[0][2] = S_SHAPE;
        return shape;
    }

    @Override
    public int shapeCode() {
        return S_SHAPE;
    }

    @Override
    public int matrixSize() {
        return 3;
    }
}
