//package com.brcthn.battleship.api;
//
//
//import com.brcthn.battleship.persistance.entity.Player;
//import com.brcthn.battleship.servis.PlayerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@EnableAutoConfiguration
//@RequestMapping("/players")
//public class PlayerController {
//
//   @Autowired
//   PlayerService playerService;
//
//    @GetMapping("/{id}")
//    public Player getPlayer(@PathVariable("id") long id){
//        return playerService.getPlayer(id);
//    }
//
//    @PostMapping("/save")
//    public Player savePlayer(@RequestBody Player player){
//        return playerService.savePlayer(player);
//    }
//
//
//
//
//}
