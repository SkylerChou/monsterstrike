/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.util;

public class Delay {
    private int delayFrame;
    private int counter;
    private boolean isPause;
    
    public Delay(int delayFrame){
        this.delayFrame = delayFrame;
        this.counter = delayFrame;
        this.isPause = true;
    }
    
    public Delay(int delayFrame, boolean isTrig){
        if (isTrig) {
            this.counter = delayFrame;
        } else {
            this.counter = 0;
        }       
        this.delayFrame = delayFrame;
        this.isPause = true;
    }
    
    public void start(){
        this.isPause = false;
    }
    
    public void pause(){
        this.isPause = true;
    }
    
    public void stop(){
        pause();
        this.counter = 0;
    }
    
    public void restart(){
        stop();
        start();
    }
    
    public boolean isTrig(){
        if(!isPause && this.counter++ == this.delayFrame){
            this.counter = 0;
            return true;
        }
        return false;
    }
}
