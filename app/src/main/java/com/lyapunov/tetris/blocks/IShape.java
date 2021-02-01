package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.I_SHAPE;

public class IShape implements Shape {
    private static final int[][] shape = new int[4][4];

    /**
     * Constructor of an I_Shape block
     * 0 0 0 0
     * 0 0 0 0
     * 1 1 1 1
     * 0 0 0 0
     */
    public IShape() {
        shape[2][0] = I_SHAPE;
        shape[2][1] = I_SHAPE;
        shape[2][2] = I_SHAPE;
        shape[2][3] = I_SHAPE;
    }

    /**
     * Getter
     * @return a 4 * 4 dimensional array containing an I_Shape block
     */
    @Override
    public int[][] getShape() {
        return shape;
    }

    /**
     * Getter
     * @return global final code of I_Shape block, here is 1, see detail in ShapeName.java class
     */
    @Override
    public int getShapeCode() {
        return I_SHAPE;
    }

    /**
     * Getter
     * @return size of the block, here the matrix is 4 * 4, hence return 4
     */
    @Override
    public int getMatrixSize() {
        return shape.length;
    }
}
