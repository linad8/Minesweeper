package com.example.minesweeper;

public class Cell {
    private boolean covered;
    private boolean mined;
    private boolean marked;

    public Cell() {
        covered = true;
        mined = false;
        marked = false;
    }

    public boolean isCovered() {
        return covered;
    }

    public boolean isMined() {
        return mined;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked() {
        this.marked = !marked;
    }
}
