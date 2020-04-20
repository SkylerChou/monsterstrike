/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import monsterstrike.graph.Vector;

public class Rebound implements Strike {

    @Override
    public void update(Marble self) {
        self.renderer.update();
        move(self);
    }

    @Override
    public void move(Marble self) {
        if (self.goVec.getValue() > 0) {
            self.goVec.setValue(self.goVec.getValue() - self.getMoveFic());
            if (self.goVec.getValue() <= 0) {
                self.goVec.setValue(0);
            }
        }
        self.offset(self.goVec.getX(), self.goVec.getY());
    }

    @Override
    public void strike(Marble self, Marble target) {
        Vector nor = new Vector(target.getCenterX() - self.getCenterX(),
                target.getCenterY() - self.getCenterY());
        updateDir(self, target, nor);
    }

    private void updateDir(Marble self, Marble target, Vector nor) {
        self.setNorVec(self.goVec.getCosProjectionVec(nor));
        self.setTanVec(self.goVec.getSinProjectionVec(nor));
        target.setNorVec(target.goVec.getCosProjectionVec(nor.multiplyScalar(-1)));
        target.setTanVec(target.goVec.getCosProjectionVec(nor.multiplyScalar(-1)));
        float myM = self.getInfo().getMass();
        float enyM = target.getInfo().getMass();
        float m11 = (myM - enyM) / (myM + enyM);
        float m12 = (2 * enyM) / (myM + enyM);
        float m21 = (2 * myM) / (myM + enyM);
        float m22 = (enyM - myM) / (myM + enyM);
        Vector newNor1 = self.norVec().multiplyScalar(m11).plus(target.norVec().multiplyScalar(m12));
        Vector newNor2 = self.norVec().multiplyScalar(m21).plus(target.norVec().multiplyScalar(m22));
        self.setNorVec(newNor1);
        self.setGo(self.norVec().plus(self.tanVec()));
        target.setNorVec(newNor2);
        target.setGo(target.norVec().plus(target.tanVec()));
    }

    public boolean die(Marble self) {
        return false;
    }

}
