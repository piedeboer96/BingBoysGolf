package com.project_1_2.group_16.themes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public interface Theme {

    public Material golfballMaterial();

    public Model flagModel(ModelBuilder builder);

    public Material holeMaterial();

    public Color grassColor(float height);

    public Color skyColor();

    public Color waterColor(); 
    
    public Color sandPitColor();

    public String treeModel();
}
