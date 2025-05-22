package org.UNESPAR.entities;

public class City {

    private final String name;
    private final Double coordX;
    private final Double coordY;


    public City(String name, Double cordX, Double cordY) {
        this.name = name;
        this.coordX = cordX;
        this.coordY = cordY;
    }

    public String getName() {
        return name;
    }

    public Double getCordX() {
        return coordX;
    }

    public Double getCordY() {
        return coordY;
    }



}
