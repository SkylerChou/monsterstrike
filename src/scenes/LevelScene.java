/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import Props.*;
import monsterstrike.graph.Vector;
import controllers.SceneController;
import interfaceskills.SkillComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import player.PlayerInfo;
import monsterstrike.gameobject.*;
import monsterstrike.gameobject.marble.*;
import monsterstrike.util.*;

public class LevelScene extends Scene {

    private Background background;
    private Item item;
    private Item blood;
    private Button[] shineFrame;
    private Marble[] myMarbles; //資訊欄顯示怪物
    private ArrayList<Marble> marbles; //我方出戰怪物
    private MarbleArray allEnemies; //敵方所有怪物
    private Arrow arrow;
    private ArrayList<Marble> battleEnemies; //小關出戰怪物
    private Prop[] props;
    private Marble m;

    private int currentIdx;
    private int count;
    private int state;
    private int idx;
    private int sceneCount;
    private int hitCount;  //strike次數
    private int round;      //小關回合計數
    private int enemyRound; //敵人攻擊回合計數
    private int myRound; //我方攻擊回合計數
    private float myHp;     //總血量
    private float currentHp; //當前血量
    private float ratio;
    private int tmpCount;
    private int tmpCount2;
    private Delay delay;
    private PlayerInfo playerinfo;
    private int overCount;

    private ArrayList<Button> buttons;
    private boolean isEnter;
    private boolean isOnButton;
    private boolean isCount;
    private boolean isClick;
    private boolean isWin;
    private boolean isDraw;

    public LevelScene(SceneController sceneController, int backIdx,
            Marble[] myMarbles, ArrayList<Marble> enemies, PlayerInfo playerinfo) {
        super(sceneController);
        this.idx = backIdx;
        this.playerinfo = playerinfo;
        this.item = new Item(ImgInfo.INFOFORM_PATH, Global.SCREEN_X / 2, Global.SCREEN_Y - Global.INFO_H / 2,
                Global.SCREEN_X, Global.INFO_H);
        this.blood = new Item(ImgInfo.BLOOD_PATH, ImgInfo.BLOOD_INFO[0], ImgInfo.BLOOD_INFO[1],
                ImgInfo.BLOOD_INFO[2], ImgInfo.BLOOD_INFO[3]);
        this.shineFrame = new Button[3];
        this.marbles = new ArrayList<>();
        this.battleEnemies = new ArrayList<>();
        this.myMarbles = myMarbles;
        this.myHp = 0;
        int w = 75;
        for (int i = 0; i < myMarbles.length; i++) {
            this.marbles.add(myMarbles[i].duplicate(Global.POSITION_X[i],
                    Global.POSITION_Y[i], 100, 100));
            this.myMarbles[i].setCenterX(w + 30);
            this.myMarbles[i].setCenterY(Global.SCREEN_Y - 70);
            w += 150;
            this.myHp += this.myMarbles[i].getInfo().getHp();
        }
        this.currentHp = this.myHp;
        this.allEnemies = new MarbleArray(enemies);
        this.delay = new Delay(50); //敵人依序攻擊delay
        this.delay.start();
        this.ratio = this.currentHp / this.myHp;
        this.tmpCount = 1;
        this.tmpCount2 = 0;
        this.props = new Prop[3];
        this.buttons = new ArrayList<>();
        this.isEnter = false;
        this.isOnButton = false;
        this.isCount = false;
        this.isClick = false;
        this.overCount = 0;
        this.isWin = false;
        this.isDraw = false;
    }

