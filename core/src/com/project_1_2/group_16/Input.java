package com.project_1_2.group_16;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.themes.Theme;

/**
 * Handles all user input for the game.
 */
public class Input {

    /**
     * Starting position of the golfball.
     */
    public static Vector2 V0;

    /**
     * Position of the hole.
     */
    public static Vector2 VT;

    /**
     * Radius of the hole.
     */
    public static float R;

    /**
     * Height Function.
     */
    public static String H;

    /**
     * Number of trees.
     */
    public static int TREES;

    /**
     * Number of sandpits.
     */
    public static int SAND;

    /**
     * Pre-computed spline for bicubic interpolation.
     */
    public static float[][] BICUBIC_INPUT;

    /**
     * Kinetic Friction.
     */
    public static float MUK;

    /**
     * Static Friction.
     */
    public static float MUS;

    /**
     * Kinetic Friction (sandpit).
     */
    public static float MUKS;

    /**
     * Static Friction (sandpit).
     */
    public static float MUSS;

    /**
     * The theme (textures) for the game.
     */
    public static Theme THEME;
}
