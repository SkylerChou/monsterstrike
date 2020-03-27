/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collisionproject.gameobject;

import collisionproject.util.Delay;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author kim19
 */
public class SpecialEffectIce extends GameObject {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;
    private Delay delay;
    private int count;

    public SpecialEffectIce(String[] path, int x, int y, int[] info) {
        super(x, y, info[0], info[1], info[2]);
        try {
            img1 = ImageIO.read(getClass().getResource(path[0]));
            img2 = ImageIO.read(getClass().getResource(path[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.delay = new Delay(25);
        this.delay.start();
        this.currentImg = img1;
        this.count = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.currentImg, (int) (this.getCenterX() - this.getR()), (int) (this.getCenterY() - this.getR()), 130, 130, null);
    }

    @Override
    public void update() {
        if (delay.isTrig()) {
            this.count++;
            if (this.count % 2 != 0) {
                this.currentImg = this.img2;
            } else {
                this.currentImg = this.img1;
            }
        }
    }

}
