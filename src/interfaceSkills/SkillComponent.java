/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import monsterstrike.gameobject.GameObject;

public class SkillComponent extends GameObject {

    private SkillRenderer renderer;
    private float dx;
    private float dy;
    private boolean isBoom;

    public SkillComponent(int skillIdx, int attribute, int imgNum, 
            int x, int y, int w, int h, int delay) {
        super(x, y, w, h, h / 2);
        this.renderer = new SkillRenderer(skillIdx, attribute, imgNum, delay);
        this.dx = 0f;
        this.dy = 0f;
        this.isBoom = false;
    }
    
    public void setIsBoom(boolean isBoom){
        this.isBoom = isBoom;
    }
    
    public boolean getIsBoom(){
        return this.isBoom;
    }

    public void setStop(boolean isStop) {
        this.renderer.setIsStop(isStop);
    }

    public boolean getIsStop() {
        return this.renderer.getIsStop();
    }
    
    public void setDx(float dx){
        this.dx = dx;
    }
    
    public void setDy(float dy){
        this.dy = dy;
    }
    
    public float getDx(){
        return this.dx;
    }
    
    public float getDy(){
        return this.dy;
    }

    @Override
    public void update() {
        this.renderer.update();
        this.move();
//        if(this.isOutOfBound()){
//            this.setStop(true);
//        }
    }

    public void move() {
        this.offset(dx, dy);
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight());
    }
    
    public SkillRenderer getRenderer(){
        return this.renderer;
    }

}
