/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PaintText {

    private static int count = 0;

    public static void paint(Graphics g, Font font,
            Color color, String text, int x, int y, int width) {
        g.setFont(font);
        g.setColor(color);
        Graphics2D g2d = (Graphics2D) g;
        int stringLen = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        int start = width / 2 - stringLen / 2;
        g2d.drawString(text, x + start, y);
    }

    public static void paintWithShadow(Graphics g, Font font, Color color,
            Color shadow, String text, int x, int y, int offset, int width) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);
        g2d.setColor(shadow);
        int stringLen = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        int start = width / 2 - stringLen / 2;
        g2d.drawString(text, x + start + offset, y + offset);
        g2d.setColor(color);
        g2d.drawString(text, x + start, y);
    }

    public static void paintWithNumber(Graphics g, Font font, Font numFont, Color color,
            Color shadow, String text, String num, int x, int y, int offset, int width) {
        Graphics2D g2d = (Graphics2D) g;
        int stringLen = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        paintWithShadow(g, font, color, shadow, text, x, y, offset, width);
        paintWithShadow(g, numFont, color, shadow, num, x + stringLen + 25, y, offset, width);
    }

    public static void paintTwinkle(Graphics g, Font font1, Font font2, Color color, Color shadow,
            String text1, String text2, int x, int y, int offset, int width, int delay) {
        if (count++ % delay < delay / 2) {
            paintWithShadow(g, font1, color, shadow, text1, x, y, offset, width);
            paintWithShadow(g, font1, color, shadow, text2, x, y + 80, offset, width);
        } else {
            paintWithShadow(g, font2, color, shadow, text1, x, y, offset, width);
            paintWithShadow(g, font2, color, shadow, text2, x, y + 80, offset, width);
        }
        if (count >= 1000) {
            count = 0;
        }
    }

    public static void paintTwinkle(Graphics g, Font font1, Color color,
            String text1, int x, int y, int width, int delay) {
        if (count++ % delay < delay / 2) {
            paint(g, font1, color, text1, x, y, width);
        }
        if (count >= 1000) {
            count = 0;
        }
    }

    public static void paintTwinkle(Graphics g, Font font1, Font font2, Color color,
            String text1, int x, int y, int width, int delay) {
        if (count++ % delay < delay / 2) {
            paint(g, font1, color, text1, x, y, width);
        } else {
            paint(g, font2, color, text1, x, y, width);
        }
        if (count >= 1000) {
            count = 0;
        }
    }

}
