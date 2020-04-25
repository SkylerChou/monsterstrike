/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.button;

import java.awt.Graphics;
import monsterstrike.gameobject.ObjectRenderer;
import monsterstrike.gameobject.SceneObject;

public class ButtonRenderer extends SceneObject {

    private ObjectRenderer renderer;
    private boolean isShow;
    private boolean isFocus;

    public ButtonRenderer(String[] path, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.renderer = new ObjectRenderer(path, 0);
        this.isShow = true;
        this.isFocus = false;
    }

    public ButtonRenderer(String[] path, int x, int y, int width, int height, int frame) {
        super(x, y, width, height);
        this.renderer = new ObjectRenderer(path, frame);
        this.isShow = true;
        this.isFocus = false;
    }

    public void resetImg() {
        this.renderer.restImg();
    }

    @Override
    public void update() {
        if (this.isFocus) {
            this.renderer.update();
        }
    }
    
    public void setFocus(boolean isFocus){
        this.isFocus = isFocus;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }
    
    public boolean getFocus(){
        return this.isFocus;
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
