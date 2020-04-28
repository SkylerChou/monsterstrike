/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import monsterstrike.gameobject.button.*;
import controllers.IRC;
import controllers.MRC;
import controllers.SceneController;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import monsterstrike.gameobject.*;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.Global;
import player.Player;
import player.PlayerInfo;

public class ChooseGame extends Scene {

    private BufferedImage fight;
    private BufferedImage friend;

    private PlayerInfo playerInfo;
    private Background menu;
    private Background[] game;
    private Player playerR;
    private Player playerL;
    private Button home;
    private Button save;
    private ButtonRenderer shineFrame;
    private int currentIdx;
    private int enterCount;
    private boolean isClick;
    private boolean isSave;

    private AudioClip music;
    private String file;

    //從硬碟讀取
    public ChooseGame(SceneController sceneController, PlayerInfo playerInfo) {
        super(sceneController);
        this.playerInfo = playerInfo;
        this.game = new Background[2];
        this.currentIdx = 0;
        this.isClick = false;
        this.isSave = false;
        this.enterCount = 0;
        this.music = MRC.getInstance().tryGetMusic("/resources/wav/charaterSeletion.wav");
        this.home = new ButtonA(ImgInfo.HOME, Global.SCREEN_X - 5 - ImgInfo.SETTING_INFO[0], 5, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1]);
        this.home.setListener(new ButtonClickListener());
        this.save = new ButtonA(ImgInfo.SAVEICON, Global.SCREEN_X - 10 - 2 * ImgInfo.SETTING_INFO[0], 5, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1]);
        this.save.setListener(new ButtonClickListener());
        this.fight = IRC.getInstance().tryGetImage(ImgInfo.FIGHT);
        this.friend = IRC.getInstance().tryGetImage(ImgInfo.FRIEND);
//        this.file = "mymarbleInfo" + playerInfo.getSerial() + ".csv";
        this.file = "mymarbleInfoTmp.csv";
    }

//    public ChooseGame(SceneController sceneController, PlayerInfo playerInfo, String myMarbleFile){
//        this(sceneController, playerInfo);
//        this.file = myMarbleFile;
//    }
    @Override
    public void sceneBegin() {
        this.music.loop();
        this.menu = new Background(ImgInfo.BACK_PATH, 0, 0, 1);
        this.playerR = new Player(this.playerInfo.playerNum() - 1, 0, 40, 450, 100, 150);
        this.playerL = new Player(this.playerInfo.playerNum() - 1, 1, Global.SCREEN_X - 140, 450, 100, 150);
        shineFrame = new ButtonRenderer(ImgInfo.SHINEFRAME_PATH, 400, 305,
                400, 250, 15);
        this.shineFrame.setIsShow(true);
        this.shineFrame.setFocus(true);
        for (int i = 0; i < this.game.length; i++) {
            this.game[i] = new Background(ImgInfo.GAME_PATH[i], 0, 0, 0);
        }

        PaintText.setFlash(15);
    }

    @Override
    public void sceneUpdate() {
        this.playerR.update();
        this.playerL.update();
        if (enterCount == 0) {
            this.shineFrame.update();
            if (currentIdx == 0) {
                this.shineFrame.setCenterX(400);
            } else {
                this.shineFrame.setCenterX(850);
            }
        }
        if (this.isClick) {
            this.music.stop();
            sceneController.changeScene(new Menu(sceneController));
        }
        if (this.isSave) {
            this.music.stop();
            sceneController.changeScene(new FileIOScene(sceneController, playerInfo, "w"));
        }
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        this.home.paint(g);
        this.save.paint(g);
        for (int i = 0; i < this.game.length; i++) {
            this.game[i].paintItem(g, 450 * (i + 1) - 228, 191, 360, 227);
        }
        this.shineFrame.paint(g);
        if (this.currentIdx == 0) {
            this.playerR.paint(g);
            g.drawImage(friend, 150, 450, 500, 100, null);
            PaintText.paint(g, new Font("Showcard Gothic", Font.PLAIN, 40),
                    Color.BLACK, "Strike", 90 + 450, 150, Global.SCREEN_X / 2);
            PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 40), new Font("Showcard Gothic", Font.PLAIN, 44),
                    Color.BLACK, "PinBall", 90, 150, Global.SCREEN_X / 2);
        } else {
            this.playerL.paint(g);
            g.drawImage(fight, 150 + 470, 450, 500, 100, null);
            PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 40), new Font("Showcard Gothic", Font.PLAIN, 44),
                    Color.BLACK, "Strike", 90 + 450, 150, Global.SCREEN_X / 2);
            PaintText.paint(g, new Font("Showcard Gothic", Font.PLAIN, 40),
                    Color.BLACK, "PinBall", 90, 150, Global.SCREEN_X / 2);
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
        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {
            if (commandCode == Global.LEFT && currentIdx == 1) {
                currentIdx = 0;
            } else if (commandCode == Global.RIGHT && currentIdx == 0) {
                currentIdx = 1;
            } else if (commandCode == Global.ENTER) {
                music.stop();
                if (currentIdx == 0) {
                    sceneController.changeScene(new Tutorial(sceneController, ImgInfo.HOWTOPLAY_PATH, 5,
                            new PinBall(sceneController, playerInfo)));
                } else {
                    sceneController.changeScene(new LevelMenu(sceneController,
                            playerInfo, file, true));
                }
            }
        }

        @Override
        public void keyTyped(char c, long trigTime) {
        }
    }

    public class MyMouseListener implements CommandSolver.MouseCommandListener {

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
            home.update(e, state);
            save.update(e, state);
            if (state == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 30 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < 30 + ImgInfo.SETTING_INFO[1] / 2) {
                isClick = true;
            }
            if (state == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 10 - 2 * ImgInfo.SETTING_INFO[0] && e.getX() < Global.SCREEN_X - 10 - ImgInfo.SETTING_INFO[0]
                    && e.getY() > 5 && e.getY() < 5 + ImgInfo.SETTING_INFO[1]) {
                isSave = true;
            }
        }
    }
}
