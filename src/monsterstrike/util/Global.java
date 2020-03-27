/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.util;

public class Global {
    
    public static final boolean IS_DEBUG = false;
    
    //視窗大小
    public static final int FRAME_X = 800;
    public static final int FRAME_Y = 600;
    public static final int SCREAN_X = FRAME_X - 8 - 8;
    public static final int SCREAN_Y = FRAME_Y - 31 - 8;

    //資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 120; //每秒更新60次
    public static final int MILLISEC_PER_UPDATE = 1000 / UPDATE_TIMES_PER_SEC; //每更新一次花費的毫秒數

    //畫面更新時間
    public static final int FRAME_LIMIT = 120;
    public static final int LIMIT_DELTA_TIME = 1000 / FRAME_LIMIT;
    
    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    
    public static final int NORMAL_MARBLE_R = 50;
    public static final int GENIE_MASS = 1;
    
    public static final int[] POSITION_X = {100, 700, 400};
    public static final int[] POSITION_Y = {100, 100, 500};
       
}
