package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.physics.Physics;

/**
 * Calculate score based on euclidean distance.
 */

public class Score {

    /**
     * Method which calculates the eucledian distance
     * @param xt  the x position of the hole
     * @param yt the y position of the hole
     * @param endx the x position of the particle
     * @param endy the y position of the particle
     * @return the eucledian distance
     */
    public static float calculateEucledianDistance(float xt, float yt, float endx, float endy){
        //will become floodfill output
        return (float) Math.sqrt(Math.pow(xt-endx, 2) + Math.pow(yt-endy, 2));
    }

    /**
     * Calculates a random valid velocity
     * @param minVelocity the minimum velocity
     * @param maxVelocity the maximum velocity
     * @return a velocity
     */
    public static float[] validVelocity(float minVelocity, float maxVelocity){
        float[] vxy = new float[2];
        vxy[0] = (float)(minVelocity + Math.random()*(maxVelocity-minVelocity));
        vxy[1] = (float)(minVelocity + Math.random()*(maxVelocity-minVelocity));
        if(Physics.magnitude(vxy[0], vxy[1]) > 5){
            vxy[0] = (float) (vxy[0]/Math.sqrt(50));
            vxy[1] = (float) (vxy[1]/Math.sqrt(50));
        }
        return vxy;
    }
}
