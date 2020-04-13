/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Props;

import java.awt.Graphics;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.ObjectRenderer;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class Prop extends GameObject {

    protected ObjectRenderer renderer;
    protected boolean isCollide;
    private String name;

    public Prop(String[] path, int x, int y, int width, int height, int r, String name) {
        super(x, y, width, height, r);
        this.renderer = new ObjectRenderer(path, 25);
        this.isCollide = false;
        this.name = name;
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) (this.getX()), (int) (this.getY()),
                (int) (this.getWidth()), (int) (this.getHeight()));
        if (Global.IS_DEBUG) {
            g.drawOval((int) (this.getCenterX() - this.getR()),
                    (int) (this.getCenterY() - this.getR()),
                    (int) (2 * this.getR()), (int) (2 * this.getR()));
        }
    }

    @Override
    public void update() {
        this.renderer.update();
    }

    public String getName() {
        return this.name;
    }
}
