package com.project_1_2.group_16.themes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.project_1_2.group_16.App;

/**
 * The default theme for the application.
 */
public class DefaultTheme implements Theme {

    private final Color sky = new Color(178f / 255, 255f / 255, 255f / 255, 1f);

    private final Color light_water = new Color(0.1098f, 0.6275f, 0.9254f, 1f);
    private final Color dark_water = new Color(0.1098f, 0.6392f, 0.9254f, 1f);

    private final Color light_sand = new Color(0.9411f, 0.9411f, 0.6667f, 1f);
    private final Color dark_sand = new Color(0.9020f, 0.9020f, 0.6667f, 1f);

    @Override
    public Model golfballModel(float size) {
        ModelBuilder builder = new ModelBuilder();
        Material golfball = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        return builder.createSphere(size, size, size, 10, 10, golfball, Usage.Position + Usage.Normal);
    }

    @Override
    public Model flagModel(float r) {
        ModelBuilder builder = new ModelBuilder();
        
        Material poleMaterial = new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY));
        Material flagMaterial = new Material(ColorAttribute.createDiffuse(Color.RED));
        Material holeMaterial = new Material(ColorAttribute.createDiffuse(Color.RED));

        Model pole = builder.createCylinder(.01f, 1.5f, .01f, 20, poleMaterial, Usage.Position + Usage.Normal);
        Model flag = builder.createBox(.25f, .125f, .005f, flagMaterial, Usage.Position + Usage.Normal);
        Model hole = builder.createCylinder(2*r, 0.1f, 2*r, 20, holeMaterial, Usage.Position);

        flag.nodes.get(0).translation.x = .125f;
        flag.nodes.get(0).translation.y = .6875f;
        hole.nodes.get(0).translation.y = -.04f;

        flag.nodes.addAll(pole.nodes);
        flag.nodes.addAll(hole.nodes);
        return flag;
    }

    @Override
    public Model treeModel(AssetManager assets) {
        assets.load("tree_model_default.g3dj", Model.class);
        assets.finishLoading();
        return assets.get("tree_model_default.g3dj", Model.class);
    }

    @Override
    public Color grassColorLight(float height) {
        return new Color(0.1568f, (200 + height * 100) / 255f, 0.1568f, 1f);
    }

    @Override
    public Color grassColorDark(float height) {
        return new Color(0.1568f, (100 + height * 100) / 255f, 0.1568f, 1f);
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

    @Override
    public Skin getUISkin() {
        return new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    @Override
    public Image getMainBackground() {
        // https://stackoverflow.com/questions/16886228/java-libgdx-how-do-i-resize-my-textures-in-libgdx
        Pixmap p1 = new Pixmap(Gdx.files.internal("background.png"));
        Pixmap p2 = new Pixmap(App.SCREEN_WIDTH, App.SCREEN_HEIGHT, p1.getFormat());
        p2.drawPixmap(p1, 0, 0, p1.getWidth(), p1.getHeight(), 0, 0, p2.getWidth(), p2.getHeight());
        return new Image(new Texture(p2));
    }

    @Override
    public Image getAdvancedBackground() {
        return this.getMainBackground();
    }

    @Override
    public Image getTerrainBackground() {
        return this.getMainBackground();
    }
}
