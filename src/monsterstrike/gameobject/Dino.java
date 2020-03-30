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
public class Dino extends SenceObject {

    public static final int[] STEPS_WALK = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static final int[] STEPS_RUN = {16, 17, 18, 19, 20, 21, 22, 23};
    //恐龍行走圖
    private BufferedImage img;
    //動作控制
    private int currentStep;
    private int[] steps;
    private int count;
    private boolean isStand;

    private Delay delay;
    private int dir;

    public Dino(String path, int x, int y, int[] steps) {
        super(x, y, ImgInfo.DINO_INFO[0], ImgInfo.DINO_INFO[1]);
        this.img = IRC.getInstance().tryGetImage(path);
        this.currentStep = 0;
        this.count = 0;
        this.dir = 0;
        this.steps = steps;
        this.delay = new Delay(10);
        this.delay.start();
        this.isStand = true;
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
                     if(this.getCenterY()<Global.SCREEN_Y / 2 - 30){
                         break;
                     }
                    this.offset(0, -50);
                    break;
                case Global.DOWN:
                    if(this.getCenterY()>Global.SCREEN_Y / 2 +115){
                         break;
                     }
                    this.offset(0, 50);
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
    public void paint(Graphics g) {
        g.drawImage(img, (int)this.getCenterX(), (int)this.getCenterY(), 
                (int)(this.getCenterX() + this.getWidth()), (int)(this.getCenterY() + this.getHeight()),
                24 * this.currentStep, 0,
                24 * this.currentStep + 24,
                24, null);
    }
}
