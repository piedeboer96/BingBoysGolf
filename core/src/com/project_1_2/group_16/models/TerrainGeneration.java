package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Collision;
import com.project_1_2.group_16.gamelogic.Terrain;

public class TerrainGeneration extends ModelBuilder implements Runnable {

    /**
     * Thread used to create the terrain model
     */
    public Thread thread;

    private Collision collision;
    
    public TerrainGeneration(Collision collision) {
        this.collision = collision;
        this.thread = new Thread(this);
    }

    @Override
    public void begin() {
        super.begin();
        this.thread.start();
    }

    @Override
    public void run() {
        float a, b, c, d;
        Vector3 p1, p2, p3, p4, avg;
        boolean checkerPattern;
        Material texture;
        for(int i = 0; i < App.FIELD_DETAIL - 1; i++) {
            for (int j = 0; j < App.FIELD_DETAIL - 1; j++) {
                a = -App.FIELD_SIZE / 2 + App.TILE_SIZE / 2 + App.TILE_SIZE * i;
                b = -App.FIELD_SIZE / 2 + App.TILE_SIZE / 2 + App.TILE_SIZE * j;
                c = -App.FIELD_SIZE / 2 + App.TILE_SIZE / 2 + App.TILE_SIZE * (i + 1);
                d = -App.FIELD_SIZE / 2 + App.TILE_SIZE / 2 + App.TILE_SIZE * (j + 1);

                p1 = new Vector3(a, Terrain.getHeight(a, b), b);
                p2 = new Vector3(a, Terrain.getHeight(a, d), d);
                p3 = new Vector3(c, Terrain.getHeight(c, b), b);
                p4 = new Vector3(c, Terrain.getHeight(c, d), d);

                checkerPattern = (i + j) % 2 == 0;

                avg = averageVector(p1, p2, p3, p4);
                if (avg.y < 0) { // water texture
                    texture = new Material(ColorAttribute.createDiffuse(App.THEME.waterColor()));
                }
                else if (this.collision.isInSandPit(avg.x, avg.z)) { // sandpit texture
                    texture = new Material(ColorAttribute.createDiffuse(App.THEME.sandColor()));
                }
                else { // grass texture (depending on height)
                    texture = new Material(ColorAttribute.createDiffuse(checkerPattern ? App.THEME.grassColorLight(avg.y) : App.THEME.grassColorDark(avg.y)));
                }

                MeshPartBuilder meshBuilder = super.part("triangle", GL20.GL_TRIANGLES, Usage.Position, texture);
                meshBuilder.triangle(p1, p2, p3);
                meshBuilder.triangle(p3, p2, p4);
            }
        }
    }

    public Vector3 averageVector(Vector3... p) {
        float sumX = 0, sumY = 0, sumZ = 0;
        for (Vector3 v : p) {
            sumX += v.x;
            sumY += v.y;
            sumZ += v.z;
        }
        return new Vector3(sumX, sumY, sumZ).scl(1f / p.length);
    }
}
