/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import monsterstrike.util.Delay;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SpecialEffect extends GameObject {

    private BufferedImage img1;
    private BufferedImage img2;
    protected BufferedImage currentImg;
    private Delay delay;
    private int count;
    private boolean isShine;

    public SpecialEffect(String[] path, int x, int y, int[] info, int delayFrame) {
        super(x, y, info[0], info[1], info[2]);
        this.img1 = IRC.getInstance().tryGetPNG(path[0]);
        this.img2 = IRC.getInstance().tryGetPNG(path[1]);
        this.delay = new Delay(delayFrame);
        this.delay.start();
        this.currentImg = this.img1;
        this.count = 0;
        this.isShine = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (this.isShine) {
            g.drawImage(this.currentImg, (int) (this.getCenterX() - this.getR()), (int) (this.getCenterY() - this.getR()), ImgInfo.SHINE_INFO[0], ImgInfo.SHINE_INFO[1], null);
        }
    }
    public void paintWinds(Graphics g) {
        if (this.isShine) {
            g.drawImage(this.currentImg, (int)this.rect.left()-20, (int) this.rect.top()-30, 200, 200, null);
        }
    }

    
    public void setShine(boolean isShine) {
        this.isShine = isShine;
    }
    
    public boolean getIsShine(){
        return this.isShine;
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
