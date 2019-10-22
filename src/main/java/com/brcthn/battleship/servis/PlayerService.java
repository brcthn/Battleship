package com.brcthn.battleship.servis;

import com.brcthn.battleship.persistance.entity.Player;
import com.brcthn.battleship.persistance.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player getPlayer(long id) {
        return playerRepository.getOne(id);
    }
}