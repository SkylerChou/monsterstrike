/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Background {

    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage img;

    public Background(String path, int x, int y, int width, int height) {
        this.img = IRC.getInstance().tryGetImage(path);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void paint(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }
}
