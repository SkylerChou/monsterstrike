/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import monsterstrike.gameobject.GameObject;

public class PickSkill extends GameObject {

    private SkillRenderer renderer;
    private int skillIdx;

    public PickSkill(int skillIdx, int attribute,int imgNum, int x, int y, int delay) {
        super(x, y, SkillImg.SKILL_UNIT_X[skillIdx][attribute], SkillImg.SKILL_UNIT_Y[skillIdx][attribute], SkillImg.SKILL_UNIT_X[skillIdx][attribute] / 2);
        this.renderer = new SkillRenderer(skillIdx, attribute, imgNum, delay);
        this.skillIdx = skillIdx;
    }

    public void setIsStop(boolean isStop) {
        this.renderer.setIsStop(isStop);
        if (!this.renderer.getIsStop()) {
            this.renderer.start();
        } else {
            this.renderer.stop();
        }
    }
    
    public boolean getStop(){
        return this.renderer.getIsStop();
    }

    @Override
    public void update() {
        if (!this.renderer.getIsStop()) {
            this.renderer.update();
            this.move();
        }else{
            this.setCenterX(-100);
            this.setCenterY(-100);
        }
    }
    
    public void move(){
        switch(skillIdx){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight());
    }
    
}
