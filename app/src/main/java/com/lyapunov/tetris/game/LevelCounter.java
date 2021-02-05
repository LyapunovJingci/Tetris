package com.lyapunov.tetris.game;

public class LevelCounter {
    private static LevelCounter levelCounter = new LevelCounter();
    private LevelCounter(){};
    public static LevelCounter getLevelCounter() {
        return levelCounter;
    }

    public int scoreToLevel(int score) {
        return score / 2000 + 1;
    }
}
