package com.lyapunov.tetris.game;

public class LevelCounter {
    private static LevelCounter levelCounter = new LevelCounter();
    private LevelCounter(){};
    public static LevelCounter getLevelCounter() {
        return levelCounter;
    }

    public int lineToLevel(int line) {
        return line / 20;
    }
}
