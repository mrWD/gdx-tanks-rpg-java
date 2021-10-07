package com.mygdx.gdxtanksrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Map {
    private enum WallType {
        HARD(0, 5, true),
        SOFT(1, 3, true),
        INDESTRUCTABLE(2, 1, false),
        NONE(0, 0, false);

        int index;
        int maxHp;
        boolean destructable;

        WallType(int index, int maxHp, boolean destructable) {
            this.index = index;
            this.maxHp = maxHp;
            this.destructable = destructable;
        }
    }

    private class Cell {
        WallType type;
        int hp;

        public Cell(WallType type) {
            this.type = type;
            this.hp = type.maxHp;
        }

        public void damage() {
            if (!type.destructable) {
                return;
            }

            hp--;
            if (hp <= 0) {
                type = WallType.NONE;
            }
        }

        public void changeType(WallType type) {
            this.type = type;
            this.hp = type.maxHp;
        }
    }

    public static final int SIZE_X = 64;
    public static final int SIZE_Y = 36;

    public static final int CELL_SIZE = 20;

    private TextureRegion grassTexture;
    private TextureRegion[][] wallsTexture;

    private Cell cells[][];

    public Map(TextureAtlas atlas) {
        this.wallsTexture = new TextureRegion(atlas.findRegion("walls")).split(CELL_SIZE, CELL_SIZE);
        this.grassTexture = atlas.findRegion("grass40");

        this.cells = new Cell[SIZE_X][SIZE_Y];

        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                cells[i][j] = new Cell(WallType.NONE);
                int cx = (int) (i / 4);
                int cy = (int) (j / 4);

                if (cx % 2 == 0 && cy % 2 == 0) {
                    if (MathUtils.random() < 0.7f) {
                        cells[i][j].changeType(WallType.HARD);
                    } else {
                        cells[i][j].changeType(WallType.SOFT);
                    }
                }
            }
        }

        for (int i = 0; i < SIZE_X; i++) {
            cells[i][0].changeType(WallType.INDESTRUCTABLE);
            cells[i][SIZE_Y- 1].changeType(WallType.INDESTRUCTABLE);
        }

        for (int i = 0; i < SIZE_Y; i++) {
            cells[0][i].changeType(WallType.INDESTRUCTABLE);
            cells[SIZE_X- 1][i].changeType(WallType.INDESTRUCTABLE);
        }
    }

    public void checkWallAndBulletsCollision(Bullet bullet) {
        int cx = (int)(bullet.getPosition().x / CELL_SIZE);
        int cy = (int)(bullet.getPosition().y / CELL_SIZE);

        if (cx >= 0 && cy >= 0 && cx < SIZE_X && cy < SIZE_Y && cells[cx][cy].type != WallType.NONE) {
            cells[cx][cy].damage();
            bullet.deactivate();
        }
    }

    public boolean isAreaClear(float x, float y, float halfSize) {
        int leftX = (int)((x - halfSize) / CELL_SIZE);
        int rightX = (int)((x + halfSize) / CELL_SIZE);
        int bottomY = (int)((y - halfSize) / CELL_SIZE);
        int topY = (int)((y + halfSize) / CELL_SIZE);

        if (leftX < 0) {
            leftX = 0;
        }

        if (rightX >= SIZE_X) {
            rightX = SIZE_X - 1;
        }

        if (bottomY < 0) {
            bottomY = 0;
        }

        if (topY >= SIZE_Y) {
            topY = SIZE_Y - 1;
        }

        for (int i = leftX; i <= rightX; i++) {
            for (int j = bottomY; j <= topY; j++) {
                if (cells[i][j].type != WallType.NONE) {
                    return false;
                }
            }
        }

        return true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < 1280 / 40; i++) {
            for (int j = 0; j < 720 / 40; j++) {
                batch.draw(grassTexture, i * 40, j * 40);
            }
        }

        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                if (cells[i][j].type != WallType.NONE) {
                    batch.draw(wallsTexture[cells[i][j].type.index][cells[i][j].hp - 1], i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }
}
