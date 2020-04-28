/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

import monsterstrike.util.Global;
import interfaceskills.*;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.*;
import monsterstrike.graph.Vector;
import monsterstrike.util.Delay;

public class Marble extends GameObject {

    private static final Strike[] SPECIES = {new Rebound(), new Penetrate(), new Stand()};
    protected Vector goVec;
    protected Vector norVec;
    protected Vector tanVec;
    protected Marble other;
    private Marble detect;
    private MarbleInfo info;
    protected Renderer renderer;
    protected Renderer rendererDie;
    protected Renderer rendererHit;
    protected boolean isCollide;
    protected boolean isDie;

    private float moveFic;
    private float strikeFic;
    private float wallFic;
    private SpecialEffect shine;
    private Strike species;
    private float fullBlood;
    private Item[] bloodItem;
    private float bloodRatio;

    private Skills[] skills;
    private int skillIdx;
    private boolean useSkill;
    private boolean isSpin;
    private float centerX;
    private float centerY;
    private Delay spinDelay;
    private int spinCount;
    private boolean spinOver;

    public Marble(int x, int y, int w, int h, MarbleInfo info) {
        super(x, y, w, h, (int) (w * info.getRatio() / 2));
        this.info = info;
        this.goVec = new Vector(0, 0);
        this.norVec = new Vector(0, 0);
        this.tanVec = new Vector(0, 0);
        String path = ImgInfo.MARBLE_ROOT + info.getImgName();
        int num = info.getImgW() / info.getImgH();
        this.fullBlood = this.info.getHp();
        this.bloodItem = new Item[2];
        this.bloodItem[0] = new Item(ImgInfo.BLOODS_PATH[0], x, (int) (y - info.getR()), ImgInfo.BLOODS_INFO[0], ImgInfo.BLOODS_INFO[1]);
        this.bloodItem[1] = new Item(ImgInfo.BLOODS_PATH[1], x, (int) (y - info.getR()), ImgInfo.BLOODS_INFO[0], ImgInfo.BLOODS_INFO[1]);
        this.renderer = new Renderer(path + ".png", num, 20);
        this.rendererHit = new Renderer(path + ".png", num, 20);
        this.rendererDie = new Renderer(path + "Die.png", 7, 10);
        int[] shineSize = {x, y, w - 10, h - 10};
        if (info.getAttribute() > 2) {
            shineSize[0] = (int) (x - 0.25f * w);
            shineSize[1] = (int) (y - 0.25f * h);
            shineSize[2] = (int) (1.5f * w);
            shineSize[3] = (int) (1.5f * h);
        }
        this.shine = new SpecialEffect(ImgInfo.SHINE_PATH[info.getAttribute()],
                shineSize[0], shineSize[1], shineSize[2], shineSize[3],
                (int) (shineSize[3] * info.getRatio() / 2));
        this.isCollide = false;
        this.skills = new Skills[]{new Explosion(), new Tornado(), new Laser(),
            new Bullet(), new Heal(), new Missile()};
        this.skillIdx = 0;
        this.useSkill = true;
        this.moveFic = 0.05f * info.getMass();
        this.strikeFic = 3;
        this.wallFic = 2;
        this.isDie = false;
        this.isSpin = false;
        this.spinCount = 0;
        this.spinDelay = new Delay(0);
        this.spinDelay.start();
        this.spinOver = false;
        if (info.getState() == 1) {
            this.species = SPECIES[2];
        } else {
            this.species = SPECIES[info.getSpecies()];
        }
    }

    @Override
    public void update() {
        if (!this.isSpin()) {
            this.species.update(this);
        }
        if (this.detect != null && (this.goVec.getValue()!= 0 && this.isBound())) {
            this.goVec.setValue(this.goVec.getValue() - this.wallFic);
        }
        this.skills[this.skillIdx].update();
        for (int i = 0; i < 2; i++) {
            this.bloodItem[i].setCenterX(this.getCenterX());
            this.bloodItem[i].setCenterY(this.getCenterY() - this.getR() - 10);
        }
        this.bloodRatio = this.info.getHp() / this.fullBlood;
        if (this.isSpin) {
            spin(centerX, centerY);
        }
    }

