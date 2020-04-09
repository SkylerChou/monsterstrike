/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;

/**
 *
 * @author kim19
 */
public class Heal implements Skills {

    @Override
    public void genSkill(Marble self, Marble target) {
        System.out.print(self.getInfo().getName() + " 使出治癒回復" + self.getInfo().getAtk() + "點生命,");
        self.getInfo().setHp(self.getInfo().getHp() + self.getInfo().getAtk());
        System.out.println(self.getInfo().getName() + " 剩下血量" + self.getInfo().getHp() + "點");
    }
    
    @Override
    public void genSkill(Marble self, ArrayList<Marble> target) {
    }

    @Override
    public void update() {

    }
    
    @Override
    public ArrayList<SkillComponent> getSkillComponent(){
        return null;
    }

    @Override
    public void paintSkill(Graphics g) {

    }

}
