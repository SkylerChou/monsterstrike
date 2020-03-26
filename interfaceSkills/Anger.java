/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceSkills;

import collisionproject.gameobject.Marble;

/**
 *
 * @author kim19
 */
public class Anger implements Skills{

    @Override
    public void useSkill(Marble self, Marble target) {
        int atk = (int)(self.getAtk()*1.5f);
        System.out.print(self.getName() + "處於憤怒狀態 ! 攻擊力提升1.5倍");
        target.setHp(target.getHp() - atk);
        System.out.println(target.getName() + " 剩下血量 :" + target.getHp() + "點");
    }
    
}
