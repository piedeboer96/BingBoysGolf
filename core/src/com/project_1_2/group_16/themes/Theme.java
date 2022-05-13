package com.project_1_2.group_16.themes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * The theme provides all textures used in the application.
 */
public interface Theme {

    public Model golfballModel(float size);

    public Model flagModel(float r);

    public Model treeModel(AssetManager assets);

    public Color grassColorLight(float height);

    public Color grassColorDark(float height);

    public Color waterColorLight();

    public Color waterColorDark();

    public Color sandColorLight();

    public Color sandColorDark();

    public Color skyColor();

    public Skin getUISkin();

    public Image getMainBackground();

    public Image getAdvancedBackground();

    public Image getTerrainBackground();
}
