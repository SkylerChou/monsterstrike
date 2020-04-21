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
import monsterstrike.gameobject.Stone;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.graph.Vector;
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
        genProps();
    }

    private void updateStones() {
        for (int i = 0; i < this.stones.size(); i++) {
            if (this.stones.get(i).getCollide()) {
                this.stones.get(i).update();
                if (this.stones.get(i).getStop()) {
                    this.stones.remove(i);
                    i--;
                }
            }
        }
    }
    
    @Override
    protected boolean removeGameObject(){
        if(removeStones()){
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
                this.stones.add(new Stone(ImgInfo.STONE_PATH, Global.SCREEN_X / 2,
                        (i + 1) * (Global.SCREEN_Y - Global.INFO_H) / 11, 50, 50));
            }
            for (int i = 0; i < Global.SCREEN_X / 100; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (Global.SCREEN_X / 2 - 10) + (i + 1) * 50,
                        30, 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (Global.SCREEN_X / 2 - 10) + (i + 1) * 50,
                        (Global.SCREEN_Y - Global.INFO_H - 30), 50, 50));
            }
        } else if (this.sceneCount == 1) {
            for (int i = 0; i < 10; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, 30,
                        (i + 1) * (Global.SCREEN_Y - Global.INFO_H) / 11, 50, 50));
            }
            for (int i = 0; i < Global.SCREEN_X / 50; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, 30 + (i + 1) * 50,
                        30, 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, 30 + (i + 1) * 50,
                        (Global.SCREEN_Y - Global.INFO_H - 30), 50, 50));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, 30,
                        (i + 1) * (Global.SCREEN_Y - Global.INFO_H) / 11, 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, Global.SCREEN_X - 30,
                        (i + 1) * (Global.SCREEN_Y - Global.INFO_H) / 11, 50, 50));
            }
            for (int i = 0; i < Global.SCREEN_X / 50; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, 30 + (i + 1) * 50,
                        30, 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, 30 + (i + 1) * 50,
                        (Global.SCREEN_Y - Global.INFO_H - 30), 50, 50));
            }
            for (int i = 0; i < 5; i++) {
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (int) ((Global.SCREEN_X / 2 - 100) + (i * 40)),
                        ((Global.SCREEN_Y - Global.INFO_H) / 2 - 100), 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (int) ((Global.SCREEN_X / 2 - 100) + (i * 40)),
                        ((Global.SCREEN_Y - Global.INFO_H) / 2 + 100), 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (int) (Global.SCREEN_X / 2 - 100),
                        ((Global.SCREEN_Y - Global.INFO_H) / 2 - 100) + (i * 40), 50, 50));
                this.stones.add(new Stone(ImgInfo.STONE_PATH, (int) (Global.SCREEN_X / 2 + 100),
                        ((Global.SCREEN_Y - Global.INFO_H) / 2 - 100) + (i * 40), 50, 50));
            }
        }
    }

    private void strikeStones() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.stones.size(); j++) {
                int dir = this.marbles.get(i).getDetect().isCollideRect(this.stones.get(j));
                if (dir > 0 && this.marbles.get(i).goVec().getValue() > 0) {
                    this.marbles.get(i).detectRect(this.stones.get(j), dir);
                    this.stones.get(j).setCollide(true);
                    Vector go = this.marbles.get(i).goVec();
                    if (dir == 1) {
                        this.marbles.get(i).setGo(new Vector(go.getX(), -go.getY()));
                    } else if (dir == 2) {
                        this.marbles.get(i).setGo(new Vector(-go.getX(), go.getY()));
                    } else {
                        this.marbles.get(i).setGo(new Vector(-go.getX(), -go.getY()));
                    }
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
