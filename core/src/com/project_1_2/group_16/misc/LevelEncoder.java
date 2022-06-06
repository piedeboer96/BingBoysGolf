package com.project_1_2.group_16.misc;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Auxiliary class for encoding the current level.
 */
public class LevelEncoder {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");

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
        String levelName = "custom-level-"+DATE_FORMAT.format(new Date());
        System.out.println(ANSI.GREEN+"saved level"+ANSI.RESET+" as: "+levelName);

        // TODO

        alreadySaved = true;
    }
}
