/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
//ImageResourceController

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IRC {

    private static class KeyPair {

        private String path;
        private BufferedImage img;
        private Image jif;

        public KeyPair(String path, BufferedImage img, Image jif) {
            this.path = path;
            this.img = img;
            this.jif = jif;
        }
    }

    private static IRC irc;

    private ArrayList<KeyPair> imgPair;

    private IRC() {//建構子為private，讓外面無法創建實體
        imgPair = new ArrayList<>();
    }

    public static IRC getInstance() {
        if (irc == null) {
            irc = new IRC(); //只創建一次實體
        }
        return irc;
    }

    public BufferedImage tryGetPNG(String path) {
        KeyPair pair = findKeyPair(path);
        if (pair == null) {
            return addPNG(path);
        }
        return pair.img;
    }

    public Image tryGetJPG(String path) {
        KeyPair pair = findKeyPair(path);
        if (pair == null) {
            return addJPG(path);
        }
        return pair.jif;
    }

    private KeyPair findKeyPair(String path) {
        for (int i = 0; i < imgPair.size(); i++) {
            KeyPair pair = imgPair.get(i);
            if (pair.path.equals(path)) {
                return pair;
            }
        }
        return null;
    }

    private BufferedImage addPNG(String path) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            imgPair.add(new KeyPair(path, img, null));
            return img;
        } catch (IOException e) {

        }
        return null;
    }

    private Image addJPG(String path) {
        try {
            Image img = new ImageIcon(getClass().getResource(path)).getImage();
            imgPair.add(new KeyPair(path, null, img));
            return img;
        } catch (Exception e) {

        }
        return null;
    }

}
