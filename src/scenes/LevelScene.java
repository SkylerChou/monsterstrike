/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import monsterstrike.graph.Vector;
import controllers.SceneController;
import interfaceskills.SkillComponent;
import interfaceskills.SkillImg;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import monsterstrike.gameobject.*;
import monsterstrike.gameobject.marble.*;
import monsterstrike.util.*;

public class LevelScene extends Scene {

    private Background background;
    private Item item;
    private Marble[] myMarbles; //資訊欄顯示怪物
    private ArrayList<Marble> marbles; //我方出戰怪物
    private MarbleArray enemies; //敵方所有怪物
    private Arrow arrow;
    private ArrayList<Marble> battleEnemies; //小關出戰怪物

    private int currentIdx;
    private int count;
    private int state;
    private int idx;
    private int sceneCount;
    private int hitCount;
    private int round;
    private int enemyRound;

    private Delay delay;

    public LevelScene(SceneController sceneController, int backIdx,
            Marble[] myMarbles, ArrayList<Marble> enemies) {
        super(sceneController);
        this.idx = backIdx;
        this.item = new Item(ImgInfo.ITEM_PATH[0], 0, 0, 0);
        this.marbles = new ArrayList<>();
        this.battleEnemies = new ArrayList<>();
        this.myMarbles = myMarbles;
        int w = 75;
        for (int i = 0; i < myMarbles.length; i++) {
            this.marbles.add(myMarbles[i].duplicate(Global.POSITION_X[i],
                    Global.POSITION_Y[i], 100, 100));
            this.myMarbles[i].setCenterX(w);
            this.myMarbles[i].setCenterY(Global.SCREEN_Y - 70);
            w += 150;
        }
        this.enemies = new MarbleArray(enemies);
        this.delay = new Delay(5);
        this.delay.start();
    }

