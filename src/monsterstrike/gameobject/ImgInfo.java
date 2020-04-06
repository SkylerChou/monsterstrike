/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import monsterstrike.util.Global;

public class ImgInfo {

    //屬性 0:冰 1:火 2:草

    private static final String MARBLE_ROOT = "/resources/marbles/";
    
    private static final String[] FIREBALL = {MARBLE_ROOT + "fireBall1.png", MARBLE_ROOT + "fireBall2.png"};
    private static final int[] FIREBALL_INFO = {120, 120, 40, 1, 40, 1};
    private static final String[] RICEBALL = {MARBLE_ROOT + "riceBall1.png", MARBLE_ROOT + "riceBall2.png"};
    private static final int[] RICEBALL_INFO = {120, 120, 40, 1, 40, 2};
    private static final String[] ICEBALL = {MARBLE_ROOT + "iceBall1.png", MARBLE_ROOT + "iceBall2.png"};
    private static final int[] ICEBALL_INFO = {120, 120, 40, 1, 40, 0};
    private static final String[] SWEETPOTATO = {MARBLE_ROOT + "sweetPotato1.png", MARBLE_ROOT + "sweetPotato2.png"};
    private static final int[] SWEETPOTATO_INFO = {120, 120, 40, 2, 40, 0};
    
    private static final String[] DEVIL = {MARBLE_ROOT + "devil1.png", MARBLE_ROOT + "devil2.png"};
    private static final int[] DEVIL_INFO = {120, 120, 40, 1, 40, 1};//img_width, img_height, r, mass, velocity, attribute
    private static final String[] BLACKDEVIL = {MARBLE_ROOT + "blackDevil1.png", MARBLE_ROOT + "blackDevil2.png"};
    private static final int[] BLACKDEVIL_INFO = {120, 120, 40, 1, 40, 1};
    private static final String[] LIMBO = {MARBLE_ROOT + "limbo1.png", MARBLE_ROOT + "limbo2.png"};
    private static final int[] LIMBO_INFO = {150, 150, 40, 1, 40, 1};
    
    private static final String[] SPIKY = {MARBLE_ROOT + "spiky1.png", MARBLE_ROOT + "spiky2.png", MARBLE_ROOT + "spikyDie.png"};
    private static final int[] SPIKY_INFO = {120, 120, 40, 2, 50, 2};   
    private static final String[] SPIKYFIRE = {MARBLE_ROOT + "spikyFire1.png", MARBLE_ROOT + "spikyFire2.png", MARBLE_ROOT + "spikyDie.png"};
    private static final int[] SPIKYFIRE_INFO = {120, 120, 40, 2, 50, 1};
    
    private static final String ENEMY_ROOT = "/resources/marbles/Enemy/";
    private static final String[] ZOMBIE = {ENEMY_ROOT + "zombie1.png", ENEMY_ROOT + "zombie2.png", ENEMY_ROOT + "zombieDie.png"};
    private static final int[] ZOMBIE_INFO = {150, 150, 60, 2, 40, 0};
    private static final String[] BLUEZOMBIE = {ENEMY_ROOT + "blueZombie1.png", ENEMY_ROOT + "blueZombie2.png", ENEMY_ROOT + "zombieDie.png"};
    private static final int[] BLUEZOMBIE_INFO = {150, 150, 60, 2, 40, 0};

    private static final String[] SKULL = {ENEMY_ROOT + "skull1.png", ENEMY_ROOT + "skull2.png", ENEMY_ROOT + "skullDie.png"};
    private static final int[] SKULL_INFO = {150, 150, 60, 2, 50, 1};
    private static final String[] HORNSKULL = {ENEMY_ROOT + "hornSkull1.png", ENEMY_ROOT + "hornSkull2.png", ENEMY_ROOT + "skullDie.png"};
    private static final int[] HORNSKULL_INFO = {150, 150, 60, 2, 50, 1};

    
    
//    private static final String[] BLUETEETH = {MARBLE_ROOT + "teethblue1.png", MARBLE_ROOT + "teethblue2.png"};
//    private static final int[] BLUETEETH_INFO = {145, 145, 50, 2, 50, 0};
//
//    private static final String[] GRAYTEETH = {MARBLE_ROOT + "teethgray1.png", MARBLE_ROOT + "teethgray2.png"};
//    private static final int[] GRAYTEETH_INFO = {145, 145, 50, 2, 50, 0};

