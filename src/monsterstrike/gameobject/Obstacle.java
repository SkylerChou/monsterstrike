/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;
import monsterstrike.graph.Vector;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

public class Obstacle extends GameObject {

    private ObjectRenderer renderer;

    private Delay moveDelay;
    private int moveCount;
    private Vector goVec;
    private float mass;
    private boolean isCollide;

    public Obstacle(String[] path, int x, int y, int w, int h, int r, float mass) {
        super(x, y, w, h, r);
        this.renderer = new ObjectRenderer(path, 20);
        this.goVec = new Vector(0, 0);
        this.isCollide = false;
        this.mass = mass;
        this.moveDelay = new Delay(5);
        this.moveCount = 0;        
    }

    @Override
    public void update() {
        if (this.isCollide) {
            move();
        }
    }

    public void move() {
        this.moveDelay.start();
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
                this.moveDelay.stop();
            }
        }
    }
    
    public void setGo(Vector go){
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
