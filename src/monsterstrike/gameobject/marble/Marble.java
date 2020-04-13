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

public class Marble extends Ball {

    private static final Strike[] SPECIES = {new Rebound(), new Penetrate(), new Stand()};
    protected Marble other;
    private Marble detect;
    protected MarbleInfo info;
    protected MarbleRenderer renderer;
    protected boolean isCollide;

    private float moveFic;
    private float strikeFic;
    private float wallFic;
    private SpecialEffect shine;
    private Strike species;
    private boolean isDie;

    private Skills[] skills;
    private int skillIdx;
    private boolean useSkill;

    public Marble(int x, int y, int w, int h, MarbleInfo info) {
        super(x, y, w, h, (int) (w * info.getRatio() / 2));
        this.info = info;
        String path = ImgInfo.MARBLE_ROOT + info.getImgName() + ".png";
        int num = info.getImgW() / info.getImgH();
        this.renderer = new MarbleRenderer(path, num, 20);

        int[] shineSize = {x, y, w, h};
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
            new Bullet(), new Missile()};

        this.skillIdx = 0;
        this.useSkill = true;
        this.moveFic = 0.05f * info.getMass();
        this.strikeFic = 3;
        this.wallFic = 2;
        this.isDie = false;
        if (info.getState() == 1) {
            this.species = SPECIES[2];
        } else {
            this.species = SPECIES[info.getSpecies()];
        }
    }

    @Override
    public void update() {
        this.species.update(this);
        if (isBound()) {
            this.goVec.setValue(this.goVec.getValue() - this.wallFic);
        }
        this.skills[this.skillIdx].update();
    }

    @Override
    public void move() {
        this.species.move(this);
    }

    public Marble strike(Marble target) {
        this.isCollide = true;
        return this.species.strike(this, target);
    }

    public boolean die() {
        this.isDie = true;
        return false;
    }

    public boolean getIsDie() {
        return this.isDie;
    }

    public MarbleRenderer getRenderer() {
        return this.renderer;
    }

    public void updateShine() {
        this.shine.setCenterX(this.getCenterX());
        this.shine.setCenterY(this.getCenterY());
        this.shine.update();
    }

    @Override
    public boolean isBound() {
        if (this.getCenterX() - this.getR() <= 0
                || this.getCenterX() + this.getR() >= Global.SCREEN_X
                || this.getCenterY() - this.getR() <= 0
                || this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
            if (this.getCenterX() - this.getR() <= 0) {
                this.setCenterX(this.getR());
                this.getGoVec().setX(-this.getGoVec().getX());
            }
            if (this.getCenterX() + this.getR() >= Global.SCREEN_X) {
                this.setCenterX(Global.SCREEN_X - this.getR());
                this.getGoVec().setX(-this.getGoVec().getX());
            }
            if (this.getCenterY() - this.getR() <= 0) {
                this.setCenterY(this.getR());
                this.getGoVec().setY(-this.getGoVec().getY());
            }
            if (this.getCenterY() + this.getR() >= Global.SCREEN_Y - Global.INFO_H) {
                this.setCenterY(Global.SCREEN_Y - Global.INFO_H - this.getR());
                this.getGoVec().setY(-this.getGoVec().getY());
            }
            return true;
        }
        return false;
    }

    public void setShine(boolean isShine) {
        this.shine.setShine(isShine);
    }

    public void setMoveFic() {
        this.moveFic = this.moveFic * 1.5f;
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

    public void paintSkill(Graphics g) {
        this.skills[this.skillIdx].paintSkill(g);
    }

    public void paintAll(Graphics g) {
        this.shine.paintComponent(g);
        paintComponent(g);
        paintSkill(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) (this.getX()),
                (int) (this.getY()),
                (int) (this.getWidth()), (int) (this.getHeight()));

        if (Global.IS_DEBUG) {
            g.drawOval((int) (this.getCenterX() - this.getR()),
                    (int) (this.getCenterY() - this.getR()),
                    (int) (2 * this.getR()), (int) (2 * this.getR()));
        }
    }
}
