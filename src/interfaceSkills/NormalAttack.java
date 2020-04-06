/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;

public class NormalAttack implements Skills {

    private static final int SKILL_IDX = 0;
    private static final int DELAY = 3; //技能變化動畫
    private int attribute;
    private ArrayList<SkillComponent> skill;
    private int num;

    public NormalAttack(int attribute) {
        this.attribute = attribute;
        this.num = 8;
    }

    @Override
    public void update() {
        for (int i = 0; i < this.skill.size(); i++) {
            this.skill.get(i).update();
        }
    }

    @Override
    public void genSkill(Marble self, Marble target) {
        System.out.println(self.getName() + " 發動普通攻擊 !");
//        target.setHp(target.getHp() - self.getAtk());
//        System.out.println(target.getName() + " 剩下血量 :" + target.getHp() + "點");
        this.skill = new ArrayList<>();
        for (int i = 0; i < this.num; i++) {
            float rad = (float) (Math.toRadians(360 / this.num) * i);
            float dx = (float) (2 * (self.getR() + target.getR()) * Math.cos((Math.PI - rad) / 2) * Math.cos(rad / 2));
            float dy = (float) (2 * (self.getR() + target.getR()) * Math.cos((Math.PI - rad) / 2) * Math.sin(rad / 2));
            this.skill.add(new SkillComponent(SKILL_IDX, this.attribute, SkillImg.SKILL_NUM[0][this.attribute],
                    (int) (target.getCenterX() + dx), (int) (target.getCenterY() + dy), DELAY));
        }
    }
    
    @Override
    public void genSkill(Marble self, ArrayList<Marble> target) {
    }
    
    @Override
    public ArrayList<SkillComponent> getSkillComponent(){
        return this.skill;
    }

    @Override
    public void paintSkill(Graphics g) {
        for (int i = 0; i < this.skill.size(); i++) {
            if (this.skill.get(i) != null) {
                this.skill.get(i).paint(g);
            }
        }
    }

}
