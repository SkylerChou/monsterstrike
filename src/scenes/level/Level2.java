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
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.Stone;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.util.Global;
import player.PlayerInfo;

/**
 *
 * @author yuin8
 */
public class Level2 extends LevelScene {

    private ArrayList<Stone> stones;
    private static final int IDX = 1;

    public Level2(SceneController sceneController, Marble[] myMarbles, ArrayList<Marble> enemies, PlayerInfo playerinfo) {
        super(sceneController, IDX, myMarbles, enemies, playerinfo);
        this.stones = new ArrayList<>();
    }

    @Override
    protected void updateGameObject() {
        updateStones();
        updateProps();
    }

    @Override
    protected void genGameObject() {
        genBattleEnemies();
        genStones();
    }

    private void updateStones() {
        for (int i = 0; i < this.stones.size(); i++) {
            if (this.stones.get(i).getCollide()) {
                this.stones.get(i).update();
                if (this.stones.get(i).getStop()) {
                    ARC.getInstance().play("/resources/wav/damage.wav");
                    this.stones.remove(i);
                    i--;
                }
            }
        }
    }

    @Override
    protected boolean removeGameObject() {
        if (removeStones()) {
            return true;
        }
        return false;
    }

    private boolean removeStones() {
        for (int i = 0; i < this.stones.size(); i++) {
            this.stones.remove(i);
        }
        if (this.stones.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    protected void hitGameObject() {
        hitProp();
        strikeStones();
    }

    private void genStones() {
        if (this.sceneCount == 0) {
            for (int i = 0; i < 10; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, Global.SCREEN_X / 2 - 25,
                        30 + i * 50, 50, 50));
            }
            for (int i = 0; i < 8; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, Global.SCREEN_X / 2 + 30 + i * 80,
                        30, 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, Global.SCREEN_X / 2 + 30 + i * 80,
                        (Global.SCREEN_Y - Global.INFO_H - 30), 50, 50));
            }
        } else if (this.sceneCount == 1) {
            for (int i = 0; i < 16; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, i * 81 + 25,
                        30, 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, i * 81 + 25,
                        (Global.SCREEN_Y - Global.INFO_H - 30), 50, 50));
            }
        } else {
            for (int i = 0; i < 16; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, i * 81 + 25,
                        30, 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, i * 81 + 25,
                        (Global.SCREEN_Y - Global.INFO_H - 30), 50, 50));
            }
            for (int i = 0; i < 3; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (int) ((Global.SCREEN_X / 2 - 105) + (i * 70)),
                        ((Global.SCREEN_Y - Global.INFO_H) / 2 - 100), 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (int) ((Global.SCREEN_X / 2 - 105) + ((i+1) * 70)),
                        ((Global.SCREEN_Y - Global.INFO_H) / 2 + 110), 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (int) (Global.SCREEN_X / 2 - 105),
                        ((Global.SCREEN_Y - Global.INFO_H) / 2 - 100) + ((i+1) * 70), 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (int) (Global.SCREEN_X / 2 + 110),
                        ((Global.SCREEN_Y - Global.INFO_H) / 2 - 100) + (i * 70), 50, 50));
            }
        }
    }

    private void strikeStones() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.stones.size(); j++) {
                if (this.marbles.get(i).getDetect().isCollision(this.stones.get(j)) && this.marbles.get(i).goVec().getValue() > 0) {
                    this.marbles.get(i).detectStill(this.stones.get(j));
                    this.marbles.get(i).hit(this.stones.get(j));
                    this.stones.get(j).setCollide(true);
                    this.hitCount += this.marbles.get(i).explode(this.stones.get(j));
                }
            }
        }
    }

    @Override
    protected void paintGameObject(Graphics g) {
        paintStones(g);
        paintProps(g);
    }

    private void paintStones(Graphics g) {
        for (int i = 0; i < this.stones.size(); i++) {
            this.stones.get(i).paint(g);
        }
    }

}
