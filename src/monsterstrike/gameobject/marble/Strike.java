/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject.marble;

public interface Strike {
    
    public void update(Marble self);
    
    public void strike(Marble self, Marble target);
    
    public void move(Marble self);
    
//    public boolean die(Marble self);
}
