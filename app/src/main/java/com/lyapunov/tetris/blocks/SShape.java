package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.S_SHAPE;

public class SShape implements Shape {
    private static final int[][] shape = new int[3][3];

    /**
     * Constructor of a S_Shape block
     * 0 5 5
     * 5 5 0
     * 0 0 0
     */
    public SShape() {
        shape[1][0] = S_SHAPE;
        shape[0][1] = S_SHAPE;
        shape[1][1] = S_SHAPE;
        shape[0][2] = S_SHAPE;
    }

    /**
     * Getter
     * @return a 3 * 3 dimensional array containing a S_Shape block
     */
    @Override
    public int[][] getShape() {
        return shape;
    }

    /**
     * Getter
     * @return global final code of S_Shape block, here is 3, see detail in ShapeName.java class
     */
    @Override
    public int getShapeCode() {
        return S_SHAPE;
    }

    /**
     * Getter
     * @return size of the block, here the matrix is 3 * 3, hence return 3
     */
    @Override
    public int getMatrixSize() {
        return shape.length;
    }
}
