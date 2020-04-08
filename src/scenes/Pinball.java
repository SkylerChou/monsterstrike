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
import monsterstrike.gameobject.Dino;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.ReboundBall;
import monsterstrike.gameobject.SpecialEffect;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.StandMarble;
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
    private Marble marble;
    private ArrayList<Marble> post;
    private int idx; //背景idx
    private Rect racket;
    private Dino dino;
    private Button setting;
    private ArrayList<SpecialEffect> b;

    private Arrow arrow;

    public Pinball(SceneController sceneController) {
        super(sceneController);
        //        this.b = new ArrayList<>();
        this.post = new ArrayList<>();
    }

    @Override
    public void sceneBegin() {
        
        for (int i = 0; i < 5; i++) {
            int y = Global.random(100, 200);
            this.post.add(new StandMarble(ImgInfo.POSTS_PATH[i], ImgInfo.POSTS_NAME, 150 + i * 200, 150 + y, ImgInfo.POSTS_INFO[i]));
        }
        this.idx = 1;
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx], 2 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);

        this.marble = new ReboundBall(ImgInfo.MYMARBLE_PATH[0], ImgInfo.MYMARBLE_NAME[0], POS_X, POS_Y, ImgInfo.MYMARBLE_INFO[0]);
        this.racket = Rect.genWithCenter(50, 600, 120, 20);
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.background.setX(2 * ImgInfo.BACKGROUND_SIZE[idx][0]);
        this.marble.setFiction(0);
        this.marble.setVelocity(10);
        this.dino = new Dino(ImgInfo.BLUEDINOJPG, 500, 500);
    }

    @Override
    public void sceneUpdate() {
        this.marble.update();
        this.marble.isBound();
        if ((racket.right() >= this.marble.getCenterX() - this.marble.getR() && racket.left() <= this.marble.getCenterX() + this.marble.getR()) && (this.marble.getCenterY() + this.marble.getR() >= 590)) {
            this.marble.setCenterY(580 - this.marble.getR());
            this.marble.getGoVec().setY(-this.marble.getGoVec().getY());
        } else {
            this.marble.move();
        }
        for (int i = 0; i < this.post.size(); i++) {
            if (this.post.get(i).getIsCollide()) {
                this.post.get(i).update();
            }
        }
        for (int i = 0; i < this.post.size(); i++) {
            if (this.marble.isCollision(this.post.get(i)) && this.marble.getGoVec().getValue() > 0) {
                Marble tmp = this.marble.strike(this.post.get(i));
                this.post.get(i).setGo(tmp.getGoVec());
                this.post.get(i).setIsCollide(true);
                this.marble.genSkill(0, this.post.get(i));
            }
        }
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.dino.paint(g);
        this.background.paint(g);
        this.arrow.paint(g);
        this.marble.paint(g);

        for (int i = 0; i < 5; i++) {
            this.post.get(i).paint(g);
        }

        Graphics2D g2d = (Graphics2D) g;
        this.racket.paint(g2d);
    }

    private boolean checkStop() {
        if (this.marble.getGoVec().getValue() != 0) {
            return false;
        }
        return true;
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
                    dino.setDir(Global.UP);
                    if (dino.getCenterY() - dino.getHeight() / 2 > 0) {
                        dino.moveArround();
                    }
                    break;
                case Global.DOWN:
                    dino.setDir(Global.DOWN);
                    if (dino.getCenterY() + dino.getHeight() / 2 < Global.SCREEN_Y) {
                        dino.moveArround();
                    }
                    break;
                case Global.LEFT2:
                    dino.setDir(Global.LEFT2);
                    if (dino.getCenterX() - dino.getWidth() / 2 > 0) {
                        dino.moveArround();
                    }
                    break;
                case Global.RIGHT2:
                    dino.setDir(Global.RIGHT2);
                    if (dino.getCenterX() + dino.getWidth() / 2 < Global.SCREEN_X) {
                        dino.moveArround();
                    }
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
}
