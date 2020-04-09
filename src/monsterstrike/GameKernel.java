/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import monsterstrike.util.CommandSolver;
import monsterstrike.util.CommandSolver.KeyListener;
import monsterstrike.util.CommandSolver.MouseCommandListener;


/**
 *
 * @author user1
 */
public class GameKernel extends Canvas {

    public static class Builder {

        private GameInterface gi;
        private GameKernel gk;
        private CommandSolver.Builder builder;

        public Builder(GameInterface gi, long limitDeltaTimePerMilli, long millisecPerUpdate) {
            this.gi = gi;
            this.gk = new GameKernel(gi, limitDeltaTimePerMilli, millisecPerUpdate);
        }
        
        public Builder initListener(){
            builder = new CommandSolver.Builder(this.gk, this.gk.millisecPerUpdate);
            return this;
        }

        public Builder initListener(int[][] array) {
            builder = new CommandSolver.Builder(this.gk, this.gk.millisecPerUpdate, array);
            return this;
        }
        
        public Builder initListener(ArrayList<int[]> cmArray) {
            builder = new CommandSolver.Builder(this.gk, this.gk.millisecPerUpdate, cmArray);
            return this;
        }

        public Builder add(int key, int command) {
            builder.add(key, command);
            return this;
        }

        public Builder enableMouseTrack(MouseCommandListener ml) {
            builder.enableMouseTrack(ml);
            return this;
        }

        public Builder enableKeyboardTrack(KeyListener kl) {
            builder.enableKeyboardTrack(kl);
            return this;
        }

        public Builder keyTypedMode() {
            builder.keyTypedMode();
            return this;
        }

        public Builder keyCleanMode() {
            builder.keyCleanMode();
            return this;
        }

        public Builder trackChar() {
            builder.trackChar();
            return this;
        }
        
        public GameKernel gen(){
            gk.cs = builder.gen();
            return gk;
        }
    }

    public interface GameInterface {

        public void paint(Graphics g);

        public void update();
    }

    private CommandSolver cs;
    private final long limitDeltaTimePerMilli;
    private final long millisecPerUpdate;
    private final GameInterface gi;

    private GameKernel(GameInterface gi, long limitDeltaTimePerMilli, long millisecPerUpdate) {
        this.gi = gi;
        this.limitDeltaTimePerMilli = limitDeltaTimePerMilli;
        this.millisecPerUpdate = millisecPerUpdate;
//        System.out.println(this.millisecPerUpdate);
    }

    public void paint() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
//        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (gi != null) {
            gi.paint(g);
        }
        g.dispose();
        bs.show();
    }

    public void run(boolean isDebug) {
        cs.start();
        long startTime = System.nanoTime();
        long passedUpdated = 0;
        long lastRepaintTime = System.nanoTime();
        int paintTimes = 0;
        int t=0;
        long timer = System.nanoTime();
        while (true) {
            long currentTime = System.nanoTime();// 系統當前時間
            long totalTime = currentTime - startTime;// 從開始到現在經過的時間
            long targetTotalUpdated = totalTime / (millisecPerUpdate);// 開始到現在應該更新的次數
            // input
            // input end
            while (passedUpdated < targetTotalUpdated) {// 如果當前經過的次數小於實際應該要更新的次數
                //update 更新追上當前次數
                if (cs != null) {
                    cs.update();
                }
                gi.update();
                passedUpdated++;
                t++;
            }
            if (currentTime - timer >= 1000000000) {
                if (isDebug) {
                    System.out.println("FPS: " + paintTimes);
//                    System.out.println("t: " + t);
                }
                t = 0;
                paintTimes = 0;
                timer = currentTime;
            }

            if (limitDeltaTimePerMilli <= currentTime - lastRepaintTime) {
                lastRepaintTime = currentTime;
                paint();
                paintTimes++;
            }
        }
    }
}
