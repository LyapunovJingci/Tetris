package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.O_SHAPE;

public class OShape implements Shape {
    @Override
    public int[][] getShape() {
        int[][] shape = new int[2][2];
        shape[0][0] = O_SHAPE;
        shape[0][1] = O_SHAPE;
        shape[1][0] = O_SHAPE;
        shape[1][1] = O_SHAPE;
        return shape;
    }

    @Override
    public int shapeCode() {
        return O_SHAPE;
    }

    @Override
    public int matrixSize() {
        return 2;
    }
}
