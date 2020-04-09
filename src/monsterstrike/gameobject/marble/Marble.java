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
import monsterstrike.gameobject.*;

public abstract class Marble extends GameObject {

    protected Vector goVec;
    protected Vector norVec;
    protected Vector tanVec;
    protected Marble other;
    protected MarbleInfo info;
    protected MarbleRenderer renderer;
    protected boolean isCollide;
    protected boolean useSkill;
    private float fiction;
    private SpecialEffect shine;

    private Skills[] skills;
    private Skills currentSkill;

    public Marble(int x, int y, int w, int h, MarbleInfo info) {
        super(x, y, w, h, (int) (w*info.getRatio()/2));
        this.info = info;
        String path = ImgInfo.MARBLE_ROOT + info.getImgName() + ".png";
        int num = info.getImgW() / info.getImgH();
        this.renderer = new MarbleRenderer(path, num, 20);
        this.shine = new Shine(ImgInfo.SHINE_PATH[info.getAttribute()], x, y, ImgInfo.SHINE_INFO);
        this.goVec = new Vector(0, 0);
        this.norVec = new Vector(0, 0);
        this.tanVec = new Vector(0, 0);
        this.isCollide = false;
        this.skills = new Skills[5];
        this.setSkills();
        this.useSkill = true;
        this.fiction = 0.05f * info.getMass();
        this.currentSkill = null;
    }

    @Override
    public void update() {
//        if(this.renderer.getIsStop()){
//            this.renderer.start();
//            this.renderer.setIsStop(false);
//        }
        this.renderer.update();
//        if (this.isCollide) {
//            this.renderer.updateOnce();
//            this.isCollide = false;
//        }
        if (isBound()) {
            this.goVec.setValue(this.goVec.getValue() - 2);
        }
        move();
    }

    public void updateSkill() {
        if (this.getCurrentSkill() != null) {
            this.getCurrentSkill().update();
        }
    }

    public void updateShine() {
        this.shine.setCenterX(this.getCenterX());
        this.shine.setCenterY(this.getCenterY());
        this.shine.update();
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
                || this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
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
            if (this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
                this.setCenterY(Global.SCREEN_Y - Global.INFO_H - this.getR());
                this.getGoVec().setY(-this.getGoVec().getY());
            }
            return true;
        }
        return false;
    }

    public abstract Marble strike(Marble other);

    public void setShine(boolean isShine) {
        this.shine.setShine(isShine);
    }
    
    public Marble duplicateStand(int x, int y, int w, int h){
        MarbleInfo copyInfo = MarbleInfo.gen(this.info);
        return new StandMarble(x, y, w, h, copyInfo);
    }
    
    public Marble duplicateRebound(int x, int y, int w, int h){
        MarbleInfo copyInfo = MarbleInfo.gen(this.info);
        return new ReboundMarble(x, y, w, h, copyInfo);
    }
    
    public MarbleInfo getInfo(){
        return this.info;
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

    public Skills getCurrentSkill() {
        return this.currentSkill;
    }

    public void paintSkill(Graphics g) {
        if (this.currentSkill != null) {
            this.currentSkill.paintSkill(g);
        }
    }

    public abstract boolean die();

    public void paintAll(Graphics g) {
        this.shine.paintComponent(g);
        paintComponent(g);
        paintSkill(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) (this.getX()),
                (int) (this.getY()),
                (int) (this.getWidth()), (int) (this.getHeight()));

        if (Global.IS_DEBUG) {
            g.drawOval((int) (this.getCenterX() - this.getR()),
                    (int) (this.getCenterY() - this.getR()),
                    (int) (2 * this.getR()), (int) (2 * this.getR()));
        }
    }

    private void setSkills() {
        Skills skills[] = {
            new NormalAttack(this.info.getAttribute()),
            new CriticalAttack(this.info.getAttribute()),
            new DecreaseHalfAttack(this.info.getAttribute()),
            new Heal(),
            new Anger(this.info.getAttribute())
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
    public void genSkill(int r, MarbleArray target) {
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
