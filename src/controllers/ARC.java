/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ARC {
    // 單例

    private static ARC irc;

    private ARC() {
    }

    public static ARC getInstance() {
        if (irc == null) {
            irc = new ARC();
        }
        return irc;
    }

    public void play(String filename) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AudioInputStream audioInputStream;
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "\\src" + filename));
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.setFramePosition(0);
                    // values have min/max values, for now don't check for outOfBounds values
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(5f);
                    clip.start();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    Logger.getLogger(ARC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

}
