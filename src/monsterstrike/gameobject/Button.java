/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Delay;

public class Button extends SceneObject {

    private ObjectRenderer renderer;

    public Button(String[] path, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.renderer = new ObjectRenderer(path, 0);
    }
    public Button(String[] path, int x, int y, int width, int height,int frame) {
        super(x, y, width, height);
        this.renderer = new ObjectRenderer(path,frame);
    }
    
    @Override
    public void update() {
        this.renderer.update();
    }

    @Override
    public void paint(Graphics g) {
        this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight());
    }
}
