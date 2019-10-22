package com.brcthn.battleship;

import com.brcthn.battleship.persistance.entity.Player;
import com.brcthn.battleship.persistance.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BattleshipApplication {

	public static void main(String[] args) {
		SpringApplication.run(BattleshipApplication.class, args);
	}


@Bean
public CommandLineRunner initData(PlayerRepository repository) {
return (args) ->{
	repository.save(new Player("Jack", "Bauer","JackB"));
	repository.save(new Player("burcu","tahan","brc"));
};
}
}