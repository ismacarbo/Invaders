package com.example.invaders.Model;

public class Enemy {
    private double x, y;

    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void moveTowards(Player player) {
        double dx = player.getX() - this.x;
        double dy = player.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double moveX = (dx / distance) * 2; // Velocità del nemico
        double moveY = (dy / distance) * 2;
        this.x += moveX;
        this.y += moveY;
    }
}
