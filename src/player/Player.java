/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Graphics;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.Renderer;

public class Player {

    private PlayerInfo info;
    private Renderer rendererRun;
    private Renderer rendererStand;
    private int state;
    private int idx;
    private int x;
    private int y;
    private int w;
    private int h;

    public Player(int idx, int dir, int x, int y, int w, int h) {
        this.idx = idx;
        this.rendererRun = new Renderer(ImgInfo.PLAYERRUN_PATH[idx],
                ImgInfo.PLAYERRUN_NUM[idx], 10);
        this.rendererStand = new Renderer(ImgInfo.PLAYERSTAND_PATH[idx][dir],
                ImgInfo.PLAYERSTAND_NUM[idx], 10);
        this.state = 0;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setName(String name) {
        info.setName(name);
    }

    public void setState(int state) {
        this.state = state;
    }

    public void update() {
        if (state == 0) {
            this.rendererStand.update();
        } else {
            this.rendererRun.update();
        }
    }

    public void paint(Graphics g) {
        if (state == 0) {
            this.rendererStand.paint(g, x, y, w, h,
                    ImgInfo.PLAYERSTAND_INFO[idx][0], ImgInfo.PLAYERSTAND_INFO[idx][1]);
        } else {
            this.rendererRun.paint(g, x, y, w, h,
                    ImgInfo.PLAYERSTAND_INFO[idx][0], ImgInfo.PLAYERRUN_INFO[idx][1]);
        }

    }

}
