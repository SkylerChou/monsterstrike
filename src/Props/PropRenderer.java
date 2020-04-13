/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Props;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.util.Delay;

public class PropRenderer {

    private BufferedImage img;
    private int imgIdx;
    private Delay delay;
    private int imgNum;
    private int propIdx;
    private boolean isStop;

    public PropRenderer(int propIdx, int imgNum, int delay) {
//        this.img = IRC.getInstance().tryGetImage(ImgInfo.PROP_PATH[propIdx]);
        this.propIdx = propIdx;
        this.imgIdx = 0;
        this.imgNum = imgNum;
        this.delay = new Delay(delay);
        this.delay.start();
        this.isStop = false;
    }

    public void update() {
        if (this.delay.isTrig()) {
            if (this.imgIdx++ == this.imgNum - 1) {                               
                this.imgIdx = this.imgNum - 1;
                this.isStop = true;
                this.stop();
            }
        }
    } 
    
    public int getImgIdx(){
        return this.imgIdx;
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
//        g.drawImage(img, x, y, x + w, y + h,
//                imgIdx * PROP_UNIT_X[propIdx], 0,
//                (imgIdx + 1) * PROP_UNIT_X[propIdx],
//                PROP_UNIT_Y[propIdx], null);
    }
}
