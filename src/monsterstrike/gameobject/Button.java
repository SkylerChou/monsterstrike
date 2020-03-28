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

/**
 *
 * @author kim19
 */
public class Button extends SenceObject {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;
    private Delay delay;
    private int count;

    public Button(String[] path, int x, int y, int width, int height) {
        super(x, y, ImgInfo.MAINBUTTON_INFO[0], ImgInfo.MAINBUTTON_INFO[1]);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);

        this.delay = new Delay(25);
        this.delay.start();
        this.currentImg = this.img1;
        this.count = 0;
    }

    @Override
    public void update() {
        if (this.delay.isTrig()) {
            this.count++;
            if (this.count % 2 != 0) {
                this.currentImg = this.img2;
            } else {
                this.currentImg = this.img1;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.img1, (int)this.rect.centerX(), (int)this.rect.centerY(), 200, 50, null);
    }
}
