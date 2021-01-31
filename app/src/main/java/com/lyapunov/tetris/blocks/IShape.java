package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.I_SHAPE;

public class IShape implements Shape {
    @Override
    public int[][] getShape() {
        int[][] shape = new int[4][4];
        shape[0][0] = I_SHAPE;
        shape[0][1] = I_SHAPE;
        shape[0][2] = I_SHAPE;
        shape[0][3] = I_SHAPE;
        return shape;
    }

    @Override
    public int shapeCode() {
        return I_SHAPE;
    }

    @Override
    public int matrixSize() {
        return 4;
    }
}
