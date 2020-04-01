/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import monsterstrike.gameobject.marble.*;
import monsterstrike.graph.Vector;
import monsterstrike.gameobject.*;
import monsterstrike.util.*;
import controllers.SceneController;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainScene extends Scene {

    public static final int POS_AX = 100;
    public static final int POS_AY = 100;
    public static final int POS_BX = Global.FRAME_X / 2;
    public static final int POS_BY = Global.FRAME_Y - 100;
    public static final int POS_CX = Global.FRAME_X - 100;
    public static final int POS_CY = 100;

    private Background background;
    private ArrayList<Marble> marbles;
    private ArrayList<SpecialEffect> shine;
    private Arrow arrow;
    private boolean isShine;
    private int currentIdx;
    private Delay delay;
    private int count;
    private int idx; //背景idx

    public MainScene(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.idx = 0;
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx],
                2 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);
        this.marbles = new ArrayList<>();
        this.shine = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.marbles.add(new ReboundMarble(ImgInfo.MYMARBLE_PATH[i], ImgInfo.MYMARBLE_NAME[i], Global.POSITION_X[i], Global.POSITION_Y[i], ImgInfo.MYMARBLE_INFO[i]));  
            this.shine.add(new SpecialEffect(ImgInfo.SHINE_PATH[ImgInfo.MYMARBLE_INFO[i][5]], (int) this.marbles.get(currentIdx).getCenterX(), (int) this.marbles.get(currentIdx).getCenterX(), ImgInfo.SHINE_INFO));
        }
        
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);

        this.currentIdx = 0;
        this.delay = new Delay(5);
        this.delay.start();
        this.count = 0;
        this.isShine = true;
    }

    @Override
    public void sceneUpdate() {
        this.shine.get(currentIdx).update();
        this.shine.get(currentIdx).setCenterX(this.marbles.get(currentIdx).getCenterX());
        this.shine.get(currentIdx).setCenterY(this.marbles.get(currentIdx).getCenterY());
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).update();
        }

        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = i + 1; j < this.marbles.size(); j++) {
                if (this.marbles.get(i).isCollision(this.marbles.get(j))) {
                    this.marbles.get(i).useSkill(0, this.marbles.get(j));
                    this.marbles.set(j, this.marbles.get(i).strike(this.marbles.get(j)));
                }
            }
        }

        if (checkAllStop()) {
            if (this.count != 0) {
                this.currentIdx = this.count % 3;
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
        if (isShine) {
            this.shine.get(currentIdx).paint(g);
        }
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).paint(g);
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
                marbles.get(currentIdx).setGo(vector.resizeVec(marbles.get(currentIdx).getVelocity()));
                count++;
                arrow.setShow(false);
            }
        }
    }
}
