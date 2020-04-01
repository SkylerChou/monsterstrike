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
    private int idx;

    public Background(String path, int x, int y) {
        super(x, y, Global.SCREEN_X, Global.SCREEN_Y);
        this.img = IRC.getInstance().tryGetImage(path);
        this.idx = 1;
    }

    public void offset(int dx) {
        this.offset(dx, 0);
    }
    

    @Override
     public void update() {}
     
    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y,
                (int)this.getX()-ImgInfo.BACKGROUND_SIZE[idx][0], 0, 
                (int)this.getX(), ImgInfo.BACKGROUND_SIZE[idx][1], null);
    }
    
    public void paintMenu(Graphics g){
        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, null);
    }
}
