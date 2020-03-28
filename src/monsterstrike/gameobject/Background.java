/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Global;

public class Background extends SenceObject{

    private BufferedImage img;

    public Background(String path, int x, int y, int width, int height) {
        super(x, y, Global.SCREEN_X, Global.SCREEN_Y);
        this.img = IRC.getInstance().tryGetImage(path);
    }

    @Override
     public void update() {}
     
    @Override
    public void paint(Graphics g) {
        g.drawImage(img, (int)this.rect.centerX(), (int)this.rect.centerY(), Global.SCREEN_X, Global.SCREEN_Y, null);
    }
}
