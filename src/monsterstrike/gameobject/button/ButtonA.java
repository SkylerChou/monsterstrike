/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.button;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ButtonA extends Button {

    
    private BufferedImage img1;
    private BufferedImage img2;


    public ButtonA(String[] path, int x, int y, int w, int h) {
        super(x, y, x+w, y+h);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
    }

    @Override
    public void def(Graphics g) {
        g.drawImage(img1, this.x, this.y, this.getW(), this.getH(), null);
    }

    @Override
    public void hover(Graphics g) {
        g.drawImage(img2, this.x, this.y, this.getW(), this.getH(), null);
    }

    @Override
    public void pressed(Graphics g) {
        g.drawImage(img1, this.x, this.y, this.getW(), this.getH(), null);
    }

}
