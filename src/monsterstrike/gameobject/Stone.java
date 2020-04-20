/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;
import monsterstrike.gameobject.GameObject;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.Renderer;

public class Stone extends GameObject {

    private Renderer renderer;
    private boolean isCollide;

    public Stone(String path, int x, int y, int width, int height) {
        super(x, y, width, height, height / 2);
        this.renderer = new Renderer(path, 3, 15);
        this.isCollide = false;
    }

    @Override
    public void update() {
        if (this.isCollide) {
            this.renderer.updateOnce();
        }
    }

    public void setCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }

    public boolean getCollide() {
        return this.isCollide;
    }

    public boolean getStop() {
        return this.renderer.getIsStop();
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) this.getX(), (int) this.getY(),
                (int) this.getWidth(), (int) this.getHeight(),
                ImgInfo.STONE_INFO[0], ImgInfo.STONE_INFO[1]);
    }

}
