/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.graph.Vector;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

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
        this.delay = new Delay(5);
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
            if (this.count == 28) {
                this.count = 0;
                this.isCollide = false;
            }
            if (this.count % 4 == 0) {
                this.offset(5, -5);
            } else if (this.count % 4 == 1) {
                this.offset(0, 5);
            } else if (this.count % 4 == 2) {
                this.offset(-5, -5);
            } else {
                this.offset(0, 5);
            }
            this.count++;
        }
    }

    @Override
    public Marble strike(Marble other) {
        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img1, (int) this.getX(), (int) this.getY(), null);
        g.drawOval((int) (this.getCenterX() - this.getR()),
                (int) (this.getCenterY() - this.getR()),
                (int) (2 * this.getR()), (int) (2 * this.getR()));
    }

}
