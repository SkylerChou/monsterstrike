/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike;

/**
 *
 * @author yuin8
 */
public class LoadingThread extends Thread {

    public interface CallbackInterface {

        public void run();
    }

    private CallbackInterface c;

    public LoadingThread(CallbackInterface c) {
        this.c = c;
    }

    @Override
    public void run() {
        c.run();
    }
}
