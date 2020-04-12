/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

public class SkillImg {

    private static final String SKILL_ROOT = "/resources/skills/";
    
    private static final String[] EXPLODE_PATH = {SKILL_ROOT + "drop.png", SKILL_ROOT + "explode.png", SKILL_ROOT + "explodeGrass.png"};
    private static final int[] EXPLODE_UNIT_X = {77, 77, 77};
    private static final int[] EXPLODE_UNIT_Y = {77, 77, 77};
    private static final int[] EXPLODE_NUM = {9, 6, 6};

    private static final String[] TORNADO_PATH = {SKILL_ROOT + "spray.png", SKILL_ROOT + "fireTornado.png", SKILL_ROOT + "tornado.png"};
    private static final int[] TORNADO_UNIT_X = {94, 150, 112};
    private static final int[] TORNADO_UNIT_Y = {150, 150, 150};
    private static final int[] TORNADO_NUM = {10, 11, 9};
    
    private static final String[] LASER_PATH = {SKILL_ROOT + "blueBeam.png", SKILL_ROOT + "redBeam.png", SKILL_ROOT + "greenBeam.png"};
    private static final int[] LASER_UNIT_X = {150, 150, 150};
    private static final int[] LASER_UNIT_Y = {1300, 1300, 1300};
    private static final int[] LASER_NUM = {4, 4, 4};
    
    private static final String[] BULLET_PATH = {SKILL_ROOT + "iceBullet.png", SKILL_ROOT + "fireBullet.png", SKILL_ROOT + "grassBullet.png"};
    private static final int[] BULLET_UNIT_X = {161, 161, 161};
    private static final int[] BULLET_UNIT_Y = {161, 161, 161};
    private static final int[] BULLET_NUM = {6, 6, 6};
    
    private static final String[] MISSILE_PATH = {SKILL_ROOT + "iceMissile.png", SKILL_ROOT + "fireMissile.png", SKILL_ROOT + "grassMissile.png"};
    private static final int[] MISSILE_UNIT_X = {800, 800, 800};
    private static final int[] MISSILE_UNIT_Y = {800, 800, 800};
    private static final int[] MISSILE_NUM = {3, 3, 3};

    public static final String[][] SKILL_PATH = {EXPLODE_PATH, TORNADO_PATH, LASER_PATH, BULLET_PATH, MISSILE_PATH};
    public static final int[][] SKILL_UNIT_X = {EXPLODE_UNIT_X, TORNADO_UNIT_X, LASER_UNIT_X, BULLET_UNIT_X, MISSILE_UNIT_X};
    public static final int[][] SKILL_UNIT_Y = {EXPLODE_UNIT_Y, TORNADO_UNIT_Y, LASER_UNIT_Y, BULLET_UNIT_Y, MISSILE_UNIT_Y};
    public static final int[][] SKILL_NUM = {EXPLODE_NUM, TORNADO_NUM, LASER_NUM, BULLET_NUM, MISSILE_NUM};
}
