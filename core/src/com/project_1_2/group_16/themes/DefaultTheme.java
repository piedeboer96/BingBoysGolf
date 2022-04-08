package com.project_1_2.group_16.themes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class DefaultTheme implements Theme {

    private final Material golfball = new Material(ColorAttribute.createDiffuse(Color.WHITE));

    private final Material hole = new Material(ColorAttribute.createDiffuse(Color.RED));
    private final Color sky = new Color(178f / 255, 255f / 255, 255f / 255, 1f);
    private final Color water = new Color(0.1098f, 0.6392f, 0.9254f, 1f);
    private final Color sand = new Color(0.9411f, 0.9411f, 0.4313f, 1f);

    @Override
    public Material golfballMaterial() {
        return this.golfball;
    }

    @Override
    public Model flagModel(ModelBuilder builder) { //TODO
        Material flagMaterial = new Material(ColorAttribute.createDiffuse(Color.ORANGE));
        return builder.createBox(.05f, .05f, .05f, flagMaterial, Usage.Position + Usage.Normal);
    }

    @Override
    public Material holeMaterial() {
        return this.hole;
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
