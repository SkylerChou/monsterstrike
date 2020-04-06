/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;

/**
 *
 * @author kim19
 */
public class Shine extends SpecialEffect {

    public Shine(String[] path, int x, int y, int[] info) {
        super(path, x, y, info, 25);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (this.getIsShine()) {
            g.drawImage(currentImg, (int) (this.getCenterX() - this.getR()), (int) (this.getCenterY() - this.getR()), ImgInfo.SHINE_INFO[0], ImgInfo.SHINE_INFO[1], null);
        }
    }
}
