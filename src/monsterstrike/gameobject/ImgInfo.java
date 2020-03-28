/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import monsterstrike.util.Global;

public class ImgInfo {

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

    public static final String BACKGROUND = "/resources/background2.png";
    public static final String ARROW = "/resources/arrow.png";
    public static final int[] ARROW_INFO = {200, 200, 100};
    //Menu
    public static final String MENU = "/resources/menu.png";
    public static final int[] MENU_INFO = {Global.SCREEN_X, Global.SCREEN_Y};//img_width, img_height
    //Menu 按鈕
    public static final String[] EXIT = {"/resources/exit1.png", "/resources/exit2.png"};
    public static final String[] HOWTOPLAY = {"/resources/howtoplay1.png", "/resources/howtoplay2.png"};
    public static final String[] MULTIPLAYER = {"/resources/multiplayer1.png", "/resources/multiplayer2.png"};
    public static final String[] SINGLE = {"/resources/single1.png", "/resources/single2.png"};
    public static final int[] MAINBUTTON_INFO = {100, 25};//img_width, img_height
    
    public static final String DINO = "/resources/Dino.png";
    
    public static final String[] SETTING = {"/resources/setting1.png", "/resources/setting2.png"};
    public static final String[] SOUND = {"/resources/turnon.png", "/resources/turnoff.png"};
    public static final String[] CONTROL = {"/resources/start.png", "/resources/stop.png"};
    public static final String RETURN = "/resources/return.png";
    public static final int[] SETTING_INFO = {25, 25};//img_width, img_height   
}
