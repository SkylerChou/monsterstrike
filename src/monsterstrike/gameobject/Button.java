/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import controllers.IRC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Button extends SceneObject {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage currentImg;
    private int count;

    public Button(String[] path, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.img1 = IRC.getInstance().tryGetPNG(path[0]);
        this.img2 = IRC.getInstance().tryGetPNG(path[1]);
        this.currentImg = this.img1;
    }

    @Override
    public void update() {
            this.count++;
            if (this.count % 2 != 0) {
                this.currentImg = this.img2;
            } else {
                this.currentImg = this.img1;
            }
    }
    
    public void paintOther(Graphics g,int [] size) {
        g.drawImage(this.currentImg, (int) this.getCenterX(), (int)this.getCenterY(), size[0], size[1], null);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.currentImg, (int) this.getCenterX(), (int)this.getCenterY(), 200, 50, null);
    }
}
