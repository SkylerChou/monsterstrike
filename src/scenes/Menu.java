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
import monsterstrike.gameobject.Dino;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.Delay;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class Menu extends Scene{

    private Background menu;
    private Dino dino;
    private ArrayList<Button> buttons;
    private Delay delay;
    private int count;
    
    public Menu(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public void sceneBegin() {
        this.menu=new Background(ImgInfo.MENU,0,0,Global.SCREEN_X, Global.SCREEN_Y);
        this.buttons=new ArrayList<>();
        this.dino=new Dino(ImgInfo.DINO,0,0,50,50,Dino.STEPS_WALK);
        this.buttons.add(new Button(ImgInfo.SINGLE,Global.SCREEN_X/2-90,Global.SCREEN_Y/2-30,100,25));
        this.buttons.add(new Button(ImgInfo.MULTIPLAYER,Global.SCREEN_X/2-90,Global.SCREEN_Y/2+20,100,25));
        this.buttons.add(new Button(ImgInfo.HOWTOPLAY,Global.SCREEN_X/2-90,Global.SCREEN_Y/2+70,100,25));
        this.buttons.add(new Button(ImgInfo.EXIT,Global.SCREEN_X/2-90,Global.SCREEN_Y/2+120,100,25));
        
        this.delay=new Delay(5);
        this.delay.start();
        this.count=0;
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
        this.menu.paint(g);
        for(int i=0;i<this.buttons.size();i++){
            this.buttons.get(i).paint(g);
        }
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
