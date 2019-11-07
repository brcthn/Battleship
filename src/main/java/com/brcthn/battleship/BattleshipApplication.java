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





            List<String>locationShip=new ArrayList<>();
            locationShip.add("'H3', 'H4' , 'H5'");

            Ship cruiser = new Ship("cruiser",locationShip );
            shipRepository.save(cruiser);
            Ship destroyer=new Ship("destroyer",locationShip);
            shipRepository.save(destroyer);
            Ship battleship=new Ship(" battleship",locationShip);
            shipRepository.save(battleship);
            Ship carrier=new Ship("carrier",locationShip);
            shipRepository.save(carrier);
            Ship submarine=new Ship("submarine",locationShip);
            shipRepository.save(submarine);

            List<Ship> addShip=new ArrayList<>();
            addShip.add(cruiser);
            addShip.add(destroyer);
            addShip.add(battleship);
            addShip.add(carrier);
            addShip.add(submarine);

            GamePlayer gp = new GamePlayer();
            gp.setGame(game2);
            gp.setPlayer(player1);
            gp.setShips(addShip);
            gamePlayerRepository.save(gp);
            GamePlayer gp1 = new GamePlayer();
            gp1.setGame(game2);
            gp1.setPlayer(player2);
            gp1.setShips(addShip);
            gamePlayerRepository.save(gp1);
            GamePlayer gp2 = new GamePlayer();
            gp2.setGame(game1);
            gp2.setPlayer(player3);
            gp2.setShips(addShip);
            gamePlayerRepository.save(gp2);
            GamePlayer gp3 = new GamePlayer();
            gp3.setGame(game3);
            gp3.setPlayer(player3);
            gp3.setShips(addShip);
            gamePlayerRepository.save(gp3);








            //gp.add(cruiser);







        };
    }
}
