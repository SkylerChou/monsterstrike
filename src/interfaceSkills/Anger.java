/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;

public class Anger extends Skills {
    private static final int SKILL_IDX = 4; //技能編號
    private static final int DELAY = 3; //技能變化動畫
    private static final int NUM = 8; //釋放一次技能數量
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    
    private int hitCount;

    public Anger() {
        super(NUM);
        this.hitCount = 0;
    }

//    @Override
//    public void genSkill(Marble self, Marble target) {
//        int atk = (int) (self.getInfo().getAtk() * 1.5f);
//        System.out.print(self.getInfo().getName() + "處於憤怒狀態 ! 攻擊力提升1.5倍");
//        target.getInfo().setHp(target.getInfo().getHp() - atk);
//        System.out.println(target.getInfo().getName() + " 剩下血量 :" + target.getInfo().getHp() + "點");
//    }

    @Override
    public int useSkill(Marble self, ArrayList<Marble> target, int idx) {
        System.out.println(self.getInfo().getName() + " 發動普通攻擊 !");
        int attr = self.getInfo().getAttribute();
        for (int i = 0; i < this.skill.length; i++) {
            float rad = (float) (Math.toRadians(360 / NUM) * i);
            float dx = (float) (2 * (self.getR() + target.get(idx).getR()) * Math.cos((Math.PI - rad) / 2) * Math.cos(rad / 2));
            float dy = (float) (2 * (self.getR() + target.get(idx).getR()) * Math.cos((Math.PI - rad) / 2) * Math.sin(rad / 2));
            this.skill[i] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[0][attr],
                    (int) (target.get(idx).getCenterX() + dx), (int) (target.get(idx).getCenterY() + dy), WIDTH, HEIGHT,DELAY);
        }
        for (int i = 0; i < target.size(); i++) {
            if (checkStrike(target.get(i))) {
                target.get(i).getInfo().setHp(target.get(i).getInfo().getHp() - self.getInfo().getAtk());
                this.hitCount++;
                System.out.println(target.get(i).getInfo().getName() + "血量:" + target.get(i).getInfo().getHp());
            }
        }
        return this.hitCount;
    }

    private boolean checkStrike(Marble target) {
        for (int j = 0; j < this.skill.length; j++) {
            if (this.skill[j].isCollision(target)) {
                return true;
            }
        }
        return false;
    }    

}
