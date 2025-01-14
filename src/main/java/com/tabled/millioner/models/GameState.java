package com.tabled.millioner.models;

public class GameState {
    private int currentLevel;
    private boolean isFiftyUsed;
    private boolean isJokerUsed;
    private boolean isSecondChanceUsed;
    private boolean isSecondChanceActive;
    private String language = "en";

    public GameState() {
        this.currentLevel = 1;
        this.isFiftyUsed = false;
        this.isJokerUsed = false;
        this.isSecondChanceUsed = false;
        this.isSecondChanceActive = false;
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
}