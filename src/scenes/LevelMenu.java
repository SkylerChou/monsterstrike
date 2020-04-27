/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.IRC;
import monsterstrike.gameobject.button.*;
import controllers.MRC;
import scenes.level.*;
import controllers.SceneController;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import player.PlayerInfo;
import monsterstrike.gameobject.*;
import monsterstrike.gameobject.marble.*;
import monsterstrike.util.*;
import player.Player;

public class LevelMenu extends Scene {

    private Item[] title;
    private ArrayList<ButtonRenderer> buttons;
    private Background menu;
    private int count;
    private int idx;
    private Marble[] fightMarbles; //我方對戰怪物
    private ArrayList<Marble> enemyFightMarbles; //敵方出戰怪物
    private ArrayList<MarbleInfo> allMarbleInfo; //所有敵人怪物info
    private ArrayList<MarbleInfo> myMarbleInfo;
    private ArrayList<Marble> myMarbles; //我方所有怪物
    private ArrayList<Marble> enemies; //敵方所有怪物
    private PlayerInfo playerInfo;
    private Marble currentMarble;
    private Background background;
    private Background mask;
    private Item lock;
    private int backIdx;
    private int x;
    private int y;
    private Delay delay;
    private Player p1;
    private Button home;
    private boolean isEnter;
    private boolean isSkip;
    private boolean[] isMask;
    private BufferedImage box;
    private AudioClip music;
    

