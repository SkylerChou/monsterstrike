/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Item extends SceneObject{

    private BufferedImage img;

    public Item(String path, int x, int y, int w, int h) { //centerX, centerY
        super(x, y, w, h);
        this.img = IRC.getInstance().tryGetImage(path);
    }

    public void offset(int dx) {
        this.offset(dx, 0);
    }

    @Override
    public void update() {
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, (int)this.getX(),(int)this.getY(), 
                (int)this.getWidth(), (int)this.getHeight(), null);
    }
    
    public void paintResize(Graphics g, float ratio){
        g.drawImage(img, (int)this.getX(),(int)this.getY(), 
                (int)(this.getWidth() * ratio), (int)this.getHeight(), null);
    }
}
