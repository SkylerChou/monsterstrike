/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import monsterstrike.graph.Vector;

/**
 *
 * @author yuin8
 */
public class Rebound implements Strike {

    @Override
    public void update(Marble self) {
        self.renderer.update();
        move(self);
    }

    @Override
    public void move(Marble self) {
        if (self.getGoVec().getValue() > 0) {
            self.getGoVec().setValue(self.getGoVec().getValue() - self.getMoveFic());
            if (self.getGoVec().getValue() <= 0) {
                self.getGoVec().setValue(0);
            }
        }
        self.offset(self.getGoVec().getX(), self.getGoVec().getY());

    }

    @Override
    public Marble strike(Marble self, Marble target) {
        Vector nor = new Vector(target.getCenterX() - self.getCenterX(),
                target.getCenterY() - self.getCenterY());
        Vector originGo = self.getGoVec();
        updateDir(self, target, nor);
        if (nor.getValue() <= self.getR() + target.getR()) {
            if (self.getGoVec().getValue() == 0 && target.getGoVec().getValue() == 0) {
                self.offset(-nor.getUnitX(), -nor.getUnitY());
                target.offset(nor.getUnitX(), nor.getUnitY());
            } else {
                if (target.getInfo().getState() == 1) {
                    self.setGo(nor.resizeVec(-1 * originGo.getValue()));
                    self.offset(self.getGoVec().getX(), self.getGoVec().getY());
                    self.getGoVec().setValue(originGo.getValue() - self.getStrikeFic());
                } else {
                    self.offset(self.getGoVec().getX(), self.getGoVec().getY());
                    target.offset(target.getGoVec().getX(), target.getGoVec().getY());
                }
            }
        }
        return target;
    }

    private void updateDir(Marble self, Marble target, Vector nor) {
        self.setNorVec(self.getGoVec().getCosProjectionVec(nor));
        self.setTanVec(self.getGoVec().getSinProjectionVec(nor));
        target.setNorVec(target.getGoVec().getCosProjectionVec(nor.multiplyScalar(-1)));
        target.setTanVec(target.getGoVec().getCosProjectionVec(nor.multiplyScalar(-1)));
        float myM = self.getInfo().getMass();
        float enyM = target.getInfo().getMass();
        float m11 = (myM - enyM) / (myM + enyM);
        float m12 = (2 * enyM) / (myM + enyM);
        float m21 = (2 * myM) / (myM + enyM);
        float m22 = (enyM - myM) / (myM + enyM);
        Vector newNor1 = self.getNorVec().multiplyScalar(m11).plus(target.getNorVec().multiplyScalar(m12));
        Vector newNor2 = self.getNorVec().multiplyScalar(m21).plus(target.getNorVec().multiplyScalar(m22));

        self.setNorVec(newNor1);
        self.setGo(self.getNorVec().plus(self.getTanVec()));
        target.setNorVec(newNor2);
        target.setGo(target.getNorVec().plus(target.getTanVec()));
    }

    @Override
    public boolean die(Marble self) {
        return false;
    }

}
