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
import monsterstrike.graph.Vector;

public class Bullet extends Skills {

    private static final int SKILL_IDX = 3;
    private static final int DELAY = 15; //技能變化動畫
    private static final int NUM = 12;
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private int hitCount;
    private ArrayList<Marble> target;
    private Marble self;

    public Bullet() {
        super(NUM);
    }

    @Override
    public void update() {
        for (int j = 0; j < this.skill.length; j++) {
            if (this.skill[j] != null && !this.skill[j].getIsStop()) {
                this.skill[j].offset(this.skill[j].getDx(), this.skill[j].getDy());
                if (checkStrike(j, target)) {
                    this.skill[j].setDx(0f);
                    this.skill[j].setDy(0f);
                    this.skill[j].update();
                    this.skill[j] = null;
                }else if(this.skill[j].isOutOfBound()){
                    this.skill[j] = null;
                }
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
//        System.out.println(self.getInfo().getName() + "子彈!");
        int attr = self.getInfo().getAttribute();
        if (!target.isEmpty()) {
            for (int i = 0; i < this.skill.length; i++) {
                float rad = (float) (Math.toRadians(360 / NUM) * i);
                float r2 = self.getR() + target.get(idx).getR();
                float dx = (float) (2 * r2 * Math.cos((Math.PI - rad) / 2) * Math.cos(rad / 2));
                float dy = (float) (2 * r2 * Math.cos((Math.PI - rad) / 2) * Math.sin(rad / 2));
                this.skill[i] = new SkillComponent(SKILL_IDX, attr, SkillImg.SKILL_NUM[0][attr],
                        (int) (self.getCenterX() + dx), (int) (self.getCenterY() - r2 + dy), WIDTH, HEIGHT, DELAY);
                Vector vec = new Vector(this.skill[i].getCenterX() - self.getCenterX(), this.skill[i].getCenterY() - self.getCenterY());
                this.skill[i].setDx(vec.getUnitX() * 4);
                this.skill[i].setDy(vec.getUnitY() * 4);
            }
        }
        ARC.getInstance().play("/resources/wav/shoot.wav");
        return this.hitCount;
    }

    private boolean checkStrike(int idx, ArrayList<Marble> target) {
        int selfA = self.getInfo().getAttribute();
        int atk = self.getInfo().getAtk();
        for (int j = 0; j < target.size(); j++) {
            int targetA = target.get(j).getInfo().getAttribute();
            if (this.skill[idx].isCollision(target.get(j)) && !this.target.get(j).getIsCollide()) {
                if ((selfA != 2 && selfA - targetA == -1) || (selfA == 2 && targetA == 0)
                        || (selfA == 4 && targetA == 3)) {
                    atk = 2 * self.getInfo().getAtk();
                } else if ((selfA != 3 && selfA - targetA == 1) || (selfA == 0 && targetA == 2)
                        || (selfA == 3 && targetA == 4)) {
                    atk = self.getInfo().getAtk()/2;    
                }
                target.get(j).getInfo().setHp(target.get(j).getInfo().getHp() - atk);       
                this.target.get(j).getInfo().setHp(this.target.get(j).getInfo().getHp() - this.self.getInfo().getAtk());
//                System.out.println(this.target.get(j).getInfo().getName() + "血量:" + this.target.get(j).getInfo().getHp());
                this.target.get(j).setIsCollide(true);
                this.hitCount++;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getHitCount(){
        return this.hitCount;
    }

    @Override
    public void paintSkill(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldForm = g2d.getTransform();
        for (int i = 0; i < this.skill.length; i++) {
            float rad = (float) (Math.toRadians(360 / NUM) * i);
            if (this.skill[i] != null) {
                g2d.rotate(rad, (int) (this.skill[i].getCenterX()), (int) (this.skill[i].getCenterY()));
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
