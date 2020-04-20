/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.awt.Graphics;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.Renderer;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class TutorialPinball extends Scene {

    private Renderer howToPlay;
    private int count;
    private boolean isEnter;

    public TutorialPinball(SceneController sceneController) {
        super(sceneController);
        this.howToPlay = new Renderer("/resources/howtoplay.png", 5, 0);
        this.count = 0;
        this.isEnter = false;
    }

    @Override
    public void sceneBegin() {

    }

    @Override
    public void sceneUpdate() {
        if(this.count<=4){
            this.howToPlay.updateOneByOne(count);
        }else{
            this.sceneController.changeScene(new PingPong(sceneController));
        }  
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.howToPlay.paint(g, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, ImgInfo.HOWTOPLAY_SIZE[0], ImgInfo.HOWTOPLAY_SIZE[1]);
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
            if (!isEnter) {
                switch (commandCode) {
                    case Global.ENTER:
                        count++;
                        isEnter = true;
                        break;
                }
            }

        }

        @Override
        public void keyReleased(int commandCode, long trigTime
        ) {
            isEnter = false;
        }

        @Override
        public void keyTyped(char c, long trigTime
        ) {

        }

    }
}
