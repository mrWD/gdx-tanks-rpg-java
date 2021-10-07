package com.mygdx.gdxtanksrpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.gdxtanksrpg.units.BotTank;
import com.mygdx.gdxtanksrpg.units.PlayerTank;
import com.mygdx.gdxtanksrpg.units.Tank;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private Map map;
    private PlayerTank player;
    private BulletEmitter bulletEmitter;
    private BotEmitter botEmitter;
    private float gameTimer;
    private Stage stage;
    private boolean paused;

    private static final boolean FRIENDLY_FIRE = false;

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public PlayerTank getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        TextureAtlas atlas = new TextureAtlas("game.pack");

        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        batch = new SpriteBatch();
        map = new Map(atlas);
        player = new PlayerTank(this, atlas);
        bulletEmitter = new BulletEmitter(atlas);
        botEmitter = new BotEmitter(this, atlas);
        gameTimer = 6.0f;
        stage = new Stage();

        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("SimpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                paused = !paused;
            }
        });
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        pauseButton.setPosition(0, 40);
        pauseButton.setPosition(0, 0);

        group.addActor(pauseButton);
        group.addActor(exitButton);
        group.setPosition(1130, 640);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        update(dt);

        Gdx.gl.glClearColor(0, 0.6f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        map.render(batch);
        player.render(batch);
        botEmitter.render(batch);
        bulletEmitter.render(batch);
        player.renderHUD(batch, font24);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void update(float dt) {
        gameTimer += dt;

        if (gameTimer > 5.0f) {
            gameTimer = 0.0f;

            float coordX, coordY;

            do {
                coordX = MathUtils.random(0, Gdx.graphics.getWidth());
                coordY = MathUtils.random(0, Gdx.graphics.getHeight());
            } while (!map.isAreaClear(coordX, coordY, 20));

            botEmitter.activate(coordX, coordY);
        }

        player.update(dt);
        botEmitter.update(dt);
        bulletEmitter.update(dt);
        checkCollisions();

        stage.act(dt);
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];

            if (!bullet.isActive()) {
                continue;
            }

            for (int j = 0; j < botEmitter.getBots().length; j++) {
                BotTank bot = botEmitter.getBots()[j];

                if (!bot.isActive() || checkBulletAndTank(bot, bullet) || !bot.getCircle().contains(bullet.getPosition())) {
                    continue;
                }

                bullet.deactivate();
                bot.takeDamage(bullet.getDamage());
                break;
            }

            if (!checkBulletAndTank(player, bullet) && player.getCircle().contains(bullet.getPosition())) {
                bullet.deactivate();
                player.takeDamage(bullet.getDamage());
            }

            map.checkWallAndBulletsCollision(bullet);
        }
    }

    public boolean checkBulletAndTank(Tank tank, Bullet bullet) {
        if (!FRIENDLY_FIRE) {
            return tank.getOwnerType() == bullet.getOwner().getOwnerType();
        }
        return tank == bullet.getOwner();
    }
}
