package com.lyapunov.tetris.components;

import android.util.Log;
import com.lyapunov.tetris.blocks.Shape;
import java.util.ArrayList;
import java.util.List;

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

    public int[] addBlock(Shape shape) {
        if (shape == null) {
            return null;
        }
        int size = shape.matrixSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.boardMatrix[i][3 + j] += shape.getShape()[i][j];
            }
        }
        notifyObservers();
        printBoard();
        int[] leftTop = new int[2];
        leftTop[0] = 3;
        return leftTop;
    }

    public int[] dropBlock(Shape shape, int left, int top) {
        if (shape == null) {
            return null;
        }
        int size = shape.matrixSize();
        for (int i = top + size; i > top; i--) {
            for (int j = left; j < left + size; j++) {
                boardMatrix[i][j] = boardMatrix[i - 1][j];
            }
        }
        for (int j = left; j < left + size; j++) {
            if (boardMatrix[top][j] == shape.shapeCode()) {
                boardMatrix[top][j] = 0;
            }
        }
        int[] leftTop = new int[2];
        leftTop[0] = left;
        leftTop[1] = top + 1;
        notifyObservers();
        printBoard();
        return leftTop;
    }





    private void printBoard() {
        for (int i = 0; i < boardMatrix.length; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < boardMatrix[0].length; j++) {
                builder.append(boardMatrix[i][j]).append(" ");
            }
            Log.d("line", builder.toString());
        }
    }

    private List<BoardObserver> observers = new ArrayList<>();

    public void attach(BoardObserver observer) {
        observers.add(observer);
    }

    public void detach(BoardObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for(BoardObserver observer : observers){
            observer.update();
        }
    }


}
