package org.vaadin.example;

import java.util.UUID;

public class House {
    private String id;
    private String address;
    private String owner;
    private double value;
    private int squareMetres;

    public House() {
        this.id = UUID.randomUUID().toString(); // Asignar ID único
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSquareMetres() {
        return squareMetres;
    }

    public void setSquareMetres(int squareMetres) {
        this.squareMetres = squareMetres;
    }
}

