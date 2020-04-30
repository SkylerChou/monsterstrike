/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.marble.Marble;

public abstract class Skills {
    protected SkillComponent[] skill;

    public Skills(int num) {
        this.skill = new SkillComponent[num];
    }

    public void update() {
        for (int j = 0; j < this.skill.length; j++) {
            if (this.skill[j] != null && !this.skill[j].getIsStop()) {
                this.skill[j].update();
            } else {
                this.skill[j] = null;
            }
        }
    }
    
    public int getHitCount(){
        return 0;
    }

    public abstract int useSkill(Marble self, ArrayList<Marble> target, int idx);
    
    public abstract int explode(Marble self, GameObject target);

    public SkillComponent[] getSkillComponent() {
        return this.skill;
    }

    public void paintSkill(Graphics g) {
        for (int j = 0; j < this.skill.length; j++) {
            if (this.skill[j] != null) {
                this.skill[j].paint(g);
            }
        }
    }

}
