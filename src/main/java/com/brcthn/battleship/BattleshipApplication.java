package com.brcthn.battleship;

import com.brcthn.battleship.persistance.entity.Game;
import com.brcthn.battleship.persistance.entity.GamePlayer;
import com.brcthn.battleship.persistance.entity.Player;
import com.brcthn.battleship.persistance.repository.GamePlayerRepository;
import com.brcthn.battleship.persistance.repository.GameRepository;
import com.brcthn.battleship.persistance.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BattleshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleshipApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData(GameRepository gameRepository, PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository) {
        return (args) -> {
            Player player1 = new Player("Jack", "Bauer", "JackB","j.bauer@ctu.gov ");
            playerRepository.save(player1);
            Player player2 = new Player("john", "Nacube", "JohnN","c.obrian@ctu.gov");
            playerRepository.save(player2);

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

            GamePlayer gp = new GamePlayer();
            gp.setGame(game2);
            gp.setPlayer(player1);
            gamePlayerRepository.save(gp);

            GamePlayer gp1 = new GamePlayer();
            gp1.setGame(game2);
            gp1.setPlayer(player2);
            gamePlayerRepository.save(gp1);



        };
    }
}
