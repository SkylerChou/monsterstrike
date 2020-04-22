/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.ArrayList;

/**
 *
 * @author kim19
 */
public class MRC {

    private static class KeyPair {

        private String path;
        private AudioClip music;

        public KeyPair(String path, AudioClip music) {
            this.path = path;
            this.music = music;
        }
    }

    private static MRC mrc;
    private ArrayList<KeyPair> musicPair;

    private MRC() {
        musicPair = new ArrayList<>();
    }

    public static MRC getInstance() {
        if (mrc == null) {
            mrc = new MRC();
        }
        return mrc;
    }

    public AudioClip tryGetMusic(String path) {
        KeyPair pair = findKeyPair(path);
        if (pair == null) {
            return addMusic(path);
        }
        return pair.music;
    }

    private KeyPair findKeyPair(String path) {
        for (int i = 0; i < musicPair.size(); i++) {
            KeyPair pair = musicPair.get(i);
            if (pair.path.equals(path)) {
                return pair;
            }
        }
        return null;
    }

    private AudioClip addMusic(String path) {
        try {
            AudioClip music = Applet.newAudioClip(getClass().getResource(path));
            musicPair.add(new KeyPair(path,music));
            return music;
        } catch (Exception ex) {

        }
        return null;
    }
}
