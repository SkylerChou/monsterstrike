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
import monsterstrike.util.Global;

public class StandMarble extends Marble {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;
    private MarbleRenderer renderer;

    private Delay moveDelay;
    private Delay delay;
    private int count;
    private int moveCount;
    private boolean isDie;

    public StandMarble(String[] path, String name, int x, int y, int[] info) {
        super(name, x, y, info);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);
        this.currentImg = this.img1;
        this.moveDelay = new Delay(10);
        this.delay = new Delay(8);
        this.delay.start();
        this.moveDelay.start();
        this.moveCount = 0;
        this.count = 0;
        this.renderer = new MarbleRenderer(path[2], 15);
        this.isDie = false;
    }

    @Override
    public void update() {
        this.delay.start();
        this.moveDelay.start();
        if (this.delay.isTrig()) {
            if (this.count % 2 == 0) {
                this.currentImg = this.img2;
            } else {
                this.currentImg = this.img1;
            }
            this.count++;
        }
        move();
    }

    @Override
    public void move() {
        if (this.moveDelay.isTrig()) {
            if (this.moveCount % 2 == 0) {
                this.offset(this.goVec.getX(), this.goVec.getY());
            } else {
                this.offset(-this.goVec.getX(), -this.goVec.getY());
            }

            this.moveCount++;
            if (this.moveCount == 2) {
                this.moveCount = 0;
                this.isCollide = false;
                this.delay.stop();
                this.moveDelay.stop();
            }
        }
    }

    @Override
    public Marble strike(Marble other) {
        this.goVec = new Vector(this.getCenterX() - other.getCenterX(),
                this.getCenterY() - other.getCenterY());
        return null;
    }

    @Override
    public boolean die() {
        this.isDie = true;
        this.renderer.update();
        if (this.renderer.getIsStop()) {
            return true;
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (Global.IS_DEBUG) {
            g.drawOval((int) (this.getCenterX() - this.getR()),
                    (int) (this.getCenterY() - this.getR()),
                    (int) (2 * this.getR()), (int) (2 * this.getR()));
        }
        if (this.isDie) {
            this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                    (int) this.getWidth(), (int) this.getHeight());
        } else {
            g.drawImage(currentImg, (int) this.getX(), (int) this.getY(), null);
        }
    }

}
