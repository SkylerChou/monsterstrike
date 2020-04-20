/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.util.ArrayList;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.graph.Vector;

public class Explosion extends Skills {

    private static final int SKILL_IDX = 0;
    private static final int DELAY = 5; //技能變化動畫
    private static final int NUM = 8;
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private int hitCount;
    private ArrayList<Marble> target;
    private Marble self;

    public Explosion() {
        super(NUM);
    }

    @Override
    public void update() {
        for (int j = 0; j < this.skill.length; j++) {
            if (this.skill[j] != null && !this.skill[j].getIsStop()) {
                this.skill[j].update();
            } else {
                this.skill[j] = null;
            }
        }
    }

    @Override
    public int useSkill(Marble self, ArrayList<Marble> target, int idx) {
        this.hitCount = 0;
        this.target = target;
        this.self = self;
        System.out.println(self.getInfo().getName() + "普通攻擊!");
        int attr = self.getInfo().getAttribute();
        for (int i = 0; i < this.skill.length; i++) {
            float rad = (float) (Math.toRadians(360 / NUM) * i);
            float r2 = self.getR() + target.get(idx).getR();
            float dx = (float) (2 * r2 * Math.cos((Math.PI - rad) / 2) * Math.cos(rad / 2));
            float dy = (float) (2 * r2 * Math.cos((Math.PI - rad) / 2) * Math.sin(rad / 2));
            this.skill[i] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[0][attr],
                    (int) (self.getCenterX() + dx), (int) (self.getCenterY() - r2 + dy), WIDTH, HEIGHT, DELAY);
            Vector vec = new Vector(this.skill[i].getCenterX() - self.getCenterX(), this.skill[i].getCenterY() - self.getCenterY());
            this.skill[i].setDx(vec.getUnitX());
            this.skill[i].setDy(vec.getUnitY());
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
                    atk = self.getInfo().getAtk()/2;    
                }
                target.get(i).getInfo().setHp(target.get(i).getInfo().getHp() - atk);
                this.hitCount++;
                System.out.println(target.get(i).getInfo().getName() + "血量:" + target.get(i).getInfo().getHp());
            }
        }
        return this.hitCount;
    }
    
    @Override
    public int explode(Marble self, GameObject target) {
        this.hitCount = 0;
        this.self = self;
        int attr = self.getInfo().getAttribute();
        for (int i = 0; i < this.skill.length; i++) {
            float rad = (float) (Math.toRadians(360 / NUM) * i);
            float r2 = self.getR() + target.getR();
            float dx = (float) (2 * r2 * Math.cos((Math.PI - rad) / 2) * Math.cos(rad / 2));
            float dy = (float) (2 * r2 * Math.cos((Math.PI - rad) / 2) * Math.sin(rad / 2));
            this.skill[i] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[0][attr],
                    (int) (self.getCenterX() + dx), (int) (self.getCenterY() - r2 + dy), WIDTH, HEIGHT, DELAY);
            Vector vec = new Vector(this.skill[i].getCenterX() - self.getCenterX(), this.skill[i].getCenterY() - self.getCenterY());
            this.skill[i].setDx(vec.getUnitX());
            this.skill[i].setDy(vec.getUnitY());
        }
        return this.hitCount++;
    }


    private boolean checkStrike(GameObject target) {
        for (int j = 0; j < this.skill.length; j++) {
            if (this.skill[j].isCollision(target)) {
                return true;
            }
        }
        return false;
    }

}
