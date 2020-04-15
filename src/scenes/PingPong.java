/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.IRC;
import controllers.SceneController;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
    private SpecialEffect home;

    private int dinoGetNum;
    private BufferedImage scoreBoard;
    private int Score;

    public PingPong(SceneController sceneController) {
        super(sceneController);
        this.post = new ArrayList<>();
    }

    @Override
    public void sceneBegin() {
        this.scoreBoard=IRC.getInstance().tryGetImage("/resources/score.png");
        for (int i = 0; i < 10; i++) {
            int y = Global.random(50, 200);
            this.post.add(new Obstacle(ImgInfo.POSTS_PATH, 150 + i * 150, 150 + y,
                    ImgInfo.POST_INFO[0], ImgInfo.POST_INFO[1], ImgInfo.POST_INFO[2], ImgInfo.POST_INFO[3]));
        }
        
        
        this.home = new SpecialEffect(ImgInfo.BALCKHOLE, 1000, 500,
                ImgInfo.BLACKHOLE_INFO[0], ImgInfo.BLACKHOLE_INFO[1], 40);
        this.home.setShine(true);
        this.background = new Background(ImgInfo.PINGPONG, 0, 0);
        this.dino = new Dino(ImgInfo.GREENDINO, 500, 500, Dino.STEPS_WALKRIGHT);
        this.ball = new Ball(ImgInfo.MYMARBLE_PATH[0], POS_X, POS_Y, 100, 100, 25, 1);
        this.racket = Rect.genWithCenter(50, 600, 120, 20);
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.background.setX(2 * ImgInfo.BACKGROUND_SIZE[idx][0]);
        this.dinoGetNum = 0;
        
    }

    @Override
    public void sceneUpdate() {
        for (int i = 0; i < this.post.size(); i++) {
            this.post.get(i).update();
        }
        this.home.update();
        if (this.ball.getGoVec().getValue() == 0) {//dino一開始不能推香菇
            this.dino.update();
            for (int i = 0; i < this.post.size(); i++) {//dino不會進到香菇裡面
                if (this.dino.isCollision(this.post.get(i))) {
                    Vector dinoVec = dinoDir();
                    this.dino.offset(-dinoVec.getX(), -dinoVec.getY());
                }
            }
        } else {
            this.ball.update();
            this.dino.update();
            this.ball.isBound();
            if ((racket.right() >= this.ball.getCenterX() - this.ball.getR()
                    && racket.left() <= this.ball.getCenterX() + this.ball.getR())
                    && (this.ball.getCenterY() + this.ball.getR() >= 590)) {
                this.ball.setCenterY(580 - this.ball.getR());
                this.ball.getGoVec().setY(-this.ball.getGoVec().getY());
                this.Score += 10;
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
                    this.dinoGetNum++;
                    Vector dinoVec = dinoDir();
                    if (!isBound(i) && this.dinoGetNum == 1) {
                        this.post.get(i).offset(dinoVec.getX(), dinoVec.getY());
                    }
                }
            }
            for (int i = 0; i < this.post.size(); i++) {
                if (!this.dino.isCollision(this.post.get(i))) {
                    this.dinoGetNum = 0;
                }
            }
            for (int i = 0; i < this.post.size(); i++) {
                if (this.home.isCollision(this.post.get(i))) {
                    this.Score += 5;
                    this.post.remove(i);
                }
            }
            if(this.post.size()<8){
               genPost();
               this.post.get(7).update();
            }
        }
    }

    @Override
    public void sceneEnd() {

    }

    private void genPost(){
        int i=Global.random(150, 1200);
        int y = Global.random(0, 500);
        this.post.add(new Obstacle(ImgInfo.POSTS_PATH,  i ,  y,
                    ImgInfo.POST_INFO[0], ImgInfo.POST_INFO[1], ImgInfo.POST_INFO[2], ImgInfo.POST_INFO[3]));
    }
    
    public boolean isBound(int i) {
        if ((this.post.get(i).getCenterX() - this.post.get(i).getR() <= 0 && this.dino.getDir() == Global.LEFT2)
                || (this.post.get(i).getCenterX() + this.post.get(i).getR() >= Global.SCREEN_X && this.dino.getDir() == Global.RIGHT2)
                || (this.post.get(i).getCenterY() - this.post.get(i).getR() <= 0 && this.dino.getDir() == Global.UP2)
                || (this.post.get(i).getCenterY() + this.post.get(i).getR() >= Global.SCREEN_Y - 100 && this.dino.getDir() == Global.DOWN2)) {
            if (this.post.get(i).getCenterX() - this.post.get(i).getR() <= 0) {
                this.post.get(i).setCenterX(this.post.get(i).getR());
            }
            if (this.post.get(i).getCenterX() + this.post.get(i).getR() >= Global.SCREEN_X) {
                this.post.get(i).setCenterX(Global.SCREEN_X - this.post.get(i).getR());
            }
            if (this.post.get(i).getCenterY() - this.post.get(i).getR() <= 0) {
                this.post.get(i).setCenterY(this.post.get(i).getR());
            }
            if (this.post.get(i).getCenterY() + this.post.get(i).getR() >= Global.SCREEN_Y - 100) {
                this.post.get(i).setCenterY(Global.SCREEN_Y - 100 - this.post.get(i).getR());
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
        this.home.paint(g);
        this.dino.paint(g);
        this.ball.paint(g);
        for (int i = 0; i < this.post.size(); i++) {
            this.post.get(i).paint(g);
        }
        Graphics2D g2d = (Graphics2D) g;
        this.racket.paint(g2d);
        this.paintText(g);
    }

    private void paintText(Graphics g) {
        g.drawImage(scoreBoard,  Global.SCREEN_X / 2 - 70, 0, 270, 70, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("VinerHandITC", Font.TRUETYPE_FONT, 44));
        g.drawString("Score: " + Score, Global.SCREEN_X / 2 - 50, 50);
        g.setColor(Color.BLACK);
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
                        racket.offset(-12, 0);
                    }
                    break;
                case Global.RIGHT:
                    if (racket.right() < Global.SCREEN_X) {
                        racket.offset(12, 0);
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
