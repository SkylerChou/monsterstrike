/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import monsterstrike.graph.Vector;
import controllers.SceneController;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import monsterstrike.gameobject.*;
import monsterstrike.gameobject.marble.*;
import monsterstrike.util.*;

public class Stage1Scene extends Scene {

    private Background background;
    private ArrayList<Marble> marbles;
    private ArrayList<Marble> enimies;
    private ArrayList<SpecialEffect> shine;
    private Arrow arrow;

    private int currentIdx;
    private int count;
    private int state;
    private Delay dropDelay;
    private int idx;

    public Stage1Scene(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.idx = 2;
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx], 2 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);
        this.marbles = new ArrayList<>();
        this.shine = new ArrayList<>();
        this.enimies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.marbles.add(new ReboundMarble(ImgInfo.MYMARBLE_PATH[i], ImgInfo.MYMARBLE_NAME[i], Global.POSITION_X[i], Global.POSITION_Y[i], ImgInfo.MYMARBLE_INFO[i]));
            this.enimies.add(new StandMarble(ImgInfo.ENEMY_PATH[i], ImgInfo.ENEMY_NAME[i], Global.ENEMYPOS_X[i], -100, ImgInfo.ENEMY_INFO[i]));
        }
        this.shine.add(new SpecialEffect(ImgInfo.SHINE_ICE, (int) this.marbles.get(currentIdx).getCenterX(), (int) this.marbles.get(currentIdx).getCenterX(), ImgInfo.SHINE_INFO));
        this.shine.add(new SpecialEffect(ImgInfo.SHINE_FIRE, (int) this.marbles.get(currentIdx).getCenterX(), (int) this.marbles.get(currentIdx).getCenterX(), ImgInfo.SHINE_INFO));
        this.shine.add(new SpecialEffect(ImgInfo.SHINE_GRASS, (int) this.marbles.get(currentIdx).getCenterX(), (int) this.marbles.get(currentIdx).getCenterX(), ImgInfo.SHINE_INFO));
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.currentIdx = 0;
        this.count = 0;

        this.state = 0;
    }

    @Override
    public void sceneUpdate() {
        if (this.state == 0) {
            this.background.setX(2 * ImgInfo.BACKGROUND_SIZE[idx][0]);
            this.dropEnemies();
        } else if (this.state == 1) {
            for (int i = 0; i < this.enimies.size(); i++) {
                if (this.enimies.get(i).getIsCollide()) {
                    this.enimies.get(i).update();
                }
            }
            this.shine.get(currentIdx).setShine(true);
            this.shine.get(currentIdx).update();
            this.shine.get(currentIdx).setCenterX(this.marbles.get(currentIdx).getCenterX());
            this.shine.get(currentIdx).setCenterY(this.marbles.get(currentIdx).getCenterY());

            for (int i = 0; i < this.marbles.size(); i++) {
                this.marbles.get(i).update();
            }

            for (int i = 0; i < this.marbles.size(); i++) {
                for (int j = i + 1; j < this.marbles.size(); j++) {
                    if (this.marbles.get(i).isCollision(this.marbles.get(j))) {
                        this.marbles.set(j, this.marbles.get(i).strike(this.marbles.get(j)));
                        for (int k = 0; k < this.enimies.size(); k++) {
//                            this.marbles.get(j).useSkill(1, this.enimies.get(k));
                        }
                    }
                }
            }

            for (int i = 0; i < this.marbles.size(); i++) {
                for (int j = 0; j < this.enimies.size(); j++) {
                    if (this.marbles.get(i).isCollision(this.enimies.get(j)) && this.marbles.get(i).getGoVec().getValue()>0) {
                        Marble tmp = this.marbles.get(i).strike(this.enimies.get(j));
                        this.enimies.get(j).setGo(tmp.getGoVec());
                        this.enimies.get(j).setIsCollide(true);
                        this.marbles.get(i).useSkill(0, this.enimies.get(j));
                        if (this.enimies.get(j).getHp() <= 0) {
                            this.enimies.get(j).setCenterY(Global.FRAME_Y + 200); //敵人死亡
                        }
                    }
                }
            }
            if (checkAllStop()) {
                if (this.count != 0) {
                    this.currentIdx = this.count % 3;
                }
                for (int j = 0; j < this.enimies.size(); j++) {
                    if (this.enimies.get(j).getIsCollide()) {
                        this.enimies.get(j).setIsCollide(false);
                    }
                }

                if (isEnd()) {
                    this.shine.get(currentIdx).setShine(false);
                    this.state = 2;
                }
            }

        } else if (state == 2) {
            scrollScene();
            resetEnemies();
        }

    }

    private void resetEnemies() {
        for (int i = 0; i < 3; i++) {
            this.enimies.set(i, new StandMarble(ImgInfo.ENEMY_PATH[i], ImgInfo.ENEMY_NAME[i], Global.ENEMYPOS_X[i], -100, ImgInfo.ENEMY_INFO[i]));
        }
    }

    private void scrollScene() {
        if (this.background.getX() > ImgInfo.BACKGROUND_SIZE[idx][0]) {
            this.background.offset(-5);
        }
        if (this.background.getX() <= ImgInfo.BACKGROUND_SIZE[idx][0]) {
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
                this.state = 1;
            }
        }
    }

    private boolean isEnd() {
        for (int i = 0; i < this.enimies.size(); i++) {
            if (this.enimies.get(i).getHp() > 0) {
                return false;
            }
        }
        return true;
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
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.background.paint(g);
        if (this.arrow.getShow()) {
            this.arrow.paint(g);
        }
        this.shine.get(currentIdx).paint(g);
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).paint(g);
        }
        for (int i = 0; i < this.enimies.size(); i++) {
            this.enimies.get(i).paint(g);
        }
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).paintSkill(g);
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
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {

            if (checkAllStop() && state == CommandSolver.MouseState.PRESSED) {
                this.startX = e.getX();
                this.startY = e.getY();
            }

            if (checkAllStop() && state == CommandSolver.MouseState.DRAGGED) {
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
            if (checkAllStop() && state == CommandSolver.MouseState.RELEASED) {
                this.endX = e.getX();
                this.endY = e.getY();
                Vector vector = new Vector(this.startX - this.endX, this.startY - this.endY);
                arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                arrow.setResizeMag(vector.getValue() / arrow.getWidth());
                marbles.get(currentIdx).setGo(vector.resizeVec(marbles.get(currentIdx).getVelocity()));
                count++;
                arrow.setShow(false);
            }
        }
    }
}
