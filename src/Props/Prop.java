/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Props;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.MarbleRenderer;

/**
 *
 * @author kim19
 */
public abstract class Prop extends GameObject {

    protected MarbleRenderer renderer;
    protected boolean isCollide;
    protected boolean isUsed;
    private String name;

    public Prop(String path, int x, int y, int width, int height, int r, int pictureNum, int delay) {
        super(x, y, width, height, r);
        this.renderer = new MarbleRenderer(path, pictureNum, delay);
        this.isCollide = false;
        this.name = name;
        this.isUsed = false;
    }

    @Override
    public void update() {
        if (this.isCollide) {
            this.renderer.updateOnce();
        }
    }
    
    public abstract void useProp(ArrayList<Marble> marble, int idx);

    @Override
    public abstract void paintComponent(Graphics g);

    public boolean getIsStop() {
        return this.renderer.getIsStop();
    }

    public void setIsCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }

    public boolean getIsCollide() {
        return this.isCollide;
    }

    public String getName() {
        return this.name;
    }
}
