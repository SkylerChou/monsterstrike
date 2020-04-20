/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.graph;

public class Circle {

    private float r;
    private float x; //center
    private float y; //center

    public Circle(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public boolean intersects(float x, float y, float r) {
        float dist = (float) Math.sqrt(Math.pow(this.x - x, 2)
                + Math.pow(this.y - y, 2));
        if (dist > this.r + r) {
            return false;
        }
        return true;
    }

    public static boolean intersects(Circle a, Circle b) {
        return a.intersects(b.x, b.y, b.r);
    }

    public void offset(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public float centerX() {
        return this.x;
    }

    public float centerY() {
        return this.y;
    }

    public float getX() {
        return this.x - this.r;
    }

    public float getY() {
        return this.y - this.r;
    }

    public float getR() {
        return this.r;
    }

    public void setR(float r) {
        this.r = r;
    }

}
