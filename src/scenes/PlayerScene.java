/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import controllers.SceneController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import player.PlayerInfo;
import monsterstrike.gameobject.Background;
import monsterstrike.gameobject.Button;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.MarbleInfo;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.Global;
import player.Player;

/**
 *
 * @author yuin8
 */
public class PlayerScene extends Scene {
    
    class ButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event) {
            // TODO Auto-generated method stub //將按鈕的名稱顯示在TextField中
            name = jtf.getText();
            jf.dispose();
            isSet = true;
        }
    }
    
    private PlayerInfo playerInfo;
    private Background menu;
    private Player[] players;
    private String name;
    private JFrame jf;
    private JPanel jp;
    private JTextField jtf;
    private JLabel label;
    private JButton btn;
    private boolean isSet;
    private boolean isReleased;
    private int currentIdx;
    private Button[] shineFrame;
    private boolean isDone;
    private ArrayList<MarbleInfo> allMarbleInfo;
    
    public PlayerScene(SceneController sceneController) {
        super(sceneController);
        this.playerInfo = FileIO.readPlayer("playerInfoInit.csv", 0);
        this.allMarbleInfo = FileIO.readMarble("marbleInfoInit.csv");
        this.players = new Player[2];
        this.shineFrame = new Button[2];
        isSet = false;
        jf = new JFrame();
        jtf = new JTextField();
        jp = new JPanel();
        btn = new JButton("確定");
        this.currentIdx = 0;
//        btn.setSize(100, 40);
        this.isDone = false;
    }
    
    @Override
    public void sceneBegin() {
        this.menu = new Background(ImgInfo.LEVELBACK_PATH, 0, 0, 1);
        for (int i = 0; i < this.players.length; i++) {
            players[i] = new Player(i, 300 * (i + 1) + 100, 200, 150, 213);
            shineFrame[i] = new Button(ImgInfo.SHINEFRAME_PATH, 300 * (i + 1) + 175, 305,
                    180, 250, 20);
            this.shineFrame[i].setIsShow(true);
        }
        btn.addActionListener(new ButtonListener());
        label = new JLabel("玩家姓名: ", JLabel.CENTER);
        label.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        jp.setLayout(new FlowLayout(FlowLayout.CENTER));
        jtf.setColumns(10);
        jtf.setHorizontalAlignment(JTextField.CENTER);
        jtf.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        jf.setBounds(Global.FRAME_X / 2 - 150, Global.FRAME_Y / 2 - 100, 250, 150);
        jf.setVisible(true);
        jp.add(label, BorderLayout.NORTH);
        jp.add(jtf, BorderLayout.CENTER);
        jp.add(btn, BorderLayout.SOUTH);
        jf.add(jp);
    }
    
    @Override
    public void sceneUpdate() {
        if (isSet) {
            this.playerInfo.setName(name);
            for (int i = 0; i < this.players.length; i++) {
                if (currentIdx == i) {
                    this.players[i].setState(1);
                } else {
                    this.players[i].setState(0);
                }
                this.players[i].update();
            }
            this.shineFrame[currentIdx].update();
            if (isDone) {
                this.playerInfo.setSerial(currentIdx + 1);
                sceneController.changeScene(new LevelMenu(sceneController, 
                        this.playerInfo, "marbleInfoInit.csv"));
            }
        }
    }
    
    @Override
    public void sceneEnd() {
        
    }
    
    @Override
    public void paint(Graphics g) {
        this.menu.paintMenu(g);
        for (int i = 0; i < this.players.length; i++) {
            this.players[i].paint(g);
            this.shineFrame[i].paint(g);
        }
        g.setFont(new Font("Arial Unicode MS", Font.PLAIN, 24));
        g.setColor(Color.BLACK);
        if (isSet) {
            if (name.equals("")) {
                name = "User";
            }
            g.drawString("玩家姓名: " + name, Global.SCREEN_X / 2 - 100, 500);
            g.drawString("Level 1", Global.SCREEN_X / 2 - 70, 540);
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
                if (commandCode == Global.LEFT && currentIdx == 1) {
                    currentIdx = 0;
                } else if (commandCode == Global.RIGHT && currentIdx == 0) {
                    currentIdx = 1;
                } else if (commandCode == Global.ENTER) {
                    isDone = true;
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
