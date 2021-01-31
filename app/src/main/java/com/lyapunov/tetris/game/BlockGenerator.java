package com.lyapunov.tetris.game;
import com.lyapunov.tetris.blocks.Shape;

import java.util.concurrent.ThreadLocalRandom;

public class BlockGenerator {
    private static BlockGenerator blockGenertor = new BlockGenerator();
    private BlockGenerator(){};
    public static BlockGenerator getBlockGenertor() {
        return blockGenertor;
    }
    public Shape generateBlock() {
        //generate random number for block generation
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 8);
        return BlockFactory.getBlockFactory().getShape(randomNumber);
    }



}
