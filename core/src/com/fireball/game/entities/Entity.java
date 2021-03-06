package com.fireball.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fireball.game.rendering.fire.FireRenderer;
import com.fireball.game.rendering.shadow.ShadowRenderer;
import com.fireball.game.rooms.collision.Slottable;
import com.fireball.game.entities.hitboxes.BodyHitbox;
import com.fireball.game.entities.hitboxes.DamagerHitbox;

public abstract class Entity extends Slottable {
    protected String name;
    protected Team team;
    protected double x, y;
    protected double xVel, yVel;
    protected double nextX, nextY;
    protected BodyHitbox[] bodyHitboxes = new BodyHitbox[0];
    protected DamagerHitbox[] damagerHitboxes = new DamagerHitbox[0];
    protected double terrainCollisionRadius = -1;

    public Entity(Team team, String name, double x, double y) {
        this.team = team;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    protected void registerEntityAndHitboxes() {
        EntityManager.current.addEntity(this);
    }

    public abstract void updatePre(double delta);
    public abstract void updateMid(double delta);
    public abstract void updatePost(double delta);
    public void draw(SpriteBatch batch) {}
    public void drawFire(FireRenderer renderer) {}
    public void drawShadow(ShadowRenderer renderer, int batchIndex) {}

    public abstract void eventTerrainCollision(double angle);

    public abstract boolean isAlive();
    public abstract void kill();

    public BodyHitbox[] getBodyHitboxes() {
        return bodyHitboxes;
    }

    public DamagerHitbox[] getDamagerHitboxes() {
        return damagerHitboxes;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getXVel() {
        return xVel;
    }

    public double getYVel() {
        return yVel;
    }

    public void setVelocity(double xVel, double yVel) {
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public double getNextX() {
        return nextX;
    }

    public double getNextY() {
        return nextY;
    }

    public void setNextPosition(double nextX, double nextY) {
        this.nextX = nextX;
        this.nextY = nextY;
    }

    public double getTerrainCollisionRadius() {
        return terrainCollisionRadius;
    }

    public Team getTeam() {
        return team;
    }

    public String toString() {
        return name + " " + team.toString() + " " + this.hashCode();
    }

    public String getName() {
        return name;
    }
}
