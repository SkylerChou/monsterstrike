/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;

public class Heal extends Skills {

    private static final int SKILL_IDX = 2;
    private static final int DELAY = 15; //技能變化動畫
    private static final int NUM = 4;
    
    public Heal() {
        super(NUM);
    }
    
    @Override
    public int useSkill(Marble self, ArrayList<Marble> target, int targetIdx) {
        System.out.print(self.getInfo().getName() + " 使出治癒回復" + self.getInfo().getAtk() + "點生命,");
        self.getInfo().setHp(self.getInfo().getHp() + self.getInfo().getAtk());
        System.out.println(self.getInfo().getName() + " 剩下血量" + self.getInfo().getHp() + "點");
        return 0;
    }
    
    

}
