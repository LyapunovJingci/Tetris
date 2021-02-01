package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.T_SHAPE;

public class TShape implements Shape {
    private static final int[][] shape = new int[3][3];

    /**
     * Constructor of a T_Shape block
     * 0 6 0
     * 6 6 6
     * 0 0 0
     */
    public TShape() {
        shape[1][0] = T_SHAPE;
        shape[1][1] = T_SHAPE;
        shape[1][2] = T_SHAPE;
        shape[0][1] = T_SHAPE;
    }

    /**
     * Getter
     * @return a 3 * 3 dimensional array containing a T_Shape block
     */
    @Override
    public int[][] getShape() {
        return shape;
    }

    /**
     * Getter
     * @return global final code of T_Shape block, here is 3, see detail in ShapeName.java class
     */
    @Override
    public int getShapeCode() {
        return T_SHAPE;
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
