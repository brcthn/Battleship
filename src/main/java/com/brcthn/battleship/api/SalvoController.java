package com.brcthn.battleship.api;


import com.brcthn.battleship.persistance.dto.*;
import com.brcthn.battleship.persistance.entity.*;
import com.brcthn.battleship.persistance.repository.GamePlayerRepository;
import com.brcthn.battleship.persistance.repository.GameRepository;
import com.brcthn.battleship.persistance.repository.PlayerRepository;
import com.brcthn.battleship.persistance.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/games")
    public CurrentPlayerDto getAll() {
        List<Game> all = gameRepository.findAll();
        List<GameDto> results = new ArrayList<>();

        CurrentPlayerDto currentPlayerDto = new CurrentPlayerDto();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player loggedUser = currentUser(authentication);
        PlayerDto loggedUserDto = new PlayerDto();
        loggedUserDto.setFirstName(loggedUser.getFirstName());
        loggedUserDto.setLastName(loggedUser.getLastName());
        loggedUserDto.setId(loggedUser.getId());
        loggedUserDto.setEmail(loggedUser.getEmail());

        currentPlayerDto.setPlayer(loggedUserDto);

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

        currentPlayerDto.setGames(results);
        return currentPlayerDto;
    }


    @RequestMapping(value = "/game_view/{nn}", method = RequestMethod.GET)
    public Object getGame(@PathVariable("nn") long gamePlayerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player loggedUser = currentUser(authentication);

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();

        if (loggedUser.getId() != gamePlayer.getPlayer().getId()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

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
        List<ShipDto> shipList = new ArrayList<>();
        for (Ship s : gamePlayer.getShips()) {
            ShipDto shipDto = new ShipDto();
            shipDto.setShipType(s.getType());
            shipDto.setShips(s.getLocations());
            shipList.add(shipDto);

        }
        List<SalvoDto> salvoList = new ArrayList<>();
        for (GamePlayer gp : game.getGamePlayers()) {
            for (Salvo s : gp.getSalvoes()) {
                SalvoDto salvoDto = new SalvoDto();
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

    private Player currentUser(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return playerRepository.findByEmail(authentication.getName());
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {
        if (email.isEmpty()) {
            return new ResponseEntity<>("No email given", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(@RequestParam String username) {
        if (username.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.UNAUTHORIZED);
        }
//game
        Game newGame = new Game();
        Date time = new Date();
        SimpleDateFormat newDate = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String data = newDate.format(time);
        newGame.setCreationData(data);
        gameRepository.save(newGame);
//player(current user)
        Player newPlayer=playerRepository.findByEmail(username);

//gamePlayer
        GamePlayer newGamePlayer = new GamePlayer();
        newGamePlayer.setPlayer(newPlayer);
        newGamePlayer.setGame(newGame);
        playerRepository.save(newPlayer);
        gamePlayerRepository.save(newGamePlayer);
//score
        Score newScore= new Score();
        newScore.setGame(newGame);
        newScore.setPlayer(newPlayer);
        scoreRepository.save(newScore);
         Long id= newGamePlayer.getId();

        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

}