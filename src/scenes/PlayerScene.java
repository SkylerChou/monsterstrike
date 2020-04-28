/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import monsterstrike.gameobject.button.EditTextA;
import monsterstrike.gameobject.button.EditText;
import monsterstrike.gameobject.button.*;
import controllers.*;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
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
    private int currentIdx;
    private ButtonRenderer shineFrame;
    private boolean isEnter;
    private Button home;
    private int enterCount;
    private AudioClip music;
    private EditText typeName;

    public PlayerScene(SceneController sceneController) {
        super(sceneController);
        this.playerInfo = FileIO.readPlayer("playerInfoInit.csv").get(0);
        this.players = new Player[2];
        this.currentIdx = 0;
        this.isEnter = false;
        this.enterCount = 0;
        this.name = "";
        this.music = MRC.getInstance().tryGetMusic("/resources/wav/charaterSeletion.wav");
        this.home = new ButtonA(ImgInfo.HOME, Global.SCREEN_X - 5 - ImgInfo.SETTING_INFO[0], 5,ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1]);
        this.home.setListener(new ButtonClickListener());
    }
    @Override
    public void sceneBegin() {
        this.typeName = new EditTextA(Global.SCREEN_X / 2, 465, Global.SCREEN_X / 2 + 250, 520);
        this.typeName.setHint("Click Here to Type");
        this.menu = new Background(ImgInfo.LEVELBACK_PATH, 0, 0, 1);
        for (int i = 0; i < this.players.length; i++) {
            players[i] = new Player(i, 0, 300 * (i + 1) + 100, 200, 150, 213);
            shineFrame = new ButtonRenderer(ImgInfo.SHINEFRAME_PATH, 475, 305,
                    180, 250, 20);
            this.shineFrame.setIsShow(true);
        }
        this.music.loop();
        PaintText.setFlash(15);
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
            if (currentIdx == 0) {
                this.shineFrame.setCenterX(475);
            } else {
                this.shineFrame.setCenterX(775);
            }
            this.shineFrame.setFocus(true);
            this.shineFrame.update();
        } else if (enterCount == 1) {
        } else if (enterCount == 2) {
            this.name=this.typeName.getText();
            this.playerInfo.setPlayerNum(currentIdx + 1);
            if (name.equals("")) {
                name = "user";
            }
            this.playerInfo.setName(name);
        } else {
            this.music.stop();
            sceneController.changeScene(new LevelMenu(sceneController,
                    this.playerInfo, "mymarbleInfoInit.csv", false));
        }
        if (this.isEnter) {
            this.music.stop();
            sceneController.changeScene(new Menu(sceneController));
        }
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        this.home.paint(g);
        this.shineFrame.paint(g);
        for (int i = 0; i < this.players.length; i++) {
            this.players[i].paint(g);
        }
        if (enterCount > 0) {
            PaintText.paint(g, new Font("VinerHandITC", Font.PLAIN, 24),
                    Color.BLACK, "Player Name: ", -70, 500, Global.SCREEN_X);
            PaintText.paint(g, new Font("VinerHandITC", Font.PLAIN, 24),
                    Color.BLACK, "Level 1", -40, 550, Global.SCREEN_X);
            this.typeName.paint(g);
            if (enterCount == 2) {
                this.typeName.setFontColor(Color.BLACK);
                PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 40), new Font("Showcard Gothic", Font.PLAIN, 44),
                        Color.BLACK, "START", Global.SCREEN_X / 2 + 100, 550, Global.SCREEN_X / 2);
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

        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {
            if (commandCode == Global.LEFT && currentIdx == 1) {
                currentIdx = 0;
            } else if (commandCode == Global.RIGHT && currentIdx == 0) {
                currentIdx = 1;
            } else if (commandCode == Global.ENTER) {
                enterCount++;
            }
        }

        @Override
        public void keyTyped(char c, long trigTime) {
            typeName.update(c, trigTime);
        }
    }

    public class MyMouseListener implements CommandSolver.MouseCommandListener {

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
            home.update(e, state);
            if (state == CommandSolver.MouseState.RELEASED
                    && typeName.withinRange(e.getX(), e.getY())) {
                typeName.active();
                if(enterCount==2){
                    typeName.setFontColor(Color.WHITE);
                    enterCount = 1;
                }
            }
            if (state == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 30 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < 30 + ImgInfo.SETTING_INFO[1] / 2) {
                isEnter = true;
            } 
        }
    }

}
