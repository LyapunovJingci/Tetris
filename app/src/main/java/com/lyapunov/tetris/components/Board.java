package com.lyapunov.tetris.components;

import android.util.Log;
import com.lyapunov.tetris.blocks.Shape;
import com.lyapunov.tetris.constants.BoardInfo;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private volatile int[][] boardMatrix = new int[BoardInfo.BOARD_HEIGHT + 4][BoardInfo.BOARD_WIDTH];
    private List<BoardObserver> observers = new ArrayList<>();
    /**
     * Singleton Pattern - Only one board should exist within a game
     */
    private static Board board = new Board();
    private Board(){
        for (int i = BoardInfo.BOARD_HEIGHT; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[0].length; j++) {
                boardMatrix[i][j] =  i;
            }
        }
    }
    public static Board getBoard() {
        return board;
    }

    /**
     * Getter
     * @return current status of the 2-D board matrix
     */
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * Generate a new block at the top of the board
     * @param shape shape of a new block
     * @return the coordinate of the leftTop corner of the shape (forever [3, 0])
     */
    public int[] addBlock(Shape shape) {
        if (shape == null) {
            return null;
        }
        int size = shape.getMatrixSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.boardMatrix[i][3 + j] += shape.getShape()[i][j];
            }
        }
        notifyObservers();

        int[] leftTop = new int[2];
        leftTop[0] = 3;
        return leftTop;
    }

    /**
     * Drop a current block down by one unit
     * @param shape shape of the current dropping block
     * @param left coordinate of the leftTop corner of the shape (j)
     * @param top coordinate of the leftTop corner of the shape (i)
     * @return the new coordinate of the leftTop corner of the shape(left, top + 1)
     */
    public int[] dropBlock(Shape shape, int left, int top) {
        if (shape == null) {
            return null;
        }
        int size = shape.getMatrixSize();
        for (int j = left; j < left + size; j++) {
            if (boardMatrix[top + size][j] + boardMatrix[top + size - 1][j] != 0 && boardMatrix[top + size][j] + boardMatrix[top + size - 1][j] != shape.getShapeCode() && boardMatrix[top + size][j] + boardMatrix[top + size - 1][j] != boardMatrix[top + size][j]) {
                int[] leftTop = new int[2];
                leftTop[0] = -1;
                return leftTop;
            }

        }

        for (int i = top + size; i > top; i--) {
            for (int j = left; j < left + size; j++) {
                boardMatrix[i][j] += boardMatrix[i - 1][j];
                boardMatrix[i - 1][j] = 0;
            }
        }
        for (int j = left; j < left + size; j++) {
            if (boardMatrix[top][j] == shape.getShapeCode()) {
                boardMatrix[top][j] = 0;
            }
        }

        int[] leftTop = new int[2];
        leftTop[0] = left;
        leftTop[1] = top + 1;
        notifyObservers();
        //printBoard();
        return leftTop;
    }


    /**
     * Print current status of the board, mainly use for testing visualization
     */
    private void printBoard() {
        for (int[] matrix : boardMatrix) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < boardMatrix[0].length; j++) {
                builder.append(matrix[j]).append(" ");
            }
            Log.d("line", builder.toString());
        }
    }

    /**
     * Attach observers to the board
     * @param observer MainActivity (updating UI)
     */
    public void attach(BoardObserver observer) {
        observers.add(observer);
    }

    /**
     * Detach observers
     * @param observer currently no usage
     */
    public void detach(BoardObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify observers that the status of the board has changed
     * MainActivity (updating UI)
     */
    protected void notifyObservers() {
        for(BoardObserver observer : observers){
            observer.update();
        }
    }


}
