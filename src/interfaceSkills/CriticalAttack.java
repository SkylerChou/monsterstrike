/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import monsterstrike.gameobject.marble.Marble;

public class CriticalAttack implements Skills {

    @Override
    public void useSkill(Marble self, Marble target) {
        int atk = (int) (self.getAtk() * Math.random() * 2 + 1);
        System.out.print(self.getName() + " 爆擊 !");
        target.setHp(target.getHp() - atk);
        System.out.println(target.getName() + " 剩下血量 :" + target.getHp() + "點");

    }

    @Override
    public void update() {

    }

    @Override
    public void paintSkill(Graphics g) {

    }

}
