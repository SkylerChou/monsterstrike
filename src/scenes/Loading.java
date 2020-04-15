/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.PlayerInfo;
import monsterstrike.gameobject.*;
import monsterstrike.gameobject.marble.MarbleInfo;
import monsterstrike.util.*;

/**
 *
 * @author kim19
 */
public class Loading extends Scene {

    private Background loading;
    private Dino dino;
    private ArrayList<MarbleInfo> allMarbleInfo;
    private PlayerInfo playerInfo;
    private Delay delay;

    public Loading(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.loading = new Background(ImgInfo.MENU, 0, 0, 1);
        this.dino = new Dino(ImgInfo.DINO, Global.SCREEN_X / 2, Global.SCREEN_Y / 2, Dino.STEPS_WALK);
        this.allMarbleInfo = FileIO.readMarble("marbleInfo.csv");
//        this.playerInfo = FileIO.readPlayer("playerInfo.csv");
        this.delay = new Delay(180, false);
        this.delay.start();
    }

    @Override
    public void sceneUpdate() {
        this.dino.update();
        if (this.delay.isTrig()) {            
            sceneController.changeScene(new LevelMenu(sceneController));
        }
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.loading.paint(g);
        this.dino.paint(g);
    }

    @Override
    public CommandSolver.KeyListener getKeyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseCommandListener getMouseListener() {
        return null;
    }

}
