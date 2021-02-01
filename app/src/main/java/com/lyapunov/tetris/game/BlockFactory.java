package com.lyapunov.tetris.game;

import com.lyapunov.tetris.blocks.IShape;
import com.lyapunov.tetris.blocks.JShape;
import com.lyapunov.tetris.blocks.LShape;
import com.lyapunov.tetris.blocks.OShape;
import com.lyapunov.tetris.blocks.SShape;
import com.lyapunov.tetris.blocks.Shape;
import com.lyapunov.tetris.blocks.TShape;
import com.lyapunov.tetris.blocks.ZShape;
import com.lyapunov.tetris.constants.ShapeName;

public class BlockFactory {
    /**
     * Singleton Pattern - Only one blockFactory should exist within a game
     * Factory Pattern - Use for generating new block
     */
    private static BlockFactory blockFactory = new BlockFactory();
    private BlockFactory(){};
    public static BlockFactory getBlockFactory() {
        return blockFactory;
    }
    public Shape getShape(int shapeType) {
        if (shapeType < 1 || shapeType > 7) {
            return null;
        }
        switch(shapeType) {
            case ShapeName.I_SHAPE:
                return new IShape();
            case ShapeName.J_SHAPE:
                return new JShape();
            case ShapeName.L_SHAPE:
                return new LShape();
            case ShapeName.O_SHAPE:
                return new OShape();
            case ShapeName.S_SHAPE:
                return new SShape();
            case ShapeName.T_SHAPE:
                return new TShape();
            case ShapeName.Z_SHAPE:
                return new ZShape();
        }
        return null;
    }


}
