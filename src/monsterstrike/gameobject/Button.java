/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monsterstrike.gameobject;

import java.awt.event.MouseEvent;
import monsterstrike.util.CommandSolver;

/**
 *
 * @author yuin8
 */
public abstract class Button extends Component {

    public interface OnClickListener {
        public void onClick(Component c);
    }

    private OnClickListener listener;

    public Button(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    @Override
    public void update(MouseEvent e, CommandSolver.MouseState state) {
        // 前一幀的狀態 => 按下
        int lastState = currentState;
        super.update(e, state);
        // 當前這一幀的狀態 => Hover

        if (lastState == Component.STATE_PRESSED
                && currentState == Component.STATE_HOVER) {
            if(listener != null){
                listener.onClick(this);
            }
        }
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }
}
