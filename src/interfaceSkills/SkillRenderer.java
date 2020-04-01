/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import controllers.IRC;
import static interfaceskills.SkillImg.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Delay;

public class SkillRenderer {

    private BufferedImage img;
    private int imgIdx;
    private Delay delay;
    private int skillIdx;
    private int imgNum;
    private boolean isStop;

    public SkillRenderer(int skillIdx, int imgNum, int delay) {
        this.img = IRC.getInstance().tryGetImage(SKILL_PATH[skillIdx]);
        this.skillIdx = skillIdx;
        this.imgIdx = 0;
        this.imgNum = imgNum;
        this.delay = new Delay(delay);
        this.delay.start();
        this.isStop = false;
    }

    public void update() {
        if (this.delay.isTrig()) {          
            if (this.imgIdx++ == this.imgNum - 1) {                 
                this.imgIdx = 0;
                this.stop();
                this.isStop = true;
            }
        }
    }

    public void stop() {
        this.delay.stop();
    }

    public void start() {
        this.delay.start();
    }
    
    public boolean getIsStop(){
        return this.isStop;
    }
    
    public void setIsStop(boolean isStop){
        this.isStop = isStop;
    }

    public void paint(Graphics g, int x, int y, int w, int h) {
        g.drawImage(img, x, y, x + w, y + h,
                imgIdx * SKILL_UNIT[skillIdx], 0,
                (imgIdx + 1) * SKILL_UNIT[skillIdx],
                SKILL_UNIT[skillIdx], null);

    }
}
