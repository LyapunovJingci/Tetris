package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.Z_SHAPE;

public class ZShape implements Shape {
    private static final int[][] shape = new int[3][3];

    /**
     * Constructor of a Z_Shape block
     * 7 7 0
     * 0 7 7
     * 0 0 0
     */
    public ZShape() {
        shape[0][0] = Z_SHAPE;
        shape[0][1] = Z_SHAPE;
        shape[1][1] = Z_SHAPE;
        shape[1][2] = Z_SHAPE;
    }

    /**
     * Getter
     * @return a 3 * 3 dimensional array containing a Z_Shape block
     */
    @Override
    public int[][] getShape() {
        return shape;
    }

    /**
     * Getter
     * @return global final code of Z_Shape block, here is 3, see detail in ShapeName.java class
     */
    @Override
    public int getShapeCode() {
        return Z_SHAPE;
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
