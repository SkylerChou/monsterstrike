/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class ReboundBall extends Marble {
    
    protected BufferedImage img1;
    protected BufferedImage img2;
    protected BufferedImage currentImg;
    protected Delay delay;
    protected int count;

    public ReboundBall(String[] path, String name, int x, int y, int[] info) {
        super(name, x, y, info);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
        this.currentImg = this.img1;
        this.delay = new Delay(20);
        this.delay.start();
    }
   

    @Override
    public boolean isBound() {
        if (this.getCenterX() - this.getR() <= 0
                || this.getCenterX() + this.getR() >= Global.SCREEN_X
                || this.getCenterY() - this.getR() <= 0) {
            if (this.getCenterX() - this.getR() <= 0) {
                this.setCenterX(this.getR());
                this.getGoVec().setX(-this.getGoVec().getX());
            }
            if (this.getCenterX() + this.getR() >= Global.SCREEN_X) {
                this.setCenterX(Global.SCREEN_X - this.getR());
                this.getGoVec().setX(-this.getGoVec().getX());
            }
            if (this.getCenterY() - this.getR() <= 0) {
                this.setCenterY(this.getR());
                this.getGoVec().setY(-this.getGoVec().getY());
            }
            return true;
        }
        return false;
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

    @Override
    public Marble strike(Marble other) {
        return null;
    }

    @Override
    public boolean die() {
        return false;
    }

    @Override
    public void paintScale(Graphics g, int x, int y, int w, int h) {
       
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(currentImg, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight(), null);
//        if (Global.IS_DEBUG) {
            g.drawOval((int) (this.getCenterX() - this.getR()),
                    (int) (this.getCenterY() - this.getR()),
                    (int) (2 * this.getR()), (int) (2 * this.getR()));
//        }
    }
}
