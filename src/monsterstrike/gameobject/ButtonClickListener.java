/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import monsterstrike.gameobject.Button.OnClickListener;

/**
 *
 * @author kim19
 */
public class ButtonClickListener implements OnClickListener {

        @Override
        public void onClick(Component c) {
            System.out.println(c);
        }
    }