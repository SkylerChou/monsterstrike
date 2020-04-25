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
import java.util.ArrayList;
import monsterstrike.gameobject.*;
import monsterstrike.util.*;
import player.Player;
import player.PlayerInfo;

public class FileIOScene extends Scene {

    private BufferedImage img1;
    private BufferedImage img2;
    private Button home;
    private Button yes;
    private Button no;
    private Background menu;
    private ButtonRenderer shineFrame;
    private ArrayList<PlayerInfo> playersInfo;
    private PlayerInfo playerInfo;
    private ArrayList<Player> players;
    private int currentIdx;
    private int enterCount;
    private boolean isClick;    //是否按下Home鍵
    private boolean isEmpty;    //讀取狀態是否沒有玩家資料
    private boolean isSave;     //決定是否儲存
    private Item[] back;
    private String state;   //"read(r)"或"write(w)"
    private boolean showWindow; //是否跳出要儲存視窗
    private boolean showCheckWindow; //是否跳出確認寫入視窗
    private boolean isWrite;  //是否寫完檔
    private boolean isCheck;  //是否確認寫入
    private boolean isRead;   //寫入檔案後是否完成讀取
    private boolean again;    //是否重新選擇寫入位置

    private AudioClip music;

    public FileIOScene(SceneController sceneController, String state) {
        super(sceneController);
        this.playersInfo = FileIO.readPlayer("playerInfo.csv");
        this.state = state;
        this.isEmpty = false;
        if (this.state.equals("r") && isEmpty()) { //讀取無存檔
            this.isEmpty = true;
        } else {
            this.players = new ArrayList<>();
            this.back = new Item[3];
            this.currentIdx = 0;
            this.enterCount = 0;
        }
        this.isClick = false;
    }

    public FileIOScene(SceneController sceneController, PlayerInfo playerInfo, String state) {
        super(sceneController);
        this.isEmpty = false;
        this.state = state;
        this.playerInfo = playerInfo;
        this.playersInfo = FileIO.readPlayer("playerInfo.csv");
        this.showWindow = true;
        if (this.state.equals("w")) {
            this.players = new ArrayList<>();
            this.back = new Item[3];
            this.currentIdx = 0;
            this.enterCount = 0;
        }
        this.isClick = false;
        this.isWrite = false;
        this.isCheck = false;
        this.showCheckWindow = false;
        this.isRead = false;
        this.again = false;

    }

