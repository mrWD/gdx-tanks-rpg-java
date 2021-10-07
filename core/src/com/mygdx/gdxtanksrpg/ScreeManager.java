package com.mygdx.gdxtanksrpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreeManager {
    public enum ScreenType {
        MENU, GAME;
    }
    private static ScreeManager ourInstance = new ScreeManager();

    public static ScreeManager getInstance() {
        return ourInstance;
    }

    private ScreeManager() {}

    private Game game;
    private GameScreen gameScreen;

    public void init(Game game, SpriteBatch batch) {
        this.game = game;
        this.gameScreen = new GameScreen(batch);
    }

    public void setScreen(ScreenType screenType) {
        Screen currentScreen = game.getScreen();
        switch (screenType) {
            case GAME:
                game.setScreen(gameScreen);
                break;
        }

        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
