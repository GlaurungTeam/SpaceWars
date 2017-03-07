package entities;

public class Missile extends Sprite {
    public Missile(double x, double y, double speed) {
        super.setPosition(x, y, speed);
    }

    public void updateMissileLocation() {
        super.setPositionX(Math.max(0, super.getPositionX() + super.getSpeed() * Constants.MISSILE_SPEED_MULTIPLIER));
    }
}