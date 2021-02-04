package com.lyapunov.tetris.components;

import android.util.Log;
import com.lyapunov.tetris.blocks.Shape;
import com.lyapunov.tetris.constants.BoardInfo;
import com.lyapunov.tetris.game.BlockGenerator;
import com.lyapunov.tetris.game.RotationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Board {
    private volatile int[][] boardMatrix = new int[BoardInfo.BOARD_HEIGHT + 2][BoardInfo.BOARD_WIDTH];
    private List<BoardObserver> observers = new ArrayList<>();
    private static final int initialPosition = 3;
    /**
     * Singleton Pattern - Only one board should exist within a game
     */
    private static Board board = new Board();
    private Board(){
        //generate boundary for the matrix
        for (int i = BoardInfo.BOARD_HEIGHT; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[0].length; j++) {
                boardMatrix[i][j] =  -1;
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
    public AtomicIntegerArray addBlock(Shape shape) {
        if (shape == null) {
            return null;
        }
        int size = shape.getMatrixSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.boardMatrix[i][initialPosition + j] += shape.getShape()[i][j];
            }
        }
        notifyObservers();
        notifyObserversNew(BlockGenerator.getBlockGenerator().getNextBlock().getShape());
        AtomicIntegerArray leftTop = new AtomicIntegerArray(2);
        leftTop.set(0, initialPosition);
        leftTop.set(1, 0);
        return leftTop;
    }

    /**
     * Drop a current block down by one unit
     * @param shape shape of the current dropping block
     * @param left coordinate of the leftTop corner of the shape (j)
     * @param top coordinate of the leftTop corner of the shape (i)
     * @return the new coordinate of the leftTop corner of the shape(left, top + 1)
     */
    public AtomicIntegerArray dropBlock(Shape shape, int left, int top, int status) {
        if (shape == null) {
            return null;
        }
        int size = shape.getMatrixSize();
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status);

        // validity check

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                if (i + 1 < size && currMatrix[i][j] == currMatrix[i + 1][j]) {
                    continue;
                }
                if (boardMatrix[top + i + 1][left + j] != 0) {
                    checkFullRow(shape, left, top);
                    AtomicIntegerArray leftTop = new AtomicIntegerArray(2);
                    leftTop.set(0, -10); // tell game end is approaching
                    return leftTop;
                }
            }
        }

        // perform drop

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                boardMatrix[i + top][j + left] -= currMatrix[i][j];
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                boardMatrix[i + top + 1][j + left] += currMatrix[i][j];
            }
        }
        AtomicIntegerArray leftTop = new AtomicIntegerArray(2);
        leftTop.set(0, left);
        leftTop.set(1, top + 1);
        notifyObservers();
