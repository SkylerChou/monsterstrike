/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collisionproject.gameobject;

import collisionproject.util.Global;
import interfaceSkills.Anger;
import interfaceSkills.CriticalAttack;
import interfaceSkills.DecreaseHalfAttack;
import interfaceSkills.Heal;
import interfaceSkills.NormalAttack;
import interfaceSkills.Skills;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Marble extends GameObject {

    private BufferedImage img;
    private Vector go;
    private Vector normal;
    private Vector tangent;
    private int mass;
    private float v;
    private float currentV;
    private float a;
    private float fiction;
    private Marble other;
    private boolean isCollide;
    private String name;
    private int hp = 100;
    private int atk = 20;
    private int attribute;
    private Skills[] skills;

    public Marble(String path, String name, int x, int y, int[] info, int attribute) {
        super(x, y, info[0], info[1], info[2]);
        this.mass = info[3];
        this.v = info[4];
        this.currentV = this.v;
        this.a = 1;
        this.fiction = 5;
        this.go = new Vector(0, 0);
        this.normal = new Vector(0, 0);
        this.tangent = new Vector(0, 0);
        this.isCollide = false;
        this.name = name;
        this.attribute = attribute;
        this.skills = new Skills[5];
        this.setSkills();
        try {
            img = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (this.isCollide) {
            this.currentV -= 0.1 * fiction;
            if (this.currentV < 0) {
                this.currentV = 0;
            }
        } else if (!this.isCollide && this.go.getValue() != 0) {
            this.currentV += 0.2 * this.a;
        }

        if (this.isBound()) {
            this.isCollide = true;
        }
        this.offset(go.getX() * this.currentV, go.getY() * this.currentV);
    }

    public float getV() {
        return this.currentV;
    }

    public void reset() {
        this.isCollide = false;
        this.go = new Vector(0, 0);
        this.normal = new Vector(0, 0);
        this.tangent = new Vector(0, 0);
        this.currentV = this.v;
    }

    public boolean isBound() {
        if (this.getCenterX() - this.getR() <= 0 || this.getCenterX() + this.getR() >= Global.SCREAN_X
                || this.getCenterY() - this.getR() <= 0 || this.getCenterY() + this.getR() >= Global.SCREAN_Y) {
            if (this.getCenterX() - this.getR() <= 0) {
                this.setCenterX(this.getR());
                go.setX(-go.getX());
            } else if (this.getCenterX() + this.getR() >= Global.SCREAN_X) {
                this.setCenterX(Global.SCREAN_X - this.getR());
                go.setX(-go.getX());
            }
            if (this.getCenterY() - this.getR() <= 0) {
                this.setCenterY(this.getR());
                go.setY(-go.getY());
            } else if (this.getCenterY() + this.getR() >= Global.SCREAN_Y) {
                this.setCenterY(Global.SCREAN_Y - this.getR());
                go.setY(-go.getY());
            }
            return true;
        }
        return false;
    }

    public Marble strike(Marble other) {
        this.other = other;
        if (this.distWith(other) < this.getR() + other.getR()) {
            Vector v1 = new Vector(this.getCenterX() - other.getCenterX(), this.getCenterY() - other.getCenterY());
            Vector v2 = v1.resizeVec(this.getR() + other.getR());
            float moveValue = (v2.getValue() - v1.getValue()) / 2;
            this.setCenterX(this.getCenterX() + moveValue * v1.getUnitX());
            this.setCenterY(this.getCenterY() + moveValue * v1.getUnitY());
            this.other.setCenterX(this.other.getCenterX() - moveValue * v1.getUnitX());
            this.other.setCenterY(this.other.getCenterY() - moveValue * v1.getUnitY());
        }
        this.setVectors(this.other);
        this.other.setVectors(this);
        float aX = this.normal.getX();
        float aY = this.normal.getY();
        float bX = this.other.normal.getX();
        float bY = this.other.normal.getY();

        float aXn = ((this.getMass() - this.other.getMass()) * aX + 2 * this.other.getMass() * bX) / (this.getMass() + this.other.getMass());
        float aYn = ((this.getMass() - this.other.getMass()) * aY + 2 * this.other.getMass() * bY) / (this.getMass() + this.other.getMass());
        float bXn = ((this.other.getMass() - this.getMass()) * bX + 2 * this.getMass() * aX) / (this.other.getMass() + this.getMass());
        float bYn = ((this.other.getMass() - this.getMass()) * bY + 2 * this.getMass() * aY) / (this.other.getMass() + this.getMass());

        this.setGo(new Vector(aXn + this.tangent.getX(),
                aYn + this.tangent.getY()));
        this.other.setGo(new Vector(bXn + this.other.tangent.getX(),
                bYn + this.other.tangent.getY()));
        return this.other;
    }

    public void setVectors(Marble other) {
        if (this.go.getValue() != 0) {
            this.normal = new Vector(other.getCenterX() - this.getCenterX(), other.getCenterY() - this.getCenterY());
            float theta = (float) Math.acos(dot(this.go, this.normal) / (this.go.getValue() * this.normal.getValue()));
            System.out.println(Math.toDegrees(theta));
            float norValue = (float) (this.go.getValue() * Math.cos(theta));
            float tanValue = (float) (this.go.getValue() * Math.sin(theta));
            Vector nor = new Vector(this.normal.getUnitX() * norValue, this.normal.getUnitY() * norValue);
            this.normal = nor;
            Vector tan = new Vector(nor.getTanX(), nor.getTanY());
            this.tangent = new Vector(tan.getUnitX() * tanValue, tan.getUnitY() * tanValue);
        }
    }

    private float distWith(Marble other) {
        return (float) Math.sqrt(Math.pow(this.getCenterX() - other.getCenterX(), 2)
                + Math.pow(this.getCenterY() - other.getCenterY(), 2));
    }

    private float dot(Vector v1, Vector v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public String getName() {
        return this.name;
    }

    public int getAttribute() {
        return this.attribute;
    }

    public int getHp() {
        return this.hp;
    }

    public int getAtk() {
        return this.atk;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public float getMass() {
        return this.mass;
    }

    public Vector getNorVec() {
        return this.normal;
    }

    public Vector getTanVec() {
        return this.tangent;
    }

    public Vector getGoVec() {
        return this.go;
    }

    public void setGo(Vector go) {
        this.go = go;
    }

    public void setIsCollide(boolean isCollide) {
        this.isCollide = isCollide;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, (int) this.getX(), (int) this.getY(), null);
        g.drawOval((int) (this.getCenterX() - this.getR()),
                (int) (this.getCenterY() - this.getR()),
                (int) (2 * this.getR()), (int) (2 * this.getR()));
    }

    private void setSkills() {
        Skills skills[] = {
            new NormalAttack(),
            new CriticalAttack(),
            new DecreaseHalfAttack(),
            new Heal(),
            new Anger()
        };

        for (int i = 0; i < skills.length; i++) {
            this.skills[i] = skills[i];
        }
    }

    public void useSkill(Marble target) {
        int r = (int) (Math.random() * 2);
        this.skills[r].useSkill(this, target);
    }
}
