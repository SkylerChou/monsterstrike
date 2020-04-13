/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.awt.Graphics;
import monsterstrike.gameobject.Background;
import monsterstrike.gameobject.Dino;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class Loading extends Scene{
    private Background loading;
    private Dino dino;
    
    public Loading(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
         this.loading= new Background(ImgInfo.MENU, 0, 0, 1);
         this.dino= new Dino(ImgInfo.DINO, Global.SCREEN_X / 2 , Global.SCREEN_Y / 2, Dino.STEPS_WALK);
    }

    @Override
    public void sceneUpdate() {
        this.dino.update();
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
