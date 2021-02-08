package com.lyapunov.tetris.game;

import android.util.Log;
import com.lyapunov.tetris.blocks.Shape;
import com.lyapunov.tetris.constants.BoardInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Board {
    private volatile int[][] boardMatrix = new int[BoardInfo.BOARD_HEIGHT + 2][BoardInfo.BOARD_WIDTH];
    private static final int initialPosition = 3;
    private AtomicInteger clearedLines = new AtomicInteger(0);
    /**
     * Singleton Pattern - Only one board should exist within a game
     */
    private static Board board = new Board();
    private Board(){
        //generate boundary for the matrix
        for (int i = BoardInfo.BOARD_HEIGHT; i < boardMatrix.length; i++) {
            for (int j = 0; j < BoardInfo.BOARD_WIDTH; j++) {
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

    public void clear() {
        for (int i = 0; i < BoardInfo.BOARD_HEIGHT; i++) {
            for (int j = 0; j < BoardInfo.BOARD_WIDTH; j++) {
                boardMatrix[i][j] = 0;
            }
        }
        clearedLines.set(0);
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
        int[][] shapeMatrix = shape.getShape();

        // check validity
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (shapeMatrix[i][j] == 0) {
                    continue;
                }
                if (boardMatrix[i][initialPosition + j] != 0) {
                    AtomicIntegerArray leftTop = new AtomicIntegerArray(2);
                    leftTop.set(0, -100);
                    return leftTop;
                }
            }
        }

        // perform adding block
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardMatrix[i][initialPosition + j] += shape.getShape()[i][j];
            }
        }

        // return coordinate of leftTop corner
        AtomicIntegerArray leftTop = new AtomicIntegerArray(2);
        leftTop.set(0, initialPosition);
        leftTop.set(1, 0);
        return leftTop;
    }

    /**
     * Drop a current block down by one unit
     * @param shape shape of the current dropping block
     * @param leftTop coordinate of the top-left corner
     * @param status rotation status of the current block (0, 1, 2, 3)
     * @return the new coordinate of the leftTop corner of the shape(left, top + 1)
     */
    public AtomicIntegerArray dropBlock(Shape shape, AtomicIntegerArray leftTop, AtomicInteger status) {
        if (shape == null) {
            return null;
        }
        if (leftTop.get(0) == -100 || leftTop.get(0) == -10) {
            return leftTop;
        }
        int size = shape.getMatrixSize();
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status.get());

        // validity check
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                if (i + 1 < size && currMatrix[i][j] == currMatrix[i + 1][j]) {
                    continue;
                }
                if (boardMatrix[leftTop.get(1) + i + 1][leftTop.get(0) + j] != 0) {
                    checkFullRow(shape, leftTop.get(1));
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
                boardMatrix[i + leftTop.get(1)][j + leftTop.get(0)] -= currMatrix[i][j];
                boardMatrix[i + leftTop.get(1) + 1][j + leftTop.get(0)] += currMatrix[i][j];
            }
        }

        leftTop.set(1, leftTop.get(1) + 1);
        //printBoard("drop");
        return leftTop;
    }


    public AtomicIntegerArray fastDropBlock(Shape shape, AtomicIntegerArray leftTop, AtomicInteger status) {
        if (shape == null) {
            return null;
        }
        int size = shape.getMatrixSize();
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status.get());

        // perform drop
        int dest = leftTop.get(1);
        int cur = leftTop.get(0);
        //Log.e("fastdrop", dest + " " + cur);
        boolean stop = false;
        while (dest < 20 && !stop) {
            dest++;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (currMatrix[i][j] == 0) {
                        continue;
                    }
                    if (boardMatrix[dest + i][cur + j] != 0) {
                        if (i + 1 < size && currMatrix[i + 1][j] == currMatrix[i][j]) {
                            continue;
                        }
                        dest--;
                        stop = true;
                    }
                }
            }
        }

        //Log.e("fastdrop", dest + " " + cur);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currMatrix[i][j] == 0) {
                    continue;
                }
                boardMatrix[i + leftTop.get(1)][j + leftTop.get(0)] -= currMatrix[i][j];
                boardMatrix[i + dest][j + leftTop.get(0)] += currMatrix[i][j];
            }
        }
        checkFullRow(shape, dest);
        leftTop.set(0, -10);
        //printBoard("fast_drop");
        return leftTop;
    }
    /**
     * Rotate a block by 90 degree counter-clockwise
     * @param shape shape of the current dropping block
     * @param leftTop coordinate of the top-left corner (0, 1, 2, 3)
     * @param status rotation status of the current block
     */
    public synchronized void rotateBlock(Shape shape, AtomicIntegerArray leftTop, AtomicInteger status) {
        if (shape == null) {
            return;
        }
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status.get());
        int newStatus = status.get() + 1;
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
                if (boardMatrix[leftTop.get(1) + i][leftTop.get(0) + j] - currMatrix[i][j] + nextMatrix[i][j] != shape.getShapeCode()) {
                    return;
                }
            }
        }

        // perform rotation
        for (int i = 0; i < shape.getMatrixSize(); i++) {
            for (int j = 0; j < shape.getMatrixSize(); j++) {
                 boardMatrix[leftTop.get(1) + i][leftTop.get(0) + j] -= currMatrix[i][j];
                 boardMatrix[leftTop.get(1) + i][leftTop.get(0) + j] += nextMatrix[i][j];
            }
        }
    }

    /**
     * Move current block left by 1 unit
     * @param shape shape of the current dropping block
     * @param leftTop coordinate of the top-left corner
     * @param status rotation status of the current block (0, 1, 2, 3)
     * @return the new coordinate of the leftTop corner of the shape(left - 1, top)
     */
    public synchronized AtomicIntegerArray moveBlockLeft(Shape shape, AtomicIntegerArray leftTop, AtomicInteger status) {
        if (shape == null) {
            return null;
        }
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status.get());
        int size = shape.getMatrixSize();

        //boundary check
        if (leftTop.get(0) == 0) {
            for (int i = 0; i < size; i++) {
                if (currMatrix[i][0] != 0) {
                    return leftTop;
                }
            }
        }
        if (leftTop.get(0) == -1) {
            for (int i = 0; i < size; i++) {
                if (currMatrix[i][1] != 0) {
                    return leftTop;
                }
            }
        }

        if (leftTop.get(0) == -2) {
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
                if (leftTop.get(0) + j - 1 >= 0 && boardMatrix[leftTop.get(1) + i][leftTop.get(0) + j - 1] != 0) {
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
                if (j + leftTop.get(0) >= 0) {
                    boardMatrix[i + leftTop.get(1)][j + leftTop.get(0)] -= currMatrix[i][j];
                }
                if (j + leftTop.get(0) - 1 >= 0) {
                    boardMatrix[i + leftTop.get(1)][j + leftTop.get(0) - 1] += currMatrix[i][j];
                }
            }
        }

        leftTop.set(0, leftTop.get(0) - 1);
        //printBoard("left");
        return leftTop;
    }

    /**
     * Move current block right by 1 unit
     * @param shape shape of the current dropping block
     * @param leftTop coordinate of the top-left corner
     * @param status rotation status of the current block (0, 1, 2, 3)
     * @return the new coordinate of the leftTop corner of the shape(left + 1, top)
     */
    public synchronized AtomicIntegerArray moveBlockRight(Shape shape, AtomicIntegerArray leftTop, AtomicInteger status) {
        if (shape == null) {
            return null;
        }
        int[][] currMatrix = RotationHandler.getRotationHandler().rotationHash.get(shape.getShapeCode()).get(status.get());
        int size = shape.getMatrixSize();

        // boundary check
        if (leftTop.get(0) + size == BoardInfo.BOARD_WIDTH) {
            for (int i = 0; i < size; i++) {
                if (currMatrix[i][size - 1] != 0) {
                    return leftTop;
                }
            }
        }
        if (leftTop.get(0) + size == BoardInfo.BOARD_WIDTH + 1) {
            for (int i = 0; i < size; i++) {
                if (currMatrix[i][size - 2] != 0) {
                    return leftTop;
                }
            }
        }

        if (leftTop.get(0) + size == BoardInfo.BOARD_WIDTH + 2) {
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
                if (leftTop.get(0) + j + 1 < boardMatrix.length && boardMatrix[leftTop.get(1) + i][leftTop.get(0) + j + 1] != 0) {
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
                if (j + leftTop.get(0) < boardMatrix.length) {
                    boardMatrix[i + leftTop.get(1)][j + leftTop.get(0)] -= currMatrix[i][j];
                }
                if (j + leftTop.get(0) + 1 < boardMatrix.length) {
                    boardMatrix[i + leftTop.get(1)][j + leftTop.get(0) + 1] += currMatrix[i][j];
                }
            }
        }

        leftTop.set(0, leftTop.get(0) + 1);
        //printBoard("right");
        return leftTop;
    }

    /**
     * Check whether a dropping block fulfill full row(s)
     * @param shape shape of the current dropping block
     * @param top i coordinate of the top-left corner
     */
    private void checkFullRow(Shape shape, int top) {
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
        } else {
            clearedLines.set(0);
        }
    }

    /**
     * Clear rows and move above rows down
     * @param fullRows i coordinate of all full rows
     */
    private void clearRow(List<Integer> fullRows) {
        for (int row: fullRows) {
            for (int i = row; i > 0; i--) {
                System.arraycopy(boardMatrix[i - 1], 0, boardMatrix[i], 0, BoardInfo.BOARD_WIDTH);
            }
        }
        for (int j = 0; j < BoardInfo.BOARD_WIDTH; j++) {
            boardMatrix[0][j] = 0;
        }
        clearedLines.set(fullRows.size());
    }

    /**
     * Getter of number of cleared rows in this block dropping turn
     * @return number of cleared rows [0, 4]
     */
    public int getClearedLines() {
        return clearedLines.get();
    }

    /**
     * Print current status of the board, mainly use for testing visualization
     */
    private void printBoard(String info) {
        for (int[] matrix : boardMatrix) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < boardMatrix[0].length; j++) {
                builder.append(matrix[j]).append(" ");
            }
            Log.d(info, builder.toString());
        }
    }

}
