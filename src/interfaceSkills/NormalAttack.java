/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import monsterstrike.gameobject.Marble;


public class NormalAttack implements Skills {

    //要在Marble 加上 String name,int hp,int atk,int attribute,Skills [] skills
    //並且設上getter/setter
    //private void setSkills(){
    //        Skills skills[]={
    //            new NormalAttack(),
    //            new CriticalAttack(),
    //            new DecreaseHalfAttack(),
    //            new Heal(),
    //            new Anger()
    //        };
    //        
    //        for (int i = 0; i < skills.length; i++) {
    //            int r = (int) (Math.random() * 5);
    //            this.skills[i]=skills[i];
    //        }
    //    }
  //   public void useSkill(Marble target) {
//             int r = (int) (Math.random() * 4);
//             this.skills[r].useSkill(this, target);
//         }

    @Override
    public void useSkill(Marble self, Marble target) {
        System.out.println(self.getName()+ " 發動普通攻擊 !");
        target.setHp(target.getHp() - self.getAtk());
        System.out.println(target.getName() + " 剩下血量 :" + target.getHp()+ "點");
    }

}
