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
import player.People;
import player.PlayerInfo;

/**
 *
 * @author yuin8
 */
public class Level3 extends LevelScene {

    private static final int IDX = 2;
    private ArrayList<People> people;
    private ArrayList<Stone> stones;

    public Level3(SceneController sceneController, Marble[] myMarbles, ArrayList<Marble> enemies, PlayerInfo playerinfo) {
        super(sceneController, IDX, myMarbles, enemies, playerinfo);
        this.people = new ArrayList<>();
        this.stones = new ArrayList<>();
    }

    @Override
    protected void updateGameObject() {
        updatePeople();
        updateStones();
        updateProps();
    }

    @Override
    protected void genGameObject() {
        genBattleEnemies();
        genProps();
        genStones();
        genPeople();
    }

    private void updatePeople() {
        for (int i = 0; i < this.people.size(); i++) {
            this.people.get(i).update();
        }
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

    private void genPeople() {
        this.people.add(new People(ImgInfo.PEOPLE_PATH[this.sceneCount], Global.SCREEN_X - 110, Global.SCREEN_Y / 2 - 75, 80, 120));
    }

    private void genStones() {
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 5; i++) {
                int x = Global.SCREEN_X / 2;
                if (i % 2 != 0) {
                    x = Global.SCREEN_X / 2 + 50;
                }
                this.stones.add(new Stone(ImgInfo.ROCK_PATH, x + Global.FRAME_X * j,
                        (2 * i + 1) * (Global.SCREEN_Y - Global.INFO_H) / 11, 60, 60));
                this.stones.add(new Stone(ImgInfo.ROCK_PATH, x + Global.FRAME_X * j,
                        (2 * i + 2) * (Global.SCREEN_Y - Global.INFO_H) / 11, 60, 60));
                this.stones.add(new Stone(ImgInfo.ROCK_PATH, x - 100 + Global.FRAME_X * j,
                        (2 * i + 1) * (Global.SCREEN_Y - Global.INFO_H) / 11, 60, 60));
                this.stones.add(new Stone(ImgInfo.ROCK_PATH, x - 100 + Global.FRAME_X * j,
                        (2 * i + 2) * (Global.SCREEN_Y - Global.INFO_H) / 11, 60, 60));

                if (i == 4) {
                    continue;
                }
                this.stones.add(new Stone(ImgInfo.ROCK_PATH, Global.SCREEN_X - 30 - 50 * i + Global.FRAME_X * j,
                        (Global.SCREEN_Y - Global.INFO_H) / 2 - 80, 60, 60));
                this.stones.add(new Stone(ImgInfo.ROCK_PATH, Global.SCREEN_X - 30 - 50 * i + Global.FRAME_X * j,
                        (Global.SCREEN_Y - Global.INFO_H) / 2 + 80, 60, 60));
                this.stones.add(new Stone(ImgInfo.ROCK_PATH, Global.SCREEN_X - 30 - 150 + Global.FRAME_X * j,
                        (Global.SCREEN_Y - Global.INFO_H) / 2 - 80 + 50 * i, 60, 60));
                this.stones.add(new Stone(ImgInfo.ROCK_PATH, Global.SCREEN_X - 30 + Global.FRAME_X * j,
                        (Global.SCREEN_Y - Global.INFO_H) / 2 - 80 + 50 * i, 60, 60));
            }
        }
    }

    @Override
    protected void hitGameObject() {
        hitProp();
        strikeStones();
        hitPeople();
    }

    private void strikeStones() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.stones.size(); j++) {                
                if (this.marbles.get(i).getDetect().isCollision(this.stones.get(j)) && this.marbles.get(i).goVec().getValue() > 0 ) {
                    this.marbles.get(i).detectStill(this.stones.get(j));
                    this.marbles.get(i).hit(this.stones.get(j));
                    this.stones.get(j).setCollide(true);
                    this.hitCount += this.marbles.get(i).explode(this.stones.get(j));
                }
            }
        }
    }
    
    private void hitPeople() {
        for (int i = 0; i < this.marbles.size(); i++) {
            if (!this.people.get(this.sceneCount).getSet() && this.marbles.get(i).isCollision(this.people.get(this.sceneCount))) {
                this.people.get(this.sceneCount).setCollide(true);
                float x = (Global.SCREEN_X / 2 + 165 + (this.sceneCount + 1) * 65
                        - this.people.get(this.sceneCount).getCenterX()) / 10;
                float y = (Global.SCREEN_Y - 80 - this.people.get(this.sceneCount).getCenterY()) / 10;
                this.people.get(this.sceneCount).setGoVec(new Vector(x, y));
            }
        }
    }

    @Override
    protected void endBattle() {
        if (((this.battleEnemies.isEmpty() && (this.people != null && this.people.get(this.sceneCount).getSet() && !this.people.get(this.sceneCount).getCollide()))
                || (this.people != null && this.people.get(this.sceneCount).getSet() && !this.people.get(this.sceneCount).getCollide()))
                && allSkillStop(this.marbles)) {

            if (removeEnemies() && removeStones()) {
                this.marbles.get(currentIdx).setShine(false);
                this.round = 0;
                this.enemyRound = 0;
                this.state = 2;
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
    protected void paintGameObject(Graphics g) {
        paintStones(g);
        paintProps(g);
        paintPeople(g);
    }

    private void paintStones(Graphics g) {
        for (int i = 0; i < this.stones.size(); i++) {
            this.stones.get(i).paint(g);
        }
    }

    private void paintPeople(Graphics g) {
        for (int i = 0; i < this.people.size(); i++) {
            this.people.get(i).paint(g);
        }
    }
}
