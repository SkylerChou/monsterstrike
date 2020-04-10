/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.MarbleInfo;

public class FileIO {

//    public static void main(String[] args) {
////        ArrayList<MarbleInfo> marbles = new ArrayList<>();
////        for (int i = 0; i < 1; i++) {
//////            String[] path = {"/resources/marbles/fireBall1.png", "/resources/marbles/fireBall2.png"};
////            MarbleInfo m = new MarbleInfo("火球", 120, 120, 40, 1, 40f, 1, 3, 0);
////            marbles.add(m);
////        }
////        write("marbleInfo.csv", marbles);
//        ArrayList<MarbleInfo> marbles = read("marbleInfo.csv");
//        for(int i=0; i<marbles.size(); i++){
//            System.out.println(marbles.get(i));
//        }
//        
//    }
    public static ArrayList<MarbleInfo> read(String fileName) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            ArrayList<MarbleInfo> arr = new ArrayList<>();
            br.readLine();
            while (br.ready()) {
                String str = br.readLine();
                String[] tmp = str.split(",");
                MarbleInfo m = new MarbleInfo(tmp[0], tmp[1], Integer.parseInt(tmp[2]),
                        Integer.parseInt(tmp[3]), Float.parseFloat(tmp[4]), Float.parseFloat(tmp[5]),
                        Float.parseFloat(tmp[6]), Integer.parseInt(tmp[7]), Integer.parseInt(tmp[8]),
                        Integer.parseInt(tmp[9]), Integer.parseInt(tmp[10]), Integer.parseInt(tmp[11]),
                        Integer.parseInt(tmp[12]), Integer.parseInt(tmp[13]));
                arr.add(m);
            }
            br.close();
            return arr;
        } catch (IOException e) {
            e.getStackTrace();
        }
        return null;
    }

    public static void write(String fileName, ArrayList<MarbleInfo> marbles) {
        try {
            BufferedWriter bw = new BufferedWriter((new FileWriter(fileName)));
            for (int i = 0; i < marbles.size(); i++) {
                MarbleInfo m = marbles.get(i);
                String tmp = m.getName() + "," + m.getImgName() + "," + m.getImgW() + ","
                        + m.getImgH() + "," + m.getRatio() + "," + m.getMass() + ","
                        + m.getV() + "," + m.getAttribute() + "," + m.getHp() + ","
                        + m.getAtk() + "," + m.getSkillRound() + "," + m.getState() + ","
                        + m.getSpecies();
                bw.append(tmp);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
