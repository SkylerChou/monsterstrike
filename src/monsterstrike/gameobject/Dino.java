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
    private Delay delay;

    public Dino(String path, int x, int y, int width, int height, int[] steps) {
        super(x, y, 24, 24);
        this.img = IRC.getInstance().tryGetImage(path);
        this.currentStep = 0;
        this.count = 0;
        this.steps = steps;
        this.delay = new Delay(10);
        this.delay.start();
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

    public void pause() {
        this.delay.pause();
    }

    public void start() {
        this.delay.start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, Global.SCREEN_X / 2 - 150, Global.SCREEN_Y / 2 - 35, Global.SCREEN_X / 2 - 150 + 48, Global.SCREEN_Y / 2 - 35 + 48,
                24 * this.currentStep, 0,
                24 * this.currentStep + 24,
                24, null);
    }
}
