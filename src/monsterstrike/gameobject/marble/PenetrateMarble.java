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


    private int count;

    public PenetrateMarble(int x, int y, int w, int h, MarbleInfo info) {
        super(x, y, w, h, info);
        this.isCollide = false;
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

        float myM = this.info.getMass();
        float enyM = this.other.info.getMass();
        float m11 = (myM - enyM) / (myM + enyM);
        float m12 = (2 * enyM) / (myM + enyM);
        float m21 = (2 * myM) / (myM + enyM);
        float m22 = (enyM - myM) / (myM + enyM);
        Vector newNor1 = this.norVec.multiplyScalar(m11).plus(this.other.norVec.multiplyScalar(m12));
        Vector newNor2 = this.norVec.multiplyScalar(m21).plus(this.other.norVec.multiplyScalar(m22));

        this.norVec = newNor1;
        this.goVec = this.norVec.plus(this.tanVec);
        this.other.norVec = newNor2;
        this.other.goVec = this.other.norVec.plus(this.other.tanVec);
    }


}
