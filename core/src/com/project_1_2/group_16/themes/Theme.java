package com.project_1_2.group_16.themes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public interface Theme {

    public Model golfballModel(ModelBuilder builder, float size);

    public Model flagModel(ModelBuilder builder, float r);

    public Model treeModel(AssetManager assets);

    public Color grassColor(float height);

    public Color skyColor();

    public Color waterColor(); 
    
    public Color sandColor();
}
