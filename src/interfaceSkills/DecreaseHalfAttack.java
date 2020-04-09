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
public class DecreaseHalfAttack implements Skills {

    private int attribute;   
    
    public DecreaseHalfAttack(int attribute) {
        this.attribute = attribute;
    }
    
    @Override
    public void genSkill(Marble self, Marble target) {
        int atk = self.getInfo().getAtk() / 2;
        System.out.print(self.getInfo().getName() + " 爆擊 !");
        target.getInfo().setHp(target.getInfo().getHp() - atk);
        System.out.println(target.getInfo().getName() + " 剩下血量 :" + target.getInfo().getHp() + "點");
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
