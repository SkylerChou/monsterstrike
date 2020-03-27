/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import monsterstrike.util.Delay;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SpecialEffect extends GameObject {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;
    private Delay delay;
    private int count;

    public SpecialEffect(String[] path, int x, int y, int[] info) {
        super(x, y, info[0], info[1], info[2]);
        this.img1 = IRC.getInstance().tryGetImage(path[0]);
        this.img2 = IRC.getInstance().tryGetImage(path[1]);

        this.delay = new Delay(25);
        this.delay.start();
        this.currentImg = img1;
        this.count = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
//        g.drawImage(this.currentImg, (int) (this.getCenterX() - this.getR()), (int) (this.getCenterY() - this.getR()), null);
        g.drawImage(this.currentImg, (int) (this.getCenterX() - this.getR()), (int) (this.getCenterY() - this.getR()), 130, 130, null);

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
}
