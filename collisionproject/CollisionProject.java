/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collisionproject;

import collisionproject.util.CommandSolver;
import collisionproject.util.Global;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class CollisionProject {

    public static void main(String[] args) {

        JFrame f = new JFrame();
        GameJPanel jp = new GameJPanel();
        f.setTitle("碰撞");
        f.setSize(Global.FRAME_X, Global.FRAME_Y);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(jp);
        f.setVisible(true);

        CommandSolver cs = new CommandSolver.Builder(jp, Global.MILLISEC_PER_UPDATE,
                new int[][]{
                    {KeyEvent.VK_W, Global.UP},
                    {KeyEvent.VK_A, Global.LEFT},
                    {KeyEvent.VK_S, Global.DOWN},
                    {KeyEvent.VK_D, Global.RIGHT}
                }).enableMouseTrack(jp).enableKeyboardTrack(jp)
                .gen();
        cs.start();

        long startTime = System.currentTimeMillis();
        long passedUpdated = 0; //當前已經更新的次數
        long lastRepaintTime = System.currentTimeMillis();

        while (true) {
            long currentTime = System.currentTimeMillis();//系統當前時間
            long totalTime = currentTime - startTime; //從開始到現在經過的時間(隨著遊戲開啟時間越長 totalTime 越大)
            long targetTotalUpdated = totalTime / Global.MILLISEC_PER_UPDATE; //從開始到現在遊戲邏輯應該更新的次數;

            while (passedUpdated < targetTotalUpdated) { //若當前更新的次數小於實際應更新的次數
                //update 追上實際應更新的次數
                cs.update();
                jp.update();
                passedUpdated++;
            }

            if (Global.LIMIT_DELTA_TIME <= currentTime - lastRepaintTime) {
                lastRepaintTime = currentTime;
                f.repaint();
            }
        }
    }

}
