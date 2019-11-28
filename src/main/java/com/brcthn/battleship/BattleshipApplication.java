package com.brcthn.battleship;

import com.brcthn.battleship.persistance.entity.*;
import com.brcthn.battleship.persistance.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class BattleshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleshipApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(GameRepository gameRepository, PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository,ShipRepository shipRepository,SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {
            Player player1 = new Player("Jack", "Bauer", "JackB","j.bauer@ctu.gov ","24");
            playerRepository.save(player1);
            Player player2 = new Player("john", "Nacube", "JohnN","c.obrian@ctu.gov", "42");
            playerRepository.save(player2);
            Player player3 = new Player("Tony ", "Almeida ", "TonyA","t.almeida@ctu.gov","mole");
            playerRepository.save(player3);

         //game1
            Date time = new Date();// su anki zamani new Date() ile aldi. time -> 00:55 23.10.2019 Wed
            SimpleDateFormat newDate=new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String data= newDate.format(time);
            
            Game game1 = new Game();
            game1.setCreationData(data);
            gameRepository.save(game1);

            Score scorePlayer1= new Score();
            scorePlayer1.setWins(1);
            scorePlayer1.setLoses(1);
            scorePlayer1.setTies(1);
            scorePlayer1.setGame(game1);
            scorePlayer1.setPlayer(player1);

            Score scorePlayer2=new Score();
            scorePlayer2.setWins(2);
            scorePlayer2.setLoses(2);
            scorePlayer2.setTies(2);
            scorePlayer2.setGame(game1);
            scorePlayer2.setPlayer(player3);

            scoreRepository.save(scorePlayer1);
            scoreRepository.save(scorePlayer2);


            //game2
            Game game2 = new Game();
            time.setHours(time.getHours() + 1); //time -> 01:55 23.10.2019 Wed
            game2.setCreationData(data);
            gameRepository.save(game2);

            Score scorePlayer3= new Score();
            scorePlayer3.setWins(3);
            scorePlayer3.setLoses(3);
            scorePlayer3.setTies(3);
            scorePlayer3.setGame(game2);
            scorePlayer3.setPlayer(player2);

            Score scorePlayer4= new Score();
            scorePlayer4.setWins(4);
            scorePlayer4.setLoses(4);
            scorePlayer4.setTies(4);
            scorePlayer4.setGame(game2);
            scorePlayer4.setPlayer(player3);

            scoreRepository.save(scorePlayer3);
            scoreRepository.save(scorePlayer4);

            //game3
            Game game3 = new Game();
            time.setHours(time.getHours() + 1); //time -> 02:55 23.10.2019 Wed
            game3.setCreationData(data);
            gameRepository.save(game3);


            // SALVO
            List<String>s1T1=new ArrayList<>();
            s1T1.add("H1");
            s1T1.add("A2");
            List<String>s2T1=new ArrayList<>();
            s2T1.add("C5");
            s2T1.add("E6");

            List<String>s1T2=new ArrayList<>();
            s1T2.add("B4");
            s1T2.add("D8");
            List<String>s2T2=new ArrayList<>();
            s2T2.add("C7");
            s2T2.add("H3");

            List<String>s3T1=new ArrayList<>();
            s3T1.add("A5");
            s3T1.add("H3");
            List<String>s3T2=new ArrayList<>();
            s3T2.add("A9");
            s3T2.add("C10");

            List<String>s4T1=new ArrayList<>();
            s4T1.add("H2");
            s4T1.add("E8");
            List<String>s4T2=new ArrayList<>();
            s4T2.add("A7");
            s4T2.add("F8");

            Salvo s1T1L1=new Salvo(1,s1T1);
            Salvo s2T1L1 =new Salvo(1,s2T1);
            Salvo s1T2L2=new Salvo(2,s1T2);
            Salvo s2T2L2=new Salvo(2,s2T2);
            Salvo s3T1L3=new Salvo(1,s3T1);
            Salvo s3T2L3=new Salvo(1,s3T2);
            Salvo s4T1L4=new Salvo(1,s4T1);
            Salvo s4T2L4=new Salvo(1,s4T2);

            //Ship
            List<String>locationShip1=new ArrayList<>();
            locationShip1.add("H3");
            locationShip1.add("H4");
            locationShip1.add("H5");
            List<String>locationShip2=new ArrayList<>();
             locationShip2.add("C6");
             locationShip2.add("D6");
             List<String>locationShip3=new ArrayList<>();
             locationShip3.add("E3");
             locationShip3.add("E4");
             List<String>locationShip4=new ArrayList<>();
             locationShip4.add("G6");
             locationShip4.add("H6");
            List<String>locationShip5=new ArrayList<>();
            locationShip4.add("E8");
            locationShip4.add("E9");



            game1 = gameRepository.findById(1L).get();
            player1 = playerRepository.findById(1L).get();
            //gp
            GamePlayer gp = new GamePlayer();
            gp.setGame(game1);
            gp.setPlayer(player1);
            Ship battleship=new Ship("battleship",locationShip1);
            Ship carrier=new Ship("carrier",locationShip2);
            gp.add(carrier);
            gp.add(battleship);
            gp.addSalvo(s1T1L1);
            gp.addSalvo(s1T2L2);


            gamePlayerRepository.save(gp);
            shipRepository.save(carrier);
            shipRepository.save(battleship);
            salvoRepository.save(s1T1L1);
            salvoRepository.save(s1T2L2);

            //gp1
            game2 = gameRepository.findById(2L).get();
            player2 = playerRepository.findById(2L).get();

            GamePlayer gp1 = new GamePlayer();
            gp1.setGame(game2);
            gp1.setPlayer(player2);
            Ship cruiser1 = new Ship("cruiser",locationShip1 );
            gp1.add(cruiser1);

            gp1.addSalvo(s2T1L1);
            gp1.addSalvo(s2T2L2);
            gamePlayerRepository.save(gp1);

            shipRepository.save(cruiser1);
            salvoRepository.save(s2T2L2);
            salvoRepository.save(s2T1L1);



           game1=gameRepository.findById(1L).get();
           player3=playerRepository.findById(3L).get();



            //gp3
            GamePlayer gp3=new GamePlayer();
            gp3.setGame(game1);
            gp3.setPlayer(player3);
            Ship destroyer=new Ship("destroyer",locationShip4);
            gp3.add(destroyer);

            gp3.addSalvo(s3T1L3);
            gp3.addSalvo(s3T2L3);
            gamePlayerRepository.save(gp3);
            shipRepository.save(destroyer);
            salvoRepository.save(s3T1L3);
            salvoRepository.save(s3T2L3);

            game2=gameRepository.findById(2L).get();
            player3=playerRepository.findById(3L).get();
            //gp4
            GamePlayer gp4=new GamePlayer();
            gp4.setGame(game2);
            gp4.setPlayer(player3);
            Ship destroyer1=new Ship("destroyer1",locationShip5);
            gp4.add(destroyer1);

            gp4.addSalvo(s4T1L4);
            gp4.addSalvo(s4T2L4);
            gamePlayerRepository.save(gp4);
            shipRepository.save(destroyer);
            salvoRepository.save(s4T1L4);
            salvoRepository.save(s4T2L4);

            System.out.println("carrier game player " + carrier.getGamePlayer());
            System.out.println("game player ship size " + gp.getShips().size());
        };
    }
}