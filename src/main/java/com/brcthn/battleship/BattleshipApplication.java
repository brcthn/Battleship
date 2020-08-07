package com.brcthn.battleship;

import com.brcthn.battleship.persistance.entity.*;
import com.brcthn.battleship.persistance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class BattleshipApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(BattleshipApplication.class, args);
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BattleshipApplication.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Autowired
    PasswordEncoder passwordEncoder;


    @Bean
    public CommandLineRunner initData(GameRepository gameRepository, PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {
            Player player1 = new Player(  "John", "Doe", "john@doe.com",passwordEncoder.encode("11"));
            playerRepository.save(player1);
            Player player2 = new Player( "Jane", "Doe", "jane@doe.com", passwordEncoder.encode("22"));
            playerRepository.save(player2);
//            Player player3 = new Player( "t.almeida@ctu.gov",passwordEncoder.encode( "mole") );
//            player3.setFirstName("Tony"); player3.setLastName("Almeida"); player3.setUserName("TonyA");
//            playerRepository.save(player3);
//            Player player4 = new Player( "kim_bauer@gmail.com", passwordEncoder.encode("kb"));
//            player4.setFirstName("Kim"); player4.setLastName("Bauer"); player4.setUserName("KimB");
//            playerRepository.save(player4);
//
//            //game1
//            Date time = new Date();// su anki zamani new Date() ile aldi. time -> 00:55 23.10.2019 Wed
//            SimpleDateFormat newDate = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//            String data = newDate.format(time);
//
//            Game game1 = new Game();
//            game1.setCreationData(data);
//            gameRepository.save(game1);
//
//            Score scorePlayer1 = new Score();
//            scorePlayer1.setWins(1);
//            scorePlayer1.setLoses(1);
//            scorePlayer1.setTies(1);
//            scorePlayer1.setGame(game1);
//            scorePlayer1.setPlayer(player1);
//
//            Score scorePlayer2 = new Score();
//            scorePlayer2.setWins(2);
//            scorePlayer2.setLoses(2);
//            scorePlayer2.setTies(2);
//            scorePlayer2.setGame(game1);
//            scorePlayer2.setPlayer(player3);
//
//            scoreRepository.save(scorePlayer1);
//            scoreRepository.save(scorePlayer2);
//
//
//            //game2
//            Game game2 = new Game();
//            time.setHours(time.getHours() + 1); //time -> 01:55 23.10.2019 Wed
//            game2.setCreationData(data);
//            gameRepository.save(game2);
//
//            Score scorePlayer3 = new Score();
//            scorePlayer3.setWins(3);
//            scorePlayer3.setLoses(3);
//            scorePlayer3.setTies(3);
//            scorePlayer3.setGame(game2);
//            scorePlayer3.setPlayer(player2);
//
//            Score scorePlayer4 = new Score();
//            scorePlayer4.setWins(4);
//            scorePlayer4.setLoses(4);
//            scorePlayer4.setTies(4);
//            scorePlayer4.setGame(game2);
//            scorePlayer4.setPlayer(player3);
//
//            scoreRepository.save(scorePlayer3);
//            scoreRepository.save(scorePlayer4);
//
//            //game3
//            Game game3 = new Game();
//            time.setHours(time.getHours() + 1); //time -> 02:55 23.10.2019 Wed
//            game3.setCreationData(data);
//            gameRepository.save(game3);
//
//            Score scorePlayer5 = new Score();
//            scorePlayer5.setWins(0);
//            scorePlayer5.setLoses(0);
//            scorePlayer5.setTies(0);
//            scorePlayer5.setGame(game3);
//            scorePlayer5.setPlayer(player4);
//            scoreRepository.save(scorePlayer5);
//
//
//            // SALVO
//            List<String> s1T1 = new ArrayList<>();
//            s1T1.add("H1");
//            s1T1.add("A2");
//            List<String> s2T1 = new ArrayList<>();
//            s2T1.add("C5");
//            s2T1.add("E6");
//
//            List<String> s1T2 = new ArrayList<>();
//            s1T2.add("B4");
//            s1T2.add("D8");
//            List<String> s2T2 = new ArrayList<>();
//            s2T2.add("C7");
//            s2T2.add("H3");
//
//            List<String> s3T1 = new ArrayList<>();
//            s3T1.add("A5");
//            s3T1.add("H3");
//            List<String> s3T2 = new ArrayList<>();
//            s3T2.add("A9");
//            s3T2.add("C10");
//
//            List<String> s4T1 = new ArrayList<>();
//            s4T1.add("H2");
//            s4T1.add("E8");
//            List<String> s4T2 = new ArrayList<>();
//            s4T2.add("A7");
//            s4T2.add("F8");
//
//            Salvo s1T1L1 = new Salvo(1, s1T1);
//            Salvo s2T1L1 = new Salvo(1, s2T1);
//            Salvo s1T2L2 = new Salvo(2, s1T2);
//            Salvo s2T2L2 = new Salvo(2, s2T2);
//            Salvo s3T1L3 = new Salvo(1, s3T1);
//            Salvo s3T2L3 = new Salvo(1, s3T2);
//            Salvo s4T1L4 = new Salvo(1, s4T1);
//            Salvo s4T2L4 = new Salvo(1, s4T2);
//
//            //Ship
//            List<String> locationShip1 = new ArrayList<>();
//            locationShip1.add("H3");
//            locationShip1.add("H4");
//            locationShip1.add("H5");
//            List<String> locationShip2 = new ArrayList<>();
//            locationShip2.add("C6");
//            locationShip2.add("D6");
//            List<String> locationShip3 = new ArrayList<>();
//            locationShip3.add("E3");
//            locationShip3.add("E4");
//            List<String> locationShip4 = new ArrayList<>();
//            locationShip4.add("G6");
//            locationShip4.add("H6");
//            List<String> locationShip5 = new ArrayList<>();
//            locationShip5.add("E8");
//            locationShip5.add("E9");
//            List<String> locationShip6 = new ArrayList<>();
//            locationShip6.add("F8");
//            locationShip6.add("F9");
//
//            game1 = gameRepository.findById(1L).get();
//            player1 = playerRepository.findById(1L).get();
//            //gp
//            GamePlayer gp = new GamePlayer();
//            gp.setGame(game1);
//            gp.setPlayer(player1);
//            Ship battleship = new Ship("battleship", locationShip1);
//            Ship carrier = new Ship("carrier", locationShip2);
//            gp.add(carrier);
//            gp.add(battleship);
//            gp.addSalvo(s1T1L1);
//            gp.addSalvo(s1T2L2);
//
//
//            gamePlayerRepository.save(gp);
//            shipRepository.save(carrier);
//            shipRepository.save(battleship);
//            salvoRepository.save(s1T1L1);
//            salvoRepository.save(s1T2L2);
//
//            //gp1
//            game2 = gameRepository.findById(2L).get();
//            player2 = playerRepository.findById(2L).get();
//
//            GamePlayer gp1 = new GamePlayer();
//            gp1.setGame(game2);
//            gp1.setPlayer(player2);
//            Ship cruiser1 = new Ship("cruiser", locationShip1);
//            gp1.add(cruiser1);
//
//            gp1.addSalvo(s2T1L1);
//            gp1.addSalvo(s2T2L2);
//            gamePlayerRepository.save(gp1);
//
//            shipRepository.save(cruiser1);
//            salvoRepository.save(s2T2L2);
//            salvoRepository.save(s2T1L1);
//
//
//            game1 = gameRepository.findById(1L).get();
//            player3 = playerRepository.findById(3L).get();
//
//
//            //gp3
//            GamePlayer gp3 = new GamePlayer();
//            gp3.setGame(game1);
//            gp3.setPlayer(player3);
//            Ship destroyer = new Ship("destroyer", locationShip4);
//            gp3.add(destroyer);
//
//            gp3.addSalvo(s3T1L3);
//            gp3.addSalvo(s3T2L3);
//            gamePlayerRepository.save(gp3);
//            shipRepository.save(destroyer);
//            salvoRepository.save(s3T1L3);
//            salvoRepository.save(s3T2L3);
//
//            game2 = gameRepository.findById(2L).get();
//            player3 = playerRepository.findById(3L).get();
//            //gp4
//            GamePlayer gp4 = new GamePlayer();
//            gp4.setGame(game2);
//            gp4.setPlayer(player3);
//            Ship destroyer1 = new Ship("destroyer1", locationShip5);
//            gp4.add(destroyer1);
//
//            gp4.addSalvo(s4T1L4);
//            gp4.addSalvo(s4T2L4);
//            gamePlayerRepository.save(gp4);
//            shipRepository.save(destroyer);
//            salvoRepository.save(s4T1L4);
//            salvoRepository.save(s4T2L4);
//
//            //gp5
//
//            GamePlayer gp5 = new GamePlayer();
//            gp5.setGame(game3);
//            gp5.setPlayer(player4);
//            Ship patrolboat = new Ship("patrolboat", locationShip6);
//            gp5.add(patrolboat);
//
//            gamePlayerRepository.save(gp5);
//            shipRepository.save(patrolboat);
//
//
//            System.out.println("carrier game player " + carrier.getGamePlayer());
//            System.out.println("game player ship size " + gp.getShips().size());
        };
    }
}


 @Configuration
  class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> {
            System.out.println(email);
            Player player = playerRepository.findByEmail(email);
            if (player != null) {
                return new User(player.getEmail(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + email);
            }
        });
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //turn off checking for CSRF tokens
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/web/games.js").permitAll()
                .antMatchers("/web/game.css").permitAll()
                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/web/game.html").permitAll()
                .antMatchers("/web/game.js").permitAll()
                .antMatchers("/web/game.css").permitAll()
                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/web/Destroyer.png").permitAll()
                .antMatchers("/web/Intro.mov").permitAll()
                .antMatchers("/web/main.js").permitAll()
                .antMatchers("/api/signup*").permitAll()
                .antMatchers("/api/login*").permitAll()
                .antMatchers("/api/games").hasAnyAuthority("USER")
                .antMatchers("/api/leader_board").hasAnyAuthority("USER")
                .antMatchers("/api/signup").hasAnyAuthority("USER")
                .antMatchers("/api/game_view/*").hasAnyAuthority("USER")
                .antMatchers("/rest/*").permitAll()

                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
                 http.logout().logoutUrl("/api/logout");

        //if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        //if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((request, response, authentication) -> clearAuthenticationAttribute(request));

        //if login fails, just send an authentication failure response
        http.formLogin().failureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        //if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttribute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}