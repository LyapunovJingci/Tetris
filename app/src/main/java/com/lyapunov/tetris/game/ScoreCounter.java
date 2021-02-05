package com.lyapunov.tetris.game;

public class ScoreCounter {
    private static ScoreCounter scoreCounter = new ScoreCounter();
    private ScoreCounter(){};
    public static ScoreCounter getScoreCounter() {
        return scoreCounter;
    }

    public int lineToScore(int line) {
        switch (line) {
            case 4:
                return 800;
            case 3:
                return 400;
            case 2:
                return 200;
            default:
                return 100;
        }
    }
}
