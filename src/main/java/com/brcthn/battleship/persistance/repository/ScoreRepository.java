package com.brcthn.battleship.persistance.repository;

import com.brcthn.battleship.persistance.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score,Long> {
}


//player game one to many
// player gameplayer one to one