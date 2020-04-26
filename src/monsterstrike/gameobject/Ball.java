/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.ARC;
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
    private Ball detect;

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
                || this.getCenterY() - this.getR() <= 0 //                || this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H
                ) {
            if (this.getCenterX() - this.getR() <= 0) {
                ARC.getInstance().play("/resources/wav/Pinball.wav");
                this.setCenterX(this.getR());
                this.getGoVec().setX(-this.getGoVec().getX());
            }
            if (this.getCenterX() + this.getR() >= Global.SCREEN_X) {
                ARC.getInstance().play("/resources/wav/Pinball.wav");
                this.setCenterX(Global.SCREEN_X - this.getR());
                this.getGoVec().setX(-this.getGoVec().getX());
            }
            if (this.getCenterY() - this.getR() <= 0) {
                ARC.getInstance().play("/resources/wav/Pinball.wav");
                this.setCenterY(this.getR());
                this.getGoVec().setY(-this.getGoVec().getY());
            }
            return true;
        }
        return false;
    }

    public void strike(Ball other) {
        this.isCollide = true;
//        this.other = other;
        Vector nor = new Vector(other.getCenterX() - this.getCenterX(),
                other.getCenterY() - this.getCenterY());
        updateDir(nor);
        if (nor.getValue() != 0) {
            updateDir(nor);
        }
    }

    public void hit(GameObject gameObject) {
        this.isCollide = true;
        Vector vec = new Vector(gameObject.getCenterX() - this.getCenterX(), gameObject.getCenterY() - this.getCenterY());
        this.norVec = this.goVec.getCosProjectionVec(vec).multiplyScalar(-1);
        this.tanVec = this.goVec.getSinProjectionVec(vec);
        this.goVec = this.norVec.plus(this.tanVec);
        this.offset(this.goVec.getX(), this.goVec.getY());
    }
    
    public void detectStill(GameObject target) {
        if (this.goVec.getValue() > 0) {
            float dist = this.getDetect().dist(target);
            Vector v1 = this.goVec;
            float x1 = this.getCenterX();
            float y1 = this.getCenterY();
            float x2 = target.getCenterX();
            float y2 = target.getCenterY();
            Vector vec = new Vector(target.getCenterX() - this.getCenterX(),
                    target.getCenterY() - this.getCenterY());
            float x = this.getCenterX() + vec.getCosProjectionVec(this.goVec).getX();
            float y = this.getCenterY() + vec.getCosProjectionVec(this.goVec).getY();
            if (dist < this.getR() + target.getR() || inMiddle(this, x, y)) {
                float a = (float) (Math.pow(v1.getValue(), 2));
                float b = 2 * ((x1 - x2) * (v1.getX()) + (y1 - y2) * (v1.getY()));
                float c = (float) (Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)
                        - Math.pow(this.getR() + target.getR(), 2));
                float d = (float) ((-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                if (Math.pow(b, 2) - 4 * a * c >= 0 && a > 0) {
                    this.offset(this.goVec.getX() * d, this.goVec.getY() * d);
                }
            }
        }
    }

    public float dist(GameObject target) {
        float dist = (float) Math.sqrt(Math.pow((target.getCenterX() - this.getCenterX()), 2)
                + Math.pow((target.getCenterY() - this.getCenterY()), 2));
        return dist;
    }

    private boolean inMiddle(Ball self, float x, float y) {
        float minX = self.getCenterX();
        float maxX = self.getDetect().getCenterX();
        float minY = self.getCenterY();
        float maxY = self.getDetect().getCenterY();
        if (self.getDetect().getCenterX() < self.getCenterX()) {
            minX = self.getDetect().getCenterX();
            maxX = self.getCenterX();
        }
        if (self.getDetect().getCenterY() < self.getCenterY()) {
            minY = self.getDetect().getCenterY();
            maxY = self.getCenterY();
        }
        if (x > minX && x < maxX && y > minY && y < maxY) {
            return true;
        }
        return false;
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

    public Ball getDetect() {
        return this.detect;
    }

    public void setDetect(Ball detect) {
        this.detect = detect;
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
