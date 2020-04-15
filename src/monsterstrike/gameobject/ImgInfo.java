/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import monsterstrike.util.Global;

public class ImgInfo {

    //屬性 0:冰 1:火 2:草
    public static final String MARBLE_ROOT = "/resources/marbles/";

    public static final int MARBLE_UNIT_X = 615;
    public static final int MARBLE_UNIT_Y = 615;
    public static final int DIE_NUM = 7;

    //屬性光圈
    public static final int[] SHINE_INFO = {130, 130, 65, 0, 0};
    private static final String[] SHINE_ICE = {MARBLE_ROOT + "ice1.png", MARBLE_ROOT + "ice2.png"};
    private static final String[] SHINE_FIRE = {MARBLE_ROOT + "fire1.png", MARBLE_ROOT + "fire2.png"};
    private static final String[] SHINE_GRASS = {MARBLE_ROOT + "grass1.png", MARBLE_ROOT + "grass2.png"};
    private static final String[] SHINE_DARK = {MARBLE_ROOT + "dark1.png", MARBLE_ROOT + "dark2.png"};
    private static final String[] SHINE_LIGHT = {MARBLE_ROOT + "light1.png", MARBLE_ROOT + "light2.png"};

    public static final String[][] SHINE_PATH = {SHINE_ICE, SHINE_FIRE, SHINE_GRASS, SHINE_LIGHT, SHINE_DARK};

    //黑洞
    public static final String[] BALCKHOLE = {"/resources/blackhole1.png", "/resources/blackhole2.png"};
    public static final int[] BLACKHOLE_INFO = {200, 200};//img_width, img_height 

    //背景
    private static final String BACKGROUND_ROOT = "/resources/backgrounds/";
    public static final String LEVELBACK_PATH = BACKGROUND_ROOT + "levelBackground.png";
    private static final String GRASS_PATH = BACKGROUND_ROOT + "grass.png";
    private static final int[] GRASS_SIZE = {1000, 1000}; //圖片寬高
    private static final String ICE_PATH = BACKGROUND_ROOT + "snow.png";
    private static final int[] ICE_SIZE = {2000, 1063};
    private static final String FIRE_PATH = BACKGROUND_ROOT + "fire.png";
    private static final int[] FIRE_SIZE = {949, 949};
    private static final String DARK_PATH = BACKGROUND_ROOT + "dark.png";
    private static final int[] DARK_SIZE = {3001, 1093};
    private static final String LIGHT_PATH = BACKGROUND_ROOT + "light.png";
    private static final int[] LIGHT_SIZE = {1920, 800};

    public static final String[] BACKGROUND_PATH = {ICE_PATH, FIRE_PATH, GRASS_PATH, LIGHT_PATH, DARK_PATH};
    public static final int[][] BACKGROUND_SIZE = {ICE_SIZE, FIRE_SIZE, GRASS_SIZE, LIGHT_SIZE, DARK_SIZE};

    //ITEM
    private static final String ITEM_ROOT = "/resources/items/";
    public static final String INFOFORM_PATH = ITEM_ROOT + "infoForm.png";

    //資訊欄血條
    public static final String BLOOD_PATH = ITEM_ROOT + "blood.png";
    public static final int[] BLOOD_INFO = {Global.SCREEN_X - 361, Global.SCREEN_Y - 113, 665, 23}; //centerX, centerY, w, h
    
    //怪物血條
    public static final String[] BLOODS_PATH = {ITEM_ROOT + "bloodRed.png", ITEM_ROOT + "blood.png"};
    public static final int[] BLOODS_INFO = {100, 10}; //w, h
   
    public static final String ARROW = "/resources/arrow.png";
    public static final int[] ARROW_INFO = {230, 230, 100};

    //LEVEL MENU 按鈕
    public static final String[] RIGHT = {ITEM_ROOT + "right1.png", ITEM_ROOT + "right2.png"};
    public static final String[] LEFT = {ITEM_ROOT + "left1.png", ITEM_ROOT + "left2.png"};
    public static final int[] CHOOSEBUTTON_INFO = {50, 50};

    //Menu
    private static final String MENU_ROOT = "/resources/menu/";
    public static final String MENU = MENU_ROOT + "menu.png";
    public static final int[] MENU_INFO = {Global.SCREEN_X, Global.SCREEN_Y};//img_width, img_height
    //Menu 按鈕
    public static final String[] EXIT = {MENU_ROOT + "exit1.png", MENU_ROOT + "exit2.png"};
    public static final String[] RANK = {MENU_ROOT + "rank1.png", MENU_ROOT + "rank2.png"};
    public static final String[] CONTINUE = {MENU_ROOT + "continue1.png", MENU_ROOT + "continue2.png"};
    public static final String[] PINBALL = {MENU_ROOT + "pinball1.png", MENU_ROOT + "pinball2.png"};
    public static final String[] SINGLE = {MENU_ROOT + "single1.png", MENU_ROOT + "single2.png"};
    public static final int[] MAINBUTTON_INFO = {200, 50};//img_width, img_height
    //恐龍
    public static final String DINO = MENU_ROOT + "Dino.png";
    public static final int[] DINO_INFO = {50, 50};//img_width, img_height  
    //彈珠台恐龍
    public static final String[] GREENDINO = {MARBLE_ROOT + "GreenDinoRight.png", MARBLE_ROOT + "GreenDinoLeft.png"};
    public static final int[] GREENDINO_INFO = {60, 60};//img_width, img_height  
    
    //動作按鈕
    public static final String[] SETTING = {MENU_ROOT + "setting1.png", MENU_ROOT + "setting2.png"};
    public static final String[] SOUND = {MENU_ROOT + "turnon.png", MENU_ROOT + "turnoff.png"};
    public static final String[] CONTROL = {MENU_ROOT + "start.png", MENU_ROOT + "stop.png"};
    public static final String[] HOME ={ MENU_ROOT + "home1.png",MENU_ROOT +"home2.png"};
    public static final int[] SETTING_INFO = {50, 50};//img_width, img_height   

    //PingPong用
    private static final String[] FIREBALL = {MARBLE_ROOT + "fireBall1.png", MARBLE_ROOT + "fireBall2.png"};
    private static final int[] FIREBALL_INFO = {90, 90, 40, 1, 30, 1};
    public static final String[][] MYMARBLE_PATH = {FIREBALL};
    public static final String[] MYMARBLE_NAME = {"火球"};
    public static final int[][] MYMARBLE_INFO = {FIREBALL_INFO};

    public static final String POSTS_PATH = MARBLE_ROOT + "mushroom.png";
    public static final String[] POSTS2_PATH = {MARBLE_ROOT + "mushroom1.png", MARBLE_ROOT + "mushroom2.png"};
    public static final int[] POST_INFO = {95, 95, 40, 2};

    public static final String PINGPONG = BACKGROUND_ROOT + "battleback6.png";

    //Props 
    private static final String PROPS_ROOT = "/resources/props/";
    public static final String HEART = PROPS_ROOT + "heart.png";
    public static final int HEART_NUM = 7;
    public static final String SHOE = PROPS_ROOT + "shoe.png";
    public static final int SHOE_NUM = 4;
    public static final String[] BOOSTER = {PROPS_ROOT + "Booster1.png", PROPS_ROOT + "Booster2.png"};
    public static final int[] PROPS_INFO = {80, 80, 40};

}
