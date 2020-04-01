/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import monsterstrike.gameobject.marble.Marble;

public class NormalAttack implements Skills {

    private int attribute;
    private Explosion[] explosion;
    private int num;

    public NormalAttack(int attribute) {
        this.attribute = attribute;
        this.num = 12;
    }

    @Override
    public void update() {
        for (int i = 0; i < this.explosion.length; i++) {
            this.explosion[i].update();
        }
    }

    @Override
    public void useSkill(Marble self, Marble target) {
        System.out.println(self.getName() + " 發動普通攻擊 !");
        target.setHp(target.getHp() - self.getAtk());
        System.out.println(target.getName() + " 剩下血量 :" + target.getHp() + "點");
        this.explosion = new Explosion[12];
        for (int i = 0; i < this.num; i++) {
            float rad = (float) (Math.toRadians(360 / this.num) * i);
            float dx = (float) (2 * (self.getR() + target.getR()) * Math.cos((Math.PI - rad) / 2) * Math.cos(rad / 2));
            float dy = (float) (2 * (self.getR() + target.getR()) * Math.cos((Math.PI - rad) / 2) * Math.sin(rad / 2));
            this.explosion[i] = new Explosion(this.attribute, SkillImg.SKILL_NUM[0][this.attribute],
                    (int) (target.getCenterX() + dx), (int) (target.getCenterY() + dy));
        }
    }

    @Override
    public void paintSkill(Graphics g) {
        for (int i = 0; i < this.explosion.length; i++) {
            this.explosion[i].paint(g);
        }
    }

}
