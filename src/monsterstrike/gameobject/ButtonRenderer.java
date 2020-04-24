/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;


public class ButtonRenderer extends SceneObject {

    private ObjectRenderer renderer;
    private boolean isShow;

    public ButtonRenderer(String[] path, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.renderer = new ObjectRenderer(path, 0);
        this.isShow = true;
    }

    public ButtonRenderer(String[] path, int x, int y, int width, int height, int frame) {
        super(x, y, width, height);
        this.renderer = new ObjectRenderer(path, frame);
        this.isShow = true;
    }

    public void resetImg() {
        this.renderer.restImg();
    }

    @Override
    public void update() {
        this.renderer.update();
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean getIsShow() {
        return this.isShow;
    }

    @Override
    public void paint(Graphics g) {
        if (this.isShow) {
            this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                    (int) this.getWidth(), (int) this.getHeight());
        }
    }

}
