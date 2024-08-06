package com.example.invaders.Controller;

import View.GameView;
import com.example.invaders.Model.Enemy;
import com.example.invaders.Model.Obstacle;
import com.example.invaders.Model.Player;
import com.example.invaders.Model.Projectile;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameController {
    private Player player;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<Projectile> projectiles;
    private GameView view;
    private boolean left, right, up, down, shoot;
    private Random random = new Random();

    public GameController(GameView view) {
        this.view = view;
        this.player = new Player(400, 300);
        this.enemies = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        initializeObstacles();
        startDataReceiver();
        startGameLoop();
        startEnemySpawner();
    }

    private void initializeObstacles() {
        obstacles.add(new Obstacle(200, 150, 100, 50));
        obstacles.add(new Obstacle(400, 300, 150, 75));
        obstacles.add(new Obstacle(600, 450, 120, 60));
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePlayerPosition();
                updateEnemies();
                updateProjectiles();
                handleCollisions();
                view.render(player, enemies, obstacles, projectiles);
            }
        }.start();
    }

    private void updatePlayerPosition() {
        if (left) player.move(-5, 0);
        if (right) player.move(5, 0);
        if (up) player.move(0, -5);
        if (down) player.move(0, 5);
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.moveTowards(player);
        }
    }

    private void updateProjectiles() {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.move();
            if (projectile.isOutOfBounds(view.getCanvas().getWidth(), view.getCanvas().getHeight())) {
                iterator.remove();
            }
        }
    }

    private void handleCollisions() {
        Iterator<Projectile> projIterator = projectiles.iterator();
        while (projIterator.hasNext()) {
            Projectile projectile = projIterator.next();
            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if (projectile.collidesWith(enemy)) {
                    enemyIterator.remove();
                    projIterator.remove();
                    break;
                }
            }
        }
    }

    private void startDataReceiver() {
        new Thread(() -> {
            try (Socket socket = new Socket("192.168.1.115", 80);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                socket.setSoTimeout(5000);  // Imposta un timeout del socket a 5 secondi
                System.out.println("Connected to ESP8266 server");

                char[] buffer = new char[256];
                int bytesRead;

                while ((bytesRead = reader.read(buffer)) != -1) {
                    String data = new String(buffer, 0, bytesRead);
                    System.out.println("Received data: " + data);  // Log dei dati ricevuti

                    String[] values = data.trim().split(",");
                    if (values.length >= 7) {
                        try {
                            int ax = Integer.parseInt(values[0].isEmpty() ? "0" : values[0]);
                            int ay = Integer.parseInt(values[1].isEmpty() ? "0" : values[1]);
                            boolean newShoot = values[6].equals("1");

                            player.move(ay / 1000.0, ax / 1000.0);  // Invertito

                            if (newShoot && !shoot) {  // Rileva un nuovo sparo
                                projectiles.add(new Projectile(player.getX() + 10, player.getY(), player.getDx(), player.getDy()));
                            }

                            shoot = newShoot;
                        } catch (NumberFormatException e) {
                            System.out.println("Number format exception: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startEnemySpawner() {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(3000);  // Attendi 3 secondi prima di spawnare un nuovo nemico
                    double x = random.nextDouble() * view.getCanvas().getWidth();
                    double y = random.nextDouble() * view.getCanvas().getHeight() / 2;
                    enemies.add(new Enemy(x, y));
                    System.out.println("Spawned new enemy at: " + x + ", " + y);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void bindScene(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) left = true;
            if (e.getCode() == KeyCode.RIGHT) right = true;
            if (e.getCode() == KeyCode.UP) up = true;
            if (e.getCode() == KeyCode.DOWN) down = true;
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) left = false;
            if (e.getCode() == KeyCode.RIGHT) right = false;
            if (e.getCode() == KeyCode.UP) up = false;
            if (e.getCode() == KeyCode.DOWN) down = false;
        });
    }
}
