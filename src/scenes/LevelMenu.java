/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.Background;
import monsterstrike.gameobject.Button;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.*;
import monsterstrike.util.*;

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
    private Marble currentMarble;
    private Background background;
    private int backIdx;
    private boolean isReleased;
    private int x;
    private int y;
    private Delay delay;

    public LevelMenu(SceneController sceneController) {
        super(sceneController);
        this.allMarbleInfo = FileIO.read("marbleInfo.csv");
        this.myMarbles = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.fightMarbles = new Marble[3];
        this.buttons = new ArrayList<>();
        this.enemyFightMarbles = new ArrayList<>();
    }

    @Override
    public void sceneBegin() {
        this.menu = new Background(ImgInfo.LEVELBACK_PATH, 0, 0, 1);
        this.count = 0;
        this.idx = 0;
        this.x = 350;
        this.y = 200;
        this.backIdx = 0;
        this.background = new Background(ImgInfo.BACKGROUND_PATH[this.backIdx], 0, 0, this.backIdx);
        for (int i = 0; i < this.allMarbleInfo.size(); i++) {
            if (this.allMarbleInfo.get(i).getState() == 0) {
                this.myMarbles.add(new Marble(this.x, this.y, 150, 150, this.allMarbleInfo.get(i)));
            } else {
                this.enemies.add(new Marble(0, 0, 150, 150, this.allMarbleInfo.get(i)));
            }
        }
        int x = 0;
        for (int i = 0; i < 3; i++) {
            buttons.add(new Button(ImgInfo.RIGHT, 420 + x, 180, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
            buttons.add(new Button(ImgInfo.LEFT, 230 + x, 180, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
            x += 290;
        }
        buttons.add(new Button(ImgInfo.RIGHT, 950, 470, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
        buttons.add(new Button(ImgInfo.LEFT, 280, 470, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
        this.currentMarble = this.myMarbles.get(0);
        this.isReleased = true;
        this.delay = new Delay(20);
        this.delay.start();
    }

    @Override
    public void sceneUpdate() {

        if (count == 4) {
            this.background = new Background(ImgInfo.BACKGROUND_PATH[backIdx], 0, 0, this.backIdx);
        } else if (this.count == 5) {
            for (int i = 0; i < this.enemies.size(); i++) {
                if (this.enemies.get(i).getInfo().getAttribute() != backIdx) {                   
                    continue; 
                }
                this.enemyFightMarbles.add(this.enemies.get(i));
            }
            
            sceneController.changeScene(new LevelScene(sceneController, backIdx, fightMarbles, this.enemyFightMarbles));
        } else {
            this.currentMarble = this.myMarbles.get(idx);
            this.currentMarble.update();
        }
        if (this.delay.isTrig()) {
            int i = 2 * (count - 1);
            if (i == -2) {
                i = 0;
            } else if (i == 8) {
                i = 6;
            }
            if (this.buttons.get(i) == null) {
                System.out.println(i + " " + this.buttons.get(i));
            }
            this.buttons.get(i).update();
            this.buttons.get(i + 1).update();
        }
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
        this.count++;
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        if (this.currentMarble != null && count < 4) {
            this.currentMarble.paintComponent(g);
        }

        for (int i = 0; i < this.fightMarbles.length; i++) {
            if (this.fightMarbles[i] != null) {
                this.fightMarbles[i].paintComponent(g);
            }
        }

        if (this.count >= 4) {
            this.background.paintItem(g, 350, 400, 580, 200);
        }

        for (int i = 0; i < this.buttons.size(); i++) {
            int[] size = new int[]{50, 50};
            this.buttons.get(i).paintOther(g, size);
        }
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
}
