package com.brcthn.battleship.persistance.dto;

import java.util.List;

public class ShipDto {

    private String shipType;
    private List<String> locations;


    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
