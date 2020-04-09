/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.graph;

import java.awt.Graphics2D;

public class Rect {

    private float left;
    private float right;
    private float top;
    private float bottom;

    public Rect(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public static Rect genWithCenter(float x, float y, float width, float height) {
        float left = x - width / 2f;
        float right = left + width;
        float top = y - height / 2f;
        float bottom = top + height;
        return new Rect(left, top, right, bottom);
    }

    public boolean intersects(float left, float top, float right, float bottom) {
        if (this.left > right) {
            return false;
        }
        if (this.right < left) {
            return false;
        }
        if (this.top > bottom) {
            return false;
        }
        if (this.bottom < top) {
            return false;
        }
        return true;
    }

    public float centerX() {
        return (this.left + this.right) / 2f;
    }

    public float centerY() {
        return (this.top + this.bottom) / 2f;
    }

    public void offset(float dx, float dy) {
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
    }

    public float left() {
        return this.left;
    }
    
     public void paint(Graphics2D g) {
        g.fillRect((int)this.left,(int) this.top, (int)this.width(), (int)this.height());
    }
       
    public float top() {
        return this.top;
    }

    public float right() {
        return this.right;
    }

    public float bottom() {
        return this.bottom;
    }

    public float width() {
        return this.right - this.left;
    }

    public float height() {
        return this.bottom - this.top;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

}
