/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.awt.Graphics;
import monsterstrike.gameobject.Background;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.ReboundMarble;

import monsterstrike.util.CommandSolver;
import monsterstrike.util.Global;

public class LevelMenu extends Scene {

    private Marble[] myMarbles;
    private Background menu;
    private int count;
    private int idx;
    private Marble[] allMarbles;
    private Marble currentMarble;
    private Background background;
    private int backIdx;
    private boolean isReleased;
    private int x;
    private int y;

    public LevelMenu(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.menu = new Background(ImgInfo.LEVELBACK_PATH, 0, 0, 1);
        this.myMarbles = new Marble[3];
        this.count = 0;
        this.idx = 0;
        this.x = 350;
        this.y = 200;
        this.allMarbles = new Marble[ImgInfo.MYMARBLE_NAME.length];
        this.backIdx = 0;
        this.background = new Background(ImgInfo.BACKGROUND_PATH[this.backIdx], 0, 0, this.backIdx);
        for (int i = 0; i < this.allMarbles.length; i++) {
            this.allMarbles[i] = new ReboundMarble(ImgInfo.MYMARBLE_PATH[i],
                    ImgInfo.MYMARBLE_NAME[i], x, y, ImgInfo.MYMARBLE_INFO[i]);
        }
        this.currentMarble = this.allMarbles[0];
        this.isReleased = true;
    }

    @Override
    public void sceneUpdate() {
        this.currentMarble = this.allMarbles[idx];
        this.currentMarble.update();
        if (count == 4) {
            this.background = new Background(ImgInfo.BACKGROUND_PATH[backIdx], 0, 0, this.backIdx);
        } else if (this.count == 5) {
            sceneController.changeScene(new LevelScene(sceneController, backIdx, myMarbles));
        }
    }

    private void enter() {
        if (count >= 5) {
            count = 5;
        }
        if (this.count > 0 && this.count < 4) {
            myMarbles[this.count - 1] = currentMarble;
            this.idx = 0;
            this.x += 290;
            for (int i = 0; i < allMarbles.length; i++) {
                allMarbles[i] = new ReboundMarble(ImgInfo.MYMARBLE_PATH[i],
                        ImgInfo.MYMARBLE_NAME[i], x, y, ImgInfo.MYMARBLE_INFO[i]);
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

        for (int i = 0; i < this.myMarbles.length; i++) {
            if (this.myMarbles[i] != null) {
                this.myMarbles[i].paintComponent(g);
            }
        }

        if (this.count >= 4) {
            this.background.paintDemo(g, 350, 400, 580, 200);
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
                                idx = 0;
                            }
                        } else {
                            backIdx--;
                            if (backIdx < 0) {
                                backIdx = 0;
                            }
                        }
                        break;
                    case Global.RIGHT:
                        if (count <= 3) {
                            idx++;
                            if (idx >= ImgInfo.MYMARBLE_NAME.length) {
                                idx = ImgInfo.MYMARBLE_NAME.length - 1;
                            }
                        } else {
                            backIdx++;
                            if (backIdx >= ImgInfo.BACKGROUND_PATH.length) {
                                backIdx = ImgInfo.BACKGROUND_PATH.length - 1;
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
