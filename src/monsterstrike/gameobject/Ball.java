/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;
import monsterstrike.graph.Vector;
import monsterstrike.util.Global;

public class Ball extends GameObject {

    protected Vector goVec;
    protected Vector norVec;
    protected Vector tanVec;
    protected Ball other;
    protected ObjectRenderer renderer;
    protected boolean isCollide;
    private float fiction;
    private float mass;

    public Ball(String[] path, int x, int y, int w, int h, int r, float mass) {
        super(x, y, w, h, r);
        this.renderer = new ObjectRenderer(path, 20);
        this.goVec = new Vector(0, 0);
        this.norVec = new Vector(0, 0);
        this.tanVec = new Vector(0, 0);
        this.isCollide = false;
        this.mass = mass;
        this.fiction = 0;
    }
    
    public Ball(int x, int y, int w, int h, int r) {
        super(x, y, w, h, r);
        this.goVec = new Vector(0, 0);
        this.norVec = new Vector(0, 0);
        this.tanVec = new Vector(0, 0);
        this.isCollide = false;
        this.mass = mass;
        this.fiction = 0;
    }

    @Override
    public void update() {
        this.renderer.update();
        if (isBound()) {
            this.goVec.setValue(this.goVec.getValue() - this.fiction);
        }
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
//                || this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H
                ) {
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
//            if (this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
//                this.setCenterY(Global.SCREEN_Y - Global.INFO_H - this.getR());
//                this.getGoVec().setY(-this.getGoVec().getY());
//            }
            return true;
        }
        return false;
    }

    public Ball strike(Ball other) {
        this.isCollide = true;
        this.other = other;
        Vector nor = new Vector(this.other.getCenterX() - this.getCenterX(),
                this.other.getCenterY() - this.getCenterY());
        updateDir(nor);
        if (nor.getValue() <= this.getR() + this.other.getR()) {
            if (this.goVec.getValue() == 0 && this.other.getGoVec().getValue() == 0) {
                this.offset(-nor.getUnitX(), -nor.getUnitY());
                this.other.offset(nor.getUnitX(), nor.getUnitY());
            } else {
                this.offset(this.goVec.getX(), this.goVec.getY());
                this.other.offset(this.other.getGoVec().getX(), this.other.getGoVec().getY());
            }
        }
        return this.other;
    }
    
    public void hit(GameObject gameObject) {
        this.isCollide = true;
        Vector vec = new Vector(gameObject.getCenterX()-this.getCenterX(), gameObject.getCenterY()-this.getCenterY());
        this.norVec = this.goVec.getCosProjectionVec(vec).multiplyScalar(-1);
        this.tanVec = this.goVec.getSinProjectionVec(vec);
        this.goVec = this.norVec.plus(this.tanVec);
        this.offset(this.goVec.getX(), this.goVec.getY());
    }

    private void updateDir(Vector nor) {
        this.norVec = this.goVec.getCosProjectionVec(nor);
        this.tanVec = this.goVec.getSinProjectionVec(nor);

        this.other.setNorVec(this.other.getGoVec().getCosProjectionVec(nor.multiplyScalar(-1)));
        this.other.setTanVec(this.other.getGoVec().getSinProjectionVec(nor.multiplyScalar(-1)));

        float myM = this.getMass();
        float enyM = this.other.getMass();
        float m11 = (myM - enyM) / (myM + enyM);
        float m12 = (2 * enyM) / (myM + enyM);
        float m21 = (2 * myM) / (myM + enyM);
        float m22 = (enyM - myM) / (myM + enyM);
        Vector newNor1 = this.norVec.multiplyScalar(m11).plus(this.other.getNorVec().multiplyScalar(m12));
        Vector newNor2 = this.norVec.multiplyScalar(m21).plus(this.other.getNorVec().multiplyScalar(m22));

        this.norVec = newNor1;
        this.goVec = this.norVec.plus(this.tanVec);
        this.other.setNorVec(newNor2);
        this.other.setGo(this.other.getNorVec().plus(this.other.getTanVec()));
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

    public boolean getIsCollide() {
        return this.isCollide;
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

}
