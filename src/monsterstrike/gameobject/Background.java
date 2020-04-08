/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.util.Global;

public class Background extends SceneObject {

    private BufferedImage img;
    private int idx;

    public Background(String path, int x, int y, int idx) {
        super(x, y, Global.SCREEN_X, Global.SCREEN_Y);
        this.img = IRC.getInstance().tryGetPNG(path);
        this.idx = idx;
    }

    public void offset(int dx) {
        this.offset(dx, 0);
    }

    @Override
    public void update() {
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y,
                (int) this.getX() - ImgInfo.BACKGROUND_SIZE[idx][0], 0,
                (int) this.getX(), ImgInfo.BACKGROUND_SIZE[idx][1], null);
    }

    public void paintMenu(Graphics g) {
        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, null);
    }

    public void paintItem(Graphics g, int x, int y, int w, int h) {
        g.drawImage(img, x, y, x + w, y + h,
                0, 0, ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], null);
    }
}
