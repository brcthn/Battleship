package com.brcthn.battleship.persistance.dto;

import com.brcthn.battleship.persistance.entity.GamePlayer;

import java.util.Date;

public class GameDto {

    private int id;
    private Date creationData;
    private GamePlayerDto gamePlayer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationData() {
        return creationData;
    }

    public void setCreationData(Date creationData) {
        this.creationData = creationData;
    }

    public GamePlayerDto getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayerDto gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
