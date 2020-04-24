/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author kim19
 */
public class ButtonA extends Button {

    
    private BufferedImage img1;
    private BufferedImage img2;
    private int w;
    private int h;

    public ButtonA(int left, int top, int right, int bottom, String[] path, int w, int h) {
        super(left, top, right, bottom);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
        this.h = h;
        this.w = w;
    }

    @Override
    public void def(Graphics g) {
        g.drawImage(img1, this.x, this.y, this.w, this.h, null);
    }

    @Override
    public void hover(Graphics g) {
        g.drawImage(img2, this.x, this.y, this.w, this.h, null);
    }

    @Override
    public void pressed(Graphics g) {
        g.drawImage(img1, this.x, this.y, this.w, this.h, null);
    }

}
