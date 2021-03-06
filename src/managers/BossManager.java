package managers;

import factories.BossFactory;
import contracts.Boss;
import models.gameObjects.Explosion;
import models.gameObjects.Missile;
import contracts.Level;
import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BossManager extends EnemyManager {

    private static final int DELAY_TILL_BOSS_SHOW = 5000;
    private static final int FIRE_DELAY_PERIOD = 2000;
    private Timer showTimer;
    private Timer shootTimer;
    private boolean justShowed;
    private BossManager bossManager = this;
    private BossFactory bossFactory;

    public BossManager() {
        this.showTimer = new Timer();
        this.shootTimer = new Timer();
        this.justShowed = true;
        this.bossFactory = new BossFactory();
    }

    private void setJustShowed(boolean justShowed) {
        this.justShowed = justShowed;
    }

    private void fire(Level level, Boss boss) {
        BufferedImage missileSpriteSheet = null;

        double missileX = boss.getPositionX();
        double missileY = boss.getPositionY() + Constants.MISSILE_POSITION_Y_OFFSET;

        try {
            missileSpriteSheet = ImageIO.read(
                    new File(Constants.PROJECT_PATH + Constants.MISSILE_SPRITESHEET_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Missile missile = new Missile(missileX, missileY, Constants.MISSILE_SPEED, missileSpriteSheet, "enemy");
        level.addMissile(missile);
    }

    public void manageBoss(Level level) {
        for (Boss boss : level.getBosses()) {
            if (level.getPlayer().getPoints() != 0 &&
                    level.getPlayer().getPoints() % Constants.POINTS_TILL_BOSS == 0 &&
                    !level.isActiveBoss()) {

                level.setIsActiveBoss(true);

                this.shootTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bossManager.fire(level, boss);
                    }
                }, DELAY_TILL_BOSS_SHOW, FIRE_DELAY_PERIOD);
            }

            if (level.isActiveBoss()) {
                if (boss.getHealth() > 0) {
                    if (this.justShowed) {
                        this.showTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                bossManager.setJustShowed(false);

                                //Restart showTimer
                                showTimer.cancel();
                                showTimer.purge();
                                showTimer = new Timer();
                            }
                        }, DELAY_TILL_BOSS_SHOW);
                    } else {
                        boss.setImage(boss.getRandomImageFromSpritesheet());
                        boss.render(level.getGc());
                        boss.move();
                        boss.setVisible(true);
                        this.updateBossLocation(boss);
                        this.manageBossCollision(level, boss);
                    }
                } else {
                    //Restart shootTimer
                    shootTimer.cancel();
                    shootTimer.purge();
                    shootTimer = new Timer();

                    this.setJustShowed(true);
                    level.setIsActiveBoss(false);
                    boss.setVisible(false);

                    boss.revive();
                    level.getPlayer().incrementKillPoints(boss.getPointsOnKill());
                }
            }
        }
    }

    private void manageBossCollision(Level level, Boss boss) {
        if (level.getPlayerManager().checkCollision(boss)) {

            BufferedImage explosionSpriteSheet = null;

            try {
                explosionSpriteSheet = ImageIO.read(
                        new File(Constants.PROJECT_PATH + Constants.EXPLOSION_SPRITESHEET_IMAGE));
            } catch (IOException e) {
                e.printStackTrace();
            }

            double explosionX = boss.getPositionX();
            double explosionY = boss.getPositionY();

            level.getExplosionManager().playAsteroidHit(
                    new Explosion(explosionX, explosionY, Constants.EXPLOSION_SPEED, explosionSpriteSheet)
            );

            level.getPlayer().decrementLives();
        }
    }

    public BossFactory getBossFactory() {
        return this.bossFactory;
    }

    private void updateBossLocation(Boss boss) {
        boss.getSvgPath().setLayoutX(boss.getPositionX());
        boss.getSvgPath().setLayoutY(boss.getPositionY());
    }
}