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

public class StandMarble extends Marble {

    private BufferedImage img1;

    private float originX;
    private float originY;
    private Delay delay;
    private int count;

    public StandMarble(String[] path, String name, int x, int y, int[] info, int attribute) {
        super(name, x, y, info, attribute);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.originX = this.getCenterX();
        this.originY = this.getCenterY();
        this.delay = new Delay(15);
        this.count = 0;
    }

    @Override
    public void update() {
        this.delay.start();
        move();
    }

    @Override
    public void move() {
        if (this.delay.isTrig()) {

            if (this.count % 2 == 0) {
                this.offset(this.goVec.getX(), this.goVec.getY());
            } else {
                this.offset(-this.goVec.getX(), -this.goVec.getY());
            }

            this.count++;
            if (this.count == 2) {
                this.count = 0;
                this.isCollide = false;
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
    public void paintComponent(Graphics g) {
//        if (this.getHp() > 0) {
        g.drawImage(img1, (int) this.getX(), (int) this.getY(), null);
//        }
//        g.drawOval((int) (this.getCenterX() - this.getR()),
//                (int) (this.getCenterY() - this.getR()),
//                (int) (2 * this.getR()), (int) (2 * this.getR()));
    }

}