    //首次玩
    public LevelMenu(SceneController sceneController, PlayerInfo playerInfo, String myMarbleFile, boolean isSkip) {
        super(sceneController);
        this.playerInfo = playerInfo;
        this.allMarbleInfo = FileIO.readMarble("marbleInfo.csv");
        this.myMarbleInfo = FileIO.readMarble(myMarbleFile);
        this.myMarbles = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.fightMarbles = new Marble[3];
        this.buttons = new ArrayList<>();
        this.enemyFightMarbles = new ArrayList<>();
        this.music = MRC.getInstance().tryGetMusic("/resources/wav/menu1.wav");
        this.isSkip = isSkip;
        this.isMask = new boolean[5];
        this.home = new ButtonA(ImgInfo.HOME, Global.SCREEN_X - 5 - ImgInfo.SETTING_INFO[0], 5, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1]);
        this.home.setListener(new ButtonClickListener());
        this.title = new Item[5];
    }

    @Override
    public void sceneBegin() {
        this.music.loop();
        this.p1 = new Player(this.playerInfo.playerNum() - 1, 0, 100, 120, 100, 150);
        this.menu = new Background(ImgInfo.LEVELBACK_PATH, 0, 0, 1);
        this.count = 0;
        this.idx = 0;
        this.x = 350;
        this.y = 200;
        this.backIdx = 0;
        this.background = new Background(ImgInfo.BACKGROUND_PATH[this.backIdx], 0, 0, this.backIdx);
        this.mask = new Background(ImgInfo.MASK_PATH, 0, 0, 0);
        this.lock = new Item(ImgInfo.LOCK_PATH, 640, 480, 100, 100);
        for (int i = 0; i < this.allMarbleInfo.size(); i++) {
            this.enemies.add(new Marble(0, 0, 150, 150, this.allMarbleInfo.get(i)));
        }
        for (int i = 0; i < this.myMarbleInfo.size(); i++) {
            this.myMarbles.add(new Marble(this.x, this.y, 150, 150, this.myMarbleInfo.get(i)));
        }
        int unitX = Global.SCREEN_X / 5;
        for (int i = 0; i < 3; i++) {
            buttons.add(new ButtonRenderer(ImgInfo.RIGHT, unitX + 190, 220, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
            buttons.add(new ButtonRenderer(ImgInfo.LEFT, unitX, 220, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
            unitX += 290;
        }
        buttons.add(new ButtonRenderer(ImgInfo.RIGHT, 970, 480, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
        buttons.add(new ButtonRenderer(ImgInfo.LEFT, 300, 480, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
        this.currentMarble = this.myMarbles.get(0);
        this.delay = new Delay(20);
        this.delay.start();
        for (int i = 0; i < this.isMask.length; i++) {
            if (i < this.playerInfo.getLevel()) {
                this.isMask[i] = false;
            } else {
                this.isMask[i] = true;
            }
            this.title[i] = new Item("/resources/items/" + ImgInfo.LEVEL_NAME1[i], Global.SCREEN_X/2, 575, 233,80);
        }
        PaintText.setFlash(30);
        this.box = IRC.getInstance().tryGetImage("/resources/items/say.png");
    }

    @Override
    public void sceneUpdate() {
        this.p1.update();
        if (count >= 0 && count < 3) {
            this.currentMarble = this.myMarbles.get(idx);
            this.currentMarble.setShine(true);
            this.currentMarble.update();
            this.currentMarble.updateShine();
        } else if (count == 3) {
            this.background = new Background(ImgInfo.BACKGROUND_PATH[backIdx], 0, 0, this.backIdx);
        } else if (this.count == 4) {
            for (int i = 0; i < this.enemies.size(); i++) {
                if (this.enemies.get(i).getInfo().getAttribute() != backIdx) {
                    continue;
                }
                this.enemyFightMarbles.add(this.enemies.get(i));
            }
            this.music.stop();
            sceneController.changeScene(chooseLevel());
            return;
        }
        if (this.delay.isTrig()) {
            this.buttons.get(2 * count).update();
            this.buttons.get(2 * count + 1).update();
        }
        if (this.isEnter) {
            sceneController.changeScene(new FileIOScene(sceneController, playerInfo, "w"));
            this.music.stop();
        }
    }

    private void enter() {
        if (!(count == 3 && this.playerInfo.getLevel() < backIdx + 1)) {
            this.count++;
        }
        if (count >= 4) {
            count = 4;
        }
        if (this.count > 0 && this.count < 4) {
            this.fightMarbles[this.count - 1] = currentMarble;
            if (!myMarbles.isEmpty()) {
                myMarbles.remove(idx);
                this.idx = 0;
                for (int i = 0; i < myMarbles.size(); i++) {
                    this.myMarbles.get(i).setCenterX(350 + 290 * count);
                }
            }
        }
    }

    private void back() {
        count--;
        if (count < 0) {
            count = 0;
        } else {
            if (this.count >= 0 && this.count < 4) {
                myMarbles.add(this.fightMarbles[this.count]);
                this.fightMarbles[this.count] = null;
                for (int i = 0; i < myMarbles.size(); i++) {
                    this.myMarbles.get(i).setCenterX(350 + 290 * count);
                }
            }
        }
    }

    private Scene chooseLevel() {
        switch (backIdx) {
            case 0:
                if (!isSkip) {
                    return new Tutorial(sceneController, ImgInfo.HOWTOSTRIKE_PATH, 5,
                            new Level1(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo));
                } else {
                    return new Level1(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo);
                }
            case 1:
                return new Level2(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo);
            case 2:
                return new Level3(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo);
            case 3:
                return new Level4(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo);
            case 4:
                return new Level5(sceneController, fightMarbles, this.enemyFightMarbles, this.playerInfo);
        }
        return null;
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
                Color.BLACK, "Lv." + this.playerInfo.getLevel(), 100, 330, 100);

        if (this.currentMarble != null && count < 3) {
            this.currentMarble.paintAll(g);
            paintText(g, this.currentMarble, 280 + 290 * count, 140);
        }
        
        g.drawImage(box, 20, Global.SCREEN_Y/2+50, 250, 200, null);
//        PaintText.paintStrongWord(g, new Font("Showcard Gothic", Font.BOLD, 18), 
//                    new Font("Showcard Gothic", Font.BOLD, 20), Color.BLACK, Color.ORANGE,
//                    "Press\"ENTER\"To Join", "Press\"BACKSPACE\"To Undo", 80, 70, 60, 95);

        for (int i = 0; i < this.fightMarbles.length; i++) {
            if (this.fightMarbles[i] != null) {
                this.fightMarbles[i].paintAll(g);
                paintText(g, this.fightMarbles[i], 280 + 290 * i, 140);
            }
        }

        if (this.count >= 3) {
            this.background.paintItem(g, 350, 380, 580, 200);
            if (isMask[backIdx]) {
                this.mask.paintItem(g, 350, 380, 580, 200);
                this.lock.paint(g);
            }
            this.title[backIdx].paint(g);
        }

        for (int i = 0; i < this.buttons.size(); i++) {
            this.buttons.get(i).paint(g);
        }
        this.home.paint(g);
    }

    public void paintText(Graphics g, Marble m, int x, int y) {
        PaintText.paint(g, new Font("Arial Unicode MS", Font.BOLD, 28),
                Color.BLACK, m.getInfo().getName(), x-5, y, (int) m.getWidth());
        PaintText.paint(g, new Font("VinerHandITC", Font.BOLD, 20),
                Color.BLACK, "HP: " + m.getInfo().getHp(), x - 8, y + 150, (int) m.getWidth());
        PaintText.paint(g, new Font("VinerHandITC", Font.BOLD, 20),
                Color.BLACK, "ATK: " + m.getInfo().getAtk(), x - 8, y + 180, (int) m.getWidth());
        PaintText.paint(g, new Font("VinerHandITC", Font.BOLD, 20),
                Color.BLACK, "Velocity: " + (int) m.getInfo().getV() + " m/s",
                x - 8, y + 210, (int) m.getWidth());
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
            switch (commandCode) {
                case Global.LEFT:
                    if (count <= 2) {
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
                    if (count <= 2) {
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
                case Global.BACKSPACE:
                    back();
                    break;
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
            if (state == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 30 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > 30 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < 30 + ImgInfo.SETTING_INFO[1] / 2) {
                isEnter = true;
            }
        }
    }
}
