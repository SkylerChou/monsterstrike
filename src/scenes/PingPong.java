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
import monsterstrike.gameobject.*;
import monsterstrike.graph.*;
import monsterstrike.util.*;

public class PingPong extends Scene {

    public static final int POS_X = 50;
    public static final int POS_Y = 50;

    private Background background;
    private Ball ball;
    private ArrayList<Obstacle> post;
    private int idx; //背景idx
    private Rect racket;
    private Dino dino;
    private Arrow arrow;

    public PingPong(SceneController sceneController) {
        super(sceneController);
        this.post = new ArrayList<>();
    }

    @Override
    public void sceneBegin() {
        for (int i = 0; i < 5; i++) {
            int y = Global.random(50, 200);
            this.post.add(new Obstacle(ImgInfo.POSTS_PATH, 150 + i * 250, 150 + y,
                    ImgInfo.POST_INFO[0], ImgInfo.POST_INFO[1], ImgInfo.POST_INFO[2], ImgInfo.POST_INFO[3]));
        }
        this.background = new Background(ImgInfo.PINGPONG, 0, 0, 0);
        this.dino = new Dino(ImgInfo.GREENDINO, 500, 500, Dino.STEPS_WALKRIGHT);
        this.ball = new Ball(ImgInfo.MYMARBLE_PATH[0], POS_X, POS_Y, 120, 120, 40, 1);
        this.racket = Rect.genWithCenter(50, 600, 120, 20);
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.background.setX(2 * ImgInfo.BACKGROUND_SIZE[idx][0]);
    }

    @Override
    public void sceneUpdate() {
        this.ball.update();
        this.ball.isBound();
        this.dino.update();
        if ((racket.right() >= this.ball.getCenterX() - this.ball.getR()
                && racket.left() <= this.ball.getCenterX() + this.ball.getR())
                && (this.ball.getCenterY() + this.ball.getR() >= 590)) {
            this.ball.setCenterY(580 - this.ball.getR());
            this.ball.getGoVec().setY(-this.ball.getGoVec().getY());
        } else {
            this.ball.move();
        }

        if (this.ball.isCollision(this.dino)) {
            this.ball.hit(dino);
        }
        for (int i = 0; i < this.post.size(); i++) {
            if (this.post.get(i).getIsCollide()) {
                this.post.get(i).update();
            }
        }
        for (int i = 0; i < this.post.size(); i++) {
            if (this.ball.isCollision(this.post.get(i)) && this.ball.getGoVec().getValue() > 0) {
                this.post.get(i).setGo(this.ball.getGoVec());
                this.ball.hit(this.post.get(i));
                this.post.get(i).setIsCollide(true);
            }
        }
        for (int i = 0; i < this.post.size(); i++) {
            if (this.dino.isCollision(this.post.get(i))) {
                Vector vec = new Vector(this.post.get(i).getCenterX() - this.dino.getCenterX(),
                        this.post.get(i).getCenterY() - this.dino.getCenterY());
                Vector dinoVec = dinoDir();
//                float dx = dinoVec.getCosProjectionVec(vec).getX();
//                float dy = dinoVec.getCosProjectionVec(vec).getY();
                if (!isBound(i)) {
                    this.post.get(i).offset(dinoVec.getX(), dinoVec.getY());
                }
            }
        }
    }

    @Override
    public void sceneEnd() {

    }

    public boolean isBound(int i) {
            if (this.post.get(i).getCenterX() - this.post.get(i).getR() <=0
                    || this.post.get(i).getCenterX()  + this.post.get(i).getR() >=Global.SCREEN_X
                    || this.post.get(i).getCenterY()  - this.post.get(i).getR() <=0
                    || this.post.get(i).getCenterY() + this.post.get(i).getR() >= Global.SCREEN_Y) {
                if (this.post.get(i).getCenterX() - this.post.get(i).getR() <= 0) {
                    this.post.get(i).setCenterX(this.post.get(i).getR());
                }
                if (this.post.get(i).getCenterX() + this.post.get(i).getR() >= Global.SCREEN_X) {
                    this.post.get(i).setCenterX(Global.SCREEN_X);
                }
                if (this.post.get(i).getCenterY() - this.post.get(i).getR() <= 0) {
                    this.post.get(i).setCenterY(this.post.get(i).getR());
                }
                if (this.post.get(i).getCenterY() + this.post.get(i).getR()>= Global.SCREEN_Y) {
                    this.post.get(i).setCenterY(Global.SCREEN_Y);
                } 
            return true;
        }
        return false;
    }

    private Vector dinoDir() {
        switch (this.dino.getDir()) {
            case 5:
                return new Vector(1, 0).multiplyScalar(this.dino.getVelocity());
            case 6:
                return new Vector(-1, 0).multiplyScalar(this.dino.getVelocity());
            case 7:
                return new Vector(0, -1).multiplyScalar(this.dino.getVelocity());
            case 8:
                return new Vector(0, 1).multiplyScalar(this.dino.getVelocity());
        }
        return null;
    }

    private boolean checkStop() {
        if (this.ball.getGoVec().getValue() != 0) {
            return false;
        }
        return true;
    }

    @Override
    public void paint(Graphics g) {
        this.background.paintMenu(g);
        this.arrow.paint(g);
        this.dino.paint(g);
        this.ball.paint(g);
        for (int i = 0; i < 5; i++) {
            this.post.get(i).paint(g);
        }

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
                case Global.UP2:
                    dino.setStand(false);
                    dino.setDir(Global.UP2);
                    dino.moveArround();
                    break;
                case Global.DOWN2:
                    dino.setStand(false);
                    dino.setDir(Global.DOWN2);
                    dino.moveArround();
                    break;
                case Global.LEFT2:
                    dino.setStand(false);
                    dino.setDir(Global.LEFT2);
                    dino.setCurrentImg(dino.getImg2());
                    dino.setSteps(Dino.STEPS_WALKLEFT);
                    dino.moveArround();
                    break;
                case Global.RIGHT2:
                    dino.setStand(false);
                    dino.setDir(Global.RIGHT2);
                    dino.setCurrentImg(dino.getImg1());
                    dino.setSteps(Dino.STEPS_WALKRIGHT);
                    dino.moveArround();
                    break;
                case Global.LEFT:
                    if (racket.left() > 0) {
                        racket.offset(-7, 0);
                    }
                    break;
                case Global.RIGHT:
                    if (racket.right() < Global.SCREEN_X) {
                        racket.offset(7, 0);
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {
            dino.setStand(true);
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

            if (checkStop()) {
                if (state == CommandSolver.MouseState.PRESSED) {
                    this.startX = e.getX();
                    this.startY = e.getY();
                }

                if (state == CommandSolver.MouseState.DRAGGED) {
                    arrow.setCenterX(ball.getCenterX());
                    arrow.setCenterY(ball.getCenterY());
                    Vector vector = new Vector(this.startX - e.getX(), this.startY - e.getY());
                    if (this.startY - e.getY() > 0) {
                        arrow.setDegree((float) -Math.acos(vector.getX() / vector.getValue()));
                    } else {
                        arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                    }
                    float value = vector.getValue();
                    if (vector.getValue() > 10 * ball.getR()) {
                        value = 10 * ball.getR();
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
                    ball.setGo(vector.resizeVec(5));
                    arrow.setShow(false);
                }
            }
        }
    }

}
