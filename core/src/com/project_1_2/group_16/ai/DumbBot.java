package com.project_1_2.group_16.ai;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

public class DumbBot {
    private StateVector sv, init_sv;
    private Game game;
    private final Vector2 target = Input.VT;
    private final float maxV = 5f;
    private float c1;
    private float c2;

    public DumbBot(StateVector sv){
        this.sv = sv;
        this.init_sv = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
        this.game = new Game();
        ScaleC();
        sv = Play();
    }

    public void ScaleC(){
        c1 = target.x - sv.x;
        c2 = target.y - sv.y;

        c1 = c1 / (Math.abs(c1)+Math.abs(c2));
        c2 = c2 / (Math.abs(c1)+Math.abs(c2));

        c1 = c1*maxV;
        c2 = c2*maxV;
    }

    public StateVector Play(){
        sv.vx = c1;
        sv.vy = c2;
        return sv;
    }

}
