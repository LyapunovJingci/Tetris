package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.O_SHAPE;

public class OShape implements Shape {
    private static final int[][] shape = new int[4][4];

    /**
     * Constructor of an O_Shape block
     * 0 0 0 0
     * 0 4 4 0
     * 0 4 4 0
     * 0 0 0 0
     */
    public OShape() {
        shape[1][1] = O_SHAPE;
        shape[1][2] = O_SHAPE;
        shape[2][1] = O_SHAPE;
        shape[2][2] = O_SHAPE;
    }
    /**
     * Getter
     * @return a 2 * 2 dimensional array containing an O_Shape block
     */
    @Override
    public int[][] getShape() {
        return shape;
    }

    /**
     * Getter
     * @return global final code of O_Shape block, here is 4, see detail in ShapeName.java class
     */
    @Override
    public int getShapeCode() {
        return O_SHAPE;
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
