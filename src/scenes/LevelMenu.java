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
import player.PlayerInfo;
import monsterstrike.gameobject.Background;
import monsterstrike.gameobject.Button;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.*;
import monsterstrike.util.*;
import player.Player;

public class LevelMenu extends Scene {

    private ArrayList<Button> buttons;
    private Background menu;
    private int count;
    private int idx;
    private Marble[] fightMarbles; //我方對戰怪物
    private ArrayList<Marble> enemyFightMarbles; //敵方出戰怪物
    private ArrayList<MarbleInfo> allMarbleInfo; //所有怪物info
    private ArrayList<Marble> myMarbles; //我方所有怪物
    private ArrayList<Marble> enemies; //敵方所有怪物
    private PlayerInfo playerInfo;
    private Marble currentMarble;
    private Background background;
    private int backIdx;
    private boolean isReleased;
    private int x;
    private int y;
    private Delay delay;
    private int level;
    private Player p1;

    private ArrayList<Button> button;
    private boolean isEnter;
    private boolean isOnButton;

    public LevelMenu(SceneController sceneController, PlayerInfo playerInfo, String file) {
        super(sceneController);
        this.playerInfo = playerInfo;
        this.allMarbleInfo = FileIO.readMarble(file);
        this.level = playerInfo.getLevel();
        this.myMarbles = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.fightMarbles = new Marble[3];
        this.buttons = new ArrayList<>();
        this.enemyFightMarbles = new ArrayList<>();
        this.button = new ArrayList<>();
    }

    public LevelMenu(SceneController sceneController, String playerFile, String file) {
        super(sceneController);
        this.playerInfo = FileIO.readPlayer(playerFile, 0);
        this.allMarbleInfo = FileIO.readMarble(file);
        this.level = playerInfo.getLevel();
        this.myMarbles = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.fightMarbles = new Marble[3];
        this.buttons = new ArrayList<>();
        this.enemyFightMarbles = new ArrayList<>();
        this.button = new ArrayList<>();
    }

