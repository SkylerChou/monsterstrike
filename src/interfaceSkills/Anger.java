/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;

public class Anger implements Skills {

    private int attribute;

    public Anger(int attribute) {
        this.attribute = attribute;
    }

    @Override
    public void genSkill(Marble self, Marble target) {
        int atk = (int) (self.getAtk() * 1.5f);
        System.out.print(self.getName() + "處於憤怒狀態 ! 攻擊力提升1.5倍");
        target.setHp(target.getHp() - atk);
        System.out.println(target.getName() + " 剩下血量 :" + target.getHp() + "點");
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
