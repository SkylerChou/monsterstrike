/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

public class PlayerInfo {

    private String name;
    private int serial;
    private int playerNum;
    private int level;
    private int[] myMarbleSerials;

    public PlayerInfo(int serial, int playerNum, String name, int level, int[] myMarbleSerials) {
        this.name = name;
        this.level = level;
        this.playerNum = playerNum;
        this.serial = serial;
        this.myMarbleSerials = myMarbleSerials;
    }

    public int getSerial() {
        return this.serial;
    }
    
    public int playerNum(){
        return this.playerNum;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }
    

    public int[] getMyMarbleSerials() {
        return this.myMarbleSerials;
    }

    public void addMyMarbleSerial(int serial) {
        int[] newSerials = new int[this.myMarbleSerials.length + 1];
        for (int i = 0; i < this.myMarbleSerials.length; i++) {
            newSerials[i] = this.myMarbleSerials[i];
        }
        newSerials[this.myMarbleSerials.length] = serial;
        this.myMarbleSerials = newSerials;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }
    
    public void setPlayerNum(int playerNum){
        this.playerNum = playerNum;
    }

    @Override
    public String toString() {
        return "編號:" +this.serial + "姓名:" + this.name + "等級:" + this.level;
    }
}
