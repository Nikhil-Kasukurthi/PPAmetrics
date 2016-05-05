package com.example.nikhil.os;

import java.util.ArrayList;

/**
 * Created by nikhil on 3/5/16.
 */
public class ClientInfo {
    private String device;
    private double total;
    private double free;
    private double used;
    private double percent;

    public ClientInfo(String d, double t, double f, double u, double p){
        this.setDevice(d);
        this.setFree(f);
        this.setTotal(t);
        this.setUsed(u);
        this.setPercent(p);
    }
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getFree() {
        return free;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getUsed() {
        return used;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }


}
