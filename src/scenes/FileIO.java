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
import monsterstrike.PlayerInfo;
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
    public static ArrayList<MarbleInfo> readMarble(String fileName) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));            
            ArrayList<MarbleInfo> arr = new ArrayList<>();
            br.readLine();
            while (br.ready()) {
                String str = br.readLine();
                String[] tmp = str.split(",");
                MarbleInfo m = new MarbleInfo(Integer.parseInt(tmp[0]), tmp[1], tmp[2], Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]),
                        Integer.parseInt(tmp[5]), Float.parseFloat(tmp[6]), Float.parseFloat(tmp[7]),
                        Float.parseFloat(tmp[8]), Integer.parseInt(tmp[9]), Integer.parseInt(tmp[10]), Integer.parseInt(tmp[11]),
                        Integer.parseInt(tmp[12]), Integer.parseInt(tmp[13]), Integer.parseInt(tmp[14]), Integer.parseInt(tmp[15])
                );
                arr.add(m);
            }
            br.close();
            return arr;
        } catch (IOException e) {
            e.getStackTrace();
        }
        return null;
    }
    
    public static PlayerInfo readPlayer(String fileName, int idx) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            ArrayList<PlayerInfo> arr = new ArrayList<>();
            br.readLine();
            while (br.ready()) {
                String str = br.readLine();
                String[] tmp = str.split(",");
                String[] tmp2 = tmp[2].split("\"")[1].split(";");
                int[] marbleSerials = new int[tmp2.length];
                for(int i=0; i<tmp2.length; i++){
                    marbleSerials[i] = Integer.parseInt(tmp2[i]);
                }
                PlayerInfo m = new PlayerInfo(tmp[0], Integer.parseInt(tmp[1]), marbleSerials);
                arr.add(m);
            }
            br.close();
            return arr.get(idx);
        } catch (IOException e) {
            e.getStackTrace();
        }
        return null;
    }

    public static void writeMarble(String fileName, MarbleInfo marble) {
        try {
            BufferedWriter bw = new BufferedWriter((new FileWriter(fileName, true)));
//            for (int i = 0; i < marbles.size(); i++) {
                MarbleInfo m = marble;
                String tmp = m.getSerial() + "," + m.getName() + "," + m.getImgName() + "," + m.getShowIdx() + ","
                        + m.getImgW() + "," + m.getImgH() + "," + m.getRatio() + "," + m.getMass() + ","
                        + m.getV() + "," + m.getAttribute() + ","+ m.getLevel()+"," + m.getHp() + ","
                        + m.getAtk() + "," + m.getSkillRound() + "," + m.getState() + ","
                        + m.getSpecies();
                bw.append(tmp);
                bw.newLine();
//            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void writePlayer(String fileName, PlayerInfo players) {
        try {
            BufferedWriter bw = new BufferedWriter((new FileWriter(fileName)));
            bw.append("姓名,等級,怪物編號");
            bw.newLine();
//            for (int i = 0; i < players.size(); i++) {
//                PlayerInfo m = players.get(i);
                int[] marbleSerials = players.getMyMarbleSerials();
                String serial = "\"";
                for(int j=0; j<marbleSerials.length; j++){
                    serial += marbleSerials[j];
                    if(j!=marbleSerials.length-1){
                        serial += ";";
                    }
                }
                String tmp = players.getName() + "," + players.getLevel() + "," + serial + "\"";
                bw.append(tmp);
                bw.newLine();
//            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
