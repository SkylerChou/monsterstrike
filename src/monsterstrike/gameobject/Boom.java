/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Boom extends GameObject{
    
    private BufferedImage img;
    private int state;

    public Boom(String path, int x, int y) {
        super(x, y, 1, 1, 1);
    }

    @Override
    public void update() {
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
    }

    
    
}
