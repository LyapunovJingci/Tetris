package com.lyapunov.tetris.game;
import com.lyapunov.tetris.blocks.Shape;

import java.util.concurrent.ThreadLocalRandom;

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
        if (nextRand == 0) {
            nextRand = ThreadLocalRandom.current().nextInt(1, 8);
            return BlockFactory.getBlockFactory().getShape(ThreadLocalRandom.current().nextInt(1, 8));
        }
        Shape shape =  BlockFactory.getBlockFactory().getShape(nextRand);
        nextRand = ThreadLocalRandom.current().nextInt(1, 8);
        return shape;
    }

    public Shape getNextBlock() {
        return BlockFactory.getBlockFactory().getShape(nextRand);
    }


}
