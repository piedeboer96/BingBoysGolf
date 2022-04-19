//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

import java.util.Random;

public class RuleBasedBot {
    static final int Population = 100;
    static Random rand = new Random();
    static final float max = 1.0F;
    static int score;
    static int bestScore;
    static boolean scoreInitialise = false;
    static float float_randomX;
    static float float_randomY;
    public static StateVector sv;
    public static StateVector newsv;

    public RuleBasedBot() {
    }

    public static void BestShot() {
        for(int i = 0; i < Population; ++i) {
            float_randomX = rand.nextFloat() * 1.0F;
            float_randomY = rand.nextFloat() * 1.0F;
            sv = new StateVector(0.0F, 0.0F, float_randomX, float_randomY);
            //score = Game.runWithAI(sv);
            if (!scoreInitialise || bestScore < score) {
                scoreInitialise = true;
                bestScore = score;
                newsv = sv;
                System.out.println(bestScore);
            }
        }

    }
}
