/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.StandMarble;
import monsterstrike.graph.Vector;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class ReboundBall extends Marble {

    protected BufferedImage img1;
    protected BufferedImage img2;
    protected BufferedImage currentImg;
    protected Delay delay;
    protected int count;

    public ReboundBall(String[] path, String name, int x, int y, int[] info) {
        super(name, x, y, info);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
        this.currentImg = this.img1;
        this.delay = new Delay(20);
        this.delay.start();
        this.isCollide = false;
    }

    @Override
    public Marble strike(Marble other) {
        this.isCollide = true;
        this.other = other;
        Vector nor = new Vector(this.other.getCenterX() - this.getCenterX(),
                this.other.getCenterY() - this.getCenterY());
        Vector originGo = this.goVec;
        updateDir(nor);
        if (nor.getValue() <= this.getR() + this.other.getR()) {
            if (this.goVec.getValue() == 0 && this.other.getGoVec().getValue() == 0) {
                this.offset(-nor.getUnitX(), -nor.getUnitY());
                this.other.offset(nor.getUnitX(), nor.getUnitY());
            } else {
                if (other instanceof StandMarble) {
                    this.setGo(nor.resizeVec(-1 * originGo.getValue()));
                    this.offset(this.goVec.getX(), this.goVec.getY());
                    this.goVec.setValue(originGo.getValue() * 0.8f);
                } else {
                    this.offset(this.goVec.getX(), this.goVec.getY());
                    this.other.offset(this.other.getGoVec().getX(), this.other.getGoVec().getY());
                }
            }
        }
        return this.other;
    }

    private void updateDir(Vector nor) {
        this.norVec = this.goVec.getCosProjectionVec(nor);
        this.tanVec = this.goVec.getSinProjectionVec(nor);

        this.other.setNorVec(this.other.getGoVec().getCosProjectionVec(nor.multiplyScalar(-1)));
        this.other.setTanVec(this.other.getGoVec().getSinProjectionVec(nor.multiplyScalar(-1)));

        float m11 = (this.getMass() - this.other.getMass()) / (this.getMass() + this.other.getMass());
        float m12 = (2 * this.other.getMass()) / (this.getMass() + this.other.getMass());
        float m21 = (2 * this.getMass()) / (this.getMass() + this.other.getMass());
        float m22 = (this.other.getMass() - this.getMass()) / (this.getMass() + this.other.getMass());
        Vector newNor1 = this.norVec.multiplyScalar(m11).plus(this.other.getNorVec().multiplyScalar(m12));
        Vector newNor2 = this.norVec.multiplyScalar(m21).plus(this.other.getNorVec().multiplyScalar(m22));

        this.norVec = newNor1;
        this.goVec = this.norVec.plus(this.tanVec);
        this.other.setNorVec(newNor2);
        this.other.setGo(this.other.getNorVec().plus(this.other.getTanVec()));
    }

    @Override
    public boolean isBound() {
        if (this.getCenterX() - this.getR() <= 0
                || this.getCenterX() + this.getR() >= Global.SCREEN_X
                || this.getCenterY() - this.getR() <= 0) {
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
            return true;
        }
        return false;
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
    }

    @Override
    public boolean die() {
        return false;
    }

    @Override
    public void paintScale(Graphics g, int x, int y, int w, int h) {

    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(currentImg, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight(), null);
        if (Global.IS_DEBUG) {
        g.drawOval((int) (this.getCenterX() - this.getR()),
                (int) (this.getCenterY() - this.getR()),
                (int) (2 * this.getR()), (int) (2 * this.getR()));
        }
    }
}
