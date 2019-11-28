package com.brcthn.battleship.persistance.dto;


import java.util.Date;
import java.util.List;

public class GameDto {

    private long id;
    private List<GamePlayerDto> gamePlayer;
    private String created;

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<GamePlayerDto> getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(List<GamePlayerDto> gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}