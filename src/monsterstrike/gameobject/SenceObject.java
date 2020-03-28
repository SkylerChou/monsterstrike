/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;
import monsterstrike.graph.Rect;

/**
 *
 * @author kim19
 */
public abstract class SenceObject {

    protected Rect rect;

    public SenceObject(int x, int y, int width, int height) {
        this.rect = Rect.genWithCenter(x, y, width, height);
    }

    public float getCenterX() {
        return this.rect.centerX();
    }

    public float getCenterY() {
        return this.rect.centerY();
    }

    public abstract void paint(Graphics g);

    public abstract void update();
}
