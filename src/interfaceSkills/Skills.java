/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceskills;

import java.awt.Graphics;
import java.util.ArrayList;
import monsterstrike.gameobject.marble.Marble;

public interface Skills {

    public void genSkill(Marble self, Marble target);
    
    public void genSkill(Marble self, ArrayList<Marble> target);

    public void update();

    public ArrayList<SkillComponent> getSkillComponent();
    
    public void paintSkill(Graphics g);

}
