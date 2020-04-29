/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Axes extends GameObject {

    private BufferedImage img;
    private boolean isGet;
    private boolean isSet;

    public Axes(String path, int x, int y, int width, int height) {
        super(x, y, width, height, (int)(height *0.4));
        this.img = IRC.getInstance().tryGetImage(path);
    }
    
    @Override
    public void update() {
        
    }
    
    public void setGet(boolean isGet){
        this.isGet = isGet;
    }
    
    public boolean isGet(){
        return this.isGet;
    }
    
    public void isSet(boolean isSet){
        this.isSet = isSet;
    }
    
    public boolean getSet(){
        return this.isSet;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight(), null);
    }

    

}
