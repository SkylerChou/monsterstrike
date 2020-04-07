/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import monsterstrike.gameobject.Arrow;
import monsterstrike.gameobject.Background;
import monsterstrike.gameobject.Button;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.ReboundBall;
import monsterstrike.gameobject.SpecialEffect;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.ReboundMarble;
import monsterstrike.graph.Rect;
import monsterstrike.graph.Vector;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class Pinball extends Scene {

    public static final int POS_X = 50;
    public static final int POS_Y = 50;

    private Background background;
    private ReboundBall marble;
    private int idx; //背景idx
    private Rect racket;
    private Button setting;
    private ArrayList<SpecialEffect> b;

    private Arrow arrow;

    public Pinball(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.idx = 1;
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx], 2 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);

        this.b = new ArrayList<>();
        this.marble = new ReboundBall(ImgInfo.MYMARBLE_PATH[0], ImgInfo.MYMARBLE_NAME[0], POS_X, POS_Y, ImgInfo.MYMARBLE_INFO[0]);
        this.racket = Rect.genWithCenter(50, 600, 120, 20);
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
         this.background.setX(2 * ImgInfo.BACKGROUND_SIZE[idx][0]);
    }

    @Override
    public void sceneUpdate() {
       
//        this.marble.update();
        System.out.println(this.marble.getCenterY());
        this.marble.bound();
        if ((racket.right() >= this.marble.getCenterX()-this.marble.getR()&& racket.left() <=this.marble.getCenterX()+this.marble.getR())&& (this.marble.getCenterY() + this.marble.getR() >= 590)) {
            this.marble.setCenterY(580 - this.marble.getR());
            this.marble.getGoVec().setY(-this.marble.getGoVec().getY());
        }
        this.marble.move();

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.background.paint(g);
        this.arrow.paint(g);
        this.marble.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        this.racket.paint(g2d);
    }

    @Override
    public CommandSolver.KeyListener getKeyListener() {
        return new MyKeyListener();
    }

    @Override
    public CommandSolver.MouseCommandListener getMouseListener() {
        return new MyMouseListener();
    }

    public class MyKeyListener implements CommandSolver.KeyListener {

        @Override
        public void keyPressed(int commandCode, long trigTime) {
            switch (commandCode) {
                case Global.UP:

                    break;
                case Global.DOWN:

                    break;
                case Global.LEFT:
                    racket.offset(-8, 0);
                    break;
                case Global.RIGHT:
                    racket.offset(8, 0);
                    break;
            }
        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {

        }

        @Override
        public void keyTyped(char c, long trigTime) {

        }

    }

    public class MyMouseListener implements CommandSolver.MouseCommandListener {

        private float startX;
        private float startY;
        private float endX;
        private float endY;

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {

            if (state == CommandSolver.MouseState.PRESSED) {
                this.startX = e.getX();
                this.startY = e.getY();
            }

            if (state == CommandSolver.MouseState.DRAGGED) {
                arrow.setCenterX(marble.getCenterX());
                arrow.setCenterY(marble.getCenterY());
                Vector vector = new Vector(this.startX - e.getX(), this.startY - e.getY());
                if (this.startY - e.getY() > 0) {
                    arrow.setDegree((float) -Math.acos(vector.getX() / vector.getValue()));
                } else {
                    arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                }
                float value = vector.getValue();
                if (vector.getValue() > 10 * marble.getR()) {
                    value = 10 * marble.getR();
                }
                arrow.setResizeMag(value / arrow.getWidth());
                arrow.setShow(true);
            }
            if (state == CommandSolver.MouseState.RELEASED) {
                this.endX = e.getX();
                this.endY = e.getY();
                Vector vector = new Vector(this.startX - this.endX, this.startY - this.endY);
                arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                arrow.setResizeMag(vector.getValue() / arrow.getWidth());
                marble.setGo(vector.resizeVec(marble.getVelocity()));
                arrow.setShow(false);
            }
        }
    }
}
