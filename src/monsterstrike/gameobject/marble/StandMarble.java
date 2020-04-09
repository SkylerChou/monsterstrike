/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import monsterstrike.gameobject.ImgInfo;
import monsterstrike.graph.Vector;
import monsterstrike.util.Delay;

public class StandMarble extends Marble {

    private Delay moveDelay;
    private int moveCount;
    private boolean isDie;

    public StandMarble(int x, int y, int w, int h, MarbleInfo info) {
        super(x, y, w, h, info);
        this.moveDelay = new Delay(5);
        this.moveDelay.start();
        this.moveCount = 0;
        this.isDie = false;
    }

    @Override
    public void update() {
        if (!this.isDie) {
            this.renderer.update();
            if (this.isCollide) {
                move();
            }
        }

    }

    @Override
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

    @Override
    public Marble strike(Marble other) {
        this.goVec = new Vector(this.getCenterX() - other.getCenterX(),
                this.getCenterY() - other.getCenterY());
        return null;
    }


    @Override
    public boolean die() {
        this.isDie = true;
        String path = ImgInfo.MARBLE_ROOT + this.info.getImgName() + "Die.png";
        this.renderer = new MarbleRenderer(path, 7, 5);

        if (this.renderer.getIsStop()) {
            return true;
        }
        return false;
    }

}
