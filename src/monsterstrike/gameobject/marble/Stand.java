/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import monsterstrike.util.Delay;

public class Stand implements Strike {

    private Delay moveDelay;
    private int moveCount;

    public Stand() {
        this.moveDelay = new Delay(5);
        this.moveCount = 0;
        
    }

    @Override
    public void update(Marble self) {
        self.renderer.update();
        if (!self.isDie && self.isCollide) {
            move(self);
        } else if (self.isDie) {
            self.rendererDie.updateHit();
        }
    }

    @Override
    public void strike(Marble self, Marble target) {
    }

    @Override
    public void move(Marble self) {
        this.moveDelay.start();
        if (this.moveDelay.isTrig()) {
            if (this.moveCount % 2 == 0) {
                self.offset(self.goVec.getX(), self.goVec.getY());
            } else {
                self.offset(-self.goVec.getX(), -self.goVec.getY());
            }
            this.moveCount++;
            if (this.moveCount == 2) {
                this.moveCount = 0;
                self.setIsCollide(false);
                this.moveDelay.stop();
            }
        }
    }

}
