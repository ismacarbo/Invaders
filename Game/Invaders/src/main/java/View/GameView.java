package View;

import com.example.invaders.Model.Enemy;
import com.example.invaders.Model.Obstacle;
import com.example.invaders.Model.Player;
import com.example.invaders.Model.Projectile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class GameView {
    private Canvas canvas;
    private GraphicsContext gc;

    public GameView(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void render(Player player, List<Enemy> enemies, List<Obstacle> obstacles, List<Projectile> projectiles) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.BLUE);
        gc.fillOval(player.getX(), player.getY(), 20, 20);

        gc.setFill(Color.RED);
        for (Enemy enemy : enemies) {
            gc.fillOval(enemy.getX(), enemy.getY(), 20, 20);
        }

        gc.setFill(Color.GRAY);
        for (Obstacle obstacle : obstacles) {
            gc.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

        gc.setFill(Color.YELLOW);
        for (Projectile projectile : projectiles) {
            gc.fillRect(projectile.getX(), projectile.getY(), 5, 10);
        }
    }
}