//        printBoard();
        return leftTop;
    }

    /**
     * Rotate a block by 90 degree counter-clockwise
     * @param shape shape of current block
     * @param left coordinate of top left corner
     * @param top coordinate of top left corner
     * @param status rotation status of current block (0, 1, 2, 3)
     */
    public void rotateBlock(Shape shape, int left, int top, int status) {
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status);
        int newStatus = status + 1;
        if (newStatus == 4) {
            newStatus = 0;
        }
        int[][] nextMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(newStatus);
        int size = shape.getMatrixSize();

        // validity check

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (nextMatrix[i][j] == 0) {
                    continue;
                }
                if (boardMatrix[top + i][left + j] - currMatrix[i][j] + nextMatrix[i][j] != shape.getShapeCode()) {
                    return;
                }
            }
        }

        // perform rotation

        for (int i = 0; i < shape.getMatrixSize(); i++) {
            for (int j = 0; j < shape.getMatrixSize(); j++) {
                 boardMatrix[top + i][left + j] -= currMatrix[i][j];
                 boardMatrix[top + i][left + j] += nextMatrix[i][j];
            }
        }
    }

    /**
     * Move current block left by 1 unit
     * @param shape shape of current block
     * @param left coordinate of top left corner
     * @param top coordinate of top left corner
     * @param status rotation status of current block (0, 1, 2, 3)
     * @return the new coordinate of the leftTop corner of the shape(left - 1, top)
     */
    public AtomicIntegerArray moveBlockLeft(Shape shape, int left, int top, int status) {
        if (shape == null) {
            return null;
        }
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status);
        int size = shape.getMatrixSize();

        AtomicIntegerArray leftTop = new AtomicIntegerArray(2);
        leftTop.set(0, left);
        leftTop.set(1, top);

        //boundary check

        if (left == 0) {
            for (int i = 0; i < size; i++) {
                if (currMatrix[i][0] != 0) {
                    return leftTop;
                }
            }
        }
        if (left == -1) {
            for (int i = 0; i < size; i++) {
                if (currMatrix[i][1] != 0) {
                    return leftTop;
                }
            }
        }

        if (left == -2) {
            return leftTop;
        }

        // validity check

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                if (j - 1 >= 0 && currMatrix[i][j] == currMatrix[i][j - 1]) {
                    continue;
                }
                if (left + j - 1 >= 0 && boardMatrix[top + i][left + j - 1] != 0) {
                    return leftTop;
                }
            }
        }


        //perform movement

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                if (j + left >= 0) {
                    boardMatrix[i + top][j + left] -= currMatrix[i][j];
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                if (j + left - 1 >= 0) {
                    boardMatrix[i + top][j + left - 1] += currMatrix[i][j];
                }
            }
        }
        leftTop.set(0, left - 1);
        leftTop.set(1, top);
        printBoard();
        notifyObservers();
        return leftTop;
    }


    /**
     * Move current block right by 1 unit
     * @param shape shape of current block
     * @param left coordinate of top left corner
     * @param top coordinate of top left corner
     * @param status rotation status of current block (0, 1, 2, 3)
     * @return the new coordinate of the leftTop corner of the shape(left + 1, top)
     */
    public AtomicIntegerArray moveBlockRight(Shape shape, int left, int top, int status) {
        if (shape == null) {
            return null;
        }
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status);
        int size = shape.getMatrixSize();

        AtomicIntegerArray leftTop = new AtomicIntegerArray(2);
        leftTop.set(0, left);
        leftTop.set(1, top);

        // boundary check

        if (left + size == BoardInfo.BOARD_WIDTH) {
            for (int i = 0; i < size; i++) {
                if (currMatrix[i][size - 1] != 0) {
                    return leftTop;
                }
            }
        }
        if (left + size == BoardInfo.BOARD_WIDTH + 1) {
            for (int i = 0; i < size; i++) {
                if (currMatrix[i][size - 2] != 0) {
                    return leftTop;
                }
            }
        }

        if (left + size == BoardInfo.BOARD_WIDTH + 2) {
            return leftTop;
        }


        //validity check

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                if (j + 1 < size && currMatrix[i][j] == currMatrix[i][j + 1]) {
                    continue;
                }
                if (left + j + 1 < boardMatrix.length && boardMatrix[top + i][left + j + 1] != 0) {
                    return leftTop;
                }
            }
        }

        // perform movement

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                if (j + left < boardMatrix.length) {
                    boardMatrix[i + top][j + left] -= currMatrix[i][j];
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                if (j + left + 1 < boardMatrix.length) {
                    boardMatrix[i + top][j + left + 1] += currMatrix[i][j];
                }
            }
        }

        leftTop.set(0, left + 1);
        leftTop.set(1, top);
                printBoard();
        notifyObservers();
        return leftTop;
    }


    private void checkFullRow(Shape shape, int left, int top) {
        int size = shape.getMatrixSize();
        List<Integer> fullRows = new ArrayList<>();
        int bottom = Math.min(top + size, BoardInfo.BOARD_HEIGHT);
        for (int i = top; i < bottom; i++) {
            int sum = 0;
            for (int j = 0; j < BoardInfo.BOARD_WIDTH; j++) {
                if (boardMatrix[i][j] != 0) {
                    sum++;
                }
            }
            if (sum == BoardInfo.BOARD_WIDTH) {
                fullRows.add(i);
            }
        }
        if (fullRows.size() != 0) {
            clearRow(fullRows);
        }
    }

    private void clearRow(List<Integer> fullRows) {
        for (int row: fullRows) {
            for (int i = row; i > 0; i--) {
                System.arraycopy(boardMatrix[i - 1], 0, boardMatrix[i], 0, BoardInfo.BOARD_WIDTH);
            }
        }
        for (int j = 0; j < BoardInfo.BOARD_WIDTH; j++) {
            boardMatrix[0][j] = 0;
        }
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

    /**
     * Notify observers that a new block is generating, its time to update the next_block board
     * @param shapeMatrix the shape of next block
     */
    protected void notifyObserversNew(int[][] shapeMatrix) {
        for(BoardObserver observer : observers){
            if (shapeMatrix.length == 4) {
                observer.generateNew(shapeMatrix);
                return;
            }
            int[][] matrix = new int[4][4];
            for (int i = 0; i < shapeMatrix.length; i++) {
                System.arraycopy(shapeMatrix[i], 0, matrix[i + 1], 1, shapeMatrix.length);
            }
            observer.generateNew(matrix);
        }
    }


}
