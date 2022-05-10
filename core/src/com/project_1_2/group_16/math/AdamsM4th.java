package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Physics;

public class AdamsM4th implements NumericalSolver{
    RK4 rk4Bootstrap = new RK4();
    private float[] partialDerivatives;
    public int count = 0;
    StateVector sv1 = new StateVector(0,0,0,0);
    StateVector sv2 = new StateVector(0,0,0,0);
    StateVector sv3 = new StateVector(0,0,0,0);
    StateVector sv4 = new StateVector(0,0,0,0);
    @Override
    /**
     * Uses the Adams Moulton 4th order method
     */
    public void solve(float h, StateVector sv) {
        //RK4 bootstrapping for the first 3 iterations
        if(count<3){
            if(count==0) {
                rk4Bootstrap.solve(h, sv);
                sv1 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
            }else if (count==1){
                rk4Bootstrap.solve(h, sv);
                sv2 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
            } else if(count==2){
                rk4Bootstrap.solve(h, sv);
                sv3 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
            }
        }else {
            //Get derivatives to update ball position
            Derivation deriv1 = Derivation.getDerivation(new StateVector(sv.x, sv.y, sv.vx, sv.vy), h, new Derivation(), 0);
            Derivation deriv2 = Derivation.getDerivation(new StateVector(sv1.x, sv1.y, sv1.vx, sv1.vy), h, new Derivation(), 0);
            Derivation deriv3 = Derivation.getDerivation(new StateVector(sv2.x, sv2.y, sv2.vx, sv2.vy), h, new Derivation(), 0);
            Derivation deriv4 = Derivation.getDerivation(new StateVector(sv3.x, sv3.y, sv3.vx, sv3.vy), h, new Derivation(), 0);

            //predictor step
            float pos_x1, pos_y1, vel_x1, vel_y1;
            pos_x1 = sv.x + (h / 24f) * ((55 * deriv4.dx_dt) - (59 * deriv3.dx_dt) + (37 * deriv2.dx_dt) - (9 * deriv1.dx_dt));
            pos_y1 = sv.y + (h / 24f) * ((55 * deriv4.dy_dt) - (59 * deriv3.dy_dt) + (37 * deriv2.dy_dt) - (9 * deriv1.dy_dt));
            vel_x1 = sv.vx + (h / 24f) * ((55 * deriv4.dvx_dt) - (59 * deriv3.dvx_dt) + (37 * deriv2.dvx_dt) - (9 * deriv1.dvx_dt));
            vel_y1 = sv.vy + (h / 24f) * ((55 * deriv4.dvy_dt) - (59 * deriv3.dvy_dt) + (37 * deriv2.dvy_dt) - (9 * deriv1.dvy_dt));

            StateVector svCorr = new StateVector(pos_x1, pos_y1, vel_x1, vel_y1);
            Derivation derivCorr = Derivation.getDerivation(svCorr, h, new Derivation(), 0);

            //corrector step
            float pos_x1Corr, pos_y1Corr, vel_x1Corr, vel_y1Corr;

            pos_x1Corr = sv.x + (h / 24f) * ((9 * derivCorr.dx_dt) + (19 * deriv4.dx_dt) - (5 * deriv3.dx_dt) + (deriv2.dx_dt));
            pos_y1Corr = sv.y + (h / 24f) * ((9 * derivCorr.dy_dt) + (19 * deriv4.dy_dt) - (5 * deriv3.dy_dt) + (deriv2.dy_dt));
            vel_x1Corr = sv.vx + (h / 24f) * ((9 * derivCorr.dvx_dt) + (19 * deriv4.dvx_dt) - (5 * deriv3.dvx_dt) + (deriv2.dvx_dt));
            vel_y1Corr = sv.vy + (h / 24f) * ((9 * derivCorr.dvy_dt) + (19 * deriv4.dvy_dt) - (5 * deriv3.dvy_dt) + (deriv2.dvy_dt));



//          update ball position
            sv.x = pos_x1Corr;
            sv.y = pos_y1Corr;
            sv.vx = vel_x1Corr;
            sv.vy = vel_y1Corr;

            sv4.set(sv3);
            sv3.set(sv2);
            sv2.set(sv1);
            sv1.set(sv);

            this.partialDerivatives = Terrain.getSlope(new float[]{sv.x, sv.y}, h);
        }
        if (Physics.magnitude(sv.vx, sv.vy) < h) {
            float[] partialDerivatives = this.getPartialDerivatives();
            sv.stop = Physics.magnitude(partialDerivatives[0], partialDerivatives[1]) < Terrain.getStaticFriction(sv);
        }
        if(sv.stop){
            this.count = 0;
            sv1 = null; sv2 = null; sv3 = null; sv4 = null;
        }
        count++;
        System.out.println("iteration " + count);
    }

    @Override
    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
    //For testing
    public static void main(String[] args) {
        AdamsM4th am4 = new AdamsM4th();
        RK4 rk4 = new RK4();
        //RK2 rk2 = new RK2();
        int counter = 0;
        float t = 0;
        float t2 = 0;
        float h = 1f;
        StateVector sv = new StateVector(0,0,0, 2);
        StateVector sv2 = new StateVector(0,0,0,2);
        while(counter++ < 1){
            am4.solve(h, sv2);
            t = t+h;
            System.out.println(counter + " " +sv2);
        }
        System.out.println(t);
        System.out.println("BORDERLINE INSANITY");
        counter = 0;
        while(counter++ < 1){
            rk4.solve(h, sv);
            t2= t2+h;
            System.out.println(counter + " " + sv);
        }
        System.out.println("time " + t2);
    }
}
