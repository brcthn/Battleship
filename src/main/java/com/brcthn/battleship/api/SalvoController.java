package com.brcthn.battleship.api;


import com.brcthn.battleship.persistance.entity.Game;
import com.brcthn.battleship.persistance.entity.GamePlayer;
import com.brcthn.battleship.persistance.entity.Player;
import com.brcthn.battleship.persistance.repository.GameRepository;
import com.brcthn.battleship.persistance.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository repo;

//    @GetMapping("/games")
//    public List<Long> getAll() {
//        List<Game> all = repo.findAll();   //[{"id":1},{"id":2},{"id":3}] java listesi
//        List<Long> idList = new ArrayList<>();
//        for (int i = 0; i < all.size(); i++) {
//            idList.add(all.get(i).getId());  //game tipinde elemanlari geziyor.
//
//        }return idList;
//
//    }

//    @GetMapping("/games")
//    public Map<String,Object> getAll(){
//        List<Game> all = repo.findAll();
//        Map<String,Object> allList= new LinkedHashMap<String, Object>();
//        for (int i = 0; i < all.size(); i++) {
//            allList.put(String.valueOf(all.get(i).getId()), all.get(i));
//        }
//
//        return allList;
//    }

    @GetMapping("/games")
    public List<Map<String, Object>> getAll() {
        List<Game> all = repo.findAll();







/*
* {
   "id":1,
   "created":1456438201629,
   "gamePlayers":[
      {
         "id":1,
         "player":{
            "id":1,
            "email":"j.bauer@ctu.gov"
         }
      },
      {
         "id":2,
         "player":{
            "id":2,
            "email":"c.obrian@ctu.gov"
         }
      }
   ]
}
*
* */






        List<Map<String, Object>> results = new ArrayList<>();
        for(int i=0;i<all.size();i++){
            Map<String,Object>gameMap= new LinkedHashMap<>();
            gameMap.put("id",all.get(i).getId()) ;
            gameMap.put("created",all.get(i).getCreationData()) ;
            List<Map<String, Object>> gamePlayerList = new ArrayList<>();
            for(int k=0;k<all.get(i).getGamePlayers().size();k++){
                Map<String,Object> gamePlayerMap= new LinkedHashMap<>();
                gamePlayerMap.put("id",all.get(i).getGamePlayers().get(k).getId());
                Map<String,Object>playerMap=new LinkedHashMap<>();
                playerMap.put("id",all.get(i).getGamePlayers().get(k).getPlayer().getId() );
                playerMap.put("email",all.get(i).getGamePlayers().get(k).getPlayer().getEmail());
                gamePlayerMap.put("player",playerMap);
                gamePlayerList.add(gamePlayerMap);
            }
            gameMap.put("gamePlayer",gamePlayerList) ;
            results.add(gameMap);
        }
        return results;
    }
}



