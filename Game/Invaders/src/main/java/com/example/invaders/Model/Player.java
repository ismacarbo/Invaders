package com.example.invaders.Model;

public class Player {
    private double x, y;
    private double dx, dy;  // Direzione del movimento

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        this.dx = dx;
        this.dy = dy;
    }
}
