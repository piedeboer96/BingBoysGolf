package com.project_1_2.group_16.themes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class DefaultTheme implements Theme {

    private final Material golfball = new Material(ColorAttribute.createDiffuse(Color.WHITE)); 
    private final Color sky = new Color(178f / 255, 255f / 255, 255f / 255, 1f);
    private final Color water = new Color(0.1098f, 0.6392f, 0.9254f, 1f);
    private final Color sand = new Color(0.9411f, 0.9411f, 0.4313f, 1f);

    @Override
    public Material golfballMaterial() {
        return this.golfball;
    }

    @Override
    public Model flagModel(ModelBuilder builder, float r) {
        Material poleMaterial = new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY));
        Material flagMaterial = new Material(ColorAttribute.createDiffuse(Color.RED));
        Material holeMaterial = new Material(ColorAttribute.createDiffuse(Color.RED));

        Model pole = builder.createCylinder(.01f, 1.5f, .01f, 20, poleMaterial, Usage.Position + Usage.Normal);
        Model flag = builder.createBox(.25f, .125f, .005f, flagMaterial, Usage.Position + Usage.Normal);
        Model hole = builder.createCylinder(r, 0.1f, r, 20, holeMaterial, Usage.Position);

        flag.nodes.get(0).translation.x = .125f;
        flag.nodes.get(0).translation.y = .6875f;
        hole.nodes.get(0).translation.y = -.04f;

        flag.nodes.addAll(pole.nodes);
        flag.nodes.addAll(hole.nodes);
        return flag;
    }

    @Override
    public Color grassColor(float height) {
        float greenValue = (100 + height * 100) / 255f;
        return new Color(0.1568f, greenValue, 0.1568f, 1f);
    }

    @Override
    public Color skyColor() {
        return this.sky;
    }

    @Override
    public Color waterColor() {
        return this.water;
    }

    @Override
    public Color sandPitColor() {
        return this.sand;
    }

    @Override
    public String treeModel() {
        return "tree_model_default.g3dj";
    }
    
}
