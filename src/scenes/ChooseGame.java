/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import monsterstrike.gameobject.Background;
import monsterstrike.gameobject.Button;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.Global;
import player.Player;
import player.PlayerInfo;

public class ChooseGame extends Scene {

    private PlayerInfo playerInfo;
    private Background menu;
    private Background[] game;
    private Player playerR;
    private Player playerL;
    private Button home;
    private Button[] shineFrame;
    private int currentIdx;
    private int enterCount;
    private boolean isOnButton;
    private boolean isClick;
    private boolean isReleased;

    public ChooseGame(SceneController sceneController, PlayerInfo playerInfo) {
        super(sceneController);
        this.playerInfo = playerInfo;
        this.shineFrame = new Button[2];
        this.game = new Background[2];
        this.currentIdx = 0;
        this.isClick = false;
        this.isOnButton = false;
        this.enterCount = 0;
    }

    @Override
    public void sceneBegin() {
        this.menu = new Background(ImgInfo.BACK_PATH, 0, 0, 1);
        this.playerR = new Player(this.playerInfo.playerNum() - 1, 0, 100, 120, 100, 150);
        this.playerL = new Player(this.playerInfo.playerNum() - 1, 1, Global.SCREEN_X - 200, 120, 100, 150);

        this.home = new Button(ImgInfo.HOME, Global.SCREEN_X - 30, 30, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1], 20);
        for (int i = 0; i < this.shineFrame.length; i++) {
            shineFrame[i] = new Button(ImgInfo.SHINEFRAME_PATH, 450 * (i + 1) - 50, 305,
                    400, 250, 14);
            this.shineFrame[i].setIsShow(true);
            this.game[i] = new Background(ImgInfo.GAME_PATH[i], 0, 0, 0);
        }
    }

    @Override
    public void sceneUpdate() {
        this.playerR.update();
        this.playerL.update();
        if (enterCount == 0) {
            this.shineFrame[currentIdx].update();
            if (currentIdx == 0) {
                this.shineFrame[0].setIsShow(true);
                this.shineFrame[1].setIsShow(false);
            } else {
                this.shineFrame[0].setIsShow(false);
                this.shineFrame[1].setIsShow(true);
            }
        } else if (enterCount == 1) {
            if (currentIdx == 0) {
                sceneController.changeScene(new Tutorial(sceneController, ImgInfo.HOWTOPLAY_PATH, 5,
                        new PinBall(sceneController, this.playerInfo)));
            } else {
                sceneController.changeScene(new LevelMenu(sceneController,
                        this.playerInfo, "mymarbleInfo" + this.playerInfo.getSerial() + ".csv", false));
            }
        }
        if (this.isClick) {
            sceneController.changeScene(new Menu(sceneController));
            return;
        }
        if (this.isOnButton) {
            this.home.update();
        }
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        this.home.paint(g);
        for (int i = 0; i < this.shineFrame.length; i++) {
            this.game[i].paintItem(g, 450 * (i + 1) - 228, 191, 360, 227);
            this.shineFrame[i].paint(g);
        }
        if (this.currentIdx == 0) {
            this.playerR.paint(g);
            PaintText.paint(g, new Font("Showcard Gothic", Font.PLAIN, 40),
                    Color.BLACK, "Strike", 90 + 450, 150, Global.SCREEN_X / 2);
            PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 40), new Font("Showcard Gothic", Font.PLAIN, 44),
                    Color.BLACK, "PinBall", 90, 150, Global.SCREEN_X / 2, 60);
            PaintText.paintTwinkle(g, new Font("Arial Unicode MS", Font.PLAIN, 30), new Font("Arial Unicode MS", Font.PLAIN, 32),
                    Color.BLACK, "找人合作蒐集稀有怪獸吧！", 90, 500, Global.SCREEN_X / 2, 60);
            PaintText.paint(g, new Font("Arial Unicode MS", Font.PLAIN, 30),
                    Color.BLACK, "出發討伐暗黑大魔王！", 90 + 450, 500, Global.SCREEN_X / 2);
        } else {
            this.playerL.paint(g);
            PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 40), new Font("Showcard Gothic", Font.PLAIN, 44),
                    Color.BLACK, "Strike", 90 + 450, 150, Global.SCREEN_X / 2, 60);
            PaintText.paint(g, new Font("Showcard Gothic", Font.PLAIN, 40),
                    Color.BLACK, "PinBall", 90, 150, Global.SCREEN_X / 2);
            PaintText.paintTwinkle(g, new Font("Arial Unicode MS", Font.PLAIN, 30), new Font("Arial Unicode MS", Font.PLAIN, 32),
                    Color.BLACK, "出發討伐暗黑大魔王！", 90 + 450, 500, Global.SCREEN_X / 2, 60);
            PaintText.paint(g, new Font("Arial Unicode MS", Font.PLAIN, 30),
                    Color.BLACK, "找人合作蒐集稀有怪獸吧！", 90, 500, Global.SCREEN_X / 2);
        }

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

        }
    }

    public class MyMouseListener implements CommandSolver.MouseCommandListener {

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
            if (state == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 30 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < 30 + ImgInfo.SETTING_INFO[1] / 2) {
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
