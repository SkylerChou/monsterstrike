/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

public class SkillImg {
    public static final String[] EXPLODE_PATH = {"/resources/drop.png", "/resources/explode.png", "/resources/explodeGrass.png"};
    public static final int[] EXPLODE_UNIT = {77, 77};
    public static final int[] EXPLODE_NUM = {9,6,6};
    
    private static final String[] FIRETORNADO_PATH = {"/resources/fireTornado.png"};
    private static final int[] FIRETORNADO_UNIT = {124, 124};
    private static final int[] FIRETORNADO_NUM = {11};
    private static final String[] SPRAY_PATH = {"/resources/spray.png"};
    private static final int[] SPRAY_UNIT = {77,124};
    private static final int[] SPRAY_NUM = {10};
    
    
    public static final String[][] SKILL_PATH = {EXPLODE_PATH, SPRAY_PATH, FIRETORNADO_PATH};
    public static final int[][] SKILL_UNIT = {EXPLODE_UNIT, SPRAY_UNIT, FIRETORNADO_UNIT};
    public static final int[][] SKILL_NUM = {EXPLODE_NUM, SPRAY_NUM, FIRETORNADO_NUM};
}
