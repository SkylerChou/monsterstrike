/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
//ImageResourceController

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class IRC {

    private static class KeyPair {

        private String path;
        private BufferedImage img;

        public KeyPair(String path, BufferedImage img) {
            this.path = path;
            this.img = img;
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

    public BufferedImage tryGetImage(String path) {
        KeyPair pair = findKeyPair(path);
        if (pair == null) {
            return addImage(path);
        }
        return pair.img;
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

    private BufferedImage addImage(String path) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            imgPair.add(new KeyPair(path, img));
            return img;
        } catch (IOException e) {

        }
        return null;
    }

}
