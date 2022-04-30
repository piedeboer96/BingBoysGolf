package com.project_1_2.group_16.themes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;

public interface Theme {

    public Model golfballModel(float size);

    public Model flagModel(float r);

    public Model treeModel(AssetManager assets);

    public Color grassColorLight(float height);

    public Color grassColorDark(float height);

    public Color skyColor();

    public Color waterColor(); 
    
    public Color sandColor();
}
