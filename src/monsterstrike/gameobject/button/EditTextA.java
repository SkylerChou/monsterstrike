/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.button;

import controllers.IRC;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EditTextA extends EditText {

    private BufferedImage img;
    
    public EditTextA(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
        img = IRC.getInstance().tryGetImage("/resources/items/input.png");
    }

    @Override
    public void focus(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
//        g.setColor(Color.WHITE);
//        g.drawRect(x, y, width, height);
    }

    @Override
    public void unfocus(Graphics g) {
        g.setColor(Color.BLACK);// set font color
    }
}
