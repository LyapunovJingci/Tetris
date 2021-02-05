package com.lyapunov.tetris.game;
import com.lyapunov.tetris.blocks.Shape;

import java.util.Random;

public class BlockGenerator {
    /**
     * Singleton Pattern - Only one blockGenerator should exist within a game
     * Factory Pattern - Use for generating new block
     */
    private static BlockGenerator blockGenerator = new BlockGenerator();
    private BlockGenerator(){};
    public static BlockGenerator getBlockGenerator() {
        return blockGenerator;
    }
    private int nextRand = 0;
    /**
     * Generating a random block following rules of factory pattern
     * @return shape of new block
     */
    public Shape generateBlock() {
        //generate a random number within range [1, 7]
        Random rand = new Random();
        if (nextRand == 0) {
            nextRand = rand.nextInt(7) + 1;
            int currentRand = rand.nextInt(7) + 1;
            Shape shape = BlockFactory.getBlockFactory().getShape(currentRand);
            rotationHelper(currentRand, shape.getShape());
            return shape;
        }
        Shape shape =  BlockFactory.getBlockFactory().getShape(nextRand);
        rotationHelper(nextRand, shape.getShape());
        nextRand = rand.nextInt(7) + 1;
        return shape;
    }

    public Shape getNextBlock() {
        return BlockFactory.getBlockFactory().getShape(nextRand);
    }

    private void rotationHelper(int shapeName, int[][] shapeMatrix) {
        if (!RotationHandler.getRotationHandler().rotationHash.containsKey(shapeName)) {
            RotationHandler.getRotationHandler().generateRotationForHash(shapeName, shapeMatrix);
        }
    }


}
