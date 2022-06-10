package com.project_1_2.group_16.io;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.files.FileHandle;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.misc.ANSI;
import com.project_1_2.group_16.themes.DefaultTheme;
import com.project_1_2.group_16.themes.MoonTheme;

/**
 * Auxiliary class for encoding the current level.
 */
public class LevelEncoder {

    /**
     * Date format used for creating the file name.
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");

    /**
     * Location where level files will be saved.
     */
    public static final String LOCATION = (App.OS_IS_WIN ? "./" : "../") + "/assets/levels/saved/";

    private static boolean alreadySaved;
    
    /**
     * Returns if the current level has already been encoded.
     * @return
     */
    public static boolean isEncoded() {
        return alreadySaved;
    }

    /**
     * Save the current level (from Input.java) as a .json file.
     * The file gets saved to the level folder in assets.
     */
    public static void encode() {
        // create level name
        String levelName = "saved-"+DATE_FORMAT.format(new Date());
        
        // create string representation of level
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("    \"starting_position\": {\n");
        builder.append("        \"x\": "+Input.V0.x+", \"y\": "+Input.V0.y+"\n");
        builder.append("    },\n\n");
        builder.append("    \"flag\": {\n");
        builder.append("        \"x\": "+Input.VT.x+", \"y\": "+Input.VT.y+",\n");
        builder.append("        \"radius\": "+Input.R+",\n");
        builder.append("        \"velocity\": "+Input.VH+"\n");
        builder.append("    },\n\n");
        builder.append("    \"terrain\": {\n");
        builder.append("        \"height_function\": \""+Input.H+"\",\n");
        builder.append("        \"trees\": [\n");
        for (int i = 0; i < Input.TREES.size(); i++) {
            builder.append("            {\"x\": "+Input.TREES.get(i).getPosition().x);
            builder.append(", \"y\": "+Input.TREES.get(i).getPosition().z);
            builder.append(", \"radius\": "+Input.TREES.get(i).getRadius()+"}");
            if (i != Input.TREES.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }
        builder.append("        ],\n");
        builder.append("        \"sandpits\": [\n");
        for (int i = 0; i < Input.SAND.size(); i++) {
            builder.append("            {\"x\": "+Input.SAND.get(i).getPosition().x);
            builder.append(", \"y\": "+Input.SAND.get(i).getPosition().y);
            builder.append(", \"radius\": "+Input.SAND.get(i).getRadius()+"}");
            if (i != Input.SAND.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }
        builder.append("        ],\n");
        if (!Input.WALLS.isEmpty()) {
            builder.append("        \"walls\": [\n");
            for (int i = 0; i < Input.WALLS.size(); i++) {
                builder.append("            {\"x\": "+Input.WALLS.get(i).getPosition().x);
                builder.append(", \"y\": "+Input.WALLS.get(i).getPosition().y);
                builder.append(", \"width\": "+Input.WALLS.get(i).getWidth());
                builder.append(", \"length\": "+Input.WALLS.get(i).getLength());
                builder.append(", \"type\": "+Input.WALLS.get(i).getType()+"}");
                if (i != Input.WALLS.size() - 1) {
                    builder.append(",");
                }
                builder.append("\n");
            }
            builder.append("        ],\n");
        }
        builder.append("        \"bicubic_input\": [\n");
        for (int r = 0; r < Input.BICUBIC_INPUT.length; r++) {
            builder.append("            [");
            for (int c = 0; c < Input.BICUBIC_INPUT.length; c++) {
                builder.append(Input.BICUBIC_INPUT[r][c]);
                if (c != Input.BICUBIC_INPUT.length - 1) {
                    builder.append(", ");
                }
            }
            builder.append("]");
            if (r != Input.BICUBIC_INPUT.length - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }
        builder.append("        ]\n");
        builder.append("    },\n\n");
        builder.append("    \"kinetic_friction\": "+Input.MUK+",\n");
        builder.append("    \"static_friction\": "+Input.MUS+",\n");
        builder.append("    \"kinetic_friction_sand\": "+Input.MUKS+",\n");
        builder.append("    \"static_friction_sand\": "+Input.MUSS+",\n\n");
        builder.append("    \"gravity\": "+Input.GRAVITY+",\n\n");
        if (Input.THEME instanceof DefaultTheme) {
            builder.append("    \"theme\": \"default\"\n");
        }
        if (Input.THEME instanceof MoonTheme) {
            builder.append("    \"theme\": \"moon\"\n");
        }
        builder.append("}");
        
        // create json file of level
        String json = builder.toString();
        FileHandle file = new FileHandle(LOCATION+levelName+".json");
        file.writeString(json, false);
        
        // confirm encoding
        alreadySaved = true;
        System.out.println(ANSI.GREEN+"saved level"+ANSI.RESET+" as: "+levelName);
    }
}
