package com.brcthn.battleship.persistance.dto;

public class GamePlayerDto {


    private PlayerDto player;
    private ScoreDto score;


    public PlayerDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDto player) {
        this.player = player;
    }

    public ScoreDto getScore() {
        return score;
    }

    public void setScore(ScoreDto score) {
        this.score = score;
    }
}
