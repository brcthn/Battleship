package com.brcthn.battleship.persistance.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


@Entity
public class GamePlayer {
    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;

    private Date joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game Game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player Player;

    public GamePlayer() {
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public com.brcthn.battleship.persistance.entity.Game getGame() {
        return Game;
    }

    public void setGame(com.brcthn.battleship.persistance.entity.Game game) {
        Game = game;
    }

    public com.brcthn.battleship.persistance.entity.Player getPlayer() {
        return Player;
    }

    public void setPlayer(com.brcthn.battleship.persistance.entity.Player player) {
        Player = player;
    }
}
