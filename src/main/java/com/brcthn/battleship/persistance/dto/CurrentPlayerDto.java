package com.brcthn.battleship.persistance.dto;

import java.util.List;

public class CurrentPlayerDto {

   private List<GameDto> games;
   private PlayerDto player;

    public List<GameDto> getGames() {
        return games;
    }

    public void setGames(List<GameDto> games) {
        this.games = games;
    }

    public PlayerDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDto player) {
        this.player = player;
    }
}
