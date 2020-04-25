/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import monsterstrike.util.Delay;

public abstract class EditText extends Component {

    private static interface EditTextState {

        public void paint(Graphics g);
    }

    private class Focus implements EditTextState {

        @Override
        public void paint(Graphics g) {
            focus(g);

            // resize font
            Font font = g.getFont().deriveFont(fontSize);
            FontMetrics fm = g.getFontMetrics(font);
            g.setFont(font);

            drawString(g, fm);

            drawCursor(g, fm);
        }
    }

    private class Unfocus implements EditTextState {

        @Override
        public void paint(Graphics g) {
            unfocus(g);
            
            // resize font
            Font font = g.getFont().deriveFont(fontSize);
            FontMetrics fm = g.getFontMetrics(font);
            g.setFont(font);

            drawString(g, fm);
        }
    }

    private EditTextState etState;
    private String text;
    private String hint;
    private float fontSize;
    private Color hintFontColor;
    private Color fontColor;
    private Color cursorColor;
    private Delay delay;
    private boolean cursorTrig;

    private static final int LEFT_BOUND_OFFSET = 2;
    private static final int CURSOR_OFFSET = 4;

    public EditText(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
        text = "";
        etState = new Unfocus();
        fontSize = 25;
        fontColor = Color.BLACK;
        cursorColor = Color.WHITE;
        hintFontColor = Color.WHITE;

        delay = new Delay(40);
        delay.start();
        cursorTrig = true;
    }

    public void setCursorFlash(int cursorFlash) {
        delay = new Delay(cursorFlash);
        delay.restart();
    }

    public void setFontColor(Color color) {
        this.fontColor = color;
    }

    public void setCursorColor(Color color) {
        this.cursorColor = color;
    }

    public void setHintFontColor(Color color){
        this.hintFontColor = color;
    }
    
    public void setFontSize(float size) {
        this.fontSize = size;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public void setHint(String hint){
        this.hint = hint;
    }

    public String getText() {
        return this.text;
    }

    public void active() {
        etState = new Focus();
    }

    public boolean isActive() {
        return etState instanceof Focus;
    }

    private void drawCursor(Graphics g, FontMetrics fm) {
        // draw cursor
        if (delay.isTrig()) {
            cursorTrig = !cursorTrig;
        }
        if (cursorTrig) {
            g.setColor(cursorColor);// set cursor color
            int heightOffset = (height - fm.getHeight()) / 2;
            g.fillRect(x + fm.stringWidth(text) + CURSOR_OFFSET,
                    y + heightOffset,
                    2,
                    height - heightOffset * 2);
        }
        g.setColor(Color.black);// reset color
    }

    private void drawString(Graphics g, FontMetrics fm) {
        if(text.length() == 0){
            g.setColor(hintFontColor);
            g.drawString(hint, x + LEFT_BOUND_OFFSET, y
                + ((height - fm.getHeight()) / 2)
                + fm.getAscent());
            return;
        }
        // check width
        if (fm.stringWidth(text) + CURSOR_OFFSET > width) {
            text = text.substring(0, text.length() - 1);
        }

        // draw string
        g.setColor(fontColor);// set font color
        g.drawString(text, x + LEFT_BOUND_OFFSET, y
                + ((height - fm.getHeight()) / 2)
                + fm.getAscent());
        g.setColor(Color.black);// reset color
    }

    public abstract void focus(Graphics g);

    public abstract void unfocus(Graphics g);

    @Override
    public void def(Graphics g) {
        etState.paint(g);
    }

    @Override
    public void hover(Graphics g) {
        etState.paint(g);
    }

    @Override
    public void pressed(Graphics g) {
        etState.paint(g);
    }

    public void update(char c, long trigTime) {
        if (!isActive()) {
            return;
        }
        if ((int) c == 8) {
            if(text.length() > 0){
                text = text.substring(0, text.length() - 1);
            }
            return;
        }
        if ((int) c == 10) {
            etState = new Unfocus();
            return;
        }
        text += c;
    }
}

