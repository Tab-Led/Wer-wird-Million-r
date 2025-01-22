package com.tabled.millioner.models;

public class GameState {
    /**
     * Represents the state of the game in "Who Wants to Be a Millionaire".

     * This class maintains the current progress of the game, the status of lifelines,
     * the selected language, and the player's current and safe amounts.
     * It is used to track and update the game state as the player progresses through levels.

     * Key Features:
     * - Tracks the player's current level.
     * - Stores the usage status of lifelines (50:50, Joker, Second Chance).
     * - Keeps track of whether the "Second Chance" lifeline is active.
     * - Manages the selected language for the game.
     * - Maintains the current safe amount, which the player retains after losing.
     */
    private int currentLevel;
    private boolean isFiftyUsed;
    private boolean isJokerUsed;
    private boolean isSecondChanceUsed;
    private boolean isSecondChanceActive;
    private String language = "en";
    private int safeAmount;

    public GameState() {
        this.currentLevel = 1;
        this.isFiftyUsed = false;
        this.isJokerUsed = false;
        this.isSecondChanceUsed = false;
        this.isSecondChanceActive = false;
        this.safeAmount = 0;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isFiftyUsed() {
        return isFiftyUsed;
    }

    public void setFiftyUsed(boolean fiftyUsed) {
        isFiftyUsed = fiftyUsed;
    }

    public boolean isJokerUsed() { return isJokerUsed;}

    public void setJokerUsed(boolean jokerUsed) { isJokerUsed = jokerUsed; }

    public boolean isSecondChanceUsed() {
        return isSecondChanceUsed;
    }

    public void setSecondChanceUsed(boolean secondChanceUsed) {
        isSecondChanceUsed = secondChanceUsed;
    }

    public boolean isSecondChanceActive() {
        return isSecondChanceActive;
    }

    public void setSecondChanceActive(boolean secondChanceActive) {
        isSecondChanceActive = secondChanceActive;
    }

    public int getSafeAmount() {
        return safeAmount;
    }

    public void setSafeAmount(int safeAmount) {
        this.safeAmount = safeAmount;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "currentLevel=" + currentLevel +
                ", isFiftyUsed=" + isFiftyUsed +
                ", isJokerUsed=" + isJokerUsed +
                ", isSecondChanceUsed=" + isSecondChanceUsed +
                ", isSecondChanceActive=" + isSecondChanceActive +
                ", language='" + language + '\'' +
                ", safeAmount=" + safeAmount +
                '}';
    }
}