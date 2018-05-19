package com.example.justweather.mvp.models.metrics;

/**
 * Created by Andrei on 08.04.2018.
 */

public class Temperature {

    public int inC() {
        return C;
    }

    public int inF() {
        return F;
    }

    private final int C;
    private final int F;


    public Temperature(double tempK) {
        this.C = (int) (tempK - 273);
        this.F = (C+32)*9/5;
    }
}
