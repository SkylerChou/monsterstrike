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
    private int imgIdx;
    private int imgNum;
    private boolean isStop;

    public MarbleRenderer(String path, int delay) {
        this.img = IRC.getInstance().tryGetPNG(path);
        this.imgNum = ImgInfo.DIE_NUM;
        this.delay = new Delay(delay);
        this.delay.start();
        this.isStop = false;
        this.imgIdx = 0;
    }

    public void update() {
        if (this.delay.isTrig()) {
            if (this.imgIdx++ == this.imgNum - 1) {
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
    
    public boolean getIsStop(){
        return this.isStop;
    }
    
    public void setIsStop(boolean isStop){
        this.isStop = isStop;
    }

    public void paint(Graphics g, int x, int y, int w, int h) {
        g.drawImage(img, x, y, x + w, y + h,
                imgIdx * ImgInfo.DIE_UNIT_X, 0,
                (imgIdx + 1) * ImgInfo.DIE_UNIT_X,
                ImgInfo.DIE_UNIT_Y, null);
    }
}
