/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import monsterstrike.util.Global;

public class ImgInfo {

    //屬性 0:冰 1:火 2:草

    public static final String[] DEVIL = {"/resources/devil1.png", "/resources/devil2.png"};
    public static final int[] DEVIL_INFO = {136, 136, 40, 1, 20, 1};//img_width, img_height, r, mass, velocity, attribute

    public static final String[] RICEBALL = {"/resources/riceBall1.png", "/resources/riceBall2.png"};
    public static final int[] RICEBALL_INFO = {140, 140, 40, 1, 22, 2};

    public static final String[] SWEETPOTATO = {"/resources/sweetPotato1.png", "/resources/sweetPotato2.png"};
    public static final int[] SWEETPOTATO_INFO = {140, 140, 40, 2, 21, 0};

    public static final String[] ZOMBIE = {"/resources/zombie1.png"};
    public static final int[] ZOMBIE_INFO = {150, 150, 60, 2, 50, 0};

    public static final String[] SKULL = {"/resources/skull1.png"};
    public static final int[] SKULL_INFO = {150, 150, 60, 2, 50, 1};

    public static final String[] SPIKY = {"/resources/spiky1.png"};
    public static final int[] SPIKY_INFO = {150, 150, 60, 2, 50, 2};

    public static final String[] BLUETEETH = {"/resources/teethblue1.png"};
    public static final int[] BLUETEETH_INFO = {150, 150, 60, 2, 50, 0};

//    public static final String[] SPIKY = {"/resources/spiky1.png"};
//    public static final int[] SPIKY_INFO = {150, 150, 60, 2, 50, 2};
    //敵人怪獸
    public static final String[][] ENEMY_PATH = {ZOMBIE, SKULL, SPIKY, DEVIL};
    public static final String[] ENEMY_NAME = {"殭屍", "骷髏頭", "刺刺", "小惡魔"};
    public static final int[][] ENEMY_INFO = {ZOMBIE_INFO, SKULL_INFO, SPIKY_INFO, DEVIL_INFO};

    //屬性光圈
    public static final String GREENGENIE = "/resources/greenGenie1.png";
    public static final int[] GREENGENIE_INFO = {200, 200, 55, 1, 50};//img_width, img_height, r, mass, velocity

    public static final String MUSHROOM = "/resources/mushroom1.png";
    public static final int[] MUSHROOM_INFO = {100, 100, 50, 1, 50};

    public static final String PIG = "/resources/pig1.png";
    public static final int[] PIG_INFO = {130, 130, 50, 2, 50};

    public static final int[] SHINE_INFO = {130, 130, 65, 0, 0};
    public static final String[] SHINE_ICE = {"/resources/ice1.png", "/resources/ice2.png"};
    public static final String[] SHINE_FIRE = {"/resources/fire1.png", "/resources/fire2.png"};
    public static final String[] SHINE_GRASS = {"/resources/grass1.png", "/resources/grass2.png"};

    //背景
    public static final String BACKGROUND_GRASS = "/resources/backgroundGrass1.png";
    public static final int[] BACKGROUND_GRASS_SIZE = {1000, 1000}; //圖片寬高
    public static final String BACKGROUND_TILE = "/resources/backgroundRock.png";
    public static final int[] BACKGROUND_TILE_SIZE = {794, 533};

    public static final String[] BACKGROUND_PATH = {"/resources/backgroundGrass1.png", "/resources/backgroundRock.png"};
    public static final int[][] BACKGROUND_SIZE = {BACKGROUND_GRASS_SIZE, BACKGROUND_TILE_SIZE};

    public static final String ARROW = "/resources/arrow.png";
    public static final int[] ARROW_INFO = {230, 230, 100};

    public static final int BOOM_UNIT_X = 64;
    public static final int BOOM_UNIT_Y = 64;
    public static final String BOOM = "/resources/explode.png";

    //Menu
    public static final String MENU = "/resources/menu.png";
    public static final int[] MENU_INFO = {Global.SCREEN_X, Global.SCREEN_Y};//img_width, img_height
    //Menu 按鈕
    public static final String[] EXIT = {"/resources/exit1.png", "/resources/exit2.png"};
    public static final String[] RANK = {"/resources/rank1.png", "/resources/rank2.png"};
    public static final String[] HOWTOPLAY = {"/resources/howtoplay1.png", "/resources/howtoplay2.png"};
    public static final String[] MULTIPLAYER = {"/resources/multiplayer1.png", "/resources/multiplayer2.png"};
    public static final String[] SINGLE = {"/resources/single1.png", "/resources/single2.png"};
    public static final int[] MAINBUTTON_INFO = {100, 25};//img_width, img_height

    public static final String DINO = "/resources/Dino.png";
    public static final int[] DINO_INFO = {50, 50,};//img_width, img_height

    public static final String[] HOME = {"/resources/home1.png", "/resources/home2.png"};
    public static final String[] SETTING = {"/resources/setting1.png", "/resources/setting2.png"};
    public static final String[] SOUND = {"/resources/turnon.png", "/resources/turnoff.png"};
    public static final String[] CONTROL = {"/resources/start.png", "/resources/stop.png"};
    public static final String RETURN = "/resources/return.png";
    public static final int[] SETTING_INFO = {25, 25};//img_width, img_height   
}
