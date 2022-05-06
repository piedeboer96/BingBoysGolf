package com.project_1_2.group_16.math;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Physics;

public class AdamsM4th implements NumericalSolver{
    RK4 rk4Bootstrap = new RK4();
    private float[] partialDerivatives;
    public int count = 0;
    StateVector sv1;
    StateVector sv2;
    StateVector sv3;
    StateVector sv4;
    @Override
    public void solve(float h, StateVector sv) {
        if(count<4){
            if(count==0) {
                rk4Bootstrap.solve(h, sv);
                sv1 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
                count++;
            }else if (count==1){
                rk4Bootstrap.solve(h, sv);
                sv2 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
                count++;
            } else if(count==2){
                rk4Bootstrap.solve(h, sv);
                sv3 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
                count++;
            } else if(count == 3){
                rk4Bootstrap.solve(h, sv);
                sv4 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);
                this.partialDerivatives = Terrain.getSlope(new float[] {sv.x, sv.y}, h);
                count++;
            }
        }else {
            Derivation deriv1 = Derivation.getDerivation(sv1, h, new Derivation(), 0);
            Derivation deriv2 = Derivation.getDerivation(sv2, h, new Derivation(), 0);
            Derivation deriv3 = Derivation.getDerivation(sv3, h, new Derivation(), 0);
            Derivation deriv4 = Derivation.getDerivation(sv4, h, new Derivation(), 0);

            float pos_x1, pos_y1, vel_x1, vel_y1;
            pos_x1 = sv.x + (h / 24f) * (55 * deriv4.dx_dt - 59 * deriv3.dx_dt + 37 * deriv2.dx_dt - 9 * deriv1.dx_dt);
            pos_y1 = sv.y + (h / 24f) * (55 * deriv4.dy_dt - 59 * deriv3.dy_dt + 37 * deriv2.dy_dt - 9 * deriv1.dy_dt);
            vel_x1 = sv.vx + (h / 24f) * (55 * deriv4.dvx_dt - 59 * deriv3.dvx_dt + 37 * deriv2.dvx_dt - 9 * deriv1.dvx_dt);
            vel_y1 = sv.vy + (h / 24f) * (55 * deriv4.dvy_dt - 59 * deriv3.dvy_dt + 37 * deriv2.dvy_dt - 9 * deriv1.dvy_dt);

            StateVector svCorr = new StateVector(pos_x1, pos_y1, vel_x1, vel_y1);
            Derivation derivCorr = Derivation.getDerivation(svCorr, h, new Derivation(), 0);

            float pos_x1Corr, pos_y1Corr, vel_x1Corr, vel_y1Corr;
            pos_x1Corr = sv.x + (h / 24f) * (9 * derivCorr.dx_dt + 19 * deriv4.dx_dt - 5 * deriv3.dx_dt + deriv2.dx_dt);
            pos_y1Corr = sv.y + (h / 24f) * (9 * derivCorr.dy_dt + 19 * deriv4.dy_dt - 5 * deriv3.dy_dt + deriv2.dy_dt);
            vel_x1Corr = sv.vx + (h / 24f) * (9 * derivCorr.dvx_dt + 19 * deriv4.dvx_dt - 5 * deriv3.dvx_dt + deriv2.dvx_dt);
            vel_y1Corr = sv.vy + (h / 24f) * (9 * derivCorr.dvy_dt + 19 * deriv4.dvy_dt - 5 * deriv3.dvy_dt + deriv2.dvy_dt);


            sv.x = pos_x1Corr;
            sv.y = pos_y1Corr;
            sv.vx = vel_x1Corr;
            sv.vy = vel_y1Corr;

            sv4 = sv3;
            sv3 = sv2;
            sv2 = sv1;
            sv1 = new StateVector(sv.x, sv.y, sv.vx, sv.vy);

            this.partialDerivatives = Terrain.getSlope(new float[]{sv.x, sv.y}, h);
        }
        if (Physics.magnitude(sv.vx, sv.vy) < h) {
            float[] partialDerivatives = this.getPartialDerivatives();
            sv.stop = Physics.magnitude(partialDerivatives[0], partialDerivatives[1]) < Terrain.getStaticFriction(sv);
        }
        if(sv.stop){
            this.count = 0;
        }
    }

    @Override
    public float[] getPartialDerivatives() {
        return this.partialDerivatives;
    }
}
