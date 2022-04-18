package com.project_1_2.group_16;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.misc.User;

public class Input {
    /**
     * Starting position of the golfball.
     */
    public static Vector2 V0 = new Vector2(-3, 0);

    /**
     * Position of the hole.
     */
    public static Vector2 VT = new Vector2(4, 1);


    /**
     * Position of the hole.
     */
    public static Vector2 V = new Vector2(2.6077209f, 1.4741254f);

    /**
     * Radius of the hole.
     */
    public static float R = 0.1f;

    /**
     * Kinetic Friction.
     */
    public static float MUK = 0.05f;

    /**
     * Static Friction.
     */
    public static float MUS = 0.2f;

    /**
     * Kinetic Friction (sandpit).
     */
    public static float MUKS = 0.32f;

    /**
     * Static Friction (sandpit).
     */
    public static float MUSS = 0.8f;

    /**
     * Height Function.
     */
    public static String H = "0.4*(0.9 - Math.pow(Math.E, (-1*(x*x+y*y))/8))";

    /**
     * Number of trees.
     */
    public static int TREES = 0;

    /**
     * Number of sandpits.
     */
    public static int SAND = 0;

    /**
     * Type of user playing the game.
     */
    public static User USER = User.USER;
}
