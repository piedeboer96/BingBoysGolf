package com.project_1_2.group_16.misc;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Sandpit;
import com.project_1_2.group_16.gamelogic.Spline;
import com.project_1_2.group_16.models.Tree;
import com.project_1_2.group_16.models.Wall;
import com.project_1_2.group_16.themes.DefaultTheme;
import com.project_1_2.group_16.themes.MoonTheme;

/**
 * Auxiliary class for decoding a pre-computed level.
 */
public class LevelDecoder {

    private static final JsonReader PARSER = new JsonReader();
    
    /**
     * Decode a pre-computed level, and parse it to the input file.
     * @param level file containing the level data
     */
    public static void decode(FileHandle level) {
        // get the full level
        JsonValue fullInput = PARSER.parse(level);
        
        // start position
        JsonValue startPosition = fullInput.get("starting_position");
        Input.V0 = new Vector2(startPosition.getFloat("x"), startPosition.getFloat("y"));

        // flag elements
        JsonValue flag = fullInput.get("flag");
        Input.VT = new Vector2(flag.getFloat("x"), flag.getFloat("y"));
        Input.R = flag.getFloat("radius");
        Input.VH = flag.getFloat("velocity");

        // terrain
        JsonValue terrain = fullInput.get("terrain");
        Input.H = terrain.getString("height_function");
        JsonIterator treeIterator = terrain.get("trees").iterator();
        Input.TREES = new ArrayList<Tree>();
        JsonValue tree;
        while (treeIterator.hasNext()) {
            tree = treeIterator.next();
            Input.TREES.add(new Tree(tree.getFloat("x"), tree.getFloat("y"), tree.getFloat("radius")));
        }
        JsonIterator sandIterator = terrain.get("sandpits").iterator();
        Input.SAND = new ArrayList<Sandpit>();
        JsonValue sandpit;
        while (sandIterator.hasNext()) {
            sandpit = sandIterator.next();
            Input.SAND.add(new Sandpit(sandpit.getFloat("x"), sandpit.getFloat("y"), sandpit.getFloat("radius")));
        }
        JsonIterator bicubicIterator = terrain.get("bicubic_input").iterator();
        Input.BICUBIC_INPUT = new float[Spline.SPLINE_SIZE][Spline.SPLINE_SIZE];
        for (int i = 0; i < Spline.SPLINE_SIZE; i++) {
            Input.BICUBIC_INPUT[i] = bicubicIterator.next().asFloatArray();
        }

        // walls (optional for maze)
        Input.WALLS = new ArrayList<Wall>();
        try {
            JsonIterator wallIterator = terrain.get("walls").iterator();
            JsonValue wall; Vector2 pos;
            while (wallIterator.hasNext()) {
                wall = wallIterator.next();
                pos = new Vector2(wall.getFloat("x"), wall.getFloat("y"));
                Input.WALLS.add(new Wall(pos, wall.getFloat("width"), wall.getFloat("length"), wall.getInt("type")));
            }
        } catch (NullPointerException e) {};
        
        // friction coefficients
        Input.MUK = fullInput.getFloat("kinetic_friction");
        Input.MUS = fullInput.getFloat("static_friction");
        Input.MUKS = fullInput.getFloat("kinetic_friction_sand");
        Input.MUSS = fullInput.getFloat("static_friction_sand");

        // gravity
        Input.GRAVITY = fullInput.getFloat("gravity");

        // theme
        String theme = fullInput.getString("theme").toLowerCase();
        switch (theme) {
            case "moon": Input.THEME = new MoonTheme(); break;
            default: Input.THEME = new DefaultTheme();
        }
    }
}
