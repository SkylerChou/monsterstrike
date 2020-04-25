/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike;

import monsterstrike.util.Global;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class MonsterStrike {

    public static void main(String[] args) {

        JFrame f = new JFrame();

        GI gi = new GI();
        int[][] commands = new int[][]{
            {KeyEvent.VK_UP, Global.UP},
            {KeyEvent.VK_W, Global.UP2},
            {KeyEvent.VK_LEFT, Global.LEFT},
            {KeyEvent.VK_A, Global.LEFT2},
            {KeyEvent.VK_DOWN, Global.DOWN},
            {KeyEvent.VK_S, Global.DOWN2},
            {KeyEvent.VK_RIGHT, Global.RIGHT},
            {KeyEvent.VK_D, Global.RIGHT2},
            {KeyEvent.VK_ENTER, Global.ENTER},
            {KeyEvent.VK_SPACE, Global.SPACE},
            {KeyEvent.VK_Z, Global.Z},
            {KeyEvent.VK_BACK_SPACE, Global.BACKSPACE}
        };

        GameKernel gk = new GameKernel.Builder(gi, Global.MILLISEC_PER_UPDATE, Global.LIMIT_DELTA_TIME)
                .initListener(commands)
                .enableMouseTrack(gi)
                .enableKeyboardTrack(gi)
                .mouseForcedRelease()
                .trackChar()
                .keyCleanMode()
                .gen();
        f.setTitle("Monster Strike");
        f.setSize(Global.FRAME_X, Global.FRAME_Y);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(gk);
        f.setVisible(true);
        gk.run(Global.IS_DEBUG);
    }

}
