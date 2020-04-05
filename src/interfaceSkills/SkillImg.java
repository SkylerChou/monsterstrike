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

    public static final String[][] SKILL_PATH = {EXPLODE_PATH, TORNADO_PATH, LASER_PATH};
    public static final int[][] SKILL_UNIT_X = {EXPLODE_UNIT_X, TORNADO_UNIT_X, LASER_UNIT_X};
    public static final int[][] SKILL_UNIT_Y = {EXPLODE_UNIT_Y, TORNADO_UNIT_Y, LASER_UNIT_Y};
    public static final int[][] SKILL_NUM = {EXPLODE_NUM, TORNADO_NUM, LASER_NUM};
}
