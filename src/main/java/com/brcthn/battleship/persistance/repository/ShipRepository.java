package com.brcthn.battleship.persistance.repository;


import com.brcthn.battleship.persistance.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

  @RepositoryRestResource
  public interface ShipRepository extends JpaRepository<Ship, Long> {

 }
