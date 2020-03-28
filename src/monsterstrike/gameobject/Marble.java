/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import monsterstrike.util.Global;
import interfaceskills.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Delay;

public class Marble extends GameObject {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;
    private Vector goVec;
    private Vector norVec;
    private Vector tanVec;
    private float mass;
    private boolean isCollide;
    private Marble other;
    private Delay delay;
    private int count;

    private String name;
    private int hp = 100;
    private int atk = 20;
    private int attribute;
    private Skills[] skills;

    public Marble(String[] path, String name, int x, int y, int[] info, int attribute) {
        super(x, y, info[0], info[1], info[2]);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
        this.currentImg = this.img1;
        this.mass = info[3];
        this.goVec = new Vector(0, 0);
        this.norVec = new Vector(0, 0);
        this.tanVec = new Vector(0, 0);
        this.isCollide = false;
        this.delay = new Delay(20);
        this.count = 0;
        this.name = name;
        this.attribute = attribute;
        this.skills = new Skills[5];
        this.setSkills();
    }

    @Override
    public void update() {
        if (this.delay.isTrig()) {
            this.count++;
            if (this.count % 2 != 0) {
                this.currentImg = this.img2;
            } else {
                this.currentImg = this.img1;
            }
        }
        isBound();
        move();
    }

    public void move() {
        this.isCollide = false;
        if (this.goVec.getValue() > 0) {
            this.goVec.setValue(this.goVec.getValue() - 0.3f);
            if (this.goVec.getValue() <= 0.01) {
                this.goVec.setValue(0);
            }
        }
        this.offset(this.goVec.getX(), this.goVec.getY());
    }


    public boolean isBound() {
        if (this.getCenterX() - this.getR() <= 0
                || this.getCenterX() + this.getR() >= Global.SCREEN_X
                || this.getCenterY() - this.getR() <= 0
                || this.getCenterY() + this.getR() >= Global.SCREEN_Y) {
            if (this.getCenterX() - this.getR() <= 0) {
                this.setCenterX(this.getR());
                this.goVec.setX(-this.goVec.getX());
            }
            if (this.getCenterX() + this.getR() >= Global.SCREEN_X) {
                this.setCenterX(Global.SCREEN_X - this.getR());
                this.goVec.setX(-this.goVec.getX());
            }
            if (this.getCenterY() - this.getR() <= 0) {
                this.setCenterY(this.getR());
                this.goVec.setY(-this.goVec.getY());
            }
            if (this.getCenterY() + this.getR() >= Global.SCREEN_Y) {
                this.setCenterY(Global.SCREEN_Y - this.getR());
                this.goVec.setY(-this.goVec.getY());
            }
            return true;
        }
        return false;
    }

    public Marble strike(Marble other) {
        this.isCollide = true;
        this.other = other;

        Vector nor = new Vector(this.other.getCenterX() - this.getCenterX(),
                this.other.getCenterY() - this.getCenterY());
        updateDir(nor);
        if (nor.getValue() < this.getR() + this.other.getR()) {
            this.move();
            this.other.move();
        }
        return this.other;
    }

    private void updateDir(Vector nor) {
        this.norVec = this.goVec.getCosProjectionVec(nor);
        this.tanVec = this.goVec.getSinProjectionVec(nor);

        this.other.norVec = this.other.goVec.getCosProjectionVec(nor.multiplyScalar(-1));
        this.other.tanVec = this.other.goVec.getSinProjectionVec(nor.multiplyScalar(-1));

        float m11 = (this.mass - this.other.mass) / (this.mass + this.other.mass);
        float m12 = (2 * this.other.mass) / (this.mass + this.other.mass);
        float m21 = (2 * this.mass) / (this.mass + this.other.mass);
        float m22 = (this.other.mass - this.mass) / (this.mass + this.other.mass);
        Vector newNor1 = this.norVec.multiplyScalar(m11).plus(this.other.norVec.multiplyScalar(m12));
        Vector newNor2 = this.norVec.multiplyScalar(m21).plus(this.other.norVec.multiplyScalar(m22));

        this.norVec = newNor1;
        this.goVec = this.norVec.plus(this.tanVec);
        this.other.norVec = newNor2;
        this.other.goVec = this.other.norVec.plus(this.other.tanVec);
    }

    public String getName() {
        return this.name;
    }

    public int getAttribute() {
        return this.attribute;
    }

    public int getHp() {
        return this.hp;
    }

    public int getAtk() {
        return this.atk;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public float getMass() {
        return this.mass;
    }

    public Vector getNorVec() {
        return this.norVec;
    }

    public Vector getTanVec() {
        return this.tanVec;
    }

    public Vector getGoVec() {
        return this.goVec;
    }

    public void setGo(Vector go) {
        this.goVec = go;
    }

    public void setIsCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(currentImg, (int) this.getX(), (int) this.getY(), null);
        g.drawOval((int) (this.getCenterX() - this.getR()),
                (int) (this.getCenterY() - this.getR()),
                (int) (2 * this.getR()), (int) (2 * this.getR()));
    }

    private void setSkills() {
        Skills skills[] = {
            new NormalAttack(),
            new CriticalAttack(),
            new DecreaseHalfAttack(),
            new Heal(),
            new Anger()
        };

        for (int i = 0; i < skills.length; i++) {
            this.skills[i] = skills[i];
        }
    }

    public void useSkill(Marble target) {
        int r = (int) (Math.random() * 2);
        this.skills[r].useSkill(this, target);
    }
}