    public void move() {
        this.isCollide = false;
        if (this.goVec.getValue() > 0) {
            this.goVec.setValue(this.goVec.getValue() - this.moveFic);
            if (this.goVec.getValue() <= 0) {
                this.goVec.setValue(0);
            }
        }
        this.offset(this.goVec.getX(), this.goVec.getY());
    }

    public void strike(Marble other) {
        this.isCollide = true;
        this.other = other;
        Vector nor = new Vector(other.getCenterX() - this.getCenterX(), other.getCenterY() - this.getCenterY());
        if (nor.getValue() != 0) {
            updateDir(nor);
        }
    }

    private void updateDir(Vector nor) {
        this.norVec = this.goVec.getCosProjectionVec(nor);
        this.tanVec = this.goVec.getSinProjectionVec(nor);
        this.other.setNorVec(this.other.goVec().getCosProjectionVec(nor.multiplyScalar(-1)));
        this.other.setTanVec(this.other.goVec().getSinProjectionVec(nor.multiplyScalar(-1)));
        float myM = this.getInfo().getMass();
        float enyM = this.other.getInfo().getMass();
        float m11 = (myM - enyM) / (myM + enyM);
        float m12 = (2 * enyM) / (myM + enyM);
        float m21 = (2 * myM) / (myM + enyM);
        float m22 = (enyM - myM) / (myM + enyM);
        Vector newNor1 = this.norVec.multiplyScalar(m11).plus(this.other.norVec().multiplyScalar(m12));
        Vector newNor2 = this.norVec.multiplyScalar(m21).plus(this.other.norVec().multiplyScalar(m22));
        this.norVec = newNor1;
        this.goVec = this.norVec.plus(this.tanVec);
        this.other.setNorVec(newNor2);
        this.other.setGo(this.other.norVec().plus(this.other.tanVec()));
    }

    public void die() {
        this.isDie = true;
    }

    public boolean getIsDie() {
        return this.isDie;
    }

