package com.lyapunov.tetris.components;

import com.lyapunov.tetris.blocks.Shape;

public class Board {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;
    private volatile int[][] boardMatrix = new int[HEIGHT][WIDTH];
    private static Board board = new Board();
    private Board(){}
    public static Board getBoard() {
        return board;
    }

    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    private void addBlock(Shape shape) {
        if (shape == null) {
            return;
        }
        int size = shape.matrixSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardMatrix[i][3 + j] += shape.getShape()[i][j];
            }
        }
    }



}