    @Override
    public void sceneBegin() {
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx], 2 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);
        for (int i = 0; i < 3; i++) {
            this.marbles.get(i).setCenterX(Global.POSITION_X[i]);
            this.marbles.get(i).setCenterY(Global.POSITION_Y[i]);
        }
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.currentIdx = 0;
        this.count = 0;
        this.state = 0;
        this.sceneCount = 0;
        this.hitCount = 0;
        this.round = 0;
        this.enemyRound = 0;
        genBattleEnemies();
    }

    @Override
    public void sceneUpdate() {
//        if (this.delay.isTrig()) { //慢動作delay，Debug再開
        if (this.state == 0) { //設定背景起始位置， 敵人怪物降落
            this.background.setX(2 * ImgInfo.BACKGROUND_SIZE[idx][0]);
            dropEnemies();
        } else if (this.state == 1) { //遊戲開始

            for (int i = 0; i < this.battleEnemies.size(); i++) {
                if (this.battleEnemies.get(i).getIsCollide()) {
                    this.battleEnemies.get(i).update(); //敵人被撞到反應更新
                }
                this.battleEnemies.get(i).updateSkill(); //敵人技能更新
                this.battleEnemies.get(i).update();
            }

            //我方怪物動畫更新
            for (int i = 0; i < this.marbles.size(); i++) {
                this.marbles.get(i).updateShine(); //光圈更新
                this.marbles.get(i).update();      //怪物更新
                this.marbles.get(i).updateSkill(); //技能更新
            }

            normalAttack();
            criticalAttack();

            for (int i = 0; i < this.battleEnemies.size(); i++) { //判斷敵人是否死亡
                if (this.battleEnemies.get(i).getInfo().getHp() <= 0) {
                    this.battleEnemies.get(i).setIsCollide(false);
                    this.battleEnemies.get(i).setCenterY(Global.FRAME_Y + 200);
                    this.battleEnemies.remove(i);

                }
            }

            if (checkAllStop()) { //當所有我方怪物靜止
                for (int i = 0; i < this.marbles.size(); i++) {
                    this.marbles.get(i).setUseSkill(true);
                }

                if (this.count != 0) { //除了第一回合，更新要發動攻擊的我方怪物
                    this.marbles.get(currentIdx).setShine(false);
                    this.currentIdx = this.count % 3;
                    this.hitCount = 0;
                    this.marbles.get(currentIdx).setShine(true);
                }
                if (this.enemyRound != 0) {
                    if (enemyRound % 3 == 0) {
                        enemyAttack();
                    } else {
                        for (int i = 0; i < this.battleEnemies.size(); i++) {
                            this.battleEnemies.get(i).setUseSkill(true);
                        }
                    }
                }

                if (this.battleEnemies.isEmpty() && allSkillStop(this.marbles)) { //所有怪物死亡且技能都施放完畢
                    this.marbles.get(currentIdx).setShine(false);
                    this.round = 0;
                    this.enemyRound = 0;
                    this.state = 2;
                }
            }

        } else if (state == 2) { //若跑完3個小關回到選單，否則移動背景進入下一小關
            if (this.sceneCount == 2) {
                sceneController.changeScene(new LevelMenu(sceneController));
            }
            scrollScene();
        }
//        }
    }

    @Override
    public void sceneEnd() {
//        this.background = null;
//        this.marbles = null;
//        this.shine = null;
//        this.enimies = null;
//        this.arrow = null;
//        this.sceneCount = 0;
//        this.state = 0;
    }

    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    private boolean allSkillStop(ArrayList<Marble> marbles) {
        for (int i = 0; i < marbles.size(); i++) {
            if (!skillStop(marbles.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean skillStop(Marble marble) {
        if (marble.getSkillComponent() != null) {
            ArrayList<SkillComponent> skills = marble.getSkillComponent();
            for (int j = 0; j < skills.size(); j++) {
                if (!skills.get(j).getStop()) {
                    return false; //技能尚未釋放完畢
                }
            }
        }
        return true; //技能釋放完畢
    }

    private void criticalAttack() {
        for (int j = 0; j < this.marbles.size(); j++) {
            if (j == currentIdx) {
                continue;
            }
            for (int i = 0; i < this.marbles.size(); i++) {
                if (i != j && this.marbles.get(i).isCollision(this.marbles.get(j))) {
                    this.marbles.set(j, (Marble)this.marbles.get(i).strike(this.marbles.get(j)));
                    if (i == currentIdx) {
                        this.marbles.get(j).getGoVec().setValue(this.marbles.get(j).getGoVec().getValue() * 0.5f);
                        if (this.marbles.get(j).getUseSkill()) {
                            this.marbles.get(j).genSkill(1, this.battleEnemies);
                            checkStrike(j, this.marbles.get(j).getSkillComponent());
                        }
                        this.marbles.get(j).setUseSkill(false);
                    }
                }
            }
        }
    }

    private void normalAttack() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.battleEnemies.size(); j++) {
                if (this.marbles.get(i).isCollision(this.battleEnemies.get(j)) && this.marbles.get(i).getGoVec().getValue() > 0) {
                    Marble tmp = (Marble)this.marbles.get(i).strike(this.battleEnemies.get(j));
                    this.battleEnemies.get(j).setGo(tmp.getGoVec());
                    this.battleEnemies.get(j).setIsCollide(true);
                    this.marbles.get(i).genSkill(0, this.battleEnemies.get(j));
                    if (checkStrike(i, j, this.marbles.get(i).getSkillComponent())) {
                        this.battleEnemies.get(j).getInfo().setHp(this.battleEnemies.get(j).getInfo().getHp() - this.marbles.get(i).getInfo().getAtk());
                        this.hitCount++;
//                        System.out.println(this.battleEnemies.get(j).getInfo().getName() + "血量:" + this.battleEnemies.get(j).getInfo().getHp());
                    }
                }
            }
        }
    }

    private void checkStrike(int idx, ArrayList<SkillComponent> skills) {
        for (int l = 0; l < skills.size(); l++) {
            for (int k = 0; k < this.battleEnemies.size(); k++) {
                if ((skills.size() == 4 && inSkillRange(idx)) || skills.get(l).isCollision(this.battleEnemies.get(k))) {
                    this.battleEnemies.get(k).setIsCollide(true);
                    int atk = (int) (this.marbles.get(idx).getInfo().getAtk() * Math.random() * 2 + 1);
                    this.battleEnemies.get(k).getInfo().setHp(this.battleEnemies.get(k).getInfo().getHp() - atk);
                    this.hitCount++;
//                    System.out.println(this.battleEnemies.get(k).getInfo().getName() + "血量:" + this.battleEnemies.get(k).getInfo().getHp());
                }
            }
        }
    }

    private boolean checkStrike(int i, int j, ArrayList<SkillComponent> skills) {
        for (int l = 0; l < skills.size(); l++) {
            if (skills.get(l).isCollision(this.battleEnemies.get(j))) {
                return true;
            }
        }
        return false;
    }

    private void enemyAttack() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (this.battleEnemies.get(i).getUseSkill()) {
                if (i == 0) {
                    this.battleEnemies.get(i).genSkill(1, this.marbles);
                    this.battleEnemies.get(i).setUseSkill(false);
                } else if (checkSkillStop(this.battleEnemies.get(i - 1))) {
                    this.battleEnemies.get(i).genSkill(1, this.marbles);
                    this.battleEnemies.get(i).setUseSkill(false);
                }
            }
        }
    }

    private boolean checkSkillStop(Marble marble) {
        ArrayList<SkillComponent> skills = marble.getSkillComponent();
        if (skills != null) {
            for (int j = 0; j < skills.size(); j++) {
                if (!skills.get(j).getStop()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean inSkillRange(int idx) {
        int left = (int) (this.marbles.get(idx).getCenterX() - SkillImg.SKILL_UNIT_X[2][0] / 2);
        int right = (int) (this.marbles.get(idx).getCenterX() + SkillImg.SKILL_UNIT_X[2][0] / 2);
        int top = (int) (this.marbles.get(idx).getCenterY() - SkillImg.SKILL_UNIT_X[2][0] / 2);
        int bottom = (int) (this.marbles.get(idx).getCenterY() + SkillImg.SKILL_UNIT_X[2][0] / 2);
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (this.battleEnemies.get(i).getCenterX() + this.battleEnemies.get(i).getR() < left
                    && this.battleEnemies.get(i).getCenterY() + this.battleEnemies.get(i).getR() < top) {
                return false;
            }
            if (this.battleEnemies.get(i).getCenterX() - this.battleEnemies.get(i).getR() > right
                    && this.battleEnemies.get(i).getCenterY() + this.battleEnemies.get(i).getR() < top) {
                return false;
            }
            if (this.battleEnemies.get(i).getCenterX() + this.battleEnemies.get(i).getR() < left
                    && this.battleEnemies.get(i).getCenterY() - this.battleEnemies.get(i).getR() > bottom) {
                return false;
            }
            if (this.battleEnemies.get(i).getCenterX() - this.battleEnemies.get(i).getR() > right
                    && this.battleEnemies.get(i).getCenterY() - this.battleEnemies.get(i).getR() > bottom) {
                return false;
            }
        }
        return true;
    }

    private void scrollScene() {
        if (this.background.getX() > ImgInfo.BACKGROUND_SIZE[idx][0]) {
            this.background.offset(-10);
        }
        if (this.background.getX() <= ImgInfo.BACKGROUND_SIZE[idx][0]) {
            this.sceneCount++;
            genBattleEnemies();
            this.state = 0;
        }
    }

    private void genBattleEnemies() {
        if (this.sceneCount == 3) {
            sceneEnd();
        }
        ArrayList<Marble> m = this.enemies.sortByLevel();
        if (this.sceneCount == 0) {            
            for (int i = 0; i < 4; i++) {
                battleEnemies.add(m.get(0).duplicate(Global.ENEMYPOS_X[i], -100, 120, 120));
            }
        } else if (this.sceneCount == 1) {
            for (int i = 0; i < 4; i++) {
                battleEnemies.add(m.get(1).duplicate(Global.random(100, Global.SCREEN_X-100), -100, 120, 120));
            }
            battleEnemies.add(m.get(2).duplicate(Global.random(100, Global.SCREEN_X-100), -100, 120, 120));
        } else {
            for (int i = 0; i < 4; i++) {
                battleEnemies.add(m.get(2).duplicate(Global.random(100, Global.SCREEN_X-100), -100, 120, 120));
            }
            battleEnemies.add(m.get(3).duplicate(Global.random(100, Global.SCREEN_X-100), -100, 120, 120));       
        }
    }

    private void dropEnemies() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (this.battleEnemies.get(i).getCenterY() < Global.ENEMYPOS_Y[i]) {
                this.battleEnemies.get(i).offset(0, Global.ENEMYPOS_Y[i] / 40);
            }
        }
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (this.battleEnemies.get(i).getCenterY() >= Global.ENEMYPOS_Y[i]) {
                this.marbles.get(currentIdx).setShine(true);
                this.state = 1;
            }
        }
    }

    private boolean checkAllStop() {
        for (int i = 0; i < this.marbles.size(); i++) {
            if (this.marbles.get(i).getGoVec().getValue() != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void paint(Graphics g) {
        if (this.background != null) {
            this.background.paint(g);
        }
        if (this.arrow != null && this.arrow.getShow()) {
            this.arrow.paint(g);
        }
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            this.battleEnemies.get(i).paint(g);
            if (this.battleEnemies.get(i).getCurrentSkill() != null) {
                this.battleEnemies.get(i).paintSkill(g);
            }
        }
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).paintAll(g);
        }
        paintText(g);
    }

    private void paintText(Graphics g) {

        if (hitCount > 0) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("VinerHandITC", Font.ITALIC, 44));
            g.drawString("Hits " + hitCount, Global.SCREEN_X - 200, 100);
            g.setColor(new Color(255, 153, 0));
            g.drawString("Hits " + hitCount, Global.SCREEN_X - 200 - 3, 100 - 3);
        }
        if (round > 0) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("VinerHandITC", Font.ITALIC, 36));
            g.drawString("Round " + round, 30, 495);
            g.setColor(new Color(255, 153, 0));
            g.drawString("Round " + round, 30 - 3, 495 - 3);
        }
        g.setFont(new Font("VinerHandITC", Font.ITALIC, 36));
        g.setColor(Color.BLACK);
        this.item.paintItem(g, 0, Global.SCREEN_Y - Global.INFO_H, Global.SCREEN_X, Global.INFO_H);
        g.setColor(Color.GRAY);
        g.drawString("Battle " + (sceneCount + 1), 800, Global.SCREEN_Y - 40);
        g.setColor(Color.BLACK);
        g.drawString("Battle " + (sceneCount + 1), 800 - 3, Global.SCREEN_Y - 40 - 3);
        for (int i = 0; i < this.myMarbles.length; i++) {
            this.myMarbles[i].paintComponent(g);
        }
    }

    @Override
    public CommandSolver.KeyListener getKeyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseCommandListener getMouseListener() {
        return new MyMouseListener();

    }

    public class MyMouseListener implements CommandSolver.MouseCommandListener {

        private float startX;
        private float startY;
        private float endX;
        private float endY;

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState mouseState, long trigTime) {

            if (state == 1 && checkAllStop()) {
                arrow.setResizeMag(0);
                if (mouseState == CommandSolver.MouseState.PRESSED) {
                    this.startX = e.getX();
                    this.startY = e.getY();
                } else if (mouseState == CommandSolver.MouseState.DRAGGED) {
                    arrow.setCenterX(marbles.get(currentIdx).getCenterX());
                    arrow.setCenterY(marbles.get(currentIdx).getCenterY());
                    Vector vector = new Vector(this.startX - e.getX(), this.startY - e.getY());
                    if (this.startY - e.getY() > 0) {
                        arrow.setDegree((float) -Math.acos(vector.getX() / vector.getValue()));
                    } else {
                        arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                    }
                    float value = vector.getValue();
                    if (vector.getValue() > 10f * marbles.get(currentIdx).getR()) {
                        value = 10f * marbles.get(currentIdx).getR();
                    }
                    arrow.setResizeMag(value / arrow.getWidth());
                    arrow.setShow(true);
                } else {
                    arrow.setShow(false);
                }
                if (mouseState == CommandSolver.MouseState.RELEASED) {
                    this.endX = e.getX();
                    this.endY = e.getY();
                    Vector vector = new Vector(this.startX - this.endX, this.startY - this.endY);
                    arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                    arrow.setResizeMag(vector.getValue() / arrow.getWidth());
                    marbles.get(currentIdx).setGo(vector.resizeVec(marbles.get(currentIdx).getInfo().getV()));
                    count++;
                    round++;
                    enemyRound++;
                    arrow.setShow(false);
                }
            }
        }
    }
}
