/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes.level;

import controllers.ARC;
import controllers.SceneController;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.Cloud;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.util.Global;
import player.PlayerInfo;

/**
 *
 * @author yuin8
 */
public class Level4 extends LevelScene {

    private static final int IDX = 3;
    private ArrayList<Cloud> clouds;

    public Level4(SceneController sceneController, Marble[] myMarbles, ArrayList<Marble> enemies, PlayerInfo playerinfo) {
        super(sceneController, IDX, myMarbles, enemies, playerinfo);
        this.clouds = new ArrayList<>();
    }

    @Override
    protected void updateGameObject() {
        updateProps();
        updateClouds();
    }

    @Override
    protected void genBattleEnemies() {
        if (this.sceneCount == 3) {
            sceneEnd();
        }
        ArrayList<Marble> m = this.allMarbleArrs.allEnemies.sortByLevel();
        if (this.sceneCount == 0) {
            for (int i = 0; i < 2; i++) {
                this.allMarbleArrs.battleEnemies.add(m.get(0).duplicate(Global.ENEMYPOS_X[2 * i], -100, 120, 120));
                this.allMarbleArrs.battleEnemies.add(m.get(1).duplicate(Global.ENEMYPOS_X[2 * i + 1], -100, 120, 120));
                this.allMarbleArrs.battleEnemies.get(2 * i).getInfo().setName(this.allMarbleArrs.battleEnemies.get(2 * i).getInfo().getName() + (i + 1));
                this.allMarbleArrs.battleEnemies.get(2 * i + 1).getInfo().setName(this.allMarbleArrs.battleEnemies.get(2 * i + 1).getInfo().getName() + (i + 1));
            }
        } else if (this.sceneCount == 1) {
            for (int i = 0; i < 2; i++) {
                this.allMarbleArrs.battleEnemies.add(m.get(2).duplicate(Global.ENEMYPOS_X[2 * i], -100, 120, 120));
                this.allMarbleArrs.battleEnemies.add(m.get(3).duplicate(Global.ENEMYPOS_X[2 * i + 1], -100, 120, 120));
                this.allMarbleArrs.battleEnemies.get(2 * i).getInfo().setName(this.allMarbleArrs.battleEnemies.get(2 * i).getInfo().getName() + (i + 1));
                this.allMarbleArrs.battleEnemies.get(2 * i + 1).getInfo().setName(this.allMarbleArrs.battleEnemies.get(2 * i + 1).getInfo().getName() + (i + 1));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                this.allMarbleArrs.battleEnemies.add(m.get(i + 2).duplicate(Global.ENEMYPOS_X[i], -100, 120, 120));
            }
            this.allMarbleArrs.battleEnemies.add(m.get(5).duplicate(Global.SCREEN_X / 2, -100, 180, 180));
        }
    }

    @Override
    protected void genGameObject() {
        genBattleEnemies();
        genClouds();
    }

    @Override
    protected void hitGameObject() {
        hitProp();
        hitClouds();
    }

    private void updateClouds() {
        for (int i = 0; i < this.clouds.size(); i++) {
            this.clouds.get(i).update();
        }
    }

    private void genClouds() {
        for (int i = 0; i < 2; i++) {
            int size = Global.random(100, 150);
            int x = Global.random(Global.SCREEN_X / 2 - 200, Global.SCREEN_X / 2 + 200);
            int y = Global.random(50, Global.SCREEN_Y - Global.INFO_H - 50);
            this.clouds.add(new Cloud(ImgInfo.CLOUD_PATH, x,
                    y, size, size, (int) ((size / 2) * 0.76)));
        }

    }

    @Override
    protected void paintGameObject(Graphics g) {
        paintProps(g);
        paintClouds(g);
    }

    private void paintClouds(Graphics g) {
        for (int i = 0; i < this.clouds.size(); i++) {
            this.clouds.get(i).paintComponent(g);

        }
    }

    private void hitClouds() {
        if (!isWin && !isLose()) {
            for (int i = 0; i < this.allMarbleArrs.marbles.size(); i++) {
                for (int j = 0; j < this.clouds.size(); j++) {
                    if (this.allMarbleArrs.marbles.get(i).getDetect().isCollision(this.clouds.get(j))) {
                        if (this.allMarbleArrs.marbles.get(i).goVec().getValue() != 0) {
                            ARC.getInstance().play("/resources/wav/collide.wav");
                        }
                        this.allMarbleArrs.marbles.get(i).detectStill(this.clouds.get(j));
                        this.clouds.get(j).setCollide(true);
                        this.clouds.get(j).setGo(this.allMarbleArrs.marbles.get(i).goVec());
                        this.allMarbleArrs.marbles.get(i).hit(this.clouds.get(j));
                    }
                }
            }
        }
    }

}
