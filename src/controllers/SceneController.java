/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Color;
import java.awt.Font;
import monsterstrike.util.CommandSolver.KeyListener;
import monsterstrike.util.CommandSolver.MouseCommandListener;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import monsterstrike.LoadingThread;
import monsterstrike.LoadingThread.*;
import monsterstrike.gameobject.Dino;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.util.Global;
import scenes.Scene;

public class SceneController {

    private class Loading implements CallbackInterface {

        @Override
        public void run() {
            currentScene.sceneBegin();
            kl = currentScene.getKeyListener();
            ml = currentScene.getMouseListener();
            isLoading = false;
        }
    }

    private Scene currentScene;
    private KeyListener kl;
    private MouseCommandListener ml;
    private boolean isLoading;
    private BufferedImage img;
    private Dino dino;

    public void changeScene(Scene scene) {
        if (this.currentScene != null) {
            this.currentScene.sceneEnd();
        }
        isLoading = true;
        currentScene = scene;
        kl = null;
        ml = null;
        this.dino = new Dino(ImgInfo.DINO, 650, 300, Dino.STEPS_RUN);
        this.img = IRC.getInstance().tryGetImage(ImgInfo.LOADING_PATH);
        new LoadingThread(new Loading()).start();
    }

    public void sceneUpdate() {
        if (!isLoading) {
            this.currentScene.sceneUpdate();
        }else{
            this.dino.update();
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

    public void paint(Graphics g) {
        if (this.isLoading) {
            g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, null);
            this.dino.paint(g);
            return;
        }
        this.currentScene.paint(g);
    }
}
