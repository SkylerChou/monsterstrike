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

public class Background {

    private int x;
    private int y;
    private BufferedImage img;
    private int idx;

    public Background(String path, int x, int y) {
        this.img = IRC.getInstance().tryGetImage(path);
        this.x = x;
        this.y = y;
        this.idx = 1;
    }

    public void offset(int dx) {
        this.x += dx;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public int getX(){
        return this.x;
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y,
                this.x-ImgInfo.BACKGROUND_SIZE[idx][0], 0, this.x, this.y, null);
    }
}
