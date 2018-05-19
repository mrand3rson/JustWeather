package com.example.justweather.mvp.models.metrics;

/**
 * Created by Andrei on 08.04.2018.
 */

public class Pressure {

    public int inMmHg() {
        return mmHg;
    }

    public int inHPascal() {
        return hPa;
    }

    private int mmHg;
    private int hPa;


    public Pressure(double pascal) {
        this.hPa = (int) pascal;
        this.mmHg = (int) (pascal * 0.75);
    }
}
