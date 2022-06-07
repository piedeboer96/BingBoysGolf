package com.project_1_2.group_16.themes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;

/**
 * The theme provides all textures used in the application.
 */
public interface Theme {

    public Model golfballModel(float size);

    public Model flagModel(float r, boolean off);

    public Model treeModel(AssetManager assets);

    public Model treeBumper(float r);

    public Model wallModel(float width, float height, float depth);

    public Color grassColorLight(float height);

    public Color grassColorDark(float height);

    public Color waterColorLight();

    public Color waterColorDark();

    public Color sandColorLight();

    public Color sandColorDark();

    public Color skyColor();
}
