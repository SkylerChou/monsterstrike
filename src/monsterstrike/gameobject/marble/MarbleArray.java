/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import java.util.ArrayList;
import monsterstrike.util.Global;

public class MarbleArray extends ArrayList<Marble> {

    private ArrayList<Marble> arr;

    public MarbleArray(ArrayList<Marble> arr) {
        this.arr = arr;
    }

    public ArrayList<Marble> getMyMarbles() {
        ArrayList<Marble> newArr = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getInfo().getState() == 0) {
                newArr.add(arr.get(i));
            }
        }
        return newArr;
    }

    public ArrayList<Marble> getEnemiesByAttribute(int attribute) {
        ArrayList<Marble> newArr = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getInfo().getState() == 1 && arr.get(i).getInfo().getAttribute() == attribute) {
                newArr.add(arr.get(i));
            }
        }
        return newArr;
    }

    public ArrayList<Marble> getAllEnemies() {
        ArrayList<Marble> newArr = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getInfo().getState() == 1) {
                newArr.add(arr.get(i));
            }
        }
        return newArr;
    }

    public ArrayList<Marble> getEnemiesByLevel(int level) {
        ArrayList<Marble> newArr = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getInfo().getState() == 1 && arr.get(i).getInfo().getLevel() == level) {
                newArr.add(arr.get(i));
            }
        }
        return newArr;
    }

    public ArrayList<Marble> sortByLevel() {
        for (int i = 1; i < this.arr.size(); i++) {
            for (int j = 0; j < this.arr.size() - i; j++) {
                if (this.arr.get(j).getInfo().getShowIdx() > this.arr.get(j + 1).getInfo().getShowIdx()) {
                    Marble tmp = this.arr.get(j);
                    this.arr.set(j, this.arr.get(j + 1));
                    this.arr.set(j + 1, tmp);
                }
            }
        }
        for (int i = 1; i < this.arr.size(); i++) {
            for (int j = 0; j < this.arr.size() - i; j++) {
                if (this.arr.get(j).getInfo().getLevel() > this.arr.get(j + 1).getInfo().getLevel()) {
                    Marble tmp = this.arr.get(j);
                    this.arr.set(j, this.arr.get(j + 1));
                    this.arr.set(j + 1, tmp);
                }
            }
        }

        return this.arr;
    }

    public Marble luckyDraw() {
        return this.arr.get(Global.random(0, this.arr.size() - 1));
    }
   
    public ArrayList<Marble> getArray() {
        return this.arr;
    }
}
