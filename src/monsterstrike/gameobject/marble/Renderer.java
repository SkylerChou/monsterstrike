/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Delay;

public class Renderer {

    private BufferedImage img;
    private Delay delay;
    private Delay collideDelay;
    private int imgIdx;
    private int imgNum;
    private boolean isStop;
    private boolean isCollide;
    

    public Renderer(String path, int num, int delay) {
        this.img = IRC.getInstance().tryGetImage(path);
        this.imgNum = num;
        this.delay = new Delay(delay);
        this.delay.start();
        this.collideDelay = new Delay(delay);
        this.collideDelay.start();
        this.isStop = false;
        this.imgIdx = 0;
        this.isCollide = false;
    }

    public void update() {
        if (this.delay.isTrig()) {
            this.imgIdx = (this.imgIdx + 1) % 2;
        }
    }
    
    public void updateOnce() {
        if (this.delay.isTrig()) {
            if (this.imgIdx++ == this.imgNum - 1) {                               
                this.imgIdx = this.imgNum - 1;
                this.isStop = true;
                this.stop();
            }
        }
    }
    
    public void updateHit() {
        if (this.collideDelay.isTrig()) {
            if (this.imgIdx++ == this.imgNum - 1) {                               
                this.imgIdx = this.imgNum - 1;
                this.isStop = true;
                this.stopHit();
            }
        }
    }
    
    public void startHit() {
        this.collideDelay.start();
        this.isStop = false;
    }
    
    public void stopHit() {
        this.collideDelay.stop();
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

    public void paint(Graphics g, int x, int y, int w, int h, int unitX, int unitY) {
        g.drawImage(img, x, y, x + w, y + h,
                imgIdx * unitX, 0,
                (imgIdx + 1) * unitX,
                unitY, null);
    }
}
