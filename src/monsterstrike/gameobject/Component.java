/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import monsterstrike.util.CommandSolver.MouseState;

/**
 *
 * @author kim19
 */
public abstract class Component {

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_HOVER = 1;
    public static final int STATE_PRESSED = 2;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    protected int currentState;

    public Component(int left, int top, int right, int bottom) {
        this.x = left;
        this.y = top;
        this.width = right - left;
        this.height = bottom - top;
    }

    public void update(MouseEvent e, MouseState state) {
        currentState = STATE_DEFAULT;
        if (!withinRange(e.getX(), e.getY())) {
            return;
        }

        if (state == MouseState.PRESSED || state == MouseState.DRAGGED) {
            currentState = STATE_PRESSED;
            return;
        }

        currentState = STATE_HOVER;
    }

    public boolean withinRange(int x, int y) {
        if (x < this.x || x > this.x + width) {
            return false;
        }
        if (y < this.y || y > this.y + height) {
            return false;
        }
        return true;
    }

    public void paint(Graphics g) {
        switch (currentState) {
            case STATE_PRESSED:
                pressed(g);
                break;
            case STATE_HOVER:
                hover(g);
                break;
            default:
                def(g);
                break;
        }
    }

    public abstract void def(Graphics g);

    public abstract void hover(Graphics g);

    public abstract void pressed(Graphics g);

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int getLeft(){
        return this.x;
    }
    public int getTop(){
        return this.y;
    }
    public int getW(){
        return this.width;
    }
    public int getH(){
        return this.height;
    }
}
