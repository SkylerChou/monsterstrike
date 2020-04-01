/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import monsterstrike.gameobject.marble.Marble;

/**
 *
 * @author kim19
 */
public class Heal implements Skills{

    @Override
    public void useSkill(Marble self, Marble target) {
        System.out.print(self.getName() + " 使出治癒回復" + self.getAtk() + "點生命,");
        self.setHp(self.getHp() + self.getAtk());
        System.out.println(self.getName() + " 剩下血量" + self.getHp() + "點");
    }
    
}
