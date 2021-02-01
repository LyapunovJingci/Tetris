package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.L_SHAPE;

public class LShape implements Shape {
    private static final int[][] shape = new int[3][3];

    /**
     * Constructor of a L_Shape block
     * 3 0 0
     * 3 0 0
     * 3 3 0
     */
    public LShape() {
        shape[0][0] = L_SHAPE;
        shape[1][0] = L_SHAPE;
        shape[2][0] = L_SHAPE;
        shape[2][1] = L_SHAPE;
    }

    /**
     * Getter
     * @return a 3 * 3 dimensional array containing a L_Shape block
     */
    @Override
    public int[][] getShape() {
        return shape;
    }

    /**
     * Getter
     * @return global final code of L_Shape block, here is 3, see detail in ShapeName.java class
     */
    @Override
    public int getShapeCode() {
        return L_SHAPE;
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
