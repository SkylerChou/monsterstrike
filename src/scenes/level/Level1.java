/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes.level;

import controllers.SceneController;
import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;
import player.PlayerInfo;

public class Level1 extends LevelScene {

    private static final int IDX = 0;

    public Level1(SceneController sceneController, Marble[] myMarbles, ArrayList<Marble> enemies, PlayerInfo playerinfo) {
        super(sceneController, IDX, myMarbles, enemies, playerinfo);
    }

    @Override
    protected void updateGameObject() {
        updateProps();
    }

    @Override
    protected void genGameObject() {
        genBattleEnemies();
    }

    @Override
    protected void paintGameObject(Graphics g) {
        paintProps(g);
    }

}
