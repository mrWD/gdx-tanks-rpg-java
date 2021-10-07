package com.mygdx.gdxtanksrpg.units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.gdxtanksrpg.GameScreen;
import com.mygdx.gdxtanksrpg.MyGdxGame;
import com.mygdx.gdxtanksrpg.Weapon;
import com.mygdx.gdxtanksrpg.utils.Direction;
import com.mygdx.gdxtanksrpg.utils.TankOwner;

public class BotTank extends Tank {
    Direction preferredDirection;
    float aiTimer;
    float aiTimerTo;
    float pursuitRadius;
    boolean active;
    Vector3 lastPosition;

    public boolean isActive() {
        return active;
    }

    public BotTank(GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen, atlas);

        this.ownerType = TankOwner.AI;
        this.weapon = new Weapon(atlas);
        this.texture = atlas.findRegion("botTankBase");
        this.textureHp = atlas.findRegion("bar");
        this.position = new Vector2(500.0f, 500.0f);
        this.lastPosition = new Vector3(0.0f, 0.0f, 0.0f);
        this.speed = 100.0f;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 3;
        this.hp = this.hpMax;
        this.aiTimerTo = 3.0f;
        this.pursuitRadius = 300.0f;
        this.preferredDirection = Direction.UP;
        this.circle = new Circle(position.x, position.y, (width + height) / 2);
    }

    public void activate(float x, float y) {
        hpMax = 3;
        hp = hpMax;
        preferredDirection = Direction.values() [MathUtils.random(0, Direction.values().length - 1)];
        angle = preferredDirection.getAngle();
        position.set(x, y);
        aiTimerTo = 3.0f;
        active = true;
    }

    @Override
    public void destroy() {
        active = false;
    }

    public void update(float dt) {
        aiTimer += dt;
        fireTimer += dt;

        if (aiTimer >= aiTimerTo) {
            aiTimer = 0.0f;
            aiTimerTo = MathUtils.random(3.5f, 6.0f);
            preferredDirection = Direction.values() [MathUtils.random(0, Direction.values().length - 1)];
            angle = preferredDirection.getAngle();
        }

        move(preferredDirection, dt);

        float dst = this.position.dst(gameScreen.getPlayer().getPosition());

        if (dst < pursuitRadius) {
            rotateTurretToPoint(gameScreen.getPlayer().getPosition().x, gameScreen.getPlayer().getPosition().y, dt);
            fire();
        }

        if (Math.abs(position.x - lastPosition.x) < 0.5f && Math.abs(position.y - lastPosition.y) < 0.5f) {
            lastPosition.z += dt;

            if (lastPosition.z > 0.3f) {
                aiTimer += 10.0f;
            }
        } else {
            lastPosition.x = position.x;
            lastPosition.y = position.y;
            lastPosition.z = 0.0f;
        }

        lastPosition.x = position.x;

        super.update(dt);
    }
}
