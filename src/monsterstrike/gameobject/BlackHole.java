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
public class BlackHole extends SpecialEffect {

    private int width;
    private int height;
    
    public BlackHole(String[] path, int x, int y, int[] info) {
        super(path, x, y, info, 10);
        this.width = info[0];
        this.height = info[1];
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(super.currentImg, (int) this.getX() ,(int)this.getY(), this.width, this.height, null);
    }
}