    public void setIsCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }

    public boolean getIsCollide() {
        return this.isCollide;
    }

    public Renderer getRenderer() {
        return this.renderer;
    }

    public Renderer getDieRenderer() {
        return this.rendererDie;
    }

    public void updateShine() {
        this.shine.setCenterX(this.getCenterX());
        this.shine.setCenterY(this.getCenterY());
        this.shine.update();
    }

    public void spin(float x, float y) {
        this.centerX = x;
        this.centerY = y;
        if (this.isSpin && this.spinDelay.isTrig()) {
            float rad = (float) (Math.toRadians(360 / 60) * spinCount);
            float r2 = 0.5f * this.getR();
            float dx = (float) (2 * r2 * Math.cos((Math.PI - rad) / 2) * Math.cos(rad / 2));
            float dy = (float) (2 * r2 * Math.cos((Math.PI - rad) / 2) * Math.sin(rad / 2));
            this.setCenterX(x + dx);
            this.setCenterY(y - r2 + dy);
            this.spinCount++;
            if (spinCount >= 60) {
                spinCount = 0;
                this.isSpin = false;
                this.spinOver = true;
            }
        }
    }

    public boolean spinOver() {
        return this.spinOver;
    }

    public void setSpinOver(boolean spinOver) {
        this.spinOver = spinOver;
    }

    public void setSpin(boolean isSpin) {
        this.isSpin = isSpin;
    }

    public boolean isSpin() {
        return this.isSpin;
    }

    public boolean isBound() {
        if (this.detect.getCenterX() - this.getR() <= 0
                || this.detect.getCenterX() + this.getR() >= Global.SCREEN_X
                || this.detect.getCenterY() - this.getR() <= 0
                || this.detect.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
            if (this.detect.getCenterX() - this.getR() <= 0) {
                this.setCenterX(this.getR());
                this.goVec.setX(-this.goVec.getX());
            }
            if (this.detect.getCenterX() + this.getR() >= Global.SCREEN_X) {
                this.setCenterX(Global.SCREEN_X - this.getR());
                this.goVec.setX(-this.goVec.getX());
            }
            if (this.detect.getCenterY() - this.getR() <= 0) {
                this.setCenterY(this.getR());
                this.goVec.setY(-this.goVec.getY());
            }
            if (this.detect.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
                this.setCenterY(Global.SCREEN_Y - Global.INFO_H - this.getR());
                this.goVec.setY(-this.goVec.getY());
            }
            return true;
        }
        return false;
    }

    public void detect(Marble target) {
        float dist = this.getDetect().dist(target.getDetect());
        Vector v1 = this.goVec;
        Vector v2 = target.goVec;
        float x1 = this.getCenterX();
        float y1 = this.getCenterY();
        float x2 = target.getCenterX();
        float y2 = target.getCenterY();
        Vector vec = new Vector(target.getDetect().getCenterX() - this.getCenterX(),
                target.getDetect().getCenterY() - this.getCenterY());
        float x = this.getCenterX() + vec.getCosProjectionVec(this.goVec).getX();
        float y = this.getCenterY() + vec.getCosProjectionVec(this.goVec).getY();
        if (dist < this.getR() + target.getR() || inMiddle(this, x, y)) {
            float a = (float) (Math.pow(v1.getValue(), 2) + Math.pow(v2.getValue(), 2)
                    - 2 * (v1.getX() * v2.getX() + v1.getY() * v2.getY()));
            float b = 2 * ((x1 - x2) * (v1.getX() - v2.getX()) + (y1 - y2) * (v1.getY() - v2.getY()));
            float c = (float) (Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)
                    - Math.pow(this.getR() + target.getR(), 2));
            float d = (float) ((-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
            if (Math.pow(b, 2) - 4 * a * c >= 0 && a > 0) {
                this.offset(this.goVec.getX() * d, this.goVec.getY() * d);
                target.offset(target.goVec.getX() * d, target.goVec.getY() * d);
            }
        }
    }

    public void detectStill(GameObject target) {
        if (this.goVec.getValue() > 0) {
            float dist = this.getDetect().dist(target);
            Vector v1 = this.goVec;
            float x1 = this.getCenterX();
            float y1 = this.getCenterY();
            float x2 = target.getCenterX();
            float y2 = target.getCenterY();
            Vector vec = new Vector(target.getCenterX() - this.getCenterX(),
                    target.getCenterY() - this.getCenterY());
            float x = this.getCenterX() + vec.getCosProjectionVec(this.goVec).getX();
            float y = this.getCenterY() + vec.getCosProjectionVec(this.goVec).getY();
            if (dist < this.getR() + target.getR() || inMiddle(this, x, y)) {
                float a = (float) (Math.pow(v1.getValue(), 2));
                float b = 2 * ((x1 - x2) * (v1.getX()) + (y1 - y2) * (v1.getY()));
                float c = (float) (Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)
                        - Math.pow(this.getR() + target.getR(), 2));
                float d = (float) ((-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                if (Math.pow(b, 2) - 4 * a * c >= 0 && a > 0) {
                    this.offset(this.goVec.getX() * d, this.goVec.getY() * d);
                }
            }
        }
    }

    public float dist(GameObject target) {
        float dist = (float) Math.sqrt(Math.pow((target.getCenterX() - this.getCenterX()), 2)
                + Math.pow((target.getCenterY() - this.getCenterY()), 2));
        return dist;
    }

    private boolean inMiddle(Marble self, float x, float y) {
        float minX = self.getCenterX();
        float maxX = self.getDetect().getCenterX();
        float minY = self.getCenterY();
        float maxY = self.getDetect().getCenterY();
        if (self.getDetect().getCenterX() < self.getCenterX()) {
            minX = self.getDetect().getCenterX();
            maxX = self.getCenterX();
        }
        if (self.getDetect().getCenterY() < self.getCenterY()) {
            minY = self.getDetect().getCenterY();
            maxY = self.getCenterY();
        }
        if (x > minX && x < maxX && y > minY && y < maxY) {
            return true;
        }
        return false;
    }

    public void hit(GameObject target) {
        this.isCollide = true;
        Vector vec = new Vector(target.getCenterX() - this.getCenterX(), target.getCenterY() - this.getCenterY());
        if (this.goVec.getValue() > 0) {
            this.norVec = this.goVec.getCosProjectionVec(vec).multiplyScalar(-1);
            this.tanVec = this.goVec.getSinProjectionVec(vec);
            this.goVec = this.norVec.plus(this.tanVec);
        }
    }

    public Marble getDetect() {
        return this.detect;
    }

    public void setDetect(Marble detect) {
        this.detect = detect;
    }

    public void setGo(Vector go) {
        this.goVec = go;
    }

    public Vector goVec() {
        return this.goVec;
    }

    public Vector norVec() {
        return this.norVec;
    }

    public Vector tanVec() {
        return this.tanVec;
    }

    public void setNorVec(Vector norVec) {
        this.norVec = norVec;
    }

    public void setTanVec(Vector tanVec) {
        this.tanVec = tanVec;
    }

    public void setVelocity(float ratio) {
        this.goVec.multiplyScalar(ratio);
    }

    public void setShine(boolean isShine) {
        this.shine.setShine(isShine);
    }

    public float getStrikeFic() {
        return this.strikeFic;
    }

    public float getMoveFic() {
        return this.moveFic;
    }

    public Marble duplicate(int x, int y, int w, int h) {
        MarbleInfo copyInfo = MarbleInfo.gen(this.info);
        return new Marble(x, y, w, h, copyInfo);
    }

    public Marble duplicate() {
        return this.duplicate((int) this.getCenterX(), (int) this.getCenterY(),
                (int) this.getWidth(), (int) this.getHeight());
    }

    public MarbleInfo getInfo() {
        return this.info;
    }

    public Skills getSkills() {
        return this.skills[this.skillIdx];
    }

    public boolean getUseSkill() {
        return this.useSkill;
    }

    public void setUseSkill(Boolean useSkill) {
        this.useSkill = useSkill;
    }

    public int useSkill(int skillIdx, ArrayList<Marble> target, int targetIdx) {
        this.skillIdx = skillIdx;
        return this.skills[skillIdx].useSkill(this, target, targetIdx);
    }

    public int explode(GameObject target) {
        return this.skills[0].explode(this, target);
    }

    public void paintSkill(Graphics g) {
        this.skills[this.skillIdx].paintSkill(g);
    }

    public void paintShine(Graphics g) {
        this.shine.paintComponent(g);
    }

    public void paintAll(Graphics g) {
        this.shine.paintComponent(g);
        if (this.skillIdx == 4) {
            paintSkill(g);
            paintComponent(g);
        } else {
            paintComponent(g);
            paintSkill(g);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (!this.isDie) {
            if (this.rendererHit.getIsCollide()) {
                this.rendererHit.paint(g, (int) (this.getX()), (int) (this.getY()),
                        (int) (this.getWidth()), (int) (this.getHeight()),
                        ImgInfo.MARBLE_UNIT_X, ImgInfo.MARBLE_UNIT_Y);
            } else {
                this.renderer.paint(g, (int) (this.getX()), (int) (this.getY()),
                        (int) (this.getWidth()), (int) (this.getHeight()),
                        ImgInfo.MARBLE_UNIT_X, ImgInfo.MARBLE_UNIT_Y);
            }
            if (info.getState() == 1) {
                this.bloodItem[0].paint(g);
                this.bloodItem[1].paintResize(g, bloodRatio);
            }
        } else {
            this.rendererDie.paint(g, (int) (this.getX()), (int) (this.getY()),
                    (int) (this.getWidth()), (int) (this.getHeight()),
                    ImgInfo.MARBLE_UNIT_X, ImgInfo.MARBLE_UNIT_Y);
        }

        if (Global.IS_DEBUG) {
            g.drawOval((int) (this.getCenterX() - this.getR()),
                    (int) (this.getCenterY() - this.getR()),
                    (int) (2 * this.getR()), (int) (2 * this.getR()));
        }
    }
}
