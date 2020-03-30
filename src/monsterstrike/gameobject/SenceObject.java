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
    protected Rect collider;

    public SenceObject(int x, int y, int width, int height) {
        this.rect = Rect.genWithCenter(x, y, width, height);
        this.collider=Rect.genWithCenter(x, y, width, height);
    }

   public float getCenterX() {
        return this.collider.centerX();
    }

    public float getCenterY() {
        return this.collider.centerY();
    }

    public float getX() {
        return this.rect.left();
    }

    public float getY() {
        return this.rect.top();
    }

    public float getWidth() {
        return this.rect.width();
    }

    public float getHeight() {
        return this.rect.height();
    }


    public void offset(float dx, float dy) {
        this.rect.offset(dx, dy);
        this.collider.offset(dx, dy);
    }

    public void setCenterX(float x) {
        this.rect.offset(x - this.rect.centerX(), 0);
        this.collider.offset(x - this.collider.centerX(), 0);
    }

    public void setCenterY(float y) {
        this.rect.offset(0, y - this.rect.centerY());
        this.collider.offset(0, y - this.collider.centerY());
    }

    public abstract void paint(Graphics g);

    public abstract void update();
}
