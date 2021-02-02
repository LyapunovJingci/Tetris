package com.lyapunov.tetris.game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class RotationHandler {
    private static RotationHandler rotationHandler = new RotationHandler();
    private RotationHandler(){ }
    public static RotationHandler getRotationHandler() {
        return rotationHandler;
    }
    public ConcurrentHashMap<Integer, ArrayList<int[][]>> rotationHash = new ConcurrentHashMap<>();

    protected void generateRotationForHash(int shapeName, int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
        }
        ArrayList<int[][]> list = new ArrayList<>();
        for (int m = 0; m < 4; m++) {
            int[][] max = new int[matrix.length][matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    max[i][j] = newMatrix[i][j];
                }
            }
            list.add(max);
            rotateMatrix(matrix.length, newMatrix);
        }
        rotationHash.put(shapeName, list);
    }

    private void rotateMatrix(int size, int[][] newMatrix) {
        for (int i = 0; i < size / 2; i++) {
            for (int j = i; j < size - i - 1; j++) {
                int tmp = newMatrix[i][j];
                newMatrix[i][j] = newMatrix[j][size - 1 - i];
                newMatrix[j][size - i - 1] = newMatrix[size - i - 1][size - j - 1];
                newMatrix[size - i - 1][size - j - 1] = newMatrix[size - j - 1][i];
                newMatrix[size - j - 1][i] = tmp;
            }
        }
    }


}
