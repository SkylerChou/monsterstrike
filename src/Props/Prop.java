/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Props;

import java.awt.Graphics;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.ObjectRenderer;
import monsterstrike.gameobject.marble.MarbleRenderer;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class Prop extends GameObject {

    protected MarbleRenderer renderer;
    protected boolean isCollide;
    private String name;

    public Prop(String path, int x, int y, int width, int height, int r, int pictureNum,String name) {
        super(x, y, width, height, r);
        this.renderer = new MarbleRenderer(path,pictureNum, 35);
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
    
    public void paintH(Graphics g) {
        this.renderer.paintH(g, (int) (this.getX()),
                (int) (this.getY()),
                (int) (this.getWidth()), (int) (this.getHeight()));
    }
    public void paintS(Graphics g) {
        this.renderer.paintS(g, (int) (this.getX()),
                (int) (this.getY()),
                (int) (this.getWidth()), (int) (this.getHeight()));
    }
    
    public boolean getIsStop(){
        return this.renderer.getIsStop();
    }

    @Override
    public void update() {
        this.renderer.update();
    }

    public void updateOnce(){
        this.renderer.updateOnce();
    }
    
    public String getName() {
        return this.name;
    }
}
