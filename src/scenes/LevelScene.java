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
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import monsterstrike.gameobject.*;
import monsterstrike.gameobject.marble.*;
import monsterstrike.util.*;

public class LevelScene extends Scene {

    private Background background;
    private Item infoForm;
    private ArrayList<Marble> marbles;
    private ArrayList<Marble> enimies;
    private Arrow arrow;

    private int currentIdx;
    private int count;
    private int state;
    private int idx;
    private int sceneCount;
    private int hitCount;
    private int round;

    private Delay delay;

    public LevelScene(SceneController sceneController, int backIdx, Marble[] myMarbles) {
        super(sceneController);
        this.idx = backIdx;
        this.infoForm = new Item(ImgInfo.INFOFORM_PATH, 0, 0, 4);
        this.marbles = new ArrayList<>();
        for (int i = 0; i < myMarbles.length; i++) {
            this.marbles.add(myMarbles[i]);
        }
        this.enimies = new ArrayList<>();
        this.delay = new Delay(5);
        this.delay.start();
    }

    @Override
    public void sceneBegin() {
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx], 2 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);
        for (int i = 0; i < 3; i++) {
            this.marbles.get(i).setCenterX(Global.POSITION_X[i]);
            this.marbles.get(i).setCenterY(Global.POSITION_Y[i]);
            this.enimies.add(new StandMarble(ImgInfo.ENEMY_PATH[i], ImgInfo.ENEMY_NAME[i], Global.ENEMYPOS_X[i], -100, ImgInfo.ENEMY_INFO[i]));
        }

        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.currentIdx = 0;
        this.count = 0;
        this.state = 0;
        this.sceneCount = 0;
        this.hitCount = 0;
        this.round = 0;
    }

    @Override
    public void sceneUpdate() {
//        if (this.delay.isTrig()) {
        if (this.state == 0) {
            this.background.setX(2 * ImgInfo.BACKGROUND_SIZE[idx][0]);
            this.dropEnemies();
        } else if (this.state == 1) {
            for (int i = 0; i < this.enimies.size(); i++) {
                if (this.enimies.get(i).getIsCollide()) {
                    this.enimies.get(i).update();
                }
            }

            for (int i = 0; i < this.marbles.size(); i++) {
                this.marbles.get(i).updateShine();
                this.marbles.get(i).update();
            }

            normalAttack();
            criticalAttack();

            for (int i = 0; i < this.enimies.size(); i++) {
                if (this.enimies.get(i).getHp() <= 0 && this.enimies.get(i).die()) {
                    this.enimies.get(i).setIsCollide(false);
                    this.enimies.get(i).setCenterY(Global.FRAME_Y + 200);
                    this.enimies.remove(i); //敵人死亡
                }
            }

            if (checkAllStop()) {
                for (int i = 0; i < this.marbles.size(); i++) {
                    this.marbles.get(i).setUseSkill(true);
                }

                if (this.count != 0) {
                    this.marbles.get(currentIdx).setShine(false);
                    this.currentIdx = this.count % 3;
                    this.hitCount = 0;
                    this.marbles.get(currentIdx).setShine(true);
                }

                if (this.enimies.isEmpty() && allSkillStop()) {
                    this.marbles.get(currentIdx).setShine(false);
                    this.round = 0;
                    this.state = 2;
                }
            }

        } else if (state == 2) {
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

    private boolean allSkillStop() {
        for (int i = 0; i < this.marbles.size(); i++) {
            if (this.marbles.get(i).getSkillComponent() != null) {
                ArrayList<SkillComponent> skills = this.marbles.get(i).getSkillComponent();
                for (int j = 0; j < skills.size(); j++) {
                    if (!skills.get(j).getStop()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void resetEnemies() {
        for (int i = 0; i < 3; i++) {
            this.enimies.add(new StandMarble(ImgInfo.ENEMY_PATH[i], ImgInfo.ENEMY_NAME[i], Global.random(100, 700), -100, ImgInfo.ENEMY_INFO[i]));
        }
        if (this.sceneCount == 3) {
            sceneEnd();
        }
    }

    private void criticalAttack() {
        for (int j = 0; j < this.marbles.size(); j++) {
            if (j == currentIdx) {
                continue;
            }
            for (int i = 0; i < this.marbles.size(); i++) {
                if (i != j && this.marbles.get(i).isCollision(this.marbles.get(j))) {
                    this.marbles.set(j, this.marbles.get(i).strike(this.marbles.get(j)));
                    if (i == currentIdx) {
                        this.marbles.get(j).setGo(this.marbles.get(j).getGoVec().multiplyScalar(0.1f));
                        if (this.marbles.get(j).getUseSkill()) {
                            this.marbles.get(j).genSkill(1, this.enimies);
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
            for (int j = 0; j < this.enimies.size(); j++) {
                if (this.marbles.get(i).isCollision(this.enimies.get(j)) && this.marbles.get(i).getGoVec().getValue() > 0) {
                    Marble tmp = this.marbles.get(i).strike(this.enimies.get(j));
                    this.enimies.get(j).setGo(tmp.getGoVec());
                    this.enimies.get(j).setIsCollide(true);
                    this.marbles.get(i).genSkill(0, this.enimies.get(j));
                    if (checkStrike(i, j, this.marbles.get(i).getSkillComponent())) {
                        this.enimies.get(j).setHp(this.enimies.get(j).getHp() - this.marbles.get(i).getAtk());
                        this.hitCount++;
                    }
                }
            }
        }
    }

    private void checkStrike(int idx, ArrayList<SkillComponent> skills) {
        for (int l = 0; l < skills.size(); l++) {
            for (int k = 0; k < this.enimies.size(); k++) {
                if ((skills.size() == 4 && inSkillRange(idx)) || skills.get(l).isCollision(this.enimies.get(k))) {
                    this.enimies.get(k).setIsCollide(true);
                    int atk = (int) (this.marbles.get(idx).getAtk() * Math.random() * 2 + 1);
                    this.enimies.get(k).setHp(this.enimies.get(k).getHp() - atk);
                    this.hitCount++;
//                    System.out.println(this.enimies.get(k).getName() + "血量:" + this.enimies.get(k).getHp());
                }
            }
        }
    }

    private boolean checkStrike(int i, int j, ArrayList<SkillComponent> skills) {
        for (int l = 0; l < skills.size(); l++) {
            if (skills.get(l).isCollision(this.enimies.get(j))) {
                return true;
//                this.enimies.get(j).setHp(this.enimies.get(j).getHp() - this.marbles.get(i).getAtk());
//                this.hitCount++;
            }
        }
        return false;
    }

    private boolean inSkillRange(int idx) {
        int left = (int) (this.marbles.get(idx).getCenterX() - SkillImg.SKILL_UNIT_X[2][0] / 2);
        int right = (int) (this.marbles.get(idx).getCenterX() + SkillImg.SKILL_UNIT_X[2][0] / 2);
        int top = (int) (this.marbles.get(idx).getCenterY() - SkillImg.SKILL_UNIT_X[2][0] / 2);
        int bottom = (int) (this.marbles.get(idx).getCenterY() + SkillImg.SKILL_UNIT_X[2][0] / 2);
        for (int i = 0; i < this.enimies.size(); i++) {
            if (this.enimies.get(i).getCenterX() + this.enimies.get(i).getR() < left
                    && this.enimies.get(i).getCenterY() + this.enimies.get(i).getR() < top) {
                return false;
            }
            if (this.enimies.get(i).getCenterX() - this.enimies.get(i).getR() > right
                    && this.enimies.get(i).getCenterY() + this.enimies.get(i).getR() < top) {
                return false;
            }
            if (this.enimies.get(i).getCenterX() + this.enimies.get(i).getR() < left
                    && this.enimies.get(i).getCenterY() - this.enimies.get(i).getR() > bottom) {
                return false;
            }
            if (this.enimies.get(i).getCenterX() - this.enimies.get(i).getR() > right
                    && this.enimies.get(i).getCenterY() - this.enimies.get(i).getR() > bottom) {
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
            resetEnemies();
            this.state = 0;
        }
    }

    private void dropEnemies() {
        for (int i = 0; i < this.enimies.size(); i++) {
            if (this.enimies.get(i).getCenterY() < Global.ENEMYPOS_Y[i]) {
                this.enimies.get(i).offset(0, Global.ENEMYPOS_Y[i] / 40);
            }
        }
        for (int i = 0; i < this.enimies.size(); i++) {
            if (this.enimies.get(i).getCenterY() >= Global.ENEMYPOS_Y[i]) {
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
        for (int i = 0; i < this.enimies.size(); i++) {
            this.enimies.get(i).paint(g);
        }
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).paintAll(g);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.ORANGE);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 48));
        g2d.drawString("Round " + round, 50, 495);
        g2d.drawString("Hits " + hitCount, Global.SCREEN_X - 200, 100);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Algerian", Font.PLAIN, 48));
        this.infoForm.paintItem(g, 0, Global.SCREEN_Y - Global.INFO_H, Global.SCREEN_X, Global.INFO_H);
        g2d.drawString(""+(sceneCount+1), 900, Global.SCREEN_Y - 40);
        int w = 0;
        for (int i = 0; i < this.marbles.size(); i++) {           
            this.marbles.get(i).paintScale(g, 25 + w, Global.SCREEN_Y - 125, 120, 120);
            w += 150;
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

            if (state == 1 && checkAllStop() && mouseState == CommandSolver.MouseState.PRESSED) {
                this.startX = e.getX();
                this.startY = e.getY();
            }

            if (state == 1 && checkAllStop() && mouseState == CommandSolver.MouseState.DRAGGED) {
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
            }
            if (state == 1 && checkAllStop() && mouseState == CommandSolver.MouseState.RELEASED) {
                this.endX = e.getX();
                this.endY = e.getY();
                Vector vector = new Vector(this.startX - this.endX, this.startY - this.endY);
                arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                arrow.setResizeMag(vector.getValue() / arrow.getWidth());
                marbles.get(currentIdx).setGo(vector.resizeVec(marbles.get(currentIdx).getVelocity()));
                count++;
                round++;
                arrow.setShow(false);
            }
        }
    }
}
