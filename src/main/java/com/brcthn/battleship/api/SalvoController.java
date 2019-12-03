package com.brcthn.battleship.api;


import com.brcthn.battleship.persistance.dto.*;
import com.brcthn.battleship.persistance.entity.*;
import com.brcthn.battleship.persistance.repository.GamePlayerRepository;
import com.brcthn.battleship.persistance.repository.GameRepository;
import com.brcthn.battleship.persistance.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/games")
    public List<GameDto> getAll() {
        List<Game> all = gameRepository.findAll();
        List<GameDto> results = new ArrayList<>();
        for (Game game : all) {
            GameDto gameDto = new GameDto();
            gameDto.setId(game.getId());
            gameDto.setCreated(game.getCreationData());

            List<GamePlayerDto> gamePlayerList = new ArrayList<>();
            for (GamePlayer gp : game.getGamePlayers()) {
                GamePlayerDto gamePlayerDto = new GamePlayerDto();
                gamePlayerDto.setId(gp.getId());

                PlayerDto playerDto = new PlayerDto();
                playerDto.setId(gp.getPlayer().getId());
                playerDto.setFirstName(gp.getPlayer().getFirstName());
                playerDto.setLastName(gp.getPlayer().getLastName());
                playerDto.setEmail(gp.getPlayer().getEmail());
                gamePlayerDto.setPlayer(playerDto);

                Score score = gp.getScore();
                if (score != null) {
                    ScoreDto scoreDto = new ScoreDto();
                    scoreDto.setScore(score.getScore());
                    scoreDto.setWins(score.getWins());
                    scoreDto.setLoses(score.getLoses());
                    scoreDto.setTies(score.getTies());
                    gamePlayerDto.setScore(scoreDto);
                }

                gamePlayerList.add(gamePlayerDto);
            }
            gameDto.setGamePlayer(gamePlayerList);
            results.add(gameDto);
        }
        return results;
    }

    @RequestMapping(value = "/game_view/{nn}", method = RequestMethod.GET)
    public GamePlayerPersonDto getGame(@PathVariable("nn") long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        GamePlayerPersonDto gamePlayerPersonDto = new GamePlayerPersonDto();
        gamePlayerPersonDto.setId(gamePlayer.getId());
        gamePlayerPersonDto.setJoinDate(gamePlayer.getJoinDate());

        List<GamePlayerDto> gamePlayerList = new ArrayList<>();
        Game game = gamePlayer.getGame();
        for (GamePlayer gp : game.getGamePlayers()) {
            GamePlayerDto gamePlayerDto = new GamePlayerDto();
            gamePlayerDto.setId(gp.getId());

            PlayerDto playerDto = new PlayerDto();
            playerDto.setId(gp.getPlayer().getId());
            playerDto.setEmail(gp.getPlayer().getEmail());
            playerDto.setFirstName(gp.getPlayer().getFirstName());
            playerDto.setLastName(gp.getPlayer().getLastName());
            gamePlayerDto.setPlayer(playerDto);

            gamePlayerList.add(gamePlayerDto);
        }
        List<ShipDto>shipList=new ArrayList<>();
        for (Ship s:gamePlayer.getShips()) {
          ShipDto shipDto=new ShipDto();
          shipDto.setShipType(s.getType());
          shipDto.setShips(s.getLocations());
          shipList.add(shipDto);

        } List<SalvoDto>salvoList=new ArrayList<>();
        for(GamePlayer gp: game.getGamePlayers()) {
            for (Salvo s : gp.getSalvoes()) {
                SalvoDto salvoDto=new SalvoDto();
                salvoDto.setTurn(s.getTurnNumber());
                salvoDto.setPlayer(gp.getPlayer().getId());
                salvoDto.setLocations(s.getLocation());
                salvoList.add(salvoDto);
            }
        }
        gamePlayerPersonDto.setShip(shipList);
        gamePlayerPersonDto.setGamePlayers(gamePlayerList);
        gamePlayerPersonDto.setSalvoes(salvoList);

        return gamePlayerPersonDto;
    }



    @RequestMapping(value = "/player", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {
              if(email.isEmpty()){
                  return new ResponseEntity<>("No email given", HttpStatus.FORBIDDEN);
              }

        if (playerRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public HttpStatus login(@RequestParam String email, @RequestParam String password) {
        if(email.isEmpty()){
            return HttpStatus.FORBIDDEN;
        }

        Player p = playerRepository.findByEmail(email);

        if (p ==  null) {
            return HttpStatus.FORBIDDEN;
        }

        if(p.getPassword().equals(passwordEncoder.encode(password))){
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }

}