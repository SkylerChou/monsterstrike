/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import monsterstrike.gameobject.marble.PenetrateMarble;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.StandMarble;
import monsterstrike.gameobject.marble.ReboundMarble;
import monsterstrike.graph.Vector;
import controllers.SceneController;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import monsterstrike.gameobject.*;
import monsterstrike.util.*;

public class Stage2Scene extends Scene {

    private Background background;
    private ArrayList<Marble> marbles;
    private ArrayList<Marble> enimies;
    private ArrayList<SpecialEffect> shine;
    private Arrow arrow;
    private int currentIdx;
    private Delay delay;
    private int count;

    public Stage2Scene(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.background = new Background(ImgInfo.BACKGROUND, 0, 0, Global.SCREEN_X, Global.SCREEN_Y);
        this.marbles = new ArrayList<>();
        this.shine = new ArrayList<>();
        this.enimies = new ArrayList<>();
        this.marbles.add(new ReboundMarble(ImgInfo.SWEETPOTATO, "番薯", Global.POSITION_X[0], Global.POSITION_Y[0], ImgInfo.SWEETPOTATO_INFO, 0));//冰
        this.marbles.add(new PenetrateMarble(ImgInfo.DEVIL, "小惡魔", Global.POSITION_X[1], Global.POSITION_Y[1], ImgInfo.DEVIL_INFO, 1));//火
        this.marbles.add(new ReboundMarble(ImgInfo.RICEBALL, "飯糰", Global.POSITION_X[2], Global.POSITION_Y[2], ImgInfo.RICEBALL_INFO, 2));//草
        this.shine.add(new SpecialEffect(ImgInfo.SHINE_ICE, (int) this.marbles.get(currentIdx).getCenterX(), (int) this.marbles.get(currentIdx).getCenterX(), ImgInfo.SHINE_INFO));
        this.shine.add(new SpecialEffect(ImgInfo.SHINE_FIRE, (int) this.marbles.get(currentIdx).getCenterX(), (int) this.marbles.get(currentIdx).getCenterX(), ImgInfo.SHINE_INFO));
        this.shine.add(new SpecialEffect(ImgInfo.SHINE_GRASS, (int) this.marbles.get(currentIdx).getCenterX(), (int) this.marbles.get(currentIdx).getCenterX(), ImgInfo.SHINE_INFO));
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.currentIdx = 0;
        this.delay = new Delay(5);
        this.delay.start();
        this.count = 0;

        this.enimies.add(new StandMarble(ImgInfo.ZOMBIE, "殭屍", Global.ENEMYPOS_X[0], Global.ENEMYPOS_Y[0], ImgInfo.ZOMBIE_INFO, 0));//冰
        this.enimies.add(new StandMarble(ImgInfo.SKULL, "骷髏頭", Global.ENEMYPOS_X[1], Global.ENEMYPOS_Y[1], ImgInfo.SKULL_INFO, 1));//火
        this.enimies.add(new StandMarble(ImgInfo.SPIKY, "刺刺", Global.ENEMYPOS_X[2], Global.ENEMYPOS_Y[2], ImgInfo.SPIKY_INFO, 2));//草
    }

    @Override
    public void sceneUpdate() {
        for (int i = 0; i < this.enimies.size(); i++) {
            if (this.enimies.get(i).getIsCollide()) {
                this.enimies.get(i).update();
            }
        }
        this.shine.get(currentIdx).update();
        this.shine.get(currentIdx).setCenterX(this.marbles.get(currentIdx).getCenterX());
        this.shine.get(currentIdx).setCenterY(this.marbles.get(currentIdx).getCenterY());
        if (this.delay.isTrig()) {
            for (int i = 0; i < this.marbles.size(); i++) {
                this.marbles.get(i).update();
            }

            for (int i = 0; i < this.marbles.size(); i++) {
                for (int j = i + 1; j < this.marbles.size(); j++) {
                    if (this.marbles.get(i).isCollision(this.marbles.get(j))) {
                        this.marbles.set(j, this.marbles.get(i).strike(this.marbles.get(j)));
                        for (int k = 0; k < this.enimies.size(); k++) {
                            this.marbles.get(j).useSkill(1, this.enimies.get(k));
                        }
                    }
                }
            }

            for (int i = 0; i < this.marbles.size(); i++) {
                for (int j = 0; j < this.enimies.size(); j++) {
                    if (this.marbles.get(i).isCollision(this.enimies.get(j))) {
                        this.marbles.get(i).strike(this.enimies.get(j));
                        this.enimies.get(j).setIsCollide(true);
                        this.marbles.get(i).useSkill(0, this.enimies.get(j));
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
                if (vector.getValue() > 10 * marbles.get(currentIdx).getR()) {
                    value = 10 * marbles.get(currentIdx).getR();
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
                marbles.get(currentIdx).setGo(vector.multiplyScalar(0.1f));
                count++;
                arrow.setShow(false);
            }
        }
    }
}
