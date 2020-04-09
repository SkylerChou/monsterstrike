/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.util.Delay;

public class MarbleRenderer {

    private BufferedImage img;
    private Delay delay;
    private Delay collideDelay;
    private int imgIdx;
    private int imgNum;
    private boolean isStop;
    private boolean isCollide;

    public MarbleRenderer(String path, int num, int delay) {
        this.img = IRC.getInstance().tryGetImage(path);
        this.imgNum = num;
        this.delay = new Delay(delay);
        this.delay.start();
        this.isStop = false;
        this.imgIdx = 0;
        this.isCollide = false;
    }

    public void update() {
        if (this.delay.isTrig()) {
            this.imgIdx = (this.imgIdx + 1) % 2;
        }
    }

    public void collideUpdate() {
        if (!this.isCollide) {
            this.collideDelay.start();
            this.isCollide = true;
        }
        if (this.collideDelay.isTrig() && this.isCollide) {            
            this.imgIdx = (this.imgIdx + 1) % this.imgNum;
            
            if (this.imgIdx == this.imgNum-1) {
                System.out.println(imgIdx + "更新完");
                this.imgIdx = 0;
                this.delay.start();
                this.collideDelay.stop();
                this.isCollide = false;
            }
        }
    }

    public void updateOnce() {
        this.delay.start();
        if (this.delay.isTrig()) {
            this.imgIdx = (this.imgIdx + 1) % this.imgNum;
            if (this.imgIdx == 0) {
                this.stop();
                this.isStop = true;
                this.imgIdx = this.imgNum - 1;
            }
        }
    }

    public void stop() {
        this.delay.stop();
    }

    public void start() {
        this.delay.start();
    }

    public boolean getIsCollide() {
        return this.isCollide;
    }
    
    public void setIsCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }

    public boolean getIsStop() {
        return this.isStop;
    }

    public void setIsStop(boolean isStop) {
        this.isStop = isStop;
    }

    public void paint(Graphics g, int x, int y, int w, int h) {
        g.drawImage(img, x, y, x + w, y + h,
                imgIdx * ImgInfo.MARBLE_UNIT_X, 0,
                (imgIdx + 1) * ImgInfo.MARBLE_UNIT_X,
                ImgInfo.MARBLE_UNIT_Y, null);
    }
}
