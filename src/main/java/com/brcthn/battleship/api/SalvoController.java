package com.brcthn.battleship.api;


import com.brcthn.battleship.persistance.dto.*;
import com.brcthn.battleship.persistance.entity.*;
import com.brcthn.battleship.persistance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

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

        GamePlayer opponentGamePlayer = null;
        GamePlayer currentGamePlayer = null;

        for (GamePlayer gp : game.getGamePlayers()) {

            //apponent player find
            if (loggedUser.getId() != gp.getPlayer().getId()) {
                opponentGamePlayer = gp;
            } else {
                currentGamePlayer = gp;
            }


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
            shipDto.setLocations(s.getLocations());
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

        List<HistoryDto> history = prepareHistory(gamePlayer, opponentGamePlayer);

        if(history !=  null){
            gamePlayerPersonDto.setHistory(history);
            if(!gamePlayer.isFinished() || !opponentGamePlayer.isFinished()){
                String state = state(currentGamePlayer, opponentGamePlayer, history);
                gamePlayerPersonDto.setState(state);
            }

        }
        return gamePlayerPersonDto;
    }

    private String state(GamePlayer currentGamePlayer, GamePlayer opponentGamePlayer, List<HistoryDto> history) {
        if (currentGamePlayer.getShips().isEmpty()) {
            return "Place Ship";
        }
        if (currentGamePlayer.getShips().size() > opponentGamePlayer.getShips().size()) {
            return "Wait";
        }
        if (currentGamePlayer.getSalvoes().isEmpty()) {
            return "Enter Salvo";
        }
        int mySum = scoreRepository.findById(currentGamePlayer.getScore().getId()).get().getSumHit();
        int opponentSum = scoreRepository.findById(opponentGamePlayer.getScore().getId()).get().getSumHit();
        if(mySum == 17 && opponentSum == 17){
            Optional<Score> myScore = scoreRepository.findById(currentGamePlayer.getScore().getId());
            myScore.get().setLoses(myScore.get().getLoses()-1);
            myScore.get().setTies(myScore.get().getTies()+1);
            scoreRepository.save(myScore.get());

            Optional<Score> opponentScore = scoreRepository.findById(opponentGamePlayer.getScore().getId());
            opponentScore.get().setWins(opponentScore.get().getWins()-1);
            opponentScore.get().setTies(opponentScore.get().getTies()+1);
            scoreRepository.save(opponentScore.get());
        } else if (mySum == 17) {
            //current wins
            Optional<Score> myScore = scoreRepository.findById(currentGamePlayer.getScore().getId());
            if (myScore.isPresent()) {
                myScore.get().setWins(myScore.get().getWins() + 1);
                scoreRepository.save(myScore.get());
            } else {
                Score myNewScore = new Score();
                myNewScore.setWins(1);
                myNewScore.setGame(currentGamePlayer.getGame());
                myNewScore.setPlayer(currentGamePlayer.getPlayer());
                scoreRepository.save(myNewScore);
            }

            //opponent looses
            Optional<Score> opponentScore = scoreRepository.findById(opponentGamePlayer.getScore().getId());
            if (opponentScore.isPresent()) {
                opponentScore.get().setLoses(myScore.get().getLoses() + 1);
                scoreRepository.save(opponentScore.get());
            } else {
                Score opponentNewScore = new Score();
                opponentNewScore.setLoses(1);
                opponentNewScore.setGame(opponentGamePlayer.getGame());
                opponentNewScore.setPlayer(opponentGamePlayer.getPlayer());
                scoreRepository.save(opponentNewScore);
            }

            currentGamePlayer.setFinished(true);
            gamePlayerRepository.save(currentGamePlayer);
            return "Game Over";
        }

        if(mySum == 17 || opponentSum == 17){
            currentGamePlayer.setFinished(true);
            gamePlayerRepository.save(currentGamePlayer);
            opponentGamePlayer.setFinished(true);
            gamePlayerRepository.save(opponentGamePlayer);
            return "Game Over";
        }

        if (currentGamePlayer.getSalvoes().size() > opponentGamePlayer.getSalvoes().size()) {
            return "Wait";
        }
        if (currentGamePlayer.getSalvoes().size() <= opponentGamePlayer.getSalvoes().size()) {
            return "Enter Salvo";
        }


        return null;
    }

    private List<HistoryDto> prepareHistory(GamePlayer gamePlayer, GamePlayer opponentGamePlayer) {
        if (opponentGamePlayer == null) {
           return null;
        }
        List<HistoryDto> history = new ArrayList<>();
        Map<String, Integer> sink = new HashMap<>();
        /**
         *  unique gemi ismi,   toplam vurulma sayisi
         *  destroyer, 2
         */

        int sumHit = 0;

        for (Ship ship : opponentGamePlayer.getShips()) {
            for (String location : ship.getLocations()) {
                if (gamePlayer != null) {
                    for (Salvo salvo : gamePlayer.getSalvoes()) {
                        if (salvo.getLocation().contains(location)) {
                            HistoryDto historyShip = contains(history, ship);
                            if (historyShip == null || historyShip.getTurn() != salvo.getTurnNumber()) {


                                //sink
                                if (!sink.containsKey(ship.getType())) {
                                    sink.put(ship.getType(), 1);
                                } else {
                                    sink.put(ship.getType(), sink.get(ship.getType()) + 1);
                                }

                                HistoryDto historyDto = new HistoryDto();
                                historyDto.setType(ship.getType());
                                historyDto.setTurn(salvo.getTurnNumber());

                                historyDto.addHitLocation(location);

                                if (historyDto.getHit() == null) {
                                    historyDto.setHit(0);
                                }
                                historyDto.setHit(historyDto.getHit() + 1);
                                sumHit++;
                                if (sink.get(ship.getType()) == ship.getLocations().size()) {
                                    historyDto.setSink(true);
                                }

                                historyDto.setLeft(ship.getLocations().size() - sink.get(ship.getType()));

                                history.add(historyDto);

                            } else {
                                historyShip.addHitLocation(location);
                                historyShip.setHit(historyShip.getHit() + 1);
                                sumHit++;
                                //sink // if ve else de ayni seyi yapiyor.
                                if (!sink.containsKey(historyShip.getType())) {
                                    sink.put(historyShip.getType(), historyShip.getHit());
                                } else {
                                    sink.put(historyShip.getType(), sink.get(historyShip.getType()) + 1);
                                }

                                if (sink.get(historyShip.getType()) == ship.getLocations().size()) {
                                    historyShip.setSink(true);
                                }
                                historyShip.setLeft(ship.getLocations().size() - sink.get(ship.getType()));
                            }
                        }
                    }
                }
            }
        }

        Optional<Score> s = scoreRepository.findById(gamePlayer.getScore().getId());

        if(s.isPresent()) {
            s.get().setSumHit(sumHit);
            scoreRepository.save(s.get());
        } else {
            Score newScore = new Score();
            newScore.setGame(gamePlayer.getGame());
            newScore.setPlayer(gamePlayer.getPlayer());
            newScore.setSumHit(sumHit);
            scoreRepository.save(newScore);
        }

        return history;
    }

    private HistoryDto contains(List<HistoryDto> history, Ship ship) {
        HistoryDto currentHistory = null;
        for (HistoryDto h : history) {
            if (h.getType().equals(ship.getType())) {
                currentHistory = h;
            }
        }
        return currentHistory;
    }

    private Player currentUser(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return playerRepository.findByEmail(authentication.getName());
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {
        if (email.isEmpty()) {
            return new ResponseEntity<>("No email given", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(@RequestParam String username) {
        if (username.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.UNAUTHORIZED);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player loggedUser = currentUser(authentication);

        if (loggedUser.getEmail().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

//game
        Game newGame = new Game();
        Date time = new Date();
        SimpleDateFormat newDate = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String data = newDate.format(time);
        newGame.setCreationData(data);
        gameRepository.save(newGame);
//player(current user)
        Player newPlayer = playerRepository.findByEmail(username);

//gamePlayer
        GamePlayer newGamePlayer = new GamePlayer();
        newGamePlayer.setPlayer(newPlayer);
        newGamePlayer.setGame(newGame);
        playerRepository.save(newPlayer);
        gamePlayerRepository.save(newGamePlayer);
//score
        Score newScore = new Score();
        newScore.setGame(newGame);
        newScore.setPlayer(newPlayer);
        scoreRepository.save(newScore);
        Long id = newGamePlayer.getId();

        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(@PathVariable("nn") long gameId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player loggedUser = currentUser(authentication);

        if (loggedUser.getEmail().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Game currentGame = gameRepository.findById(gameId).get();
        if (currentGame == null) {
            return new ResponseEntity<>("No such game ", HttpStatus.FORBIDDEN);
        }

//        if (currentGame.getGamePlayers().size() == 2) {
//            return new ResponseEntity<>("Game is full ", HttpStatus.FORBIDDEN);
//        }
        //gamePlayer
        GamePlayer newGamePlayer = new GamePlayer();
        newGamePlayer.setPlayer(loggedUser);
        newGamePlayer.setGame(currentGame);
        gamePlayerRepository.save(newGamePlayer);
        //score
        Score newScore = new Score();
        newScore.setGame(currentGame);
        newScore.setPlayer(loggedUser);
        scoreRepository.save(newScore);
        Long id = newGamePlayer.getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);

    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Object> createShip(@PathVariable("gamePlayerId") long gamePlayerId, @RequestBody List<ShipDto> shipList) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player userLogged = currentUser(authentication);
        if (userLogged.getEmail().isEmpty()) {
            return new ResponseEntity<>("There is no current user logged in", HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (gamePlayer == null) {
            return new ResponseEntity<>("There is no game player with the given ID", HttpStatus.UNAUTHORIZED);
        }
        if (userLogged.getId() != gamePlayer.getPlayer().getId()) {
            return new ResponseEntity<>("The current user is not the game player the ID references", HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getShips() != null && gamePlayer.getShips().size() > 0) {
            return new ResponseEntity<>("The user already has ships placed", HttpStatus.FORBIDDEN);
        }

        for (ShipDto shipDto : shipList) {
            Ship ship = new Ship(shipDto.getShipType(), shipDto.getLocations());
            shipRepository.save(ship);
            gamePlayer.add(ship);
        }
        gamePlayerRepository.save(gamePlayer);

        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Object> createSalvo(@PathVariable("gamePlayerId") long gamePlayerId, @RequestBody SalvoDto salvoList) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player userLogged = currentUser(authentication);
        if (userLogged.getEmail().isEmpty()) {
            return new ResponseEntity<>("There is no current user logged in", HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (gamePlayer == null) {
            return new ResponseEntity<>("There is no game player with the given ID", HttpStatus.UNAUTHORIZED);
        }
        if (userLogged.getId() != gamePlayer.getPlayer().getId()) {
            return new ResponseEntity<>("The current user is not the game player the ID references", HttpStatus.UNAUTHORIZED);
        }

        for (Salvo s : gamePlayer.getSalvoes()) {
            if (s.getTurnNumber() == salvoList.getTurn()) {
                return new ResponseEntity<>("The user already has salvos placed", HttpStatus.FORBIDDEN);
            }
        }

        Salvo salvo = new Salvo(salvoList.getTurn(), salvoList.getLocations());
        salvoRepository.save(salvo);
        gamePlayer.addSalvo(salvo);
        gamePlayerRepository.save(gamePlayer);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }
}
