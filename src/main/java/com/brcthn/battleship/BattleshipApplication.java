package com.brcthn.battleship;

import com.brcthn.battleship.persistance.entity.Game;
import com.brcthn.battleship.persistance.entity.GamePlayer;
import com.brcthn.battleship.persistance.entity.Player;
import com.brcthn.battleship.persistance.entity.Ship;
import com.brcthn.battleship.persistance.repository.GamePlayerRepository;
import com.brcthn.battleship.persistance.repository.GameRepository;
import com.brcthn.battleship.persistance.repository.PlayerRepository;
import com.brcthn.battleship.persistance.repository.ShipRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BattleshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleshipApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData(GameRepository gameRepository, PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository,ShipRepository shipRepository) {
        return (args) -> {
            Player player1 = new Player("Jack", "Bauer", "JackB","j.bauer@ctu.gov ","1234");
            playerRepository.save(player1);
            Player player2 = new Player("john", "Nacube", "JohnN","c.obrian@ctu.gov", "123456");
            playerRepository.save(player2);
            Player player3 = new Player("Luis", "Strosbery", "LuisS","l.strosberyn@ctu.gov","1234567");
            playerRepository.save(player3);


        Date time = new Date();// su anki zamani new Date() ile aldi. time -> 00:55 23.10.2019 Wed
            Game game1 = new Game();
            game1.setCreationData(time);
            gameRepository.save(game1);

            Game game2 = new Game();
            time.setHours(time.getHours() + 1); //time -> 01:55 23.10.2019 Wed
            game2.setCreationData(time);
            gameRepository.save(game2);


            Game game3 = new Game();
            time.setHours(time.getHours() + 1); //time -> 02:55 23.10.2019 Wed
            game3.setCreationData(time);
            gameRepository.save(game3);


            List<String>locationShip1=new ArrayList<>();
            locationShip1.add("H3");
            locationShip1.add("H4");
            locationShip1.add("H5");
            List<String>locationShip2=new ArrayList<>();
             locationShip2.add("C6");
             locationShip2.add("D6");

            GamePlayer gp = new GamePlayer();
            gp.setGame(game2);
            gp.setPlayer(player1);
            Ship battleship=new Ship(" battleship",locationShip1);
            Ship carrier=new Ship("carrier",locationShip2);
            gp.add(carrier);
            gp.add(battleship);
            gamePlayerRepository.save(gp);
            shipRepository.save(carrier);
            shipRepository.save(battleship);

            GamePlayer gp1 = new GamePlayer();
            gp1.setGame(game2);
            gp1.setPlayer(player2);
            Ship cruiser1 = new Ship("cruiser",locationShip1 );
            gp1.add(cruiser1);
            gamePlayerRepository.save(gp1);
            shipRepository.save(cruiser1);

            GamePlayer gp2 = new GamePlayer();
            gp2.setGame(game1);
            gp2.setPlayer(player3);
            Ship cruiser2 = new Ship("cruiser",locationShip1 );
            Ship carrier2 = new Ship("carrier",locationShip2);
            gp2.add(cruiser2);
            gp2.add(carrier2);
            gamePlayerRepository.save(gp2);
            shipRepository.save(cruiser2);
            shipRepository.save(carrier2);

            System.out.println("carrier game player " + carrier.getGamePlayer());
            System.out.println("game player ship size " + gp.getShips().size());
        };
    }
}
