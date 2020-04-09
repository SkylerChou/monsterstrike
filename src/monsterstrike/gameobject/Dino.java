/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class Dino extends GameObject {

    public static final int[] STEPS_WALK = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static final int[] STEPS_RUN = {16, 17, 18, 19, 20, 21, 22, 23};
    public static final int[] STEPS_WALKRIGHT = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int[] STEPS_WALKLEFT = {22, 21, 20, 19, 18, 17, 16, 15, 14};
    //恐龍行走圖
    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currendImg;
    //動作控制
    private int currentStep;
    private int[] steps;
    private int count;
    private boolean isStand;

    private Delay delay;
    private int dir;

    public Dino(String path, int x, int y, int[] steps) {
        super(x, y, ImgInfo.DINO_INFO[0], ImgInfo.DINO_INFO[1],20);
        this.img1 = IRC.getInstance().tryGetImage(path);
        this.currendImg = this.img1;
        this.currentStep = 0;
        this.count = 0;
        this.dir = 0;
        this.steps = steps;
        this.delay = new Delay(5);
        this.delay.start();
        this.isStand = true;
    }

    public Dino(String[] path, int x, int y, int[] steps) {
        super(x, y, ImgInfo.GREENDINO_INFO[0], ImgInfo.GREENDINO_INFO[1],20);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
        this.currendImg = this.img1;
        this.currentStep = 0;
        this.count = 0;
        this.dir = 0;
        this.steps = steps;
        this.delay = new Delay(5);
        this.delay.start();
        this.isStand = true;
    }

    public BufferedImage getImg1() {
        return this.img1;
    }

    public BufferedImage getImg2() {
        return this.img2;
    }

    public void setCurrentImg(BufferedImage img) {
        this.currendImg = img;
    }

    public void setSteps(int[] steps) {
        this.steps = steps;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public void setStand(boolean isStand) {
        this.isStand = isStand;
    }

    @Override
    public void update() {
        if (this.delay.isTrig()) {
            if (this.count >= this.steps.length) {
                this.count = 0;
            }
            this.currentStep = this.steps[this.count++];
        }
    }

    public void move() {
        if (!this.isStand) {
            switch (this.dir) {
                case Global.UP:
                    if (this.getCenterY() < Global.SCREEN_Y / 2 ) {
                        break;
                    }
                    this.offset(0, -50);
                    break;
                case Global.DOWN:
                    if (this.getCenterY() > Global.SCREEN_Y / 2 + 150) {
                        break;
                    }
                    this.offset(0, 50);
                    break;
            }
        }
    }

    public void moveArround() {
        if (!this.isStand) {
            switch (this.dir) {
                case Global.UP2:
                    if (this.getCenterY() <= 0) {
                        break;
                    }
                    this.offset(0, -8);
                    break;
                case Global.DOWN2:
                    if (this.getCenterY() >= Global.SCREEN_Y) {
                        break;
                    }
                    this.offset(0, 8);
                    break;
                case Global.LEFT2:
                    if (this.getCenterX() <= 0) {
                        break;
                    }
                    this.offset(-8, 0);
                    break;
                case Global.RIGHT2:
                    if (this.getCenterX() >= Global.SCREEN_X) {
                        break;
                    }
                    this.offset(8, 0);
                    break;
            }
        }
    }

    public boolean getIsStand() {
        return this.isStand;
    }

    public void setDinoRun() {
        this.steps = STEPS_RUN;
    }

    public void reset() {
        this.steps = STEPS_WALK;
    }

    public void pause() {
        this.delay.pause();
    }

    public void start() {
        this.delay.start();
    }



    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.currendImg, (int) this.rect.left(), (int) this.rect.top(),
                (int) this.rect.right(), (int) this.rect.bottom(),
                24 * this.currentStep, 0,
                24 * this.currentStep + 24,
                24, null);
    }
}
