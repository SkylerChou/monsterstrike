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
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.gameobject.marble.ReboundMarble;

import monsterstrike.util.CommandSolver;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

public class LevelMenu extends Scene {

    private Marble[] myMarbles;
    private int[] myIdx;
    private ArrayList<Button> buttons;
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
    private Delay delay;

    public LevelMenu(SceneController sceneController) {
        super(sceneController);
        this.myMarbles = new Marble[3];
        this.myIdx = new int[3];
        this.buttons = new ArrayList<>();
    }

    @Override
    public void sceneBegin() {
        this.menu = new Background(ImgInfo.LEVELBACK_PATH, 0, 0, 1);
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
        int x = 0;
        for (int i = 0; i < 3; i++) {
            buttons.add(new Button(ImgInfo.RIGHT, 420 + x, 180, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
            buttons.add(new Button(ImgInfo.LEFT, 230 + x, 180, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
            x += 290;
        }
        buttons.add(new Button(ImgInfo.RIGHT, 950, 470, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
        buttons.add(new Button(ImgInfo.LEFT, 280, 470, ImgInfo.CHOOSEBUTTON_INFO[0], ImgInfo.CHOOSEBUTTON_INFO[1]));
        this.currentMarble = this.allMarbles[0];
        this.isReleased = true;
        this.delay = new Delay(20);
        this.delay.start();
    }

    @Override
    public void sceneUpdate() {
        this.currentMarble = this.allMarbles[idx];
        this.currentMarble.update();
        if (count == 4) {
            this.background = new Background(ImgInfo.BACKGROUND_PATH[backIdx], 0, 0, this.backIdx);
        } else if (this.count == 5) {
            sceneController.changeScene(new LevelScene(sceneController, backIdx, myIdx));
        }
        if (this.delay.isTrig()) {
            if(this.count==0){
                this.count = 1;
            }
            this.buttons.get(2 * (count-1)).update();
            this.buttons.get(2 * (count-1) + 1).update();
        }
    }

    private void enter() {
        if (count >= 5) {
            count = 5;
        }
        if (this.count > 0 && this.count < 4) {
            myMarbles[this.count - 1] = currentMarble;
            myIdx[this.count-1] = idx;
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
