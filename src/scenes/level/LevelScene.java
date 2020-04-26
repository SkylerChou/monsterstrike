/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes.level;

import monsterstrike.gameobject.button.*;
import Props.*;
import controllers.ARC;
import controllers.MRC;
import monsterstrike.graph.Vector;
import controllers.SceneController;
import interfaceskills.SkillComponent;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import player.PlayerInfo;
import monsterstrike.gameobject.*;
import monsterstrike.gameobject.marble.*;
import monsterstrike.util.*;
import player.Player;
import scenes.*;

public abstract class LevelScene extends Scene {

    private Background background;
    private Item item; //資訊欄
    private Item blood; //血條
    private ButtonRenderer[] shineFrame; //資訊欄怪物亮框
    private Marble[] myMarbles; //資訊欄顯示怪物
    protected ArrayList<Marble> marbles; //我方出戰怪物
    private MarbleArray allEnemies; //敵方所有怪物
    private Arrow arrow;
    protected ArrayList<Marble> battleEnemies; //小關出戰怪物
    protected ArrayList<Prop> props;
    private Marble m; //抽中怪物    
    private Player player;
    private int[] atkRound;

    protected int currentIdx;
    private int count;
    protected int state;
    private int idx;
    protected int sceneCount;
    protected int hitCount;  //strike次數
    protected int round;      //小關回合計數
    protected int enemyRound; //敵人攻擊回合計數
    private float myHp;     //總血量
    private float currentHp; //當前血量
    private float ratio; //計算current HP/ 原始HP
    private int tmpCount;
    private int tmpCount2;
    private Delay delay;
    private PlayerInfo playerinfo;
    private int overCount;

    private Button home;
    private boolean isEnter;
    private boolean isCount;
    private boolean isClick;
    protected boolean isWin;
    private boolean isDraw;
    private boolean enemyIsAtk;

    private AudioClip music;

