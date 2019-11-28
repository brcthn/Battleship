package com.brcthn.battleship.persistance.dto;

import java.util.List;

public class ShipDto {

    private String shipType;
    private List<String> ships;


    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getShips() {
        return ships;
    }

    public void setShips(List<String> ships) {
        this.ships = ships;
    }
}
