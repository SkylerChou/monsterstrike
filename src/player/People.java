/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Graphics;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.Renderer;
import monsterstrike.graph.Vector;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

public class People extends GameObject {

    private Renderer renderer;
    private boolean isCollide;
    private Vector goVec;
    private boolean isSet;
    private Delay delay;

    public People(String path, int x, int y, int width, int height) {
        super(x, y, width, height, height / 2);
        this.renderer = new Renderer(path, 2, 15);
        this.isCollide = false;
        this.goVec = new Vector(0, 0);
        this.isSet = false;
        this.delay = new Delay(20);
        this.delay.start();
    }

    @Override
    public void update() {
        if (this.isCollide && this.getY() < Global.SCREEN_Y - 100) {
            if (this.delay.isTrig()) {
                this.isSet = true;
                this.offset(this.goVec.getX(), this.goVec.getY());
                if (this.getWidth() <= 50) {
                    this.setWidth(50);
                } else {
                    this.setWidth(this.getWidth() * 0.9f);
                }
            }

        } else if (this.getY() >= Global.SCREEN_Y - 100) {
            this.isCollide = false;
        } else {
            this.renderer.update();
        }
    }

    public boolean getSet() {
        return this.isSet;
    }

    public void setGoVec(Vector goVec) {
        this.goVec = goVec;
    }

    public void setCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }

    public boolean getCollide() {
        return this.isCollide;
    }

    public boolean getStop() {
        return this.renderer.getIsStop();
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight(),
                ImgInfo.PEOPLE_INFO[0], ImgInfo.PEOPLE_INFO[1]);
    }
}
