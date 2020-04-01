/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import monsterstrike.gameobject.GameObject;

public class Explosion extends GameObject {

    private SkillRenderer renderer;

    public Explosion(int imgIdx, int imgNum, int x, int y) {
        super(x, y, SkillImg.SKILL_UNIT[imgIdx], SkillImg.SKILL_UNIT[imgIdx], SkillImg.SKILL_UNIT[imgIdx] / 2);
        this.renderer = new SkillRenderer(imgIdx, imgNum, 3);
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
        }else{
            this.setCenterX(-100);
            this.setCenterY(-100);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight());
    }

}
