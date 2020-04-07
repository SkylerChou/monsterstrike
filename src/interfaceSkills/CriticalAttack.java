/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.util.Global;

public class CriticalAttack implements Skills {

    private static final int[] SKILL_IDX = {1, 2};
    private static final int[] DELAY = {3, 10}; //技能變化動畫
    private int attribute;
    private ArrayList<SkillComponent> skill;
    private int r;

    public CriticalAttack(int attribute) {
        this.attribute = attribute;
        this.r = 1;
    }

    @Override
    public void update() {
        for (int i = 0; i < this.skill.size(); i++) {
            this.skill.get(i).update();
        }
    }

    @Override
    public void genSkill(Marble self, ArrayList<Marble> target) {
//        int atk = (int) (self.getAtk() * Math.random() * 2 + 1);
        System.out.print(self.getName() + " 爆擊 !");
//        target.setHp(target.getHp() - atk);
//        System.out.println(target.getName() + " 剩下血量 :" + target.getHp() + "點");
        this.skill = new ArrayList<>();
        this.r = Global.random(0, 1);
        if (this.r == 0) {
            for (int i = 0; i < target.size(); i++) {
                this.skill.add(new SkillComponent(SKILL_IDX[r], this.attribute, SkillImg.SKILL_NUM[SKILL_IDX[r]][this.attribute],
                        (int) (target.get(i).getCenterX()), (int) (target.get(i).getCenterY()), DELAY[r]));
            }
            for (int i = target.size(); i < 8; i++) {
                this.skill.add(new SkillComponent(SKILL_IDX[r], this.attribute, SkillImg.SKILL_NUM[SKILL_IDX[r]][this.attribute],
                        (int) (Global.random(50, Global.SCREEN_X - 50)), (int) (Global.random(50, Global.SCREEN_Y - 50)), DELAY[r]));
            }
        } else {
            int halfLength = SkillImg.SKILL_UNIT_Y[SKILL_IDX[r]][this.attribute] / 2;
            this.skill.add(new SkillComponent(SKILL_IDX[r], this.attribute, SkillImg.SKILL_NUM[SKILL_IDX[r]][this.attribute],
                    (int) (self.getCenterX()), (int) (self.getCenterY() - self.getR() - halfLength), DELAY[r]));
            this.skill.add(new SkillComponent(SKILL_IDX[r], this.attribute, SkillImg.SKILL_NUM[SKILL_IDX[r]][this.attribute],
                    (int) (self.getCenterX() + self.getR() + halfLength), (int) (self.getCenterY()), DELAY[r]));
            this.skill.add(new SkillComponent(SKILL_IDX[r], this.attribute, SkillImg.SKILL_NUM[SKILL_IDX[r]][this.attribute],
                    (int) (self.getCenterX()), (int) (self.getCenterY() + self.getR() + halfLength), DELAY[r]));
            this.skill.add(new SkillComponent(SKILL_IDX[r], this.attribute, SkillImg.SKILL_NUM[SKILL_IDX[r]][this.attribute],
                    (int) (self.getCenterX() - self.getR() - halfLength), (int) (self.getCenterY()), DELAY[r]));
        }
    }

    @Override
    public void genSkill(Marble self, Marble target) {
    }

    @Override
    public ArrayList<SkillComponent> getSkillComponent() {
        return this.skill;
    }

    @Override
    public void paintSkill(Graphics g) {
        if (this.r == 0) {
            for (int i = 0; i < this.skill.size(); i++) {
                this.skill.get(i).paint(g);
            }
        } else {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform oldForm = g2d.getTransform();
            if (!this.skill.isEmpty()) {
                for (int i = 0; i < this.skill.size(); i++) {
                    g2d.rotate(i * (Math.PI / 2), (int) (this.skill.get(i).getCenterX()), (int) (this.skill.get(i).getCenterY()));
                    this.skill.get(i).paint(g2d);
                    g2d.setTransform(oldForm);
                }
            }

        }
    }
}
