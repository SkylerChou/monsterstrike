/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import player.PlayerInfo;
import monsterstrike.gameobject.marble.MarbleInfo;
import monsterstrike.util.Global;

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
            br = new BufferedReader(new FileReader(Global.FILE_ROOT + fileName));
            ArrayList<MarbleInfo> arr = new ArrayList<>();
            br.readLine();
            while (br.ready()) {
                String str = br.readLine();
                String[] tmp = str.split(",");
                MarbleInfo m = new MarbleInfo(Integer.parseInt(tmp[0]), tmp[1], tmp[2], Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]),
                        Integer.parseInt(tmp[5]), Float.parseFloat(tmp[6]), Float.parseFloat(tmp[7]),
                        Float.parseFloat(tmp[8]), Integer.parseInt(tmp[9]), Integer.parseInt(tmp[10]), Integer.parseInt(tmp[11]),
                        Integer.parseInt(tmp[12]), Integer.parseInt(tmp[13]), Integer.parseInt(tmp[14]), Integer.parseInt(tmp[15]), Integer.parseInt(tmp[16])
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

    public static ArrayList<PlayerInfo> readPlayer(String fileName) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(Global.FILE_ROOT + fileName));
            ArrayList<PlayerInfo> arr = new ArrayList<>();
            br.readLine();
            while (br.ready()) {
                String str = br.readLine();
                String[] tmp = str.split(",");
                String[] tmp2 = tmp[4].split("\"")[1].split(";");
                int[] marbleSerials = new int[tmp2.length];
                for (int i = 0; i < tmp2.length; i++) {
                    marbleSerials[i] = Integer.parseInt(tmp2[i]);
                }
                PlayerInfo m = new PlayerInfo(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), tmp[2],
                        Integer.parseInt(tmp[3]), marbleSerials);
                arr.add(m);
            }
            br.close();
            return arr;
        } catch (IOException e) {
            e.getStackTrace();
        }
        return null;
    }
    
    public static void copy(String from, String to){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(Global.FILE_ROOT + from));
            BufferedWriter bw = new BufferedWriter((new FileWriter(Global.FILE_ROOT + to)));
            while (br.ready()) {
                String str = br.readLine();
                bw.append(str);
                bw.newLine();
            }
            bw.flush();
            bw.close();
            br.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public static void writeMarble(String fileName, MarbleInfo marble) {
        try {
            BufferedWriter bw = new BufferedWriter((new FileWriter(Global.FILE_ROOT + fileName, true)));
            if (marble != null) {
                MarbleInfo m = marble;
                String tmp = m.getSerial() + "," + m.getName() + "," + m.getImgName() + "," + m.getShowIdx() + ","
                        + m.getImgW() + "," + m.getImgH() + "," + m.getRatio() + "," + m.getMass() + ","
                        + m.getV() + "," + m.getAttribute() + "," + m.getLevel() + "," + m.getHp() + ","
                        + m.getAtk() + "," + m.getSkillRound() + "," + m.getState() + ","
                        + m.getSpecies() + "," + m.getSkill();
                bw.append(tmp);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writePlayer(ArrayList<PlayerInfo> origin, String fileName, PlayerInfo players, int idx) {
        try {
            BufferedWriter bw = new BufferedWriter((new FileWriter(Global.FILE_ROOT + fileName)));
            bw.append("序號,角色編號,姓名,等級,怪物編號");
            bw.newLine();
            int[] marbleSerials = players.getMyMarbleSerials();
            String serial = serialToString(marbleSerials);
            String tmp = (idx + 1) + "," + players.playerNum() + "," + players.getName() + "," + players.getLevel() + "," + serial + "\"";
            for (int i = 0; i < origin.size(); i++) {
                if (i == idx) {
                    bw.append(tmp);
                } else {
                    String s = serialToString(origin.get(i).getMyMarbleSerials());
                    String data = (i + 1) + "," + origin.get(i).playerNum() + "," + origin.get(i).getName() + "," + origin.get(i).getLevel() + "," + s + "\"";
                    bw.append(data);
                }
                bw.newLine();
            }
            if (origin.size() <= idx) {
                bw.append(tmp);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String serialToString(int[] marbleSerials) {
        String serial = "\"";
        for (int j = 0; j < marbleSerials.length; j++) {
            serial += marbleSerials[j];
            if (j != marbleSerials.length - 1) {
                serial += ";";
            }
        }
        return serial;
    }

}