    @Override
    public void sceneBegin() {
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx], 3 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);
        for (int i = 0; i < 3; i++) {
            this.marbles.get(i).setCenterX(Global.POSITION_X[i]);
            this.marbles.get(i).setCenterY(Global.POSITION_Y[i]);
        }
        for (int i = 0; i < this.shineFrame.length; i++) {
            this.shineFrame[i] = new Button(ImgInfo.SHINEFRAME_PATH, (int) this.myMarbles[i].getCenterX(), (int) this.myMarbles[i].getCenterY(),
                    ImgInfo.SHINEFRAME_INFO[0], ImgInfo.SHINEFRAME_INFO[1], 20);
            this.shineFrame[i].setIsShow(false);
        }
        this.buttons.add(new Button(ImgInfo.HOME, Global.SCREEN_X - 50, Global.SCREEN_Y - 50, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1], 20));
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.currentIdx = 0;
        this.count = 0;
        this.state = 0;
        this.sceneCount = 0; //用於小關場景移動
        this.hitCount = 0;
        this.round = 0;
        this.enemyRound = 0;
        this.myRound = 0;
        genBattleEnemies();
    }

    @Override
    public void sceneUpdate() {
//        if (this.delay.isTrig()) { //慢動作delay，Debug再開
        if (this.state == 0) { //設定背景起始位置， 敵人怪物降落           
            this.background.setX((3 - this.sceneCount) * ImgInfo.BACKGROUND_SIZE[idx][0]);
            if (dropEnemies()) {
                if (this.sceneCount == 1) {//第二小關
                    this.props[0] = new Heart(ImgInfo.HEART, Global.random(50, Global.SCREEN_X - 50), 50, 80, 80, 40, ImgInfo.HEART_NUM, 10);
                } else if (this.sceneCount == 2) {//第三小關
                    this.props[1] = new Booster(ImgInfo.SHOE, Global.SCREEN_X - 50, Global.random(50, Global.SCREEN_Y - Global.INFO_H - 50), 80, 80, 40, ImgInfo.SHOE_NUM, 35);
                    Marble tmp = null;
                    for (int i = 0; i < this.battleEnemies.size(); i++) {
                        if (this.battleEnemies.get(i).getInfo().getLevel() == 5) {
                            tmp = this.battleEnemies.get(i);
                        }
                    }
                    if (tmp != null) {
                        this.props[2] = new Shield(ImgInfo.SHIELD, (int) tmp.getCenterX(), (int) tmp.getCenterY(), (int) (tmp.getInfo().getR() * 1.5f), (int) (tmp.getInfo().getR() * 1.5f), (int) (tmp.getInfo().getR() * 1.5f) / 2, ImgInfo.SHIELD_NUM, 20);
                        this.props[2].setIsUsed(false);
                    }
                }
            }
        } else if (this.state == 1) { //遊戲開始
            if (!isLose() && !isWin) {
                for (int i = 0; i < this.battleEnemies.size(); i++) {
                    this.battleEnemies.get(i).update();
                }

                //我方怪物動畫更新
                for (int i = 0; i < this.marbles.size(); i++) {
                    this.marbles.get(i).updateShine(); //光圈更新
                    this.marbles.get(i).update();      //怪物、技能更新
                    this.shineFrame[i].update();
                }
            }

            for (int i = 0; i < this.props.length; i++) {
                if (this.props[i] != null) {
                    this.props[i].update();
                    if (this.props[i].getIsStop()) {
                        this.props[i] = null;
                    }
                }
            }

            strikeEnemies();
            teamHelp();
            hitProp();

            for (int i = 0; i < this.battleEnemies.size(); i++) { //判斷敵人是否死亡
                if (this.battleEnemies.get(i).getInfo().getHp() <= 0) {
                    this.battleEnemies.get(i).setIsCollide(false);
                    this.battleEnemies.get(i).die();
//                        this.battleEnemies.get(i).setCenterY(Global.FRAME_Y + 200);
                    if (this.battleEnemies.get(i).getIsDie() && this.battleEnemies.get(i).getDieRenderer().getIsStop()) {
                        this.battleEnemies.remove(i);
                    }
                }
            }

            if (isLose()) {
                FileIO.writePlayer("playerInfo.csv", this.playerinfo);
                FileIO.writeMarble("marbleInfo.csv", null);
                if (isEnter) {
                    sceneController.changeScene(new LevelMenu(sceneController, this.playerinfo, "marbleInfo.csv"));
                    return;
                }
            }
            calculateHP();
            if (checkAllStop()) { //當所有我方怪物靜止
                for (int i = 0; i < this.marbles.size(); i++) {
                    this.marbles.get(i).setUseSkill(true);
                }

                if (this.count != 0 && !this.isCount) { //除了第一回合，更新要發動攻擊的我方怪物
                    this.marbles.get(currentIdx).setShine(false);
                    this.currentIdx = this.count % 3;
                    this.hitCount = 0;
                    this.tmpCount = 0;
                    this.marbles.get(currentIdx).setShine(true);
                    this.enemyRound++;
                    this.myRound++;
                    this.isCount = true;
                }
                enemyAttack();

                if (this.battleEnemies.isEmpty() && allSkillStop(this.marbles)) { //所有怪物死亡且技能都施放完畢
                    this.marbles.get(currentIdx).setShine(false);
                    this.round = 0;
                    this.enemyRound = 0;
                    this.state = 2;
                }
            }

        } else if (state == 2) { //若跑完3個小關回到選單，否則移動背景進入下一小關
            if (this.sceneCount == 2 && !isDraw) {
                m = this.allEnemies.luckyDraw();
                m.getInfo().setState(0);
                m.setCenterX(780);
                m.setCenterY(350);
                isWin = true;
                this.playerinfo.addMyMarbleSerial(m.getInfo().getSerial());
                if (this.playerinfo.getLevel() - 1 == this.idx && this.playerinfo.getLevel() != 5) {
                    this.playerinfo.setLevel(this.playerinfo.getLevel() + 1);
                }
                FileIO.writePlayer("playerInfo.csv", this.playerinfo);
                FileIO.writeMarble("marbleInfo.csv", m.getInfo());
                System.out.println("抽中怪物:" + m.getInfo().getName());
                System.out.println(this.playerinfo);
                this.isDraw = true;
            } else if (!isWin) {
                scrollScene();
            }
            if (isWin) {
                m.update();
                m.updateShine();
            }
            if (isEnter) {
                sceneController.changeScene(new LevelMenu(sceneController, this.playerinfo, "marbleInfo.csv"));
            }
        }
        if (this.isClick) {
            sceneController.changeScene(new Menu(sceneController));
        }
        if (this.isOnButton) {
            this.buttons.get(0).update();
        }
//        }
    }

    @Override
    public void sceneEnd() {
//        this.background = null;
//        this.marbles = null;
//        this.shine = null;
//        this.enimies = null;
//        this.arrow = null;
//        this.sceneCount = 0;
//        this.state = 0;
    }

    private void hitProp() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.props.length; j++) {
                if (this.props[j] != null && this.marbles.get(i).isCollision(this.props[j])) {
                    this.props[j].setIsCollide(true);
                    this.props[j].useProp(marbles, i);
                }
            }
        }
    }

    private boolean isLose() {
        if (this.currentHp <= 0) {
            return true;
        }
        return false;
    }

    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    private void calculateHP() {
        int tmp = 0;
        for (int i = 0; i < this.marbles.size(); i++) {
            tmp += this.marbles.get(i).getInfo().getHp();
        }
        if (tmp > this.myHp) {
            tmp = (int) this.myHp;
        }
        this.currentHp = tmp;
        this.ratio = this.currentHp / this.myHp;
    }

    private boolean allSkillStop(ArrayList<Marble> marbles) {
        for (int i = 0; i < marbles.size(); i++) {
            if (!skillStop(marbles.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean skillStop(Marble marble) {
        SkillComponent[] skills = marble.getSkills().getSkillComponent();
        for (int j = 0; j < skills.length; j++) {
            if (skills[j] != null && !skills[j].getIsStop()) {
                return false; //技能尚未釋放完畢
            }
        }
        return true; //技能釋放完畢
    }

    private void setCollide() {
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).setIsCollide(false);
        }
    }

    private void teamHelp() {
        for (int j = 0; j < this.marbles.size(); j++) {
            if (j == currentIdx) {
                continue;
            }
            for (int i = 0; i < this.marbles.size(); i++) {
                if (i != j && this.marbles.get(i).isCollision(this.marbles.get(j))) {
                    this.marbles.set(j, this.marbles.get(i).strike(this.marbles.get(j)));
                    if (i == currentIdx) {
                        this.marbles.get(j).getGoVec().setValue(this.marbles.get(j).getGoVec().getValue() * 0.5f);
                        if (this.marbles.get(j).getUseSkill()) {
                            int r = Global.random(1, 4);
                            if (r == 4) {
                                this.marbles.get(j).useSkill(r, this.marbles, 0);
                            } else {
                                this.hitCount += this.marbles.get(j).useSkill(r, this.battleEnemies, 0);
                            }
                            this.marbles.get(j).setUseSkill(false);
                        }

                    }
                }
            }
        }
    }

    private void strikeEnemies() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.battleEnemies.size(); j++) {
                if (this.marbles.get(i).isCollision(this.battleEnemies.get(j)) && this.marbles.get(i).getGoVec().getValue() > 0) {
                    Marble tmp = this.marbles.get(i).strike(this.battleEnemies.get(j));
                    this.battleEnemies.get(j).setGo(tmp.getGoVec());
                    this.battleEnemies.get(j).setIsCollide(true);
                    this.hitCount += this.marbles.get(i).useSkill(0, this.battleEnemies, j);
                }
            }
        }
    }

    private void enemyAttack() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (enemyRound != 0 && enemyRound % this.battleEnemies.get(i).getInfo().getSkillRound() == 0) {
                if (this.battleEnemies.get(i).getUseSkill() && this.delay.isTrig()) {
                    this.battleEnemies.get(i).useSkill(5, this.marbles, 0);
                    this.battleEnemies.get(i).setUseSkill(false);
                }
                this.hitCount = 0;
                tmpCount = 0;
            } else {
                setCollide();
                this.battleEnemies.get(i).setUseSkill(true);
            }
        }
    }

    private void scrollScene() {
        if (this.background.getX() > (2 - this.sceneCount) * ImgInfo.BACKGROUND_SIZE[idx][0]) {
            this.background.offset(-10);
        }
        if (this.background.getX() <= (2 - this.sceneCount) * ImgInfo.BACKGROUND_SIZE[idx][0]) {
            this.sceneCount++;
            this.hitCount = 0;
            genBattleEnemies();
            this.state = 0;
        }
    }

    private void genBattleEnemies() {
        if (this.sceneCount == 3) {
            sceneEnd();
        }
        ArrayList<Marble> m = this.allEnemies.sortByLevel();
        if (this.sceneCount == 0) {
            for (int i = 0; i < 3; i++) {
                battleEnemies.add(m.get(0).duplicate(Global.ENEMYPOS_X[i], -100, 120, 120));
                battleEnemies.get(i).getInfo().setName(battleEnemies.get(i).getInfo().getName() + (i + 1));
            }
        } else if (this.sceneCount == 1) {
            for (int i = 0; i < 2; i++) {
                battleEnemies.add(m.get(1).duplicate(Global.random(100, Global.SCREEN_X - 100), -100, 120, 120));
                battleEnemies.add(m.get(2).duplicate(Global.random(100, Global.SCREEN_X - 100), -100, 120, 120));
                battleEnemies.get(2 * i).getInfo().setName(battleEnemies.get(2 * i).getInfo().getName() + (i + 1));
                battleEnemies.get(2 * i + 1).getInfo().setName(battleEnemies.get(2 * i + 1).getInfo().getName() + (i + 1));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                battleEnemies.add(m.get(i).duplicate(Global.random(100, Global.SCREEN_X - 100), -100, 120, 120));
            }
            battleEnemies.add(m.get(3).duplicate(Global.random(100, Global.SCREEN_X - 100), -100, 180, 180));
        }
    }

    private boolean dropEnemies() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (this.battleEnemies.get(i).getCenterY() < Global.ENEMYPOS_Y[i]) {
                this.battleEnemies.get(i).offset(0, Global.ENEMYPOS_Y[i] / 40);
            }
        }
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (this.battleEnemies.get(i).getCenterY() >= Global.ENEMYPOS_Y[i]) {
                this.marbles.get(currentIdx).setShine(true);
                this.state = 1;
                return true;
            }
        }
        return false;
    }

    private boolean checkAllStop() {
        for (int i = 0; i < this.marbles.size(); i++) {
            if (this.marbles.get(i).getGoVec().getValue() != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void paint(Graphics g) {
        if (this.background != null) {
            this.background.paint(g);
        }

        if (this.arrow != null && this.arrow.getShow()) {
            this.arrow.paint(g);
        }
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            this.battleEnemies.get(i).paint(g);
            this.battleEnemies.get(i).paintSkill(g);
        }
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).paintAll(g);
        }
        for (int i = 0; i < this.props.length; i++) {
            if (this.props[i] != null) {
                this.props[i].paintComponent(g);
            }
        }

        paintText(g);
        for (int i = 0; i < this.shineFrame.length; i++) {
            this.shineFrame[i].paint(g);
        }
        this.buttons.get(0).paint(g);
        if (isLose()) {
            if (overCount++ % 30 < 15) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 48));
                g.drawString(this.playerinfo.getName() + " Lose!", Global.SCREEN_X / 2 - 120, 270);
                g.setColor(Color.gray);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 48));
                g.drawString(this.playerinfo.getName() + " Lose!", Global.SCREEN_X / 2 - 120 - 2, 270 - 2);
            } else {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 54));
                g.drawString(this.playerinfo.getName() + " Lose!", Global.SCREEN_X / 2 - 150, 270);
                g.setColor(Color.gray);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 54));
                g.drawString(this.playerinfo.getName() + " Lose!", Global.SCREEN_X / 2 - 150 - 2, 270 - 2);
            }
            if (overCount == 100) {
                overCount = 0;
            }
        } else if (isWin) {
            if (overCount++ % 30 < 15) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 48));
                g.drawString(this.playerinfo.getName() + " Win!", Global.SCREEN_X / 2 - 120, 270);
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 48));
                g.drawString(this.playerinfo.getName() + " Win!", Global.SCREEN_X / 2 - 120 - 2, 270 - 2);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 36));
                g.drawString("You Gain", Global.SCREEN_X / 2 - 100, 350);
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 36));
                g.drawString("You Gain", Global.SCREEN_X / 2 - 100 - 2, 350 - 2);
            } else {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 54));
                g.drawString(this.playerinfo.getName() + " Win!", Global.SCREEN_X / 2 - 150, 270);
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 54));
                g.drawString(this.playerinfo.getName() + " Win!", Global.SCREEN_X / 2 - 150 - 2, 270 - 2);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 42));
                g.drawString("You Gain", Global.SCREEN_X / 2 - 120, 350);
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Showcard Gothic", Font.PLAIN, 42));
                g.drawString("You Gain", Global.SCREEN_X / 2 - 120 - 2, 350 - 2);
            }
            m.paintAll(g);
            if (overCount == 1000) {
                overCount = 0;
            }
        }
    }

    private void paintText(Graphics g) {

        if (hitCount > 0) {
            if (tmpCount2 >= hitCount) {
                tmpCount2 = hitCount;
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Showcard Gothic", Font.PLAIN, 36));
            g.drawString("Hits ", Global.SCREEN_X - 200, 100);
            g.setFont(new Font("Showcard Gothic", Font.PLAIN, 42));
            g.drawString("" + tmpCount2, Global.SCREEN_X - 105, 100);
            g.setColor(new Color(255, 153, 0));
            g.setFont(new Font("Showcard Gothic", Font.PLAIN, 36));
            g.drawString("Hits ", Global.SCREEN_X - 200 - 2, 100 - 2);
            g.setFont(new Font("Showcard Gothic", Font.PLAIN, 42));
            g.drawString("" + tmpCount2, Global.SCREEN_X - 105 - 2, 100 - 2);
            tmpCount2 = tmpCount++ / 10 + 1;
        }
        if (round > 0) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Showcard Gothic", Font.PLAIN, 28));
            g.drawString("Round ", 30, 495);
            g.setFont(new Font("Showcard Gothic", Font.PLAIN, 36));
            g.drawString("" + round, 140, 495);
            g.setColor(new Color(255, 153, 0));
            g.setFont(new Font("Showcard Gothic", Font.PLAIN, 28));
            g.drawString("Round ", 30 - 2, 495 - 2);
            g.setFont(new Font("Showcard Gothic", Font.PLAIN, 36));
            g.drawString("" + round, 140 - 2, 495 - 2);
        }

        this.item.paint(g);
        this.blood.paintResize(g, this.ratio);
        g.setFont(new Font("VinerHandITC", Font.ITALIC, 20));
//        g.setFont(new Font("Showcard Gothic", Font.ITALIC, 20));
        g.setColor(Color.BLACK);
        g.drawString((int) this.currentHp + " / " + (int) this.myHp, 1100, Global.SCREEN_Y - 100);
        g.setColor(Color.YELLOW);
        g.drawString((int) this.currentHp + " / " + (int) this.myHp, 1100 - 2, Global.SCREEN_Y - 100 - 2);
        g.setColor(Color.GRAY);
        g.setFont(new Font("Showcard Gothic", Font.PLAIN, 30));
        g.drawString("Battle  ", 800, Global.SCREEN_Y - 40);
        g.setFont(new Font("Showcard Gothic", Font.PLAIN, 36));
        g.drawString("" + (sceneCount + 1), 930, Global.SCREEN_Y - 40);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Showcard Gothic", Font.PLAIN, 30));
        g.drawString("Battle  ", 800 - 2, Global.SCREEN_Y - 40 - 2);
        g.setFont(new Font("Showcard Gothic", Font.PLAIN, 36));
        g.drawString("" + (sceneCount + 1), 930 - 2, Global.SCREEN_Y - 40 - 2);

        g.setColor(Color.GRAY);
        g.setFont(new Font("VinerHandITC", Font.PLAIN, 20));
        g.drawString("Player: " + this.playerinfo.getName(), 530, Global.SCREEN_Y - 60);
        g.setColor(Color.BLACK);
        g.drawString("Player: " + this.playerinfo.getName(), 530 - 2, Global.SCREEN_Y - 60 - 2);
        g.setColor(Color.GRAY);
        g.drawString("Level: " + this.playerinfo.getLevel(), 530, Global.SCREEN_Y - 30);
        g.setColor(Color.BLACK);
        g.drawString("Level: " + this.playerinfo.getLevel(), 530 - 2, Global.SCREEN_Y - 30 - 2);

        g.setFont(new Font("Showcard Gothic", Font.PLAIN, 24));
        g.setColor(Color.GRAY);
        g.drawString("HP ", 525, Global.SCREEN_Y - 105);
        g.setColor(Color.BLACK);
        g.drawString("HP ", 525 - 2, Global.SCREEN_Y - 105 - 2);

        for (int i = 0; i < this.myMarbles.length; i++) {
            this.myMarbles[i].paintComponent(g);
            int atkRound = this.myMarbles[i].getInfo().getSkillRound()
                    - this.myRound % this.myMarbles[i].getInfo().getSkillRound();
            int x = (int) (this.myMarbles[i].getCenterX() + this.myMarbles[i].getR());
            int y = (int) (this.myMarbles[i].getCenterY() + this.myMarbles[i].getR());
            g.setColor(Color.BLACK);
            g.drawString("" + atkRound, x, y);
            g.setColor(new Color(255, 153, 0));
            g.drawString("" + atkRound, x - 2, y - 2);
            if (atkRound == this.myMarbles[i].getInfo().getSkillRound() && this.myRound != 0) {
                this.shineFrame[i].setIsShow(true);
            }
        }
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            int atkRound = this.battleEnemies.get(i).getInfo().getSkillRound()
                    - this.enemyRound % this.battleEnemies.get(i).getInfo().getSkillRound();
            int x = (int) (this.battleEnemies.get(i).getCenterX() + this.battleEnemies.get(i).getR());
            int y = (int) (this.battleEnemies.get(i).getCenterY() + this.battleEnemies.get(i).getR());
            g.setColor(Color.BLACK);
            g.drawString("" + atkRound, x, y);
            g.setColor(new Color(255, 153, 0));
            g.drawString("" + atkRound, x - 2, y - 2);
        }
    }

    @Override
    public CommandSolver.KeyListener getKeyListener() {
        return new MyKeyListener();
    }

    @Override
    public CommandSolver.MouseCommandListener getMouseListener() {
        return new MyMouseListener();

    }

    public class MyKeyListener implements CommandSolver.KeyListener {

        @Override
        public void keyPressed(int commandCode, long trigTime) {

            if ((isLose() || isWin) && commandCode == Global.ENTER) {
                System.out.println("!");
                isEnter = true;
            }
        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {
        }

        @Override
        public void keyTyped(char c, long trigTime) {
        }
    }

    public class MyMouseListener implements CommandSolver.MouseCommandListener {

        private float startX;
        private float startY;
        private float endX;
        private float endY;

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState mouseState, long trigTime) {
            if (mouseState == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 50 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 50 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > Global.SCREEN_Y - 50 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < Global.SCREEN_Y - 50 + ImgInfo.SETTING_INFO[1] / 2) {
                isClick = true;
            }
            if (mouseState == CommandSolver.MouseState.MOVED && e.getX() > Global.SCREEN_X - 50 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 50 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > Global.SCREEN_Y - 50 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < Global.SCREEN_Y - 50 + ImgInfo.SETTING_INFO[1] / 2) {
                isOnButton = true;
            } else {
                isOnButton = false;
            }

            if (state == 1 && checkAllStop() && allSkillStop(battleEnemies)) {
                arrow.setResizeMag(0);
                if (mouseState == CommandSolver.MouseState.PRESSED) {
                    this.startX = e.getX();
                    this.startY = e.getY();
                } else if (mouseState == CommandSolver.MouseState.DRAGGED) {
                    arrow.setCenterX(marbles.get(currentIdx).getCenterX());
                    arrow.setCenterY(marbles.get(currentIdx).getCenterY());
                    Vector vector = new Vector(this.startX - e.getX(), this.startY - e.getY());
                    if (this.startY - e.getY() > 0) {
                        arrow.setDegree((float) -Math.acos(vector.getX() / vector.getValue()));
                    } else {
                        arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                    }
                    float value = vector.getValue();
                    if (vector.getValue() > 10f * marbles.get(currentIdx).getR()) {
                        value = 10f * marbles.get(currentIdx).getR();
                    }
                    arrow.setResizeMag(value / arrow.getWidth());
                    arrow.setShow(true);
                } else {
                    arrow.setShow(false);
                }
                if (mouseState == CommandSolver.MouseState.RELEASED) {
                    this.endX = e.getX();
                    this.endY = e.getY();
                    Vector vector = new Vector(this.startX - this.endX, this.startY - this.endY);
                    arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                    arrow.setResizeMag(vector.getValue() / arrow.getWidth());
                    marbles.get(currentIdx).setGo(vector.resizeVec(marbles.get(currentIdx).getInfo().getV()));
                    count++;
                    round++;
                    isCount = false;
                    arrow.setShow(false);
                }
            }
        }
    }
}
