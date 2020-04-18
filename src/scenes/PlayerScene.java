/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.IRC;
import controllers.SceneController;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import player.PlayerInfo;
import monsterstrike.gameobject.*;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.Global;
import player.Player;

/**
 *
 * @author yuin8
 */
public class PlayerScene extends Scene {

    private PlayerInfo playerInfo;
    private Background menu;
    private Player[] players;
    private String name;
    private boolean isSet;
    private boolean isReleased;
    private int currentIdx;
    private Button[] shineFrame;
    private boolean isDone;
    private boolean isEnter;
    private boolean isOnButton;
    private boolean isClick;
    private Button button;
    private int overCount;
    private int enterCount;
    private BufferedImage img;

    public PlayerScene(SceneController sceneController) {
        super(sceneController);
        this.playerInfo = FileIO.readPlayer("playerInfoInit.csv", 0);
        this.img = IRC.getInstance().tryGetImage("/resources/items/input.png");
        this.players = new Player[2];
        this.shineFrame = new Button[2];
        isSet = false;
        this.currentIdx = 0;
        this.isDone = false;
        this.isEnter = false;
        this.isOnButton = false;
        this.isClick = false;
        this.overCount = 0;
        this.enterCount = 0;
        this.name = "";
    }

    @Override
    public void sceneBegin() {
        this.menu = new Background(ImgInfo.LEVELBACK_PATH, 0, 0, 1);
        for (int i = 0; i < this.players.length; i++) {
            players[i] = new Player(i, 300 * (i + 1) + 100, 200, 150, 213);
            shineFrame[i] = new Button(ImgInfo.SHINEFRAME_PATH, 300 * (i + 1) + 175, 305,
                    180, 250, 20);
            this.shineFrame[i].setIsShow(true);
        }
        this.button = new Button(ImgInfo.HOME, Global.SCREEN_X - 30, 30,
                ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1], 20);
    }

    @Override
    public void sceneUpdate() {
        if (enterCount == 0) {
            for (int i = 0; i < this.players.length; i++) {
                if (currentIdx == i) {
                    this.players[i].setState(1);
                } else {
                    this.players[i].setState(0);
                }
                this.players[i].update();
            }
            this.shineFrame[currentIdx].update();
        } else if (enterCount == 1) {
            if (currentIdx == 0) {
                this.shineFrame[1].setIsShow(false);
            } else {
                this.shineFrame[0].setIsShow(false);
            }
        } else if(enterCount ==2){
            this.playerInfo.setSerial(currentIdx + 1);
            if(name.equals("")){
                name = "user";
            }
            this.playerInfo.setName(name);
        }else{

            sceneController.changeScene(new LevelMenu(sceneController, 
                        this.playerInfo, "marbleInfoInit.csv"));
        }
        if (this.isEnter) {
            sceneController.changeScene(new Menu(sceneController));
        }
        if (this.isOnButton) {
            this.button.update();
        }
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        this.button.paint(g);
        for (int i = 0; i < this.players.length; i++) {
            this.players[i].paint(g);
            this.shineFrame[i].paint(g);
        }

        if (enterCount > 0) {
            g.setFont(new Font("VinerHandITC", Font.PLAIN, 24));
            g.setColor(Color.BLACK);
            g.drawString("Player Name: ", Global.SCREEN_X / 2 - 150, 500);
            g.drawString("Level 1", Global.SCREEN_X / 2 - 150, 530);

            if (enterCount == 1) {
                g.drawImage(img, Global.SCREEN_X / 2 + 10, 475, 150, 30, null);
                if (isDone || isClick) {
                    g.setFont(new Font("VinerHandITC", Font.PLAIN, 24));
                    g.setColor(Color.WHITE);
                    g.drawString(name, Global.SCREEN_X / 2 + 10, 500);
                    g.drawImage(img, Global.SCREEN_X / 2 + 10, 475, 150, 30, null);
                    if (overCount++ % 30 < 15) {
                        g.setFont(new Font("VinerHandITC", Font.BOLD, 24));
                        g.setColor(Color.WHITE);
                        g.drawString("|", Global.SCREEN_X / 2 + 10, 495);
                    }
                    if (overCount == 1000) {
                        overCount = 0;
                    }
                }
            } else if (enterCount == 2) {
                g.setFont(new Font("VinerHandITC", Font.PLAIN, 24));
                g.setColor(Color.BLACK);
                g.drawString(name, Global.SCREEN_X / 2 + 10, 500);

                if (overCount++ % 30 < 15) {
                    g.setFont(new Font("Showcard Gothic", Font.PLAIN, 40));
                    g.setColor(Color.BLACK);
                    g.drawString("START", Global.SCREEN_X - 300, 500);
                }else{
                    g.setFont(new Font("Showcard Gothic", Font.PLAIN, 44));
                    g.setColor(Color.BLACK);
                    g.drawString("START", Global.SCREEN_X - 290, 500);
                }
                if (overCount == 1000) {
                    overCount = 0;
                }
            }
        }
//        g.drawString("Level 1", Global.SCREEN_X / 2 - 70, 540);
    }

    @Override
    public CommandSolver.KeyListener getKeyListener() {
        return new MyKeyListener();
    }

    @Override
    public CommandSolver.MouseCommandListener getMouseListener() {
        return new MyMouseListener();
    }

    public class MyKeyListener implements CommandSolver.KeyListener {

        @Override
        public void keyPressed(int commandCode, long trigTime) {
            if (isReleased) {
                isReleased = false;
                if (commandCode == Global.LEFT && currentIdx == 1) {
                    currentIdx = 0;
                } else if (commandCode == Global.RIGHT && currentIdx == 0) {
                    currentIdx = 1;
                } else if (commandCode == Global.ENTER) {
                    isDone = true;
                    enterCount++;
                }
            }
        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {
            isReleased = true;
        }

        @Override
        public void keyTyped(char c, long trigTime) {
            if (isDone && enterCount == 1) {
                if (c >= 65 && c <= 90) {
                    name += c;
                    name = name.toLowerCase();
                }
            }
        }
    }

    public class MyMouseListener implements CommandSolver.MouseCommandListener {

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
            if (state == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 30 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < 30 + ImgInfo.SETTING_INFO[1] / 2) {
                isEnter = true;
            } else if (state == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X / 2 && e.getX() < Global.SCREEN_X / 2 + 150
                    && e.getY() > 475 && e.getY() < 505) {
                isClick = true;
            }
            if (state == CommandSolver.MouseState.MOVED && e.getX() > Global.SCREEN_X - 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 30 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < 30 + ImgInfo.SETTING_INFO[1] / 2) {
                isOnButton = true;
            } else {
                isOnButton = false;
            }
        }
    }

}
