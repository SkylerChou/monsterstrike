/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.graph.Vector;
import monsterstrike.util.Delay;

public class ReboundMarble extends Marble {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;

    private Delay delay;
    private int count;

    public ReboundMarble(String[] path, String name, int x, int y, int[] info) {
        super(name, x, y, info);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
        this.currentImg = this.img1;
        this.delay = new Delay(20);
        this.delay.start();
        this.count = 0;
        this.isCollide = false;
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
        if (this.getCurrentSkill() != null) {
            this.getCurrentSkill().update();
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
        Vector originGo = this.goVec;
        updateDir(nor);
        if (nor.getValue() <= this.getR() + this.other.getR()) {
            if (this.goVec.getValue() == 0 && this.other.goVec.getValue() == 0) {
                this.offset(-nor.getUnitX(), -nor.getUnitY());
                this.other.offset(nor.getUnitX(), nor.getUnitY());
            }else{
                if (other instanceof StandMarble) {
                    this.setGo(nor.resizeVec(-1 * originGo.getValue()));
                    this.offset(this.goVec.getX(), this.goVec.getY());
                    this.goVec.setValue(originGo.getValue() * 0.8f);
                } else {
                    this.offset(this.goVec.getX(), this.goVec.getY());
                    this.other.offset(this.other.goVec.getX(), this.other.goVec.getY());
                }
            }
        }
        return this.other;
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
        g.drawOval((int) (this.getCenterX() - this.getR()),
                (int) (this.getCenterY() - this.getR()),
                (int) (2 * this.getR()), (int) (2 * this.getR()));
    }

}
