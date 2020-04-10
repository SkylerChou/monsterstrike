/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//黑洞, 怪物Shine
package monsterstrike.gameobject;

import java.awt.Graphics;

public class SpecialEffect extends GameObject {

    private ObjectRenderer renderer;
    private boolean isShine;

    public SpecialEffect(String[] path, int x, int y, int w, int h, int r) {
        super(x, y, w, h, r);
        this.renderer = new ObjectRenderer(path, 20);
        this.isShine = false;
    }

    @Override
    public void update() {
        this.renderer.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (this.isShine) {
            this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                    (int) this.getWidth(), (int) this.getHeight());
        }
    }
    
    public void setShine(boolean isShine) {
        this.isShine = isShine;
    }

    public boolean getIsShine() {
        return this.isShine;
    }
}
