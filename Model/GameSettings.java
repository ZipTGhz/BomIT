package Model;

public class GameSettings {
    private static GameSettings instance;
    private int numPlayers = 1;
    private int numEnemies = 1;
    private int difficulty = 0;

    private GameSettings() {}

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    // Getters and Setters
    public int getNumPlayers() { return numPlayers; }
    public void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }

    public int getNumEnemies() { return numEnemies; }
    public void setNumEnemies(int numEnemies) { this.numEnemies = numEnemies; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public void resetGameSetting() {
        this.numPlayers = 1;
        this.numEnemies = 1;
        this.difficulty = 0;
    }
} 