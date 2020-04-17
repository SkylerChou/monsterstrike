/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import monsterstrike.gameobject.marble.Marble;

public class PlayerInfo {

    private String name;
    private int serial;
    private int level;
    private int[] myMarbleSerials;

    public PlayerInfo(int serial, String name, int level, int[] myMarbleSerials) {
        this.name = name;
        this.level = level;
        this.serial = serial;
        this.myMarbleSerials = myMarbleSerials;
    }

    public int getSerial() {
        return this.serial;
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

    @Override
    public String toString() {
        return "姓名:" + this.name + "等級:" + this.level;
    }
}
