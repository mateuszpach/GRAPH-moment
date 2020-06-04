package com.github.mtdrewski.GRAPH_moment.model.utils;

public class Coords {

    private double x;
    private double y;

    public Coords(double xx,double yy){
        x=xx;
        y=yy;
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Coords subtract(Coords c){
        return new Coords(x-c.getX(), y - c.getY());
    }

    public Coords add(Coords c){
        return new Coords(x-c.getX(), y - c.getY());
    }

    public Coords unit(){
        if(length() == 0)
            return new Coords(0.000001,0.0000001);
        else
            return new Coords(x/Math.sqrt(x*x + y*y),y/Math.sqrt(y*y + x*x));
    }

    public double length(){
        return Math.sqrt(x*x + y*y);
    }

    public double distance(Coords c){
        return Math.sqrt((x-c.getX())*(x-c.getX()) + (y-c.getY())*(y-c.getY()));
    }

    public Coords scale(float k){
        return new Coords(k*x,k*y);
    }
}
