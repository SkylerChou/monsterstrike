/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import monsterstrike.graph.Vector;
import monsterstrike.util.Global;
import interfaceskills.*;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.GameObject;

public abstract class Marble extends GameObject {

    protected Vector goVec;
    protected Vector norVec;
    protected Vector tanVec;
    protected Marble other;
    private float mass;
    protected boolean isCollide;
    protected boolean useSkill;
    private float fiction;
    private float velocity;

    private String name;
    private int hp = 500;
    private int atk = 20;
    private int attribute;
    private Skills[] skills;
    private Skills currentSkill;

    public Marble(String name, int x, int y, int[] info) {
        super(x, y, info[0], info[1], info[2]);
        this.mass = info[3];
        this.velocity = info[4];
        this.attribute = info[5];
        this.goVec = new Vector(0, 0);
        this.norVec = new Vector(0, 0);
        this.tanVec = new Vector(0, 0);
        this.isCollide = false;
        this.name = name;
        this.skills = new Skills[5];
        this.setSkills();
        this.useSkill = true;
        this.fiction = 0.05f * this.mass;
        this.currentSkill = null;
    }

    @Override
    public void update() {
        isBound();
        move();
    }

    public void move() {
        this.isCollide = false;
        if (this.goVec.getValue() > 0) {
            this.goVec.setValue(this.goVec.getValue() - this.fiction);
            if (this.goVec.getValue() <= 0) {
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

    public boolean getUseSkill() {
        return this.useSkill;
    }

    public void setUseSkill(Boolean useSkill) {
        this.useSkill = useSkill;
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

    public boolean getIsCollide() {
        return this.isCollide;
    }

    public float getVelocity() {
        return this.velocity;
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

    public Skills getCurrentSkill() {
        return this.currentSkill;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            hp = 0;
        }
        this.hp = hp;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public void paintSkill(Graphics g) {
        if (this.currentSkill != null) {
            this.currentSkill.paintSkill(g);
        }
    }

    public abstract boolean die();

    @Override
    public abstract void paintComponent(Graphics g);

    private void setSkills() {
        Skills skills[] = {
            new NormalAttack(this.attribute),
            new CriticalAttack(this.attribute),
            new DecreaseHalfAttack(this.attribute),
            new Heal(),
            new Anger(this.attribute)
        };

        for (int i = 0; i < skills.length; i++) {
            this.skills[i] = skills[i];
        }
    }

    public void genSkill(int r, Marble target) {
        this.skills[r].genSkill(this, target);
        this.currentSkill = this.skills[r];
    }

    public void genSkill(int r, ArrayList<Marble> target) {
        this.skills[r].genSkill(this, target);
        this.currentSkill = this.skills[r];
    }

    public ArrayList<SkillComponent> getSkillComponent() {
        if (this.currentSkill != null) {
            return this.currentSkill.getSkillComponent();
        }
        return null;
    }
}
