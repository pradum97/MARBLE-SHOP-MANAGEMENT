package com.example.jasperrepoprt;

public class Pk {

    String name;
    int mrp;

    public Pk(String name, int mrp) {
        this.name = name;
        this.mrp = mrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }
}
