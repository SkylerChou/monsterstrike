/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import collisionproject.gameobject.*;
import collisionproject.util.*;
import controller.SceneController;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainScene extends Scene {

    public static final int POS_AX = 100;
    public static final int POS_AY = 100;
    public static final int POS_BX = 400;
    public static final int POS_BY = 500;
    public static final int POS_CX = 700;
    public static final int POS_CY = 100;

    private Background background;
    private ArrayList<Marble> marbles;
    private GameObject shine;
    private boolean isShine;
    private int currentIdx;
    private Delay delay;
    private int value;
    private int count;

    public MainScene(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.background = new Background(ImgInfo.BACKGROUND, 0, 0, Global.SCREAN_X, Global.SCREAN_Y);
        this.marbles = new ArrayList<>();
        this.marbles.add(new Marble(ImgInfo.GREENGENIE,"綠水靈", POS_AX, POS_AY, ImgInfo.GREENGENIE_INFO,0));//冰
        this.marbles.add(new Marble(ImgInfo.MUSHROOM, "菇菇寶貝",POS_BX, POS_BY, ImgInfo.MUSHROOM_INFO,1));//火
        this.marbles.add(new Marble(ImgInfo.PIG,"肥肥", POS_CX,POS_CY, ImgInfo.PIG_INFO,2));//草

        this.currentIdx = 0;
        this.value = 0;
        this.delay = new Delay(5);
        this.delay.start();
        this.count = 0;
        this.shine = new SpecialEffectIce(ImgInfo.SHINE, 0, 0, ImgInfo.SHINE_INFO);
        this.shine.setCenterX(this.marbles.get(currentIdx).getCenterX());
        this.shine.setCenterY(this.marbles.get(currentIdx).getCenterY());
        this.isShine = true;
    }

    @Override
    public void sceneUpdate() {
        this.shine.update();
        if (this.delay.isTrig()) {
            for (int i = 0; i < this.marbles.size(); i++) {
                this.marbles.get(i).update();
            }

            for (int i = 0; i < this.marbles.size(); i++) {
                for (int j = i + 1; j < this.marbles.size(); j++) {
                    if (this.marbles.get(i).isCollision(this.marbles.get(j))) {
                        this.marbles.get(i).setIsCollide(true);
                        this.marbles.get(i).useSkill(this.marbles.get(j));
                        this.marbles.set(j, this.marbles.get(i).strike(this.marbles.get(j)));
                    }
                }
            }
        }
        if (checkAllStop()) {
            this.shine.setCenterX(this.marbles.get(currentIdx).getCenterX());
            this.shine.setCenterY(this.marbles.get(currentIdx).getCenterY());
            this.isShine = true;
            if (this.count != 0) {
                this.currentIdx = this.count % 3;
                for (int i = 0; i < this.marbles.size(); i++) {
                    this.marbles.get(i).reset();
                }
            }
        }else {
            this.isShine = false;
        }
    }

    private boolean checkAllStop() {
        int j = 0;
        for (int i = 0; i < this.marbles.size(); i++) {
            if (this.marbles.get(i).getV() == 0 || this.marbles.get(i).getGoVec().getValue() == 0) {
                j++;
            }
        }
        if (j == 3) {
            return true;
        }
        return false;
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.background.paint(g);
        if (isShine) {
            this.shine.paint(g);
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
                Vector vector = new Vector(this.startX - e.getX(), this.startY - e.getY());
                //value = (int) this.vector.getValue();
            }
            if (checkAllStop() && state == CommandSolver.MouseState.RELEASED) {
                this.endX = e.getX();
                this.endY = e.getY();
                Vector vector = new Vector(this.startX - this.endX, this.startY - this.endY);
                marbles.get(currentIdx).setGo(vector.getUnitVec());
                value = (int) vector.getValue();
                count++;
            }
        }
    }
}