    //My怪獸
    public static final String[][] MYMARBLE_PATH = {FIREBALL, RICEBALL, ICEBALL, SWEETPOTATO,DEVIL, BLACKDEVIL, LIMBO, SPIKYFIRE};
    public static final String[] MYMARBLE_NAME = {"火球", "飯糰", "藍水球", "番薯", "小惡魔", "黑惡魔", "蝌蚪", "火刺刺"};
    public static final int[][] MYMARBLE_INFO = {FIREBALL_INFO, RICEBALL_INFO, ICEBALL_INFO, SWEETPOTATO_INFO, DEVIL_INFO,BLACKDEVIL_INFO , LIMBO_INFO, SPIKYFIRE_INFO};
//    , "獨眼藍怪", "綠齒怪", "灰齒怪"

    //敵人怪獸
    public static final String[][] ENEMY_PATH = {ZOMBIE, BLUEZOMBIE, SKULL, HORNSKULL};
    public static final String[] ENEMY_NAME = {"綠殭屍", "藍殭屍", "骷髏頭", "惡魔骷髏"};
    public static final int[][] ENEMY_INFO = {ZOMBIE_INFO, BLUEZOMBIE_INFO, SKULL_INFO, HORNSKULL_INFO};
    
    public static final int DIE_UNIT_X = 150;
    public static final int DIE_UNIT_Y = 150;
    public static final int DIE_NUM = 7;

    //屬性光圈
    public static final int[] SHINE_INFO = {130, 130, 65, 0, 0};
    private static final String[] SHINE_ICE = {MARBLE_ROOT + "ice1.png", MARBLE_ROOT + "ice2.png"};
    private static final String[] SHINE_FIRE = {MARBLE_ROOT + "fire1.png", MARBLE_ROOT + "fire2.png"};
    private static final String[] SHINE_GRASS = {MARBLE_ROOT + "grass1.png", MARBLE_ROOT + "grass2.png"};

    public static final String[][] SHINE_PATH = {SHINE_ICE, SHINE_FIRE, SHINE_GRASS};

    //背景
    private static final String BACKGROUND_ROOT = "/resources/backgrounds/";
    public static final String LEVELBACK_PATH = BACKGROUND_ROOT + "levelBackground.png";
    public static final String GRASS_PATH = BACKGROUND_ROOT + "grass.png";
    public static final int[] GRASS_SIZE = {1000, 1000}; //圖片寬高
    public static final String TILE_PATH = BACKGROUND_ROOT + "rock1.png";
    public static final int[] TILE_SIZE = {1280, 680};
    public static final String ICE_PATH = BACKGROUND_ROOT + "snow.png";
    public static final int[] ICE_SIZE = {1280, 680};
    public static final String FIRE_PATH = BACKGROUND_ROOT + "fire.png";
    public static final int[] FIRE_SIZE = {950, 940};

    public static final String[] BACKGROUND_PATH = {GRASS_PATH, TILE_PATH, ICE_PATH, FIRE_PATH};
    public static final int[][] BACKGROUND_SIZE = {GRASS_SIZE, TILE_SIZE, ICE_SIZE, FIRE_SIZE};

    public static final String ARROW = "/resources/arrow.png";
    public static final int[] ARROW_INFO = {230, 230, 100};

    //Menu
    private static final String MENU_ROOT = "/resources/menu/";
    public static final String MENU = MENU_ROOT + "menu.png";
    public static final int[] MENU_INFO = {Global.SCREEN_X, Global.SCREEN_Y};//img_width, img_height
    //Menu 按鈕
    public static final String[] EXIT = {MENU_ROOT + "exit1.png", MENU_ROOT + "exit2.png"};
    public static final String[] RANK = {MENU_ROOT + "rank1.png", MENU_ROOT + "rank2.png"};
    public static final String[] HOWTOPLAY = {MENU_ROOT + "howtoplay1.png", MENU_ROOT + "howtoplay2.png"};
    public static final String[] MULTIPLAYER = {MENU_ROOT + "multiplayer1.png", MENU_ROOT + "multiplayer2.png"};
    public static final String[] SINGLE = {MENU_ROOT + "single1.png", MENU_ROOT + "single2.png"};
    public static final int[] MAINBUTTON_INFO = {100, 25};//img_width, img_height

    public static final String DINO = MENU_ROOT + "Dino.png";
    public static final int[] DINO_INFO = {50, 50};//img_width, img_height  

    public static final String[] SETTING = {MENU_ROOT + "setting1.png", MENU_ROOT + "setting2.png"};
    public static final String[] SOUND = {MENU_ROOT + "turnon.png", MENU_ROOT + "turnoff.png"};
    public static final String[] CONTROL = {MENU_ROOT + "start.png", MENU_ROOT + "stop.png"};
    public static final String RETURN = MENU_ROOT + "return.png";
    public static final int[] SETTING_INFO = {25, 25};//img_width, img_height   

    //黑洞
    public static final String[] BALCKHOLE={"/resources/blackhole1.png","/resources/blackhole2.png"};
    public static final int[] BLACKHOLE_INFO={200,200,40};//img_width, img_height 
}
