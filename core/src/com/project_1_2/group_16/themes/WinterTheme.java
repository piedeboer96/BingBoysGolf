package com.project_1_2.group_16.themes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.project_1_2.group_16.models.Wall;

/**
 * A winter theme.
 */
public class WinterTheme implements Theme {

    private final Color sky = new Color(220f / 255, 255f / 255, 255f / 255, 1f);

    private final Color light_water = new Color(0.6475f, 0.9102f, 0.9847f, 1f);
    private final Color dark_water = new Color(0.6275f, 0.8902f, 0.9647f, 1f);

    private final Color light_sand = new Color(0.99f, 0.99f, 0.99f, 0.99f);
    private final Color dark_sand = new Color(0.98f, 0.98f, 0.98f, 1f);

    private final Color light_grass = new Color(0.8029f, 0.8147f, 0.8343f, 1f);
    private final Color dark_grass = new Color(0.7480f, 0.7755f, 0.8225f, 1f);

    private final Color hole_off = Color.RED;
    private final Color hole_on = Color.GREEN;

    private final Material wall = new Material(ColorAttribute.createDiffuse(Color.GRAY));
    private final Material tree_bumper = new Material(ColorAttribute.createDiffuse(0.411765f, 0.262745f, 0.176471f, 1),
                                                      ColorAttribute.createAmbient(0, 0, 0, 1));
    private final ModelBuilder builder = new ModelBuilder();

    private Model treeModel;

    @Override
    public Model golfballModel(float size) {
        Material golfball = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        return this.builder.createSphere(size, size, size, 10, 10, golfball, Usage.Position + Usage.Normal);
    }

    @Override
    public Model flagModel(float r, boolean off) { 
        Material poleMaterial = new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY));
        Material flagMaterial = new Material(ColorAttribute.createDiffuse(Color.RED));
        Material holeMaterial = new Material(ColorAttribute.createDiffuse(off ? this.hole_off : this.hole_on));

        Model pole = this.builder.createCylinder(.01f, 1.5f, .01f, 20, poleMaterial, Usage.Position + Usage.Normal);
        Model flag = this.builder.createBox(.25f, .125f, .005f, flagMaterial, Usage.Position + Usage.Normal);
        Model hole = this.builder.createCylinder(2*r, 0.1f, 2*r, 20, holeMaterial, Usage.Position);

        flag.nodes.get(0).translation.x = .125f;
        flag.nodes.get(0).translation.y = .6875f;
        hole.nodes.get(0).translation.y = -.04f;

        flag.nodes.addAll(pole.nodes);
        flag.nodes.addAll(hole.nodes);
        return flag;
    }

    @Override
    public Model treeModel(AssetManager assets) {
        if (this.treeModel == null) {
            assets.load("tree_model_winter.g3dj", Model.class);
            assets.finishLoading();
            this.treeModel = assets.get("tree_model_winter.g3dj", Model.class);
        }
        return this.treeModel;
    }

    @Override
    public Model treeBumper(float r) {
        return this.builder.createCylinder(2*r, 0.3f, 2*r, 20, this.tree_bumper, Usage.Position + Usage.Normal);
    }

    @Override
    public Model wallModel(float width, float height, float depth, int type) {
        if (type == Wall.MAZE_WALL) return this.builder.createBox(width, height, depth, wall, Usage.Position + Usage.Normal);
        else return this.builder.createBox(width, height + 0.025f, depth, 
        new Material(ColorAttribute.createDiffuse(hole_off)), Usage.Position + Usage.Normal);
    }

    @Override
    public Color grassColorLight(float height) {
        return light_grass;
    }

    @Override
    public Color grassColorDark(float height) {
        return dark_grass;
    }

    @Override
    public Color waterColorLight() {
        return this.light_water;
    }

    @Override
    public Color waterColorDark() {
        return this.dark_water;
    }

    @Override
    public Color sandColorLight() {
        return this.light_sand;
    }

    @Override
    public Color sandColorDark() {
        return this.dark_sand;
    }

    @Override
    public Color skyColor() {
        return this.sky;
    }
}
