/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Color;
import java.awt.Graphics;

public class EditTextA extends EditText {

    public EditTextA(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    @Override
    public void focus(Graphics g) {
        g.setColor(Color.ORANGE);
        g.drawRect(x, y, width, height);
    }

    @Override
    public void unfocus(Graphics g) {
    }
}
