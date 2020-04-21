/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Props;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.ImgInfo;
import monsterstrike.gameobject.marble.Marble;
import monsterstrike.util.Global;

/**
 *
 * @author kim19
 */
public class Booster extends Prop {

    public Booster(String path, int x, int y, int width, int height, int r, int pictureNum, int delay) {
        super(path, x, y, width, height, r, pictureNum, delay);
    }

    @Override
    public void useProp(ArrayList<Marble> marble, int idx) {
        if (!isUsed) {
            marble.get(idx).setVelocity(1.2f);
//            System.out.println(marble.get(idx).getInfo().getName() + "加速");
            this.isUsed = true;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        this.renderer.paint(g, (int) (this.getX()), (int) (this.getY()),
                (int) (this.getWidth()), (int) (this.getHeight()),
                ImgInfo.SHOE_INFO[0], ImgInfo.SHOE_INFO[1]);
        if (Global.IS_DEBUG) {
            g.drawOval((int) (this.getCenterX() - this.getR()),
                    (int) (this.getCenterY() - this.getR()),
                    (int) (2 * this.getR()), (int) (2 * this.getR()));
        }
    }
}
