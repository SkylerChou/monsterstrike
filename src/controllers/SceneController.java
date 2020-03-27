/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import monsterstrike.util.CommandSolver.KeyListener;
import monsterstrike.util.CommandSolver.MouseCommandListener;
import java.awt.Graphics;
import scenes.Scene;

public class SceneController {
    private Scene currentScene;
    private KeyListener kl;
    private MouseCommandListener ml;
    
    public void changeScene(Scene scene){
        if(this.currentScene!=null){
            this.currentScene.sceneEnd();
        }
        this.currentScene = scene;
        kl = currentScene.getKeyListener();
        ml = currentScene.getMouseListener();
        this.currentScene.sceneBegin();
    }
    
    public void sceneUpdate(){
        this.currentScene.sceneUpdate();
    }
    
    public KeyListener getKL(){
        return this.kl;
    }
    
    public MouseCommandListener getML(){
        return this.ml;
    }
    
    public void paint(Graphics g){
        this.currentScene.paint(g);
    }
}
