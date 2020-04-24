/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import controllers.ARC;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.marble.Marble;

public class Laser extends Skills {

    private static final int SKILL_IDX = 2;
    private static final int DELAY = 20; //技能變化動畫
    private static final int NUM = 4;
    private static final int WIDTH = 150;
    private static final int HEIGHT = 1300;

    private int hitCount;

    public Laser() {
        super(NUM);
        this.hitCount = 0;
    }

    @Override
    public int useSkill(Marble self, ArrayList<Marble> target, int targetIdx) {
//        System.out.print(self.getInfo().getName() + " 爆擊 !");
        ARC.getInstance().play("/resources/wav/laser.wav");
        int attr = self.getInfo().getAttribute();
        int halfLength = SkillImg.SKILL_UNIT_Y[SKILL_IDX][attr] / 2;
        this.skill[0] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[SKILL_IDX][attr],
                (int) (self.getCenterX()), (int) (self.getCenterY() - self.getR() - halfLength), WIDTH, HEIGHT, DELAY);
        this.skill[1] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[SKILL_IDX][attr],
                (int) (self.getCenterX() + self.getR() + halfLength), (int) (self.getCenterY()), WIDTH, HEIGHT, DELAY);
        this.skill[2] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[SKILL_IDX][attr],
                (int) (self.getCenterX()), (int) (self.getCenterY() + self.getR() + halfLength), WIDTH, HEIGHT, DELAY);
        this.skill[3] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[SKILL_IDX][attr],
                (int) (self.getCenterX() - self.getR() - halfLength), (int) (self.getCenterY()), WIDTH, HEIGHT, DELAY);
        int selfA = self.getInfo().getAttribute();
        for (int i = 0; i < target.size(); i++) {
            int targetA = target.get(i).getInfo().getAttribute();
            if (inSkillRange(self, target.get(i))) {
                int atk = (int) (self.getInfo().getAtk() * Math.random() * 2 + 1);
                if ((selfA != 2 && selfA - targetA == -1) || (selfA == 2 && targetA == 0)
                        || (selfA == 4 && targetA == 3)) {
                    atk = 2 * self.getInfo().getAtk();
                } else if ((selfA != 3 && selfA - targetA == 1) || (selfA == 0 && targetA == 2)
                        || (selfA == 3 && targetA == 4)) {
                    atk = self.getInfo().getAtk()/2;    
                }
                
                target.get(i).getInfo().setHp(target.get(i).getInfo().getHp() - atk);
                this.hitCount++;
//                System.out.println(target.get(i).getInfo().getName() + "血量:" + target.get(i).getInfo().getHp());
            }
        }
        return this.hitCount;
    }

    private boolean inSkillRange(Marble m, Marble target) {
        int left = (int) (m.getCenterX() - SkillImg.SKILL_UNIT_X[2][0] / 2);
        int right = (int) (m.getCenterX() + SkillImg.SKILL_UNIT_X[2][0] / 2);
        int top = (int) (m.getCenterY() - SkillImg.SKILL_UNIT_X[2][0] / 2);
        int bottom = (int) (m.getCenterY() + SkillImg.SKILL_UNIT_X[2][0] / 2);

        if (target.getCenterX() + target.getR() < left
                && target.getCenterY() + target.getR() < top) {
            return false;
        }
        if (target.getCenterX() - target.getR() > right
                && target.getCenterY() + target.getR() < top) {
            return false;
        }
        if (target.getCenterX() + target.getR() < left
                && target.getCenterY() - target.getR() > bottom) {
            return false;
        }
        if (target.getCenterX() - target.getR() > right
                && target.getCenterY() - target.getR() > bottom) {
            return false;
        }

        return true;
    }

    @Override
    public void paintSkill(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldForm = g2d.getTransform();
        for (int i = 0; i < this.skill.length; i++) {
            if (this.skill[i] != null) {
                g2d.rotate(i * (Math.PI / 2), (int) (this.skill[i].getCenterX()), (int) (this.skill[i].getCenterY()));
                this.skill[i].paint(g2d);
                g2d.setTransform(oldForm);
            }
        }
    }
    
    @Override
    public int explode(Marble self, GameObject target) {
        return 0;
    }

}