    @Override
    public void sceneBegin() {
        this.p1 = new Player(this.playerInfo.getSerial() - 1, 100, 120, 100, 150);
        this.menu = new Background(ImgInfo.LEVELBACK_PATH, 0, 0, 1);
        this.count = 0;
        this.idx = 0;
        this.x = 350;
        this.y = 200;
        this.backIdx = 0;
        this.button.add(new Button(ImgInfo.HOME, Global.SCREEN_X - 30, 30, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1], 20));
        this.background = new Background(ImgInfo.BACKGROUND_PATH[this.backIdx], 0, 0, this.backIdx);
        for (int  i = 0; i < this.allMarbleInfo.size(); i++) {
            if (inMySerials(this.allMarbleInfo.get(i))) {
                this.myMarbles.add(new Marble(this.x, this.y, 150, 150, this.allMarbleInfo.get(i)));
            } else {
                this.enemies.add(new Marble(0, 0, 150, 150, this.allMarbleInfo.get(i)));
            }

        }
        int unitX = Global.SCREEN_X / 5;
        for (int i = 0; i < 3; i++) {
            buttons.add(new Button(ImgInfo.RIGHT, unitX + 190, 220, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
            buttons.add(new Button(ImgInfo.LEFT, unitX, 220, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
            unitX += 290;
        }
        buttons.add(new Button(ImgInfo.RIGHT, 970, 480, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
        buttons.add(new Button(ImgInfo.LEFT, 300, 480, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
        this.currentMarble = this.myMarbles.get(0);
        this.isReleased = true;
        this.delay = new Delay(20);
        this.delay.start();
    }

    @Override
    public void sceneUpdate() {
        this.p1.update();
        if (count == 4) {
            this.background = new Background(ImgInfo.BACKGROUND_PATH[backIdx], 0, 0, this.backIdx);
        } else if (this.count == 5) {
            for (int i = 0; i < this.enemies.size(); i++) {
                if (this.enemies.get(i).getInfo().getAttribute() != backIdx) {
                    continue;
                }
                this.enemyFightMarbles.add(this.enemies.get(i));
            }
            sceneController.changeScene(chooseLevel());
            return;
        } else {
            this.currentMarble = this.myMarbles.get(idx);
            this.currentMarble.setShine(true);
            this.currentMarble.update();
            this.currentMarble.updateShine();
        }
        if (this.delay.isTrig()) {
            int i = 2 * (count - 1);
            if (i == -2) {
                i = 0;
            } else if (i == 8) {
                i = 6;
            }
            this.buttons.get(i).update();
            this.buttons.get(i + 1).update();
        }
        if (this.isEnter) {
            sceneController.changeScene(new Menu(sceneController));
        }
        if (this.isOnButton) {
            this.button.get(0).update();
        }
    }
    
    private LevelScene chooseLevel(){
        switch(backIdx){
            case 0:
                return new Level1(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo);

            case 1:
                return new Level2(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo);

            case 2:
                return new Level3(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo);
        }
        return null;
    }

    private void enter() {
        if (count >= 5) {
            count = 5;
        }
        if (this.count > 0 && this.count < 4) {
            this.fightMarbles[this.count - 1] = currentMarble;
            myMarbles.remove(idx);
            this.idx = 0;
            this.x += 290;

            for (int i = 0; i < myMarbles.size(); i++) {
                this.myMarbles.get(i).setCenterX(this.x);
            }
        }
        if (this.backIdx + 1 <= this.level) {
            this.count++;
        }
    }

    private boolean inMySerials(MarbleInfo info) {
        int[] serials = this.playerInfo.getMyMarbleSerials();
        for (int i = 0; i < serials.length; i++) {
            if (info.getSerial() == serials[i] && info.getState() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        this.p1.paint(g);
        PaintText.paint(g, new Font("Arial Unicode MS", Font.BOLD, 24), 
                Color.BLACK, this.playerInfo.getName(), 100, 300, 100); 
        PaintText.paint(g, new Font("Arial Unicode MS", Font.BOLD, 20), 
                Color.BLACK, "Lv." + this.playerInfo.getLevel(), 105, 110, 100); 
        int c = count;
        if (c == 0) {
            c = 1;
        }
        if (this.currentMarble != null && count < 4) {
            this.currentMarble.paintAll(g);
            paintText(g, this.currentMarble, 280 + 290 * (c - 1), 140);
        }

        for (int i = 0; i < this.fightMarbles.length; i++) {
            if (this.fightMarbles[i] != null) {
                this.fightMarbles[i].paintAll(g);
                paintText(g, this.fightMarbles[i], 280 + 290 * i, 140);
            }
        }

        if (this.count >= 4) {
            this.background.paintItem(g, 350, 380, 580, 200);
        }

        for (int i = 0; i < this.buttons.size(); i++) {
            this.buttons.get(i).paint(g);
        }
        this.button.get(0).paint(g);
    }

    public void paintText(Graphics g, Marble m, int x, int y) {
        PaintText.paint(g, new Font("Arial Unicode MS", Font.BOLD, 32), 
                Color.BLACK, m.getInfo().getName(), x, y, (int)m.getWidth());     
        PaintText.paint(g, new Font("VinerHandITC", Font.BOLD, 24), 
                Color.BLACK, "HP: " + m.getInfo().getHp(), x-8, y+150, (int)m.getWidth());
        PaintText.paint(g, new Font("VinerHandITC", Font.BOLD, 24), 
                Color.BLACK, "ATK: " + m.getInfo().getAtk(), x-8, y+180, (int)m.getWidth());
        PaintText.paint(g, new Font("VinerHandITC", Font.BOLD, 24), 
                Color.BLACK, "Velocity: " + (int) m.getInfo().getV() + " m/s", 
                x-8, y+210, (int)m.getWidth());
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
                switch (commandCode) {
                    case Global.LEFT:
                        if (count <= 3) {
                            idx--;
                            if (idx < 0) {
                                idx = myMarbles.size() - 1;
                            }
                        } else {
                            backIdx--;
                            if (backIdx < 0) {
                                backIdx = ImgInfo.BACKGROUND_PATH.length - 1;
                            }
                        }
                        break;
                    case Global.RIGHT:
                        if (count <= 3) {
                            idx++;
                            if (idx >= myMarbles.size()) {
                                idx = 0;
                            }
                        } else {
                            backIdx++;
                            if (backIdx >= ImgInfo.BACKGROUND_PATH.length) {
                                backIdx = 0;
                            }
                        }
                        break;
                    case Global.ENTER:
                        enter();
                        break;
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
                isEnter = true;
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
