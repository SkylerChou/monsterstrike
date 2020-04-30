/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes.level;

import controllers.SceneController;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.ObjectRenderer;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.util.Global;
import player.PlayerInfo;

public class Level1 extends LevelScene {

    private static final int IDX = 0;
    private ObjectRenderer img;
    private int imgCount;

    public Level1(SceneController sceneController, Marble[] myMarbles, ArrayList<Marble> enemies, PlayerInfo playerinfo) {
        super(sceneController, IDX, myMarbles, enemies, playerinfo);
        this.img = new ObjectRenderer(ImgInfo.LV1_SWEET, 20);
        this.imgCount = 0;
    }
    
    @Override
    protected void updateSweet(){
        this.img.update();
    }

    @Override
    protected void updateGameObject() {
        updateProps();
    }

    @Override
    protected void genGameObject() {
        genBattleEnemies();
    }

    @Override
    protected void paintGameObject(Graphics g) {
        if (!isStart && this.imgCount < 200) {
            this.img.paint(g, Global.SCREEN_X/2-250, 180, 500, 150);
            imgCount++;
        }
        if (imgCount == 200) {
            isStart = true;
        }
        paintProps(g);
    }

    @Override
    protected void genBattleEnemies() {
        if (this.sceneCount == 3) {
            sceneEnd();
        }
        ArrayList<Marble> m = this.allMarbleArrs.allEnemies.sortByLevel();
        if (this.sceneCount == 0) {
            for (int i = 0; i < 3; i++) {
                this.allMarbleArrs.battleEnemies.add(m.get(0).duplicate(Global.ENEMYPOS_X[i], -100, 120, 120));
                this.allMarbleArrs.battleEnemies.get(i).getInfo().setName(this.allMarbleArrs.battleEnemies.get(i).getInfo().getName() + (i + 1));
            }
        } else if (this.sceneCount == 1) {
            for (int i = 0; i < 2; i++) {
                this.allMarbleArrs.battleEnemies.add(m.get(1).duplicate(Global.ENEMYPOS_X[2 * i], -100, 120, 120));
                this.allMarbleArrs.battleEnemies.add(m.get(2).duplicate(Global.ENEMYPOS_X[2 * i + 1], -100, 120, 120));
                this.allMarbleArrs.battleEnemies.get(2 * i).getInfo().setName(this.allMarbleArrs.battleEnemies.get(2 * i).getInfo().getName() + (i + 1));
                this.allMarbleArrs.battleEnemies.get(2 * i + 1).getInfo().setName(this.allMarbleArrs.battleEnemies.get(2 * i + 1).getInfo().getName() + (i + 1));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                this.allMarbleArrs.battleEnemies.add(m.get(i).duplicate(Global.ENEMYPOS_X[i], -100, 120, 120));
            }
            this.allMarbleArrs.battleEnemies.add(m.get(3).duplicate(Global.SCREEN_X / 2, -100, 180, 180));
        }
    }
}
