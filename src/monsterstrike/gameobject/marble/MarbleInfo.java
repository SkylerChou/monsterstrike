/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

public class MarbleInfo {

    private static final String[] ATTRIBUTE = {"冰", "火", "草", "光", "黯"};
    private static final String[] STATE = {"收服", "野生"};
    private static final String[] SPECIES = {"反彈", "貫穿"};
    private int serial;
    private String name;
    private String imgName;
    private int showIdx;
    private int imgW;
    private int imgH;
    private float r;
    private float mass;
    private float v;
    private int attribute;
    private int skillRound;
    private int state;
    private int level;
    private int hp;
    private int atk;
    private int species;

    public MarbleInfo(int serial, String name, String imgName, int showIdx, int imgW, int imgH, float ratio,
            float mass, float v, int attribute, int level, int hp,
            int atk, int skillRound, int state, int species) {
        this.serial = serial;
        this.name = name;
        this.imgName = imgName;
        this.showIdx = showIdx;
        this.imgH = imgH;
        this.imgW = imgW;
        this.r = imgH * ratio / 2;
        this.mass = mass;
        this.v = v;
        this.attribute = attribute;
        this.skillRound = skillRound;
        this.state = state;
        this.level = level;
        this.atk = atk;
        this.hp = hp;
        this.species = species;
    }

    public static MarbleInfo gen(MarbleInfo info) {
        return new MarbleInfo(info.getSerial(), info.getName(), info.getImgName(), info.getShowIdx(), info.getImgW(), info.getImgH(),
                info.getRatio(), info.getMass(), info.getV(), info.getAttribute(), info.getLevel(),
                info.getHp(), info.getAtk(), info.getSkillRound(), info.getState(), info.getSpecies());
    }

    public int getSerial() {
        return this.serial;
    }

    public String getName() {
        return this.name;
    }

    public String getImgName() {
        return this.imgName;
    }

    public int getShowIdx() {
        return this.showIdx;
    }

    public int getImgH() {
        return this.imgH;
    }

    public int getImgW() {
        return this.imgW;
    }

    public float getRatio() {
        return 2 * this.r / this.imgH;
    }

    public float getR() {
        return this.r;
    }

    public float getMass() {
        return this.mass;
    }

    public float getV() {
        return this.v;
    }

    public int getAttribute() {
        return this.attribute;
    }

    public int getSkillRound() {
        return this.skillRound;
    }

    public int getState() {
        return this.state;
    }

    public int getLevel() {
        return this.level;
    }

    public int getHp() {
        return this.hp;
    }

    public int getAtk() {
        return this.atk;
    }

    public int getSpecies() {
        return this.species;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            hp = 0;
        }
        this.hp = hp;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.name + " 屬性:" + ATTRIBUTE[this.attribute]
                + " 速度:" + this.v + "(m/s)"
                + " HP:" + this.hp + " ATK:" + this.atk
                + " 等級:" + this.level
                + " Strike:" + SPECIES[this.species]
                + " 狀態:" + STATE[this.state];
    }
}
