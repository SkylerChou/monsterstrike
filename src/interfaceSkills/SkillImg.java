/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

public class SkillImg {

    private static final String SKILL_ROOT = "/resources/skills/";

    private static final String[] EXPLODE_PATH = {SKILL_ROOT + "explodeIce.png", SKILL_ROOT + "explode.png", SKILL_ROOT + "explodeGrass.png",
        SKILL_ROOT + "wind.png", SKILL_ROOT + "explodeDark.png"};
    private static final int[] EXPLODE_UNIT_X = {77, 77, 77, 198, 77};
    private static final int[] EXPLODE_UNIT_Y = {77, 77, 77, 198, 77};
    private static final int[] EXPLODE_NUM = {6, 6, 6, 10, 6};

    private static final String[] TORNADO_PATH = {SKILL_ROOT + "spray.png", SKILL_ROOT + "fireTornado.png", SKILL_ROOT + "ground.png",
        SKILL_ROOT + "tornado.png", SKILL_ROOT + "darkStone.png"};
    private static final int[] TORNADO_UNIT_X = {94, 150, 190, 112, 198};
    private static final int[] TORNADO_UNIT_Y = {150, 150, 190, 150, 198};
    private static final int[] TORNADO_NUM = {10, 11, 9, 9, 9};

    private static final String[] LASER_PATH = {SKILL_ROOT + "blueBeam.png", SKILL_ROOT + "redBeam.png", SKILL_ROOT + "greenBeam.png",
        SKILL_ROOT + "lightBeam.png", SKILL_ROOT + "darkBeam.png"};
    private static final int[] LASER_UNIT_X = {150, 150, 150, 150, 150};
    private static final int[] LASER_UNIT_Y = {1300, 1300, 1300, 1300, 1300};
    private static final int[] LASER_NUM = {4, 4, 4, 4, 4};

    private static final String[] BULLET_PATH = {SKILL_ROOT + "iceBullet.png", SKILL_ROOT + "fireBullet.png", SKILL_ROOT + "grassBullet.png",
        SKILL_ROOT + "lightBullet.png", SKILL_ROOT + "darkBullet.png"};
    private static final int[] BULLET_UNIT_X = {161, 161, 161, 161, 161};
    private static final int[] BULLET_UNIT_Y = {161, 161, 161, 161, 161};
    private static final int[] BULLET_NUM = {6, 6, 6, 6, 6};

    private static final String[] MISSILE_PATH = {SKILL_ROOT + "iceMissile.png", SKILL_ROOT + "fireMissile.png", SKILL_ROOT + "grassMissile.png",
        SKILL_ROOT + "lightMissile.png", SKILL_ROOT + "darkMissile.png"};
    private static final int[] MISSILE_UNIT_X = {800, 800, 800, 800, 800};
    private static final int[] MISSILE_UNIT_Y = {800, 800, 800, 800, 800};
    private static final int[] MISSILE_NUM = {3, 3, 3, 3, 3};

    private static final String[] HEAL_PATH = {SKILL_ROOT + "stars.png", SKILL_ROOT + "stars.png", SKILL_ROOT + "stars.png",
        SKILL_ROOT + "stars.png", SKILL_ROOT + "stars.png"};
    private static final int[] HEAL_UNIT_X = {354, 354, 354, 354, 354};
    private static final int[] HEAL_UNIT_Y = {354, 354, 354, 354, 354};
    private static final int[] HEAL_NUM = {17, 17, 17, 17, 17};

    public static final String[][] SKILL_PATH = {EXPLODE_PATH, TORNADO_PATH, LASER_PATH, BULLET_PATH, HEAL_PATH, MISSILE_PATH};
    public static final int[][] SKILL_UNIT_X = {EXPLODE_UNIT_X, TORNADO_UNIT_X, LASER_UNIT_X, BULLET_UNIT_X, HEAL_UNIT_X, MISSILE_UNIT_X};
    public static final int[][] SKILL_UNIT_Y = {EXPLODE_UNIT_Y, TORNADO_UNIT_Y, LASER_UNIT_Y, BULLET_UNIT_Y, HEAL_UNIT_Y, MISSILE_UNIT_Y};
    public static final int[][] SKILL_NUM = {EXPLODE_NUM, TORNADO_NUM, LASER_NUM, BULLET_NUM, HEAL_NUM, MISSILE_NUM};
}
