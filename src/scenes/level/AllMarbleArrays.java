/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes.level;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.MarbleArray;
import monsterstrike.graph.Vector;
import monsterstrike.util.Global;

public class AllMarbleArrays {

    protected MarbleArray marbles; //我方出戰怪物
    protected MarbleArray allEnemies; //敵方所有怪物
    protected MarbleArray battleEnemies; //小關出戰怪物
    protected int currentIdx;
    protected int sceneCount;

    public AllMarbleArrays(ArrayList<Marble> enemies, Marble[] myMarbles, int backIdx, int currentIdx, int sceneCount) {
        this.allEnemies = new MarbleArray(enemies);
        this.battleEnemies = new MarbleArray(null);
        this.marbles = new MarbleArray(null);
        this.currentIdx = currentIdx;
        this.sceneCount = sceneCount;
        for (int i = 0; i < myMarbles.length; i++) {
            this.marbles.add(myMarbles[i].duplicate(Global.POSITION_X[i],
                    Global.POSITION_Y[i], 100, 100));
        }
        for (int i = 0; i < 3; i++) {
            this.marbles.get(i).setCenterX(Global.POSITION_X[i]);
            this.marbles.get(i).setCenterY(Global.POSITION_Y[i]);
            this.marbles.get(i).setDetect(this.marbles.get(i).duplicate());
        }
    }

    public void check() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = i + 1; j < this.marbles.size(); j++) {
                if (this.marbles.get(i).isCollision(this.marbles.get(j))) {
                    Vector v = new Vector(marbles.get(j).getCenterX() - marbles.get(i).getCenterX(),
                            marbles.get(j).getCenterY() - marbles.get(i).getCenterY());
                    float r = marbles.get(i).getR() + marbles.get(j).getR();
                    float value = (r - v.getValue()) / 2 + 3;
                    marbles.get(i).offset(-v.getUnitX() * value, -v.getUnitY() * value);
                    marbles.get(j).offset(v.getUnitX() * value, v.getUnitY() * value);
                }
            }
        }
    }
    
    public void checkEnemies() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.battleEnemies.size(); j++) {
                if (this.marbles.get(i).isCollision(this.battleEnemies.get(j))) {
                    float y = battleEnemies.get(j).getCenterY() - marbles.get(i).getCenterY();
                    float r = marbles.get(i).getR() + battleEnemies.get(j).getR();
                    if (y <= 0) {
                        this.marbles.get(i).offset(0, r + y);
                    } else {
                        this.marbles.get(i).offset(0, -r + y);
                    }
                }
            }
        }
    }

    public void scrollMarbles() {
        for (int i = 0; i < this.marbles.size(); i++) {
            float x = this.marbles.get(i).getCenterX();
            float y = this.marbles.get(i).getCenterY();
            if (x > Global.POSITION_X[i] + 10 || x < Global.POSITION_X[i] - 10) {
                this.marbles.get(i).offset((Global.POSITION_X[i] - x) / 35f, 0);
            }
            if (y > Global.POSITION_Y[i] + 10 || y < Global.POSITION_Y[i] - 10) {
                this.marbles.get(i).offset(0, (Global.POSITION_Y[i] - y) / 35f);
            }
        }
    }

    public boolean removeEnemies() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            this.battleEnemies.remove(i);
        }
        if (this.battleEnemies.isEmpty()) {
            return true;
        }
        return false;
    }

    public void dropEnemies() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (this.battleEnemies.get(i).getCenterY() < Global.ENEMYPOS_Y[i]) {
                this.battleEnemies.get(i).offset(0, Global.ENEMYPOS_Y[i] / 40);
            }
        }
    }

    public void enemyDie() {
        for (int i = 0; i < this.battleEnemies.size(); i++) { //判斷敵人是否死亡
            if (this.battleEnemies.get(i).getInfo().getHp() <= 0) {
                this.battleEnemies.get(i).setIsCollide(false);
                this.battleEnemies.get(i).die();
                if (this.battleEnemies.get(i).getIsDie() && this.battleEnemies.get(i).getDieRenderer().getIsStop()) {
                    this.battleEnemies.remove(i);
                }
            }
        }
    }

    public boolean checkAllStop() {
        for (int i = 0; i < this.marbles.size(); i++) {
            if (this.marbles.get(i).goVec().getValue() != 0) {
                return false;
            }
        }
        return true;
    }  //Marble停止移動

    public void setCollide() {
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).setIsCollide(false);
        }
    }
    
    public void updateEnemies(){
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            this.battleEnemies.get(i).update();
        }
    }

    public void updateMarbles() {
        //我方怪物動畫更新
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).updateShine(); //光圈更新
            this.marbles.get(i).update();      //怪物、技能更新
            this.marbles.get(i).getDetect().setCenterX(this.marbles.get(i).getCenterX() + this.marbles.get(i).goVec().getX());
            this.marbles.get(i).getDetect().setCenterY(this.marbles.get(i).getCenterY() + this.marbles.get(i).goVec().getY());
        }
    }

    public void paint(Graphics g) {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            this.battleEnemies.get(i).paint(g);
            this.battleEnemies.get(i).paintSkill(g);
        }
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).paintAll(g);
        }
    }
}
