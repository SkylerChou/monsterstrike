/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;

public class Boom extends GameObject {

    private int state;
    private Renderer renderer;

    public Boom(String path, int x, int y) {
        super(x, y, ImgInfo.BOOM_UNIT_X, ImgInfo.BOOM_UNIT_Y, 1);
        this.renderer = new Renderer(path, 15);
    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paintComponent(g, (int) this.getX(), (int) this.getY(),
                ImgInfo.BOOM_UNIT_X, ImgInfo.BOOM_UNIT_Y);
    }

}
