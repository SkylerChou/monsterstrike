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

public class ObjectRenderer {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;

    private Delay delay;
    private int count;

    public ObjectRenderer(String[] path, int delayFrame) {
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
        this.currentImg = this.img1;
        this.delay = new Delay(delayFrame);
        this.delay.start();
        this.count = 0;
    }

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

    public void restImg(){
        this.currentImg=this.img1;
    }
    
    public void paint(Graphics g, int x, int y, int w, int h) {
        g.drawImage(currentImg, x, y, w, h, null);
    }
}
