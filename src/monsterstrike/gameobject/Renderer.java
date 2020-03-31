/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Delay;

public class Renderer {

    private BufferedImage img;
    private Delay delay;
    private int idx;

    public Renderer(String path, int delay) {
        this.img = IRC.getInstance().tryGetImage(path);
        this.idx = 0;
        this.delay = new Delay(5);
        this.delay.start();
    }

    public void update() {
        if (this.delay.isTrig()) {
            idx = (idx + 1) % 4;
        }
    }

    public void paintComponent(Graphics g, int x, int y, int w, int h) {
        g.drawImage(img, x, y, x + w, y + h,
                ImgInfo.BOOM_UNIT_X * idx, 0,
                ImgInfo.BOOM_UNIT_X * (idx + 1), ImgInfo.BOOM_UNIT_Y, null);
    }
}
