/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Arrow extends GameObject {

    private BufferedImage img;
    private boolean isShow;
    private float rad;
    private float resizeMag;

    public Arrow(String path, int x, int y, int[] info) {
        super(x, y, info[0], info[1], info[2]);
        this.img = IRC.getInstance().tryGetImage(path);
        this.isShow = false;
        this.resizeMag = 1;
    }
    
    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean getShow() {
        return this.isShow;
    }

    public void setDegree(float rad) {
        this.rad = rad;
    }

    public void setResizeMag(float resizeMag) {
        this.resizeMag = resizeMag;
    }

    public void reset() {
        this.resizeMag = 1;
    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldForm = g2d.getTransform();
        g2d.rotate(-this.rad, this.getCenterX(), this.getCenterY());
        g2d.drawImage(img, (int) (this.getCenterX()-0.5*this.resizeMag*this.getWidth()), 
                (int) (this.getCenterY()-0.5*this.getHeight()),
                (int) (this.resizeMag*this.getWidth()),(int)(this.getHeight()), null);
        g2d.setTransform(oldForm);

    }

}
