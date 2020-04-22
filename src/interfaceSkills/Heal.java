/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import controllers.ARC;
import java.util.ArrayList;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.marble.Marble;

public class Heal extends Skills {

    private static final int SKILL_IDX = 4;
    private static final int DELAY = 8; //技能變化動畫
    private static final int NUM = 8; //skillComponent個數
    private static final int WIDTH = 120;
    private static final int HEIGHT = 120;
    private int hitCount;
    private ArrayList<Marble> selfMarbles;

    public Heal() {
        super(NUM);
    }
    
    @Override
    public void update() {
        for (int j = 0; j < this.selfMarbles.size(); j++) {
            if (this.skill[j] != null && !this.skill[j].getIsStop()) {
                this.skill[j].update();
                this.skill[j].setCenterX(this.selfMarbles.get(j).getCenterX());
                this.skill[j].setCenterY(this.selfMarbles.get(j).getCenterY());
            } else {
                this.skill[j] = null;
            }
        }
    }

    @Override
    public int useSkill(Marble self, ArrayList<Marble> selfMarbles, int idx) {
        this.hitCount = 0;
        this.selfMarbles = selfMarbles;
        ARC.getInstance().play("/resources/wav/heal.wav");
//        System.out.println(self.getInfo().getName() + "使出治癒");
        int attr = self.getInfo().getAttribute();
        for (int i = 0; i < selfMarbles.size(); i++) {
            this.skill[i] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[SKILL_IDX][attr],
                    (int) (selfMarbles.get(i).getCenterX()), (int) (selfMarbles.get(i).getCenterY()), WIDTH, HEIGHT, DELAY);
        }

        for (int i = 0; i < selfMarbles.size(); i++) {
            selfMarbles.get(i).getInfo().setHp(selfMarbles.get(i).getInfo().getHp() + self.getInfo().getAtk());
//            System.out.println(selfMarbles.get(i).getInfo().getName() + "血量:" + selfMarbles.get(i).getInfo().getHp());
        }
        return this.hitCount;
    }
    
    @Override
    public int explode(Marble self, GameObject target) {
        return 0;
    }
}
