package com.brcthn.battleship.persistance.dto;

import java.util.Date;
import java.util.List;

public class GamePlayerPersonDto {
    private Long id;
    private Date joinDate;
    private List<GamePlayerDto> gamePlayers;
    private List<SalvoDto> salvoes;
    private List<ShipDto> ship;
    private List<HistoryDto> history;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<HistoryDto> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryDto> history) {
        this.history = history;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public List<GamePlayerDto> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<GamePlayerDto> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public List<SalvoDto> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(List<SalvoDto> salvoes) {
        this.salvoes = salvoes;
    }

    public List<ShipDto> getShip() {
        return ship;
    }

    public void setShip(List<ShipDto> ship) {
        this.ship = ship;
    }
}
