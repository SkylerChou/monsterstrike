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
public class BlackHole extends GameObject {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;
    private Delay delay;
    private int count;

    public BlackHole(String[] path, int x, int y, int[] info) {
        super(x, y, info[0], info[1], info[2]);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);

        this.delay = new Delay(10);
        this.delay.start();
        this.currentImg = this.img1;
        this.count = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(currentImg, (int) this.getX() ,(int)this.getY(), ImgInfo.BLACKHOLE_INFO[0], ImgInfo.BLACKHOLE_INFO[1], null);
    }

    @Override
    public void update() {
        if (this.delay.isTrig()) {
            this.count++;
            if (this.count % 2 != 0) {
                this.currentImg = this.img2;
            } else {
                this.currentImg = this.img1;
            }
        }
    }

}