    public LevelScene(SceneController sceneController, int backIdx,
            Marble[] myMarbles, ArrayList<Marble> enemies, PlayerInfo playerinfo) {
        super(sceneController);
        this.idx = backIdx;
        this.playerinfo = playerinfo;
        this.player = new Player(this.playerinfo.playerNum() - 1, 0, Global.SCREEN_X / 2 + 100,
                Global.SCREEN_Y - 85, 50, 75);
        this.item = new Item(ImgInfo.INFOFORM_PATH, Global.SCREEN_X / 2, Global.SCREEN_Y - Global.INFO_H / 2,
                Global.SCREEN_X, Global.INFO_H);
        this.blood = new Item(ImgInfo.BLOOD_PATH, ImgInfo.BLOOD_INFO[0], ImgInfo.BLOOD_INFO[1],
                ImgInfo.BLOOD_INFO[2], ImgInfo.BLOOD_INFO[3]);
        this.shineFrame = new ButtonRenderer[3];
        this.atkRound = new int[3];
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
            this.atkRound[i] = myMarbles[i].getInfo().getSkillRound();
        }
        this.currentHp = this.myHp;
        this.allEnemies = new MarbleArray(enemies);
        this.delay = new Delay(50); //敵人依序攻擊delay
        this.delay.start();
        this.ratio = this.currentHp / this.myHp;
        this.tmpCount = 1;
        this.tmpCount2 = 0;
        this.props = new ArrayList<>();
        this.isEnter = false;
        this.isCount = false;
        this.isClick = false;
        this.overCount = 0;
        this.isWin = false;
        this.isDraw = false;
        this.enemyIsAtk = false;
        this.music = MRC.getInstance().tryGetMusic("/resources/wav/battle.wav");
        this.home = new ButtonA(ImgInfo.HOME, Global.SCREEN_X - 30 - ImgInfo.SETTING_INFO[0], Global.SCREEN_Y - 75, ImgInfo.SETTING_INFO[0], ImgInfo.SETTING_INFO[1]);
        this.home.setListener(new ButtonClickListener());
    }

    @Override
    public void sceneBegin() {
        this.music.loop();
        this.background = new Background(ImgInfo.BACKGROUND_PATH[idx], 3 * ImgInfo.BACKGROUND_SIZE[idx][0], ImgInfo.BACKGROUND_SIZE[idx][1], idx);
        for (int i = 0; i < 3; i++) {
            this.marbles.get(i).setCenterX(Global.POSITION_X[i]);
            this.marbles.get(i).setCenterY(Global.POSITION_Y[i]);
            this.marbles.get(i).setDetect(this.marbles.get(i).duplicate());
        }
        for (int i = 0; i < this.shineFrame.length; i++) {
            this.shineFrame[i] = new ButtonRenderer(ImgInfo.SHINEFRAME_PATH, (int) this.myMarbles[i].getCenterX(), (int) this.myMarbles[i].getCenterY(),
                    ImgInfo.SHINEFRAME_INFO[0], ImgInfo.SHINEFRAME_INFO[1], 20);
            this.shineFrame[i].setIsShow(false);
        }
        this.arrow = new Arrow(ImgInfo.ARROW, 0, 0, ImgInfo.ARROW_INFO);
        this.currentIdx = 0;
        this.count = 0;
        this.state = 0;
        this.sceneCount = 0; //用於小關場景移動
        this.hitCount = 0;
        this.round = 0;
        this.enemyRound = 0;
        genGameObject();
        PaintText.setFlash(15);
    }

    @Override
    public void sceneUpdate() {
//        if (this.delay.isTrig()) { //慢動作delay，Debug再開
        if (this.state == 0) { //設定背景起始位置， 敵人怪物降落           
            this.background.setX((this.sceneCount + 1) * ImgInfo.BACKGROUND_SIZE[idx][0]);
            if (dropEnemies()) {
                genProps();
            }
        } else if (this.state == 1) { //遊戲開始
            if (!isLose() && !isWin) {
                updateMarbles();
            }
            updateGameObject();
            strikeEnemies(); //攻擊敵人
            teamHelp(); //撞到隊友，隊友使出技能
            hitGameObject(); //撞到其他物品

            calculateHP();//計算我方HP

            enemyDie();//判斷敵人死亡
            
            for (int i = 0; i < this.battleEnemies.size(); i++) {
                if (this.battleEnemies.get(i).getIsCollide()) {
                    ARC.getInstance().play("/resources/wav/die.wav");
                }
            }

            updateShineFrame();

            if (isLose()) { //我方輸了
                String mymarbleFile = "mymarbleInfoTmp.csv";
                FileIO.writeMarble(mymarbleFile, null);
                if (isEnter) {
                    this.music.stop();
                    sceneController.changeScene(new LevelMenu(sceneController, this.playerinfo, mymarbleFile, true));
                    return;
                }
            }

            if (checkAllStop()) { //當所有我方怪物靜止
                resetBattle();
                enemyAttack();
                endBattle();
            }

        } else if (state == 2) { //若跑完3個小關回到選單，否則移動背景進入下一小關
            if (this.sceneCount == 2 && !isDraw) {
                win();
            } else if (!isWin) {
                scrollScene();
            }
            if (isWin) {
                m.update(); //抽中角色動畫
            }
            if (isEnter) {  //Win之後按Enter回LevelMenu
                String mymarbleFile = "mymarbleInfoTmp.csv";
                FileIO.writeMarble(mymarbleFile, m.getInfo());
                this.music.stop();
                sceneController.changeScene(new ChooseGame(sceneController, this.playerinfo, mymarbleFile));
                return;
            }
        }
        if (this.isClick) { //滑鼠按下Home回主畫面
            String mymarbleFile = "mymarbleInfoTmp.csv";
            FileIO.writeMarble(mymarbleFile, null);
            this.music.stop();
            sceneController.changeScene(new FileIOScene(sceneController, this.playerinfo, "w"));
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

    private void win() {
        this.music.stop();
        ARC.getInstance().play("/resources/wav/win2.wav");
        m = this.allEnemies.luckyDraw();
        m.getInfo().setState(0);
        m.setCenterX(850);
        m.setCenterY(300);
        isWin = true;
        this.playerinfo.addMyMarbleSerial(m.getInfo().getSerial());
        if (this.playerinfo.getLevel() - 1 == this.idx && this.playerinfo.getLevel() != 5) {
            this.playerinfo.setLevel(this.playerinfo.getLevel() + 1);
        }
        System.out.println("抽中怪物:" + m.getInfo().getName());
        System.out.println(this.playerinfo);
        this.isDraw = true;
    }

    private void scrollScene() {
        if (this.background.getX() < (this.sceneCount + 2) * ImgInfo.BACKGROUND_SIZE[idx][0]) {
            this.background.offset(12);
            scrollMarbles();
        }

        if (this.background.getX() >= (this.sceneCount + 2) * ImgInfo.BACKGROUND_SIZE[idx][0]) {
            this.sceneCount++;
            this.hitCount = 0;
            genGameObject();
            this.state = 0;
        }
    }

    private void scrollMarbles() {
        for (int i = 0; i < this.marbles.size(); i++) {
            float x = this.marbles.get(i).getCenterX();
            float y = this.marbles.get(i).getCenterY();
            if (x > Global.POSITION_X[i]) {
                this.marbles.get(i).offset((Global.POSITION_X[i] - x) / 35f, 0);
            }
            if (y > Global.POSITION_Y[i] + 10 || y < Global.POSITION_Y[i] - 10) {
                this.marbles.get(i).offset(0, (Global.POSITION_Y[i] - y) / 35f);
            }
        }
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

    private void resetBattle() {
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
            for (int i = 0; i < this.atkRound.length; i++) {
                this.atkRound[i]--;
                if (this.atkRound[i] < 0) {
                    this.atkRound[i] = 0;
                }
            }
            this.isCount = true;
        }
    }

    protected void endBattle() {
        if (this.battleEnemies.isEmpty() && allSkillStop(this.marbles)) { //所有怪物死亡且技能都施放完畢
            if (removeEnemies() && removeGameObject()) {
                this.marbles.get(currentIdx).setShine(false);
                this.round = 0;
                this.enemyRound = 0;
                this.state = 2;
            }
        }
    }

    protected boolean removeGameObject() {
        return true;
    }

    protected boolean removeEnemies() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            this.battleEnemies.remove(i);
        }
        if (this.battleEnemies.isEmpty()) {
            return true;
        }
        return false;
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

    //condition
    private void enemyDie() {
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
    }

    protected boolean isLose() {
        if (this.currentHp <= 0) {
            ARC.getInstance().play("/resources/wav/lose1.wav");
            return true;
        }
        return false;
    }

    protected boolean allSkillStop(ArrayList<Marble> marbles) {
        for (int i = 0; i < marbles.size(); i++) {
            if (!skillStop(marbles.get(i))) {
                return false;
            }
        }
        return true;
    } //技能釋放完畢

    private boolean skillStop(Marble marble) {
        SkillComponent[] skills = marble.getSkills().getSkillComponent();
        for (int j = 0; j < skills.length; j++) {
            if (skills[j] != null && !skills[j].getIsStop()) {
                return false; //技能尚未釋放完畢
            }
        }
        return true; //技能釋放完畢
    }

    private boolean checkAllStop() {
        for (int i = 0; i < this.marbles.size(); i++) {
            if (this.marbles.get(i).goVec().getValue() != 0) {
                return false;
            }
        }
        return true;
    }  //Marble停止移動

    //Getter, Setter
    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    private void setCollide() {
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).setIsCollide(false);
        }
    }

    //Strike   
    private void teamHelp() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = i + 1; j < this.marbles.size(); j++) {
                if (this.marbles.get(i).getDetect().isCollision(this.marbles.get(j).getDetect())) {
                    this.marbles.get(i).detect(this.marbles.get(j));
                    this.marbles.get(i).strike(this.marbles.get(j));
                    if (i == currentIdx && this.marbles.get(j).getUseSkill()) {
                        int r = Global.random(1, 3);
                        if (r == 4) {
                            this.marbles.get(j).useSkill(r, this.marbles, 0);
                        } else {
                            this.hitCount += this.marbles.get(j).useSkill(r, this.battleEnemies, 0);
                        }
                        this.marbles.get(j).setUseSkill(false);
                    } else if (j == currentIdx) {
                        int r = Global.random(1, 3);
                        if (r == 4) {
                            this.marbles.get(i).useSkill(r, this.marbles, 0);
                        } else {
                            this.hitCount += this.marbles.get(i).useSkill(r, this.battleEnemies, 0);
                        }
                        this.marbles.get(j).setUseSkill(false);
                    }
                }
            }
        }
    }

    private void strikeEnemies() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.battleEnemies.size(); j++) {
                if (this.marbles.get(i).getDetect().isCollision(this.battleEnemies.get(j))
                        && this.marbles.get(i).goVec().getValue() > 0 && this.battleEnemies.get(j).getInfo().getHp() > 0) {
                    this.marbles.get(i).detectStill(this.battleEnemies.get(j));
                    this.battleEnemies.get(j).setIsCollide(true);
                    this.battleEnemies.get(j).setGo(this.marbles.get(i).goVec().resizeVec(10));
                    this.marbles.get(i).hit(this.battleEnemies.get(j));
                    this.hitCount += this.marbles.get(i).useSkill(0, this.battleEnemies, j);
                }
            }
        }
    }

    protected void hitGameObject() {
        hitProp();
    }

    protected void hitProp() {
        for (int i = 0; i < this.marbles.size(); i++) {
            for (int j = 0; j < this.props.size(); j++) {
                if (this.props.get(j) != null && this.marbles.get(i).isCollision(this.props.get(j))) {
                    this.props.get(j).setIsCollide(true);
                    this.props.get(j).useProp(marbles, i);
                }
            }
        }
    }

    private void enemyAttack() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            if (enemyRound != 0 && enemyRound % this.battleEnemies.get(i).getInfo().getSkillRound() == 0) {
                if (this.battleEnemies.get(i).getUseSkill() && this.delay.isTrig()) {
                    enemyIsAtk = true;
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
        if (enemyIsAtk && this.allSkillStop(battleEnemies)) {
            enemyIsAtk = false;
        }
    }

    //Generate
    protected void genBattleEnemies() {
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
                battleEnemies.add(m.get(1).duplicate(Global.ENEMYPOS_X[2 * i], -100, 120, 120));
                battleEnemies.add(m.get(2).duplicate(Global.ENEMYPOS_X[2 * i + 1], -100, 120, 120));
                battleEnemies.get(2 * i).getInfo().setName(battleEnemies.get(2 * i).getInfo().getName() + (i + 1));
                battleEnemies.get(2 * i + 1).getInfo().setName(battleEnemies.get(2 * i + 1).getInfo().getName() + (i + 1));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                battleEnemies.add(m.get(i).duplicate(Global.ENEMYPOS_X[i], -100, 120, 120));
            }
            battleEnemies.add(m.get(3).duplicate(Global.SCREEN_X / 2, -100, 180, 180));
        }
    }

    protected abstract void genGameObject();

    protected void genProps() {
        if (this.sceneCount == 1) {//第二小關
            this.props.add(new Heart(ImgInfo.HEART, Global.random(50, Global.SCREEN_X - 50), 50, 80, 80, 40, ImgInfo.HEART_NUM, 10));
        } else if (this.sceneCount == 2) {//第三小關
            this.props.add(new Booster(ImgInfo.SHOE, Global.SCREEN_X - 50, Global.random(50, Global.SCREEN_Y - Global.INFO_H - 50), 80, 80, 40, ImgInfo.SHOE_NUM, 35));
            Marble tmp = null;
            for (int i = 0; i < this.battleEnemies.size(); i++) {
                if (this.battleEnemies.get(i).getInfo().getLevel() >= 4) {
                    tmp = this.battleEnemies.get(i);
                }
            }
            if (tmp != null) {
                this.props.add(new Shield(ImgInfo.SHIELD, (int) tmp.getCenterX(),
                        (int) tmp.getCenterY(), (int) (tmp.getInfo().getR() * 1.5f),
                        (int) (tmp.getInfo().getR() * 1.5f), (int) (tmp.getInfo().getR() * 1.5f) / 2, ImgInfo.SHIELD_NUM, 20));
                this.props.get(2).setIsUsed(false);
            }
        }
    }

    //Update
    private void updateMarbles() {
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            this.battleEnemies.get(i).update();
        }
        //我方怪物動畫更新
        for (int i = 0; i < this.marbles.size(); i++) {
            this.marbles.get(i).updateShine(); //光圈更新
            this.marbles.get(i).update();      //怪物、技能更新
            this.marbles.get(i).getDetect().setCenterX(this.marbles.get(i).getCenterX() + this.marbles.get(i).goVec().getX());
            this.marbles.get(i).getDetect().setCenterY(this.marbles.get(i).getCenterY() + this.marbles.get(i).goVec().getY());
            this.shineFrame[i].update();
        }
    }

    private void updateShineFrame() {
        for (int i = 0; i < this.myMarbles.length; i++) {
            if (atkRound[i] == 0) {
                this.shineFrame[i].setIsShow(true);
                atkRound[i] = 0;
            }
        }
    }

    protected abstract void updateGameObject();

    protected void updateProps() {
        for (int i = 0; i < this.props.size(); i++) {
            if (this.props.get(i) != null) {
                this.props.get(i).update();
                if (this.props.get(i).getIsStop()) {
                    this.props.set(i, null);
                }
            }
        }
    }

    //Paint
    protected abstract void paintGameObject(Graphics g);

    protected void paintProps(Graphics g) {
        for (int i = 0; i < this.props.size(); i++) {
            if (this.props.get(i) != null) {
                this.props.get(i).paintComponent(g);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (this.background != null) {
            this.background.paint(g);
        }
        this.item.paint(g);        
        this.blood.paintResize(g, this.ratio);
        paintGameObject(g);
        this.home.paint(g);
        this.player.paint(g);
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

        for (int i = 0; i < this.shineFrame.length; i++) {
            this.shineFrame[i].paint(g);
        }
        paintText(g);

        if (isLose()) {
            PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 48),
                    new Font("Showcard Gothic", Font.PLAIN, 54), Color.gray, Color.BLACK,
                    this.playerinfo.getName() + " Lose!", "", 0, 270, 2, Global.SCREEN_X);
            PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 22),
                    new Font("Showcard Gothic", Font.PLAIN, 24), Color.ORANGE, Color.BLACK,
                    "Press   \" ENTER \"  to MENU", "", 450, 500, 2, Global.SCREEN_X);
            if (overCount == 100) {
                overCount = 0;
            }
        } else if (isWin) {
            PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 48),
                    new Font("Showcard Gothic", Font.PLAIN, 54), Color.YELLOW, Color.BLACK,
                    this.playerinfo.getName() + " Win!", "You Gain", 0, 270, 2, Global.SCREEN_X);
            PaintText.paintTwinkle(g, new Font("Showcard Gothic", Font.PLAIN, 22),
                    new Font("Showcard Gothic", Font.PLAIN, 24), Color.ORANGE, Color.BLACK,
                    "Press   \" ENTER \"  to MENU", "", 450, 500, 2, Global.SCREEN_X);
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

        PaintText.paintWithShadow(g, new Font("VinerHandITC", Font.ITALIC, 20), Color.YELLOW,
                Color.BLACK, (int) this.currentHp + " / " + (int) this.myHp, 1120, Global.SCREEN_Y - 100, 2, 30);
        PaintText.paintWithNumber(g, new Font("Showcard Gothic", Font.PLAIN, 30),
                new Font("Showcard Gothic", Font.PLAIN, 36), Color.BLACK, Color.GRAY,
                "Battle", "" + (sceneCount + 1), 565, Global.SCREEN_Y - 40, 2, 20);
        PaintText.paintWithShadow(g, new Font("VinerHandITC", Font.PLAIN, 18), Color.BLACK,
                Color.GRAY, this.playerinfo.getName(), 740, Global.SCREEN_Y - 85, 1, 30);
        PaintText.paintWithShadow(g, new Font("VinerHandITC", Font.BOLD, 16), Color.BLACK,
                Color.GRAY, "Lv. " + this.playerinfo.getLevel(), 700, Global.SCREEN_Y - 40, 2, 30);
        PaintText.paintWithShadow(g, new Font("Showcard Gothic", Font.PLAIN, 24), Color.BLACK,
                Color.GRAY, "HP ", 525, Global.SCREEN_Y - 105, 2, 30);

        for (int i = 0; i < this.myMarbles.length; i++) {
            this.myMarbles[i].paintComponent(g);
            int x = (int) (this.myMarbles[i].getCenterX() + this.myMarbles[i].getR());
            int y = (int) (this.myMarbles[i].getCenterY() + this.myMarbles[i].getR());
            if (!this.shineFrame[i].getIsShow()) {
                PaintText.paintWithShadow(g, new Font("Showcard Gothic", Font.PLAIN, 24), new Color(255, 153, 0),
                        Color.BLACK, "" + atkRound[i], x, y, 2, 5);
            }
        }
        for (int i = 0; i < this.battleEnemies.size(); i++) {
            int enemyAtkRound = this.battleEnemies.get(i).getInfo().getSkillRound()
                    - this.enemyRound % this.battleEnemies.get(i).getInfo().getSkillRound();
            int x = (int) (this.battleEnemies.get(i).getCenterX() + this.battleEnemies.get(i).getR());
            int y = (int) (this.battleEnemies.get(i).getCenterY() + this.battleEnemies.get(i).getR());
            PaintText.paintWithShadow(g, new Font("Showcard Gothic", Font.PLAIN, 24), new Color(255, 153, 0),
                    Color.BLACK, "" + enemyAtkRound, x, y, 2, 5);
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
        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {
            if ((isLose() || isWin) && commandCode == Global.ENTER) {
                isEnter = true;
            }
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
        private boolean isPressed = false;
        private boolean isDrag = false;

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState mouseState, long trigTime) {
            home.update(e, mouseState);
            if (mouseState == CommandSolver.MouseState.PRESSED && e.getX() > Global.SCREEN_X - 50 - ImgInfo.SETTING_INFO[1] / 2 && e.getX() < Global.SCREEN_X - 50 + ImgInfo.SETTING_INFO[1] / 2
                    && e.getY() > Global.SCREEN_Y - 50 - ImgInfo.SETTING_INFO[1] / 2 && e.getY() < Global.SCREEN_Y - 50 + ImgInfo.SETTING_INFO[1] / 2) {
                isClick = true;
            }
            if (state == 1 && checkAllStop() && allSkillStop(battleEnemies)) {
                arrow.setResizeMag(0);
                if (mouseState == CommandSolver.MouseState.DRAGGED) {
                    if (!this.isPressed) {
                        arrow.setResizeMag(0);
                        this.startX = e.getX();
                        this.startY = e.getY();
                        this.isPressed = true;
                    }
                    if (this.startX < 0 || this.startX > Global.SCREEN_X || this.startY < 0 || this.startY > Global.SCREEN_Y - Global.INFO_H) {
                        this.isDrag = false;
                        this.isPressed = false;
                        return;
                    }
                    arrow.setCenterX(marbles.get(currentIdx).getCenterX());
                    arrow.setCenterY(marbles.get(currentIdx).getCenterY());
                    Vector vector = new Vector(this.startX - e.getX(), this.startY - e.getY());
                    if (this.startY - e.getY() > 0) {
                        arrow.setDegree((float) -Math.acos(vector.getX() / vector.getValue()));
                    } else {
                        arrow.setDegree((float) Math.acos(vector.getX() / vector.getValue()));
                    }
                    float value = vector.getValue();

                    if (value > 10f * marbles.get(currentIdx).getR()) {
                        value = 10f * marbles.get(currentIdx).getR();
                    } else if (value < 10) {
                        this.isDrag = false;
                        return;
                    }
                    arrow.setResizeMag(value / arrow.getWidth());
                    arrow.setShow(true);
                    this.isDrag = true;
                }
                if (mouseState == CommandSolver.MouseState.RELEASED && this.isDrag) {
                    this.endX = e.getX();
                    this.endY = e.getY();
                    Vector vec = new Vector(this.startX - this.endX, this.startY - this.endY);
                    arrow.setDegree((float) Math.acos(vec.getX() / vec.getValue()));
                    arrow.setResizeMag(vec.getValue() / arrow.getWidth());
                    marbles.get(currentIdx).setGo(vec.resizeVec(marbles.get(currentIdx).getInfo().getV()));
                    count++;
                    round++;
                    isCount = false;
                    this.isPressed = false;
                    arrow.setShow(false);
                }
            }

            if (state == 1 && checkAllStop() && allSkillStop(battleEnemies) && mouseState == CommandSolver.MouseState.PRESSED
                    && mouseInRange(e) && shineFrame[currentIdx].getIsShow()) {
                this.isDrag = false;
                marbles.get(currentIdx).useSkill(4, marbles, 0);
                atkRound[currentIdx] = marbles.get(currentIdx).getInfo().getSkillRound() + 1;
                shineFrame[currentIdx].setIsShow(false);
                count++;
                round++;
                isCount = false;
                resetBattle();
            } else if (mouseState == CommandSolver.MouseState.PRESSED) {
                this.isDrag = false;
            }
        }

        private boolean mouseInRange(MouseEvent e) {
            Marble m = myMarbles[currentIdx];
            if (e.getX() > m.getX() && e.getX() < m.getX() + m.getWidth()
                    && e.getY() > m.getY() && e.getY() < m.getY() + m.getHeight()) {
                return true;
            }
            return false;
        }
    }
}
