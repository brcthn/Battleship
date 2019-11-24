package com.brcthn.battleship.api;


import com.brcthn.battleship.persistance.entity.*;
import com.brcthn.battleship.persistance.repository.GamePlayerRepository;
import com.brcthn.battleship.persistance.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @GetMapping("/games")
    public List<Map<String, Object>> getAll() {
        List<Game> all = gameRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            Map<String, Object> gameMap = new LinkedHashMap<>();
            gameMap.put("id", all.get(i).getId());
            gameMap.put("created", all.get(i).getCreationData());
            List<Map<String, Object>> gamePlayerList = new ArrayList<>();
            for (int k = 0; k < all.get(i).getGamePlayers().size(); k++) {
                Map<String, Object> gamePlayerMap = new LinkedHashMap<>();
                gamePlayerMap.put("id", all.get(i).getGamePlayers().get(k).getId());

                Map<String, Object> playerMap = new LinkedHashMap<>();
                playerMap.put("id", all.get(i).getGamePlayers().get(k).getPlayer().getId());
                playerMap.put("email", all.get(i).getGamePlayers().get(k).getPlayer().getEmail());
                gamePlayerMap.put("player", playerMap);

                Map<String,Object>scoreMap=new LinkedHashMap<>();
                Score score = all.get(i).getGamePlayers().get(k).getScore();
                if(score != null){
                    scoreMap.put("id",score.getId());
                    scoreMap.put("score",score.getScore());
                    scoreMap.put("gameId",score.getGame().getId());
                    scoreMap.put("playerId",score.getPlayer().getId());

                    gamePlayerMap.put("score",scoreMap);
                }

                gamePlayerList.add(gamePlayerMap);
            }
            gameMap.put("gamePlayer", gamePlayerList);
            results.add(gameMap);
        }
        return results;
    }

    @RequestMapping(value = "/game_view/{nn}", method = RequestMethod.GET)
    public Map<String, Object> getGame(@PathVariable("nn") long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        Map<String, Object> gamePlayers = new LinkedHashMap<>();
        gamePlayers.put("id", gamePlayer.getId());
        gamePlayers.put("created", gamePlayer.getJoinDate());
        List<Map<String, Object>> gamePlayerList = new ArrayList<>();

        Game game = gamePlayer.getGame();
        for (GamePlayer gp : game.getGamePlayers()) {
            Map<String, Object> gpMap = new LinkedHashMap<>();
            gpMap.put("id", gp.getId());
            gpMap.put("player", gp.getPlayer());
            gamePlayerList.add(gpMap);
        }

        List<Map<String, Object>> shipList = new ArrayList<>();
        for (Ship s:gamePlayer.getShips()) {
            Map<String, Object> shipMap = new LinkedHashMap<>();
            shipMap.put("type", s.getShipType());
            shipMap.put("ships", s.getLocations());
            shipList.add(shipMap);

        }

        List<Map<String,Object>> salvoList=new ArrayList<>();
        for(GamePlayer gp: game.getGamePlayers()){
            for(Salvo s: gp.getSalvoes()){
                Map<String,Object>salvoMap=new LinkedHashMap<>();
                salvoMap.put("turn",s.getTurnNumber());
                salvoMap.put("player",gp.getPlayer().getId());
                salvoMap.put("locations",s.getLocation());
                salvoList.add(salvoMap);
            }
        }
        gamePlayers.put("gamePlayers", gamePlayerList);
        gamePlayers.put("ship", shipList);
        gamePlayers.put("salvoes",salvoList);
        return gamePlayers;
    }
    
}