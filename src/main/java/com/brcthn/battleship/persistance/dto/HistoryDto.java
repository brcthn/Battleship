package com.brcthn.battleship.persistance.dto;

public class HistoryDto {
    private String type;
    private Integer turn;
    private boolean sink;
    private Integer hit;
    private Integer left;

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public boolean isSink() {
        return sink;
    }

    public void setSink(boolean sink) {
        this.sink = sink;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }
}
