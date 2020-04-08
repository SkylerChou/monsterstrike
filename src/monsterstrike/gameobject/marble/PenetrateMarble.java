/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import monsterstrike.graph.Vector;
import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

public class PenetrateMarble extends Marble {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;

    private Delay delay;
    private int count;

    public PenetrateMarble(String[] path, String name, int x, int y, int[] info) {
        super(name, x, y, info);
        this.img1 = IRC.getInstance().tryGetPNG(path[0]);
        this.img2 = IRC.getInstance().tryGetPNG(path[1]);
        this.currentImg = this.img1;
        this.delay = new Delay(20);
        this.count = 0;
        this.isCollide = false;
    }

    @Override
    public void update() {
        this.delay.start();
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

    @Override
    public Marble strike(Marble other) {
        this.isCollide = true;
        this.other = other;
        Vector nor = new Vector(this.other.getCenterX() - this.getCenterX(),
                this.other.getCenterY() - this.getCenterY());

        if (nor.getValue() <= this.getR() + this.other.getR()) {
            if (other instanceof StandMarble) {
                this.goVec.setValue(this.goVec.getValue() * 0.8f);
                this.offset(this.goVec.getX(), this.goVec.getY());
            } else {
                updateDir(nor);
                this.offset(this.goVec.getX(), this.goVec.getY());
                this.other.offset(this.other.goVec.getX(), this.other.goVec.getY());
            }
        }
        return this.other;
    }

    @Override
    public boolean die() {
        return true;
    }

    private void updateDir(Vector nor) {
        this.norVec = this.goVec.getCosProjectionVec(nor);
        this.tanVec = this.goVec.getSinProjectionVec(nor);

        this.other.norVec = this.other.goVec.getCosProjectionVec(nor.multiplyScalar(-1));
        this.other.tanVec = this.other.goVec.getSinProjectionVec(nor.multiplyScalar(-1));

        float m11 = (this.getMass() - this.other.getMass()) / (this.getMass() + this.other.getMass());
        float m12 = (2 * this.other.getMass()) / (this.getMass() + this.other.getMass());
        float m21 = (2 * this.getMass()) / (this.getMass() + this.other.getMass());
        float m22 = (this.other.getMass() - this.getMass()) / (this.getMass() + this.other.getMass());
        Vector newNor1 = this.norVec.multiplyScalar(m11).plus(this.other.norVec.multiplyScalar(m12));
        Vector newNor2 = this.norVec.multiplyScalar(m21).plus(this.other.norVec.multiplyScalar(m22));

        this.norVec = newNor1;
        this.goVec = this.norVec.plus(this.tanVec);
        this.other.norVec = newNor2;
        this.other.goVec = this.other.norVec.plus(this.other.tanVec);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(currentImg, (int) this.getX(), (int) this.getY(), null);
        if (Global.IS_DEBUG) {
            g.drawOval((int) (this.getCenterX() - this.getR()),
                    (int) (this.getCenterY() - this.getR()),
                    (int) (2 * this.getR()), (int) (2 * this.getR()));
        }
    }

    @Override
    public void paintScale(Graphics g, int x, int y, int w, int h) {
        
    }
}
