/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.util;

public class Global {

    public static final boolean IS_DEBUG = false;

    //視窗大小
    public static final int FRAME_X = 1280;
    public static final int FRAME_Y = 680;
    public static final int SCREEN_X = FRAME_X - 8 - 8;
    public static final int SCREEN_Y = FRAME_Y - 31 - 8;

    //資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 60; //每秒更新60次
    public static final int MILLISEC_PER_UPDATE = (int) Math.pow(10, 9) / UPDATE_TIMES_PER_SEC; //每更新一次花費的毫秒數

    //畫面更新時間
    public static final int FRAME_LIMIT = 120;
    public static final int LIMIT_DELTA_TIME = (int) Math.pow(10, 9) / FRAME_LIMIT;

    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;

    public static final int NORMAL_MARBLE_R = 50;
    public static final int GENIE_MASS = 1;

    public static final int[] POSITION_X = {random(60,250), random(60,250), random(60,250)};
    public static final int[] POSITION_Y = {170, 340, 510};

    public static final int[] ENEMYPOS_X = {850, 1000, 850};
    public static final int[] ENEMYPOS_Y = {170, 340, 510};
    
    public static int random(int min, int max) {
        if (min > max) {
            int tmp = min;
            min = max;
            max = tmp;
        }
        return (int) (Math.random() * (max - min + 1) + min);
    }

}
