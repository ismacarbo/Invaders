package com.example.invaders.Model;

public class Projectile {
    private double x, y;
    private double dx, dy;  // Direzione del movimento
    private final double speed = 5;

    public Projectile(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void move() {
        this.x += dx * speed;
        this.y += dy * speed;
    }

    public boolean isOutOfBounds(double width, double height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public boolean collidesWith(Enemy enemy) {
        return x >= enemy.getX() && x <= enemy.getX() + 20 && y >= enemy.getY() && y <= enemy.getY() + 20;
    }
}
