/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.Renderer;
import monsterstrike.graph.Vector;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

public class Cloud extends GameObject {

    private Renderer renderer;
    private boolean isCollide;
    private Vector goVec;
    private Vector moveVec;
    private Delay moveDelay;
    private int moveCount;

    public Cloud(String path, int x, int y, int width, int height, int r) {
        super(x, y, width, height, r);
        this.renderer = new Renderer(path, 4, 15);
        this.moveVec = new Vector(-1, 0);
        this.goVec = new Vector(0, 0);
        this.isCollide = false;
        this.moveDelay = new Delay(2);
        this.moveCount = 0;
    }

    @Override
    public void update() {
        this.renderer.update();
        if (this.isCollide) {
            move();
        }
        isBound();
        this.offset(this.moveVec.getX(), this.moveVec.getY());
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
                this.setCollide(false);
                this.moveDelay.stop();
            }
        }
    }

    public void setGo(Vector go) {
        this.goVec = go;
    }

    public void setCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }

    public boolean getCollide() {
        return this.isCollide;
    }

    public boolean isBound() {
        if (this.getCenterX() - this.getR() <= 0
                || this.getCenterX() + this.getR() >= Global.SCREEN_X
                || this.getCenterY() - this.getR() <= 0
                || this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
            if (this.getCenterX() - this.getR() <= 0) {
                this.setCenterX(this.getR());
                this.moveVec.setX(-this.moveVec.getX());
            }
            if (this.getCenterX() + this.getR() >= Global.SCREEN_X) {
                this.setCenterX(Global.SCREEN_X - this.getR());
                this.moveVec.setX(-this.moveVec.getX());
            }
            if (this.getCenterY() - this.getR() <= 0) {
                this.setCenterY(this.getR());
                this.moveVec.setY(-this.moveVec.getY());
            }
            if (this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
                this.setCenterY(Global.SCREEN_Y - Global.INFO_H - this.getR());
                this.moveVec.setY(-this.moveVec.getY());
            }
            return true;
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight(),
                ImgInfo.CLOUD_INFO[0], ImgInfo.CLOUD_INFO[1]);
    }

}
