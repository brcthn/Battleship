package com.brcthn.battleship.persistance.dto;


import java.util.List;

public class SalvoDto {
    private Long player;
    private int turn;
    private List<String> locations;


    public Long getPlayer() {
        return player;
    }

    public void setPlayer(Long player) {
        this.player = player;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
