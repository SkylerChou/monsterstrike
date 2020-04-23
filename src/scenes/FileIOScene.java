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
import java.util.ArrayList;
import monsterstrike.gameobject.*;
import monsterstrike.util.*;
import player.Player;
import player.PlayerInfo;

public class FileIOScene extends Scene {

    private Button home;
    private Background menu;
    private Button shineFrame;
    private ArrayList<PlayerInfo> playersInfo;
    private PlayerInfo playerInfo;
    private ArrayList<Player> players;
    private int currentIdx;
    private int enterCount;
    private boolean isOnButton;  //滑鼠在Home鍵上
    private boolean isClick;    //是否按下Home鍵
    private boolean isReleased; 
    private boolean isEmpty;    //讀取狀態是否沒有玩家資料
    private boolean isSave;     //決定是否儲存
    private Item[] back;
    private String state;   //"read(r)"或"write(w)"
    private boolean showWindow; //是否跳出要儲存視窗
    private boolean showCheckWindow; //是否跳出確認寫入視窗
    private boolean yesOnBtn; //滑鼠在按鈕"是"上
    private boolean noOnBtn;  //滑鼠在按鈕"否"上
    private boolean isWrite;  //是否寫完檔
    private boolean isCheck;  //是否確認寫入
    private boolean isRead;   //寫入檔案後是否完成讀取
    private boolean again;    //是否重新選擇寫入位置

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
        this.isOnButton = false;
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
        this.isOnButton = false;
        this.yesOnBtn = false;
        this.noOnBtn = false;
        this.isWrite = false;
        this.isCheck = false;
        this.showCheckWindow = false;
        this.isRead = false;
        this.again = false;
    }

    @Override
    public void sceneBegin() {
        this.menu = new Background(ImgInfo.BACK_PATH, 0, 0, 1);
        if (this.isEmpty) {//讀取無存檔
            this.home = new Button(ImgInfo.HOME, Global.SCREEN_X / 2 + 100, Global.SCREEN_Y / 2 - 20, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1], 20);
        } else {
            this.home = new Button(ImgInfo.HOME, Global.SCREEN_X - 30, 30, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1], 20);
            this.shineFrame = new Button(ImgInfo.SHINEFRAME_PATH, Global.SCREEN_X / 2, 150 + 120 * (1 + currentIdx),
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

    }

    @Override
    public void sceneUpdate() {
        if (this.isEmpty || (!isSave && !showWindow && this.playersInfo.isEmpty())) {
            this.home.update();
            this.home.setCenterX(Global.SCREEN_X / 2 + 100);
            this.home.setCenterY(Global.SCREEN_Y / 2 - 20);
        } else {
            this.shineFrame.update();
            this.shineFrame.setCenterY(150 + 120 * (1 + currentIdx));
            if (this.players.get(currentIdx) != null) {
                this.players.get(currentIdx).update();
            }
            if (this.state.equals("r") && this.enterCount == 1) { //read
                if (this.playersInfo.get(currentIdx).playerNum() != 0) {
                    sceneController.changeScene(new ChooseGame(sceneController, this.playersInfo.get(currentIdx)));
                }
            } else if (this.state.equals("w")) { //write               
                if (!isWrite) {
                    if (!isSave) { //不存檔
                        if (this.playersInfo.get(currentIdx).playerNum() != 0 && this.enterCount == 1) {
                            sceneController.changeScene(new ChooseGame(sceneController, this.playersInfo.get(currentIdx)));
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
                        sceneController.changeScene(new ChooseGame(sceneController, this.playersInfo.get(currentIdx)));
                    }
                }
            }
            if (this.isOnButton) {
                this.home.update();
            }
        }

        if (isWrite && !isRead) {
            readAgain();
        }

        if (this.isClick || (isCheck && isRead)) {
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
                    Color.BLACK, "尚無存檔！", 0, Global.SCREEN_Y / 2, Global.SCREEN_X, 30);
        } else if (this.showWindow) {
            PaintText.paintWindow(g, new Font("Arial Unicode MS", Font.PLAIN, 24), "是否儲存檔案？", yesOnBtn, noOnBtn);
        } else if (this.showCheckWindow) {
            PaintText.paintWindow(g, new Font("Arial Unicode MS", Font.PLAIN, 24), "確定寫入嗎？", yesOnBtn, noOnBtn);
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
            if (isReleased && !isEmpty && !showWindow) {
                isReleased = false;
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
            if (isEmpty || (!isSave && !showWindow && playersInfo.isEmpty())) { //讀取無存檔
                if (state == CommandSolver.MouseState.PRESSED && e.getX() > 710 && e.getX() < 755
                        && e.getY() > 276 && e.getY() < 323) {
                    isClick = true;
                }
            } else if (showWindow || showCheckWindow) {
                if (state == CommandSolver.MouseState.PRESSED && e.getX() > 552 && e.getX() < 612
                        && e.getY() > 330 && e.getY() < 360) {
                    if (showWindow) {
                        isSave = true;
                        showWindow = false;
                    } else {
                        isCheck = true;
                        showCheckWindow = false;
                    }
                } else if (state == CommandSolver.MouseState.PRESSED && e.getX() > 653 && e.getX() < 710
                        && e.getY() > 330 && e.getY() < 360) {
                    if (showWindow) {
                        isSave = false;
                        showWindow = false;
                    } else {
                        again = true;
                        isCheck = false;
                        showCheckWindow = false;
                    }
                }
                if (state == CommandSolver.MouseState.MOVED && e.getX() > 552 && e.getX() < 612
                        && e.getY() > 330 && e.getY() < 360) {
                    yesOnBtn = true;
                } else if (state == CommandSolver.MouseState.MOVED && e.getX() > 653 && e.getX() < 710
                        && e.getY() > 330 && e.getY() < 360) {
                    noOnBtn = true;
                } else {
                    yesOnBtn = false;
                    noOnBtn = false;
                }
            } else {
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
}
