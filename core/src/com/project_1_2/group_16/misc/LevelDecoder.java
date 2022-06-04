package com.project_1_2.group_16.misc;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Spline;
import com.project_1_2.group_16.themes.DefaultTheme;

/**
 * Auxiliary class for decoding a pre-computed level.
 */
public class LevelDecoder {

    private static final JsonReader PARSER = new JsonReader();
    
    /**
     * Decode a pre-computed level, and parse it to the input file.
     * @param level file containing the level data
     */
    public static void decode(File level) {
        // get the full level
        JsonValue fullInput = PARSER.parse(new FileHandle(level));
        
        // start position
        JsonValue startPosition = fullInput.get("starting_position");
        Input.V0 = new Vector2(startPosition.getFloat("x"), startPosition.getFloat("y"));

        // flag position
        JsonValue flagPosition = fullInput.get("flag_position");
        Input.VT = new Vector2(flagPosition.getFloat("x"), flagPosition.getFloat("y"));
        Input.R = flagPosition.getFloat("radius");

        // terrain
        JsonValue terrain = fullInput.get("terrain");
        Input.H = terrain.getString("height_function");
        Input.TREES = terrain.getInt("number_of_trees");
        Input.SAND = terrain.getInt("number_of_sandpits");
        JsonIterator bicubicIterator = terrain.get("bicubic_input").iterator();
        Input.BICUBIC_INPUT = new float[Spline.SPLINE_SIZE][Spline.SPLINE_SIZE];
        for (int i = Spline.SPLINE_SIZE - 1; i >= 0; i--) {
            Input.BICUBIC_INPUT[i] = bicubicIterator.next().asFloatArray();
        }
        
        // friction coefficients
        Input.MUK = fullInput.getFloat("kinetic_friction");
        Input.MUS = fullInput.getFloat("static_friction");
        Input.MUKS = fullInput.getFloat("kinetic_friction_sand");
        Input.MUSS = fullInput.getFloat("static_friction_sand");

        // theme
        String theme = fullInput.getString("theme").toLowerCase();
        switch (theme) {
            default: Input.THEME = new DefaultTheme();
        }
    }
}
