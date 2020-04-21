/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import monsterstrike.graph.*;
import monsterstrike.util.Global;
import java.awt.Color;
import java.awt.Graphics;

public abstract class GameObject {

    private Circle collider;
    protected Rect rect;

    public GameObject(int x, int y, int width, int height, int r) {
        this.rect = Rect.genWithCenter(x, y, width, height);
        this.collider = new Circle(x, y, r);
    }

    public float top() {
        return this.rect.top();
    }

    public float bottom() {
        return this.rect.bottom();
    }

    public float right() {
        return this.rect.right();
    }

    public float left() {
        return this.rect.left();
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

    public void setX(float x) {
        this.rect.setLeft(x);
    }

    public void setY(float y) {
        this.rect.setTop(y);
    }

    public void setWidth(float width) {
        float ratio = width / this.getWidth();
        float h = ratio * this.getHeight();
        this.rect.setLeft(this.rect.centerX() - width / 2);
        this.rect.setRight(this.rect.left() + width);
        this.rect.setTop(this.rect.centerY() - h / 2);
        this.rect.setBottom(this.rect.top() + h);
//        this.collider.setR(this.getR() * ratio);
    }

    public float getR() {
        return this.collider.getR();
    }

    public float getWidth() {
        return this.rect.width();
    }

    public float getHeight() {
        return this.rect.height();
    }

    public boolean isCollision(GameObject obj) {
        if (this.collider == null || obj.collider == null) {
            return false;
        }
        return Circle.intersects(this.collider, obj.collider);
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

    public void paint(Graphics g) {
        paintComponent(g);
        if (Global.IS_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect((int) this.rect.left(), (int) this.rect.top(), (int) this.rect.width(), (int) this.rect.height());
            g.setColor(Color.BLUE);
            g.drawOval((int) this.collider.getX(), (int) this.collider.getY(), (int) (2 * this.collider.getR()), (int) (2 * this.collider.getR()));
            g.setColor(Color.BLACK);
        }
    }

    public boolean isOutOfBound() {
        if (this.getX() + this.getWidth() <= 0 || this.getY() + this.getHeight() <= 0
                || this.getX() >= Global.SCREEN_X || this.getY() >= Global.SCREEN_Y - Global.INFO_H) {
            return true;
        }
        return false;
    }

    public abstract void paintComponent(Graphics g);

    public abstract void update();

}
