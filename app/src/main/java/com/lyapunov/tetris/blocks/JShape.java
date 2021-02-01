package com.lyapunov.tetris.blocks;

import static com.lyapunov.tetris.constants.ShapeName.J_SHAPE;

public class JShape implements Shape {
    private static final int[][] shape = new int[3][3];

    /**
     * Constructor of a J_Shape block
     * 0 2 0
     * 0 2 0
     * 2 2 0
     */
    public JShape() {
        shape[0][1] = J_SHAPE;
        shape[1][1] = J_SHAPE;
        shape[2][1] = J_SHAPE;
        shape[2][0] = J_SHAPE;
    }

    /**
     * Getter
     * @return a 3 * 3 dimensional array containing a J_Shape block
     */
    @Override
    public int[][] getShape() {
        return shape;
    }

    /**
     * Getter
     * @return global final code of J_Shape block, here is 2, see detail in ShapeName.java class
     */
    @Override
    public int getShapeCode() {
        return J_SHAPE;
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
