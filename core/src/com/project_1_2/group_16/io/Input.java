package com.project_1_2.group_16.io;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.gamelogic.Sandpit;
import com.project_1_2.group_16.models.Tree;
import com.project_1_2.group_16.models.Wall;
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
     * Maximum velocity allowed for a hole to count.
     */
    public static float VH;

    /**
     * Height Function.
     */
    public static String H;

    /**
     * If the game should use pre-defined or random obstacles.
     */
    public static boolean RANDOM_OBSTACLES;

    /**
     * Number of random trees (only applicable if RANDOM_OBSTACLES is true).
     */
    public static int NUMBER_OF_TREES;

    /**
     * Number of random sandpits (only applicable if RANDOM_OBSTACLES is true).
     */
    public static int NUMBER_OF_SANDPITS;

    /**
     * Trees.
     */
    public static List<Tree> TREES;

    /**
     * Sandpits.
     */
    public static List<Sandpit> SAND;

    /**
     * Walls.
     */
    public static List<Wall> WALLS;

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
     * Gravity constant.
     */
    public static float GRAVITY;

    /**
     * The theme (textures) for the game.
     */
    public static Theme THEME;
}
