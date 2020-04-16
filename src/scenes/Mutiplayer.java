/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import Props.Booster;
import Props.Heart;
import Props.Prop;
import monsterstrike.gameobject.marble.*;
import monsterstrike.graph.Vector;
import monsterstrike.gameobject.*;
import monsterstrike.util.*;
import controllers.SceneController;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Mutiplayer extends Scene {

    public static final int[] POS_X = {150, Global.FRAME_X / 2, Global.FRAME_X - 150};
    public static final int[] POS_Y = {100, Global.FRAME_Y - 150, 100};

    private Background background;
    private ArrayList<Marble> marbles;
    private ArrayList<MarbleInfo> allMarbleInfo;
    private Arrow arrow;

    private int currentIdx;
    private Delay delay;
    private int count;
    private int idx; //背景idx
    private Button setting;
    private ArrayList<SpecialEffect> b;//黑洞

    private Prop heart;
    private Prop shoe;
    private Prop booster;

    private boolean isTouchHeart;
    private boolean isTouchShoe;

    public Mutiplayer(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.isTouchHeart = false;
        this.isTouchShoe = false;
        this.idx = 1;
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx], 2 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);
        this.marbles = new ArrayList<>();
        this.b = new ArrayList<>();
        this.allMarbleInfo = FileIO.readMarble("marbleInfo.csv");
        for (int i = 0; i < 3; i++) {
            this.marbles.add(new Marble(POS_X[i], POS_Y[i], 120, 120, this.allMarbleInfo.get(i)));

//            this.heart = new Heart(ImgInfo.HEART, 300, 100, ImgInfo.PROPS_INFO[0], ImgInfo.PROPS_INFO[1], ImgInfo.PROPS_INFO[2], ImgInfo.HEART_NUM, "愛心");
//            this.shoe = new Booster(ImgInfo.SHOE, 600, 100, ImgInfo.PROPS_INFO[0], ImgInfo.PROPS_INFO[1], ImgInfo.PROPS_INFO[2], ImgInfo.SHOE_NUM, "加速");
//            this.props.add(new Booster(ImgInfo.BOOSTER, 500, 600, ImgInfo.PROPS_INFO[0], ImgInfo.PROPS_INFO[1], ImgInfo.PROPS_INFO[2],"加速"));

            this.b.add(new SpecialEffect(ImgInfo.BALCKHOLE, 250 * (i + 1), 250,
                    ImgInfo.BLACKHOLE_INFO[0], ImgInfo.BLACKHOLE_INFO[1], ImgInfo.BLACKHOLE_INFO[0] / 5));
            this.b.get(i).setShine(true);
        }

        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.currentIdx = 0;
        this.delay = new Delay(25);
        this.delay.start();
        this.count = 0;
    }

    @Override
    public void sceneUpdate() {
        this.background.setX(2 * ImgInfo.BACKGROUND_SIZE[idx][0]);
//        if (this.delay.isTrig()) {
//            this.setting.update();
//        }

        for (int i = 0; i < this.b.size(); i++) {
            this.b.get(i).update();
        }

        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).update();
        }

        for (int i = 0; i < this.marbles.size(); i++) {//每一顆彈珠有沒有跟道具有碰撞  
            if (this.heart != null && isCollisionHeart()) {
                System.out.println(isCollisionHeart());
                this.isTouchHeart = true;
                this.marbles.get(i).getInfo().setHp(this.allMarbleInfo.get(i).getHp() + 50);
                for (int k = 0; k < this.marbles.size(); k++) {//每一隻怪物HP +50
                    System.out.println(this.marbles.get(k).getInfo().getName() + " " + this.marbles.get(k).getInfo().getHp());
                }
            } else if (this.shoe != null && isCollisionShoe()) {
                this.isTouchShoe = true;
                this.marbles.get(i).setVelocity(1.2f);
            }
            if (this.heart != null && this.isTouchHeart) {
//                this.heart.updateOnce();
                if (this.heart.getIsStop()) {
                    this.heart = null;
                    this.isTouchHeart = false;
                }
            }
            if (this.shoe != null && this.isTouchShoe) {
//                this.shoe.updateOnce();
                if (this.shoe.getIsStop()) {
                    this.shoe = null;
                    this.isTouchShoe = false;
                }
            }
        }

        for (int i = 0; i < this.marbles.size(); i++) {//黑洞移動
            for (int j = 0; j < this.b.size(); j++) {
                if (this.marbles.get(i).isCollision(this.b.get(j))) {
                    int r;
                    do {
                        r = Global.random(0, 2);
                        if (r != j) {
                            break;
                        }
                    } while (true);
                    this.marbles.get(i).setCenterX(this.b.get(r).getCenterX());
                    if (this.marbles.get(i).getGoVec().getY() > 0) {
                        this.marbles.get(i).setCenterY(this.b.get(r).getCenterY() + 110);
                    } else {
                        this.marbles.get(i).setCenterY(this.b.get(r).getCenterY() - 110);
                    }
                    this.marbles.get(i).offset(this.marbles.get(i).getGoVec().getX(), this.marbles.get(i).getGoVec().getY());
                    this.marbles.get(i).move();
                }
            }
        }

        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = i + 1; j < this.marbles.size(); j++) {
                if (this.marbles.get(i).isCollision(this.marbles.get(j))) {
//                    this.marbles.get(i).useSkill(0, this.marbles.get(j));
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

    private boolean isCollisionHeart() {
        for (int i = 0; i < this.marbles.size(); i++) {
            if (this.heart != null && this.marbles.get(i).isCollision(this.heart)) {
                return true;
            } 
        }
        return false;
    }

    private boolean isCollisionShoe() {
        for (int i = 0; i < this.marbles.size(); i++) {
            if (this.shoe != null && this.marbles.get(i).isCollision(this.shoe)) {
                return true;
            }
        }
        return false;
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
        if (this.heart != null) {
//            this.heart.paintH(g);
        }
        if (this.shoe != null) {
//            this.shoe.paintS(g);
        }
//        this.setting.paintOther(g, ImgInfo.SETTING_INFO);
        for (int i = 0; i < this.b.size(); i++) {
            this.b.get(i).paint(g);
        }
        if (this.arrow.getShow()) {
            this.arrow.paint(g);
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
                marbles.get(currentIdx).setGo(vector.resizeVec(marbles.get(currentIdx).getInfo().getV()));
//                marbles.get(currentIdx).setGo(vector.resizeVec(marbles.get(currentIdx).getVelocity()));
                count++;
                arrow.setShow(false);
            }
        }
    }
}
