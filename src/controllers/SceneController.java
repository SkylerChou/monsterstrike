/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Color;
import monsterstrike.util.CommandSolver.KeyListener;
import monsterstrike.util.CommandSolver.MouseCommandListener;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.LoadingThread;
import monsterstrike.LoadingThread.*;
import scenes.Scene;

public class SceneController {

    private class Loading implements CallbackInterface {

        @Override
        public void run() {
            kl = currentScene.getKeyListener();
            ml = currentScene.getMouseListener();
            currentScene.sceneBegin();
            isLoading = false;
        }
    }

    private Scene currentScene;
    private KeyListener kl;
    private MouseCommandListener ml;
    private boolean isLoading;
    private BufferedImage img;

    public void changeScene(Scene scene) {
        if (this.currentScene != null) {
            this.currentScene.sceneEnd();
        }

        isLoading = true;
        currentScene = scene;
        new LoadingThread(new Loading()).start();
    }

    public void sceneUpdate() {
        if (!isLoading) {
            this.currentScene.sceneUpdate();
        }
    }

    public KeyListener getKL() {
        if (!isLoading) {
            return this.kl;
        }
        return null;
    }

    public MouseCommandListener getML() {
        if (!isLoading) {
            return this.ml;
        }
        return null;
    }

    private int test = 0;

    public void paint(Graphics g) {
        if (this.isLoading) {
            g.setColor(Color.white);
            g.drawString((test++) + "", 600, 340);
//            g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, null);
            return;
        }
        this.currentScene.paint(g);
    }
}
