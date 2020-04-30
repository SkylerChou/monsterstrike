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
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.SpecialEffect;
import monsterstrike.gameobject.Stone;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.graph.Vector;
import monsterstrike.util.Global;
import player.PlayerInfo;

public class Level5 extends LevelScene {

    private static final int IDX = 4;
    private ArrayList<SpecialEffect> blackholes;
    private ArrayList<Stone> stones;
    private static final int[] SPOS_X = {300, 400, 650, 800};
    private static final int[] SPOS_Y = {150, 360, 120, 300};
    private static final int[] HPOS_X = {120, 600, 1150};
    private static final int[] HPOS_Y = {350, 380, 200};

    public Level5(SceneController sceneController, Marble[] myMarbles, ArrayList<Marble> enemies, PlayerInfo playerinfo) {
        super(sceneController, IDX, myMarbles, enemies, playerinfo);
        this.blackholes = new ArrayList<>();
        this.stones = new ArrayList<>();
    }

    @Override
    protected boolean removeGameObject() {
        if (removeHoles() && removeStones()) {
            return true;
        }
        return false;
    }

    @Override
    protected void updateGameObject() {
        updateStones();
        updateProps();
        updateHoles();
    }

    @Override
    protected void genGameObject() {
        genBattleEnemies();
        genStones();
        genHoles();
    }

    @Override
    protected void hitGameObject() {
        hitProp();
        hitHoles();
        strikeStones();
    }

    private void updateStones() {
        for (int i = 0; i < this.stones.size(); i++) {
            if (this.stones.get(i).getCollide()) {
                ARC.getInstance().play("/resources/wav/damage.wav");
            }
        }
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

    private boolean removeHoles() {
        for (int i = 0; i < this.blackholes.size(); i++) {
            this.blackholes.remove(i);
        }
        if (this.blackholes.isEmpty()) {
            return true;
        }
        return false;
    }

    private void updateHoles() {
        for (int i = 0; i < this.blackholes.size(); i++) {
            this.blackholes.get(i).update();
        }
    }

    private void genStones() {
        for (int i = 0; i < 4; i++) {
            this.stones.add(new Stone(ImgInfo.DARKSTONE_PATH, SPOS_X[i],
                    SPOS_Y[i], 60, 60));
        }

    }

    private void genHoles() {
        for (int i = 0; i < 3; i++) {
            this.blackholes.add(new SpecialEffect(ImgInfo.HOLE, HPOS_X[i], HPOS_Y[i], 200, 200, 50));
            this.blackholes.get(i).setShine(true);
        }
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
            for (int i = 0; i < 4; i++) {
                this.allMarbleArrs.battleEnemies.add(m.get(i+2).duplicate(Global.ENEMYPOS_X[i], -100, 120, 120));         
            }
        } else {
            for (int i = 0; i < 4; i++) {
               this.allMarbleArrs.battleEnemies.add(m.get(i+2).duplicate(Global.ENEMYPOS_X[i], -100, 120, 120));
            }
            this.allMarbleArrs.battleEnemies.add(m.get(6).duplicate(Global.SCREEN_X / 2, -100, 240, 240));
        }
    }

    private void strikeStones() {
        for (int i = 0; i < this.allMarbleArrs.marbles.size(); i++) {
            for (int j = 0; j < this.stones.size(); j++) {
                if (this.allMarbleArrs.marbles.get(i).getDetect().isCollision(this.stones.get(j)) && this.allMarbleArrs.marbles.get(i).goVec().getValue() > 0) {
                    this.allMarbleArrs.marbles.get(i).detectStill(this.stones.get(j));
                    this.allMarbleArrs.marbles.get(i).hit(this.stones.get(j));
                }
            }
        }
    }

    private void hitHoles() {
        for (int i = 0; i < this.allMarbleArrs.marbles.size(); i++) {
            for (int j = 0; j < this.blackholes.size(); j++) {
                if (dist(this.allMarbleArrs.marbles.get(i), this.blackholes.get(j)) < 30
                        && !this.allMarbleArrs.marbles.get(i).isSpin() && !this.allMarbleArrs.marbles.get(i).spinOver()) {
                    this.allMarbleArrs.marbles.get(i).spin(this.blackholes.get(j).getCenterX(), this.blackholes.get(j).getCenterY());
                    this.allMarbleArrs.marbles.get(i).setSpin(true);
                }
                if (this.allMarbleArrs.marbles.get(i).spinOver()) {
                    int r = chooseHole(j);
                    this.allMarbleArrs.marbles.get(i).setCenterX(this.blackholes.get(r).getCenterX());
                    this.allMarbleArrs.marbles.get(i).setCenterY(this.blackholes.get(r).getCenterY());
                    this.allMarbleArrs.marbles.get(i).setGo(this.allMarbleArrs.marbles.get(i).goVec().multiplyScalar(0.6f));
                    if (this.allMarbleArrs.marbles.get(i).goVec().getValue() <= 1) {
                        this.allMarbleArrs.marbles.get(i).offset(0, 40);
                    } else if (this.allMarbleArrs.marbles.get(i).goVec().getValue() < 40) {
                        Vector vec = this.allMarbleArrs.marbles.get(i).goVec().resizeVec(40);
                        this.allMarbleArrs.marbles.get(i).offset(vec.getX(), vec.getY());
//                        System.out.println(dist(this.allMarbleArrs.marbles.get(i), this.blackholes.get(r)));
                    } else {
                        this.allMarbleArrs.marbles.get(i).offset(this.allMarbleArrs.marbles.get(i).goVec().getX(), this.allMarbleArrs.marbles.get(i).goVec().getY());
                    }
                }
                this.allMarbleArrs.marbles.get(i).setSpinOver(false);
            }
        }
    }

    private int chooseHole(int j) {
        int idx = Global.random(0, 1);
        switch (j) {
            case 0:
                return idx + 1;
            case 1:
                if (idx == 1) {
                    return 2;
                } else {
                    return idx;
                }
            case 2:
                return idx;
        }
        return 0;
    }

    private float dist(Marble m, GameObject obj) {
        float d = (float) Math.sqrt(Math.pow((m.getCenterX() - obj.getCenterX()), 2)
                + Math.pow((m.getCenterY() - obj.getCenterY()), 2));
        return d;
    }

    @Override
    protected void paintGameObject(Graphics g) {
        paintStones(g);
        paintProps(g);
        paintHoles(g);
    }

    private void paintHoles(Graphics g) {
        for (int i = 0; i < this.blackholes.size(); i++) {
            this.blackholes.get(i).paintComponent(g);
        }
    }

    private void paintStones(Graphics g) {
        for (int i = 0; i < this.stones.size(); i++) {
            this.stones.get(i).paint(g);
        }
    }

}
