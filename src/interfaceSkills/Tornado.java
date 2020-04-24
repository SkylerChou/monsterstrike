/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import controllers.ARC;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.util.Global;

public class Tornado extends Skills {

    private static final int SKILL_IDX = 1;
    private static final int DELAY = 7; //技能變化動畫
    private static final int NUM = 8;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 100;
    private int hitCount;

    public Tornado() {
        super(NUM);
    }

    @Override
    public int useSkill(Marble self, ArrayList<Marble> target, int idx) {
        this.hitCount = 0;
//        System.out.println(self.getInfo().getName() + " 爆擊 !");
        ARC.getInstance().play("/resources/wav/middle_punch.wav");
        int attr = self.getInfo().getAttribute();
        for (int i = 0; i < target.size(); i++) {
            this.skill[i] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[SKILL_IDX][attr],
                    (int) (target.get(i).getCenterX()), (int) (target.get(i).getCenterY()), WIDTH, HEIGHT, DELAY);
        }
        for (int i = target.size(); i < 8; i++) {
            this.skill[i] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[SKILL_IDX][attr],
                    (int) (Global.random(50, Global.SCREEN_X - 50)), (int) (Global.random(50, Global.SCREEN_Y - 50)), WIDTH, HEIGHT, DELAY);
        }
        int selfA = self.getInfo().getAttribute();
        int atk = self.getInfo().getAtk();
        for (int i = 0; i < target.size(); i++) {
            int targetA = target.get(i).getInfo().getAttribute();
            if (checkStrike(target.get(i))) {
                if ((selfA != 2 && selfA - targetA == -1) || (selfA == 2 && targetA == 0)
                        || (selfA == 4 && targetA == 3)) {
                    atk = 2 * self.getInfo().getAtk();
                } else if ((selfA != 3 && selfA - targetA == 1) || (selfA == 0 && targetA == 2)
                        || (selfA == 3 && targetA == 4)) {
                    atk = self.getInfo().getAtk() / 2;
                }
                target.get(i).getInfo().setHp(target.get(i).getInfo().getHp() - atk);
                this.hitCount++;
//                System.out.println(target.get(i).getInfo().getName() + "血量:" + target.get(i).getInfo().getHp());
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
    
    @Override
    public int explode(Marble self, GameObject target) {
        return 0;
    }
}
