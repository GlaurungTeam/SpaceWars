package contracts;

import helpers.LeaderboardWriter;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import managers.*;
import models.gameObjects.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Level {

    void initializeLevel(
            PlayerImpl player,
            GraphicsContext gc,
            Canvas canvas,
            Scene scene,
            Double currentFrame,
            EnemyManager enemyManager,
            BossManager bossManager,
            PlayerManager playerManager,
            FuelManager fuelManager,
            ExplosionManager explosionManager) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException;

    List<HealthableGameObject> getEnemies();

    void addMissile(GameObject missile);

    void removeMissile(GameObject missile);

    List<Boss> getBosses();

    void setDifficultyParameters() throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException;

    PlayerImpl getPlayer();

    GraphicsContext getGc();

    Canvas getCanvas();

    void setCanvas(Canvas canvas);

    Scene getScene();

    Double getCurrentFrame();

    void setCurrentFrame(Double currentFrame);

    List<GameObject> getMissiles();

    Image getBackgroundImage();

    void setBackgroundImage(Image backgroundImage);

    boolean isActiveBoss();

    void setIsActiveBoss(boolean hasActiveBoss);

    PlayerManager getPlayerManager();

    FuelManager getFuelManager();

    ExplosionManager getExplosionManager();

    void initializeBoss(String bossName) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException;

    void initializeEnemies(int asteroidsHealth, int asteroidsSpeed, int asteroidsCount, int ufoHealth, int ufoSpeed, int ufoCount) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException;

    LeaderboardWriter getLeaderboardWriter();
}