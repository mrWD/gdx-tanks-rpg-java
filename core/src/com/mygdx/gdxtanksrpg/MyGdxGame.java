package com.mygdx.gdxtanksrpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

public class MyGdxGame extends Game {
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		ScreeManager.getInstance().init(this, batch);
		ScreeManager.getInstance().setScreen(ScreeManager.ScreenType.GAME);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();

		getScreen().render(dt);
	}

	@Override
	public void dispose () {
		batch.dispose();
 	}
}
