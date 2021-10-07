package com.mygdx.gdxtanksrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private TextureRegion texture;
    private float firePeriod;
    private float radius;
    private float projectileSpeed;
    private float projectileLifeTime;
    private int damage;

    public TextureRegion getTexture() {
        return texture;
    }

    public float getRadius() {
        return radius;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getProjectileLifeTime() {
        return projectileLifeTime;
    }

    public int getDamage() {
        return damage;
    }

    public Weapon(TextureAtlas atlas) {
        this.texture = atlas.findRegion("simpleWeapon");
        this.firePeriod = 0.4f;
        this.damage = 1;
        this.radius = 150.0f;
        this.projectileSpeed = 320.0f;
        this.projectileLifeTime = this.radius / this.projectileSpeed;
    }
}
