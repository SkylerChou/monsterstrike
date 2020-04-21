/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.*;
import monsterstrike.util.*;

/**
 *
 * @author kim19
 */
public class Menu extends Scene {

    private Background menu;
    private Dino dino;
    private ArrayList<Button> buttons;
    private Delay delay;
    private int h;

    private boolean isEnter;
    
    public Menu(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.menu = new Background(ImgInfo.MENU, 0, 0, 1);
        this.buttons = new ArrayList<>();
        this.h = 50;
        this.dino = new Dino(ImgInfo.DINO, Global.SCREEN_X / 2 - 120, Global.SCREEN_Y / 2, Dino.STEPS_WALK);
        this.buttons.add(new Button(ImgInfo.SINGLE, Global.SCREEN_X / 2, Global.SCREEN_Y / 2, ImgInfo.MAINBUTTON_INFO[0], ImgInfo.MAINBUTTON_INFO[1]));
        this.buttons.add(new Button(ImgInfo.CONTINUE, Global.SCREEN_X / 2, Global.SCREEN_Y / 2 + h, ImgInfo.MAINBUTTON_INFO[0], ImgInfo.MAINBUTTON_INFO[1]));
        this.buttons.add(new Button(ImgInfo.PINBALL, Global.SCREEN_X / 2, Global.SCREEN_Y / 2 + 2 * h, ImgInfo.MAINBUTTON_INFO[0], ImgInfo.MAINBUTTON_INFO[1]));
        this.buttons.add(new Button(ImgInfo.EXIT, Global.SCREEN_X / 2, Global.SCREEN_Y / 2 + 3 * h, ImgInfo.MAINBUTTON_INFO[0], ImgInfo.MAINBUTTON_INFO[1]));
        this.isEnter = false;
        this.delay = new Delay(25);
        this.delay.start();
        
    }

    @Override
    public void sceneUpdate() {
        this.dino.update();
        if (this.delay.isTrig()) {
            int tmp = 0;
            for (int i = 0; i < this.buttons.size(); i++) {
                if (this.dino.getCenterY() == this.buttons.get(i).getCenterY()) {
                    tmp = i;
                    this.buttons.get(i).update();
                }
            }
            for (int k = 0; k < this.buttons.size(); k++) {
                if (k == tmp) {
                    continue;
                }
                this.buttons.get(k).resetImg();
            }
        }

        if (this.isEnter && this.dino.getCenterY() == Global.SCREEN_Y / 2) {
            sceneController.changeScene(new PlayerScene(sceneController));
            this.isEnter = false;
        } else if (this.isEnter && this.dino.getCenterY() == Global.SCREEN_Y / 2 + h) {
            sceneController.changeScene(new LevelMenu(sceneController, "playerInfo.csv", "marbleInfo.csv", true));
            this.isEnter = false;
        } else if (this.isEnter && this.dino.getCenterY() == Global.SCREEN_Y / 2 + 2 * h) {
            sceneController.changeScene(new Tutorial(sceneController, "/resources/howtoplay.png", 
                    5, new PingPong(sceneController)));
//            sceneController.changeScene(new TutorialPinball(sceneController));
            this.isEnter = false;
        } else if (this.isEnter && this.dino.getCenterY() == Global.SCREEN_Y / 2 + 3 * h) {
            System.exit(0);
            this.isEnter = false;
        }
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        for (int i = 0; i < this.buttons.size(); i++) {
            this.buttons.get(i).paint(g);
        }
        this.dino.paint(g);
    }

    @Override
    public CommandSolver.KeyListener getKeyListener() {
        return new MyKeyListener();
    }

    @Override
    public CommandSolver.MouseCommandListener getMouseListener() {
        return null;
    }

    public class MyKeyListener implements CommandSolver.KeyListener {

        @Override
        public void keyPressed(int commandCode, long trigTime) {
            if (dino.getIsStand()) {
                dino.setStand(false);
                switch (commandCode) {
                    case Global.UP:
                        dino.setDir(Global.UP);
                        dino.move();
                        break;
                    case Global.DOWN:
                        dino.setDir(Global.DOWN);
                        dino.move();
                        break;
                    case Global.ENTER:
                        isEnter = true;
                        dino.setDinoRun();
                        break;
                }
            }
        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {
            dino.reset();
            dino.setStand(true);
        }

        @Override
        public void keyTyped(char c, long trigTime) {
        }
    }
}
