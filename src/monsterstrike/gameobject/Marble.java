/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import monsterstrike.graph.Vector;
import controllers.IRC;
import monsterstrike.util.Global;
import interfaceskills.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Delay;

public abstract class Marble extends GameObject {

    protected Vector goVec;
    protected Vector norVec;
    protected Vector tanVec;
    protected Marble other;
    private float mass;
    protected boolean isCollide;
    private Delay delay;
    private int count;
    private int collideTimes;

    private String name;
    private int hp = 100;
    private int atk = 20;
    private int attribute;
    private Skills[] skills;

    public Marble(String name, int x, int y, int[] info, int attribute) {
        super(x, y, info[0], info[1], info[2]);
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
                this.getGoVec().setX(-this.getGoVec().getX());
            }
            if (this.getCenterX() + this.getR() >= Global.SCREEN_X) {
                this.setCenterX(Global.SCREEN_X - this.getR());
                this.getGoVec().setX(-this.getGoVec().getX());
            }
            if (this.getCenterY() - this.getR() <= 0) {
                this.setCenterY(this.getR());
                this.getGoVec().setY(-this.getGoVec().getY());
            }
            if (this.getCenterY() + this.getR() >= Global.SCREEN_Y) {
                this.setCenterY(Global.SCREEN_Y - this.getR());
                this.getGoVec().setY(-this.getGoVec().getY());
            }
            return true;
        }
        return false;
    }

    public abstract Marble strike(Marble other);

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

    public void setNorVec(Vector nor) {
        this.norVec = nor;
    }

    public void setTanVec(Vector tan) {
        this.tanVec = tan;
    }

    public void setGo(Vector go) {
        this.goVec = go;
    }

    public void setIsCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }
    
    public boolean getIsCollide(){
        return this.isCollide;
    }

    @Override
    public abstract void paintComponent(Graphics g);

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

    public void useSkill(int r, Marble target) {
        this.skills[r].useSkill(this, target);        
    }
}
