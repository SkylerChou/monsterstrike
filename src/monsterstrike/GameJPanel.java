/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike;

import monsterstrike.util.CommandSolver;
import monsterstrike.util.CommandSolver.KeyListener;
import monsterstrike.util.CommandSolver.MouseCommandListener;
import controllers.SceneController;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import scenes.*;

public class GameJPanel extends javax.swing.JPanel implements KeyListener, MouseCommandListener{

    private SceneController sceneController;
    
    public GameJPanel() {
        sceneController = new SceneController();
        sceneController.changeScene(new Menu(sceneController));
    }

    public void update() {
        sceneController.sceneUpdate();
    }

    @Override
    public void paintComponent(Graphics g) {
        sceneController.paint(g);
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        if (sceneController.getKL() != null) {
            sceneController.getKL().keyPressed(commandCode, trigTime);
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        if (sceneController.getKL() != null) {
            sceneController.getKL().keyReleased(commandCode, trigTime);
        }
    }

    @Override
    public void keyTyped(char c, long trigTime) {
        if (sceneController.getKL() != null) {
            sceneController.getKL().keyTyped(c, trigTime);
        }
    }

    @Override
    public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if (state != null && sceneController.getML() != null) {
            sceneController.getML().mouseTrig(e, state, trigTime);
        }
    }
}