    @Override
    public void sceneBegin() {
        this.img1 = IRC.getInstance().tryGetImage(ImgInfo.SAVE);
        this.img2 = IRC.getInstance().tryGetImage(ImgInfo.WRITE);
        this.home = new ButtonA(ImgInfo.HOME, Global.SCREEN_X - 5 - ImgInfo.SETTING_INFO[0], 5, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1]);
        this.home.setListener(new ButtonClickListener());
        this.yes = new ButtonA(ImgInfo.YES, Global.SCREEN_X / 2 - 100, Global.SCREEN_Y / 2, ImgInfo.YesNo_INFO[0], ImgInfo.YesNo_INFO[1]);
        this.yes.setListener(new ButtonClickListener());
        this.no = new ButtonA(ImgInfo.NO, Global.SCREEN_X / 2 + 20, Global.SCREEN_Y / 2, ImgInfo.YesNo_INFO[0], ImgInfo.YesNo_INFO[1]);
        this.no.setListener(new ButtonClickListener());
        this.music = MRC.getInstance().tryGetMusic("/resources/wav/charaterSeletion.wav");
        this.music.loop();
        this.menu = new Background(ImgInfo.BACK_PATH, 0, 0, 1);
        if (this.isEmpty) {//讀取無存檔
            this.home = new ButtonA(ImgInfo.HOME, Global.SCREEN_X / 2 + 100, Global.SCREEN_Y / 2 - 20, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1]);
        } else {
            this.shineFrame = new ButtonRenderer(ImgInfo.SHINEFRAME_PATH, Global.SCREEN_X / 2, 150 + 120 * (1 + currentIdx),
                    350, 100, 14);
            this.shineFrame.setIsShow(true);
            for (int i = 0; i < this.playersInfo.size(); i++) {
                if (this.playersInfo.get(i).playerNum() != 0) {
                    this.players.add(i, new Player(this.playersInfo.get(i).playerNum() - 1, 0, Global.SCREEN_X / 2 - 130, 110 + 120 * (1 + i), 50, 75));
                } else {
                    this.players.add(null);
                }
            }
            for (int i = 0; i < this.back.length; i++) {
                this.back[i] = new Item("/resources/items/input.png", Global.SCREEN_X / 2, 150 + 120 * (1 + i), 330, 100);
            }
        }
        PaintText.setFlash(30);
    }

    @Override
    public void sceneUpdate() {
        if (this.isEmpty || (!isSave && !showWindow && this.playersInfo.isEmpty())) {
            this.home.setX(Global.SCREEN_X / 2 + 90);
            this.home.setY(Global.SCREEN_Y / 2 - 40);
        } else {
            this.shineFrame.update();
            this.shineFrame.setCenterY(150 + 120 * (1 + currentIdx));
            if (this.players.get(currentIdx) != null) {
                this.players.get(currentIdx).update();
            }
            if (this.state.equals("r") && this.enterCount == 1) { //read
                if (this.playersInfo.get(currentIdx).playerNum() != 0) {
                    this.music.stop();
                    sceneController.changeScene(new ChooseGame(sceneController, this.playersInfo.get(currentIdx)));
                    return;
                }
            } else if (this.state.equals("w")) { //write               
                if (!isWrite) {
                    if (!isSave) { //不存檔
                        if (this.playersInfo.get(currentIdx).playerNum() != 0 && this.enterCount == 1) {
                            this.music.stop();
                            sceneController.changeScene(new ChooseGame(sceneController, this.playersInfo.get(currentIdx)));
                            return;
                        } else if (!showWindow) {
                            this.music.stop();
                            sceneController.changeScene(new Menu(sceneController));
                            return;
                        }
                    } else if (enterCount == 1 && !again) { //要存檔
                        this.showCheckWindow = true; //顯示確認視窗
                        if (isCheck) { //確認存檔
                            String file = "mymarbleInfo" + (currentIdx + 1) + ".csv";
                            FileIO.copy("mymarbleInfoTmp.csv", file);
                            FileIO.writePlayer(playersInfo, "playerInfo.csv", playerInfo, currentIdx);
                            isWrite = true; //寫完檔
                        }
                    }
                } else if (enterCount == 2) {
                    if (this.playersInfo.get(currentIdx).playerNum() != 0) {
                        this.music.stop();
                        sceneController.changeScene(new ChooseGame(sceneController, this.playersInfo.get(currentIdx)));
                        return;
                    }
                }
            }
        }

        if (isWrite && !isRead) {
            readAgain();
        }

        if (this.isClick || (isCheck && isRead)) {
//            this.showIsDone = true;
            this.music.stop();
            sceneController.changeScene(new Menu(sceneController));
        }

    }

    private boolean isEmpty() {
        for (int i = 0; i < this.playersInfo.size(); i++) {
            if (this.playersInfo.get(i).playerNum() != 0) {
                return false;
            }
        }
        return true;
    }

    private void readAgain() {
        this.playersInfo = FileIO.readPlayer("playerInfo.csv");
        for (int i = 0; i < this.playersInfo.size(); i++) {
            if (this.playersInfo.get(i).playerNum() != 0) {
                this.music.stop();
                this.players.set(i, new Player(this.playersInfo.get(i).playerNum() - 1, 0, Global.SCREEN_X / 2 - 130, 110 + 120 * (1 + i), 50, 75));
            }
        }
        isRead = true;
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        this.home.paint(g);
        if (isEmpty || (!isSave && !showWindow && this.playersInfo.isEmpty())) {
            PaintText.paintTwinkle(g, new Font("Arial Unicode MS", Font.PLAIN, 26), new Font("Arial Unicode MS", Font.PLAIN, 32),
                    Color.BLACK, "尚無存檔！", 0, Global.SCREEN_Y / 2, Global.SCREEN_X);
        } else if (this.showWindow) {
            g.drawImage(img1, Global.SCREEN_X * 2 / 5, Global.SCREEN_Y / 3 + 30, ImgInfo.WINDOW_INFO[0], ImgInfo.WINDOW_INFO[1], null);
            this.yes.paint(g);
            this.no.paint(g);
        } else if (this.showCheckWindow) {
            g.drawImage(img2, Global.SCREEN_X * 2 / 5, Global.SCREEN_Y / 3 + 30, ImgInfo.WINDOW_INFO[0], ImgInfo.WINDOW_INFO[1], null);
            this.yes.paint(g);
            this.no.paint(g);
        } else {
            for (int i = 0; i < this.back.length; i++) {
                this.back[i].paint(g);
            }
            this.shineFrame.paint(g);
            for (int i = 0; i < this.players.size(); i++) {
                if (this.players.get(i) != null) {
                    this.players.get(i).paint(g);
                    PaintText.paint(g, new Font("VinerHandITC", Font.PLAIN, 24), Color.WHITE,
                            "Name: " + this.playersInfo.get(i).getName(), 25, 140 + 120 * (1 + i), Global.SCREEN_X);
                    PaintText.paint(g, new Font("VinerHandITC", Font.PLAIN, 24), Color.WHITE,
                            "Level " + this.playersInfo.get(i).getLevel(), 3, 175 + 120 * (1 + i), Global.SCREEN_X);
                } else {
                    PaintText.paint(g, new Font("VinerHandITC", Font.PLAIN, 24), Color.WHITE,
                            "Empty", 0, 160 + 120 * (1 + i), Global.SCREEN_X);
                }
            }

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
            if (!isEmpty && !showWindow) {
                int bound = playersInfo.size() - 1;
                if (isSave) {
                    bound = 2;
                }
                if (commandCode == Global.UP) {
                    if (currentIdx == 0) {
                        currentIdx = 0;
                    } else {
                        currentIdx -= 1;
                    }
                } else if (commandCode == Global.DOWN) {
                    if (currentIdx == bound) {
                        currentIdx = bound;
                    } else {
                        currentIdx += 1;
                    }
                } else if (commandCode == Global.ENTER && !isSave
                        && playersInfo.get(currentIdx).playerNum() != 0) {
                    enterCount++;
                } else if (commandCode == Global.ENTER && again) {
                    again = false;
                } else if (commandCode == Global.ENTER && isSave) {
                    enterCount++;
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
            yes.update(e, state);
            no.update(e, state);
            if (isEmpty || (!isSave && !showWindow && playersInfo.isEmpty())) { //讀取無存檔
                if (state == CommandSolver.MouseState.PRESSED && e.getX() > home.getLeft() && e.getX() < home.getRight()
                        && e.getY() > home.getTop() && e.getY() < home.getBottom()) {
                    isClick = true;
                }
            } else if (showWindow || showCheckWindow) {
                if (state == CommandSolver.MouseState.PRESSED && e.getX() > yes.getLeft() && e.getX() < yes.getLeft() + yes.getW()
                        && e.getY() > yes.getTop() && e.getY() < yes.getTop() + yes.getH()) {
                    if (showWindow) {
                        isSave = true;
                        showWindow = false;
                    } else {
                        isCheck = true;
                        showCheckWindow = false;
                    }
                } else if (state == CommandSolver.MouseState.PRESSED && e.getX() > no.getLeft() && e.getX() < no.getLeft() + no.getW()
                        && e.getY() > no.getTop() && e.getY() < no.getTop() + no.getH()) {
                    if (showWindow) {
                        isSave = false;
                        showWindow = false;
                    } else {
                        again = true;
                        isCheck = false;
                        showCheckWindow = false;
                    }
                }
            } else {
                home.update(e, state);
                if (state == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 30 + ImgInfo.SETTING_INFO[1] / 2
                        && e.getY() > 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < 30 + ImgInfo.SETTING_INFO[1] / 2) {
                    isClick = true;
                }
            }
        }
    }
}
