package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.io.Input;

/**
 * Class used for building the terrain model.
 */
public class TerrainBuilder extends ModelBuilder {

    /**
     * Begin building the terrain.
     */
    @Override
    public void begin() {
        super.begin();

        // create terrain
        float a, b, c, d;
        Vector3 p1, p2, p3, p4, avg;
        boolean checkerPattern;
        Material texture;
        for(int i = 0; i < App.FIELD_DETAIL - 1; i++) {
            for (int j = 0; j < App.FIELD_DETAIL - 1; j++) {
                // coordinates of the corners
                a = -App.FIELD_SIZE / 2 + App.TILE_SIZE / 2 + App.TILE_SIZE * i;
                b = -App.FIELD_SIZE / 2 + App.TILE_SIZE / 2 + App.TILE_SIZE * j;
                c = -App.FIELD_SIZE / 2 + App.TILE_SIZE / 2 + App.TILE_SIZE * (i + 1);
                d = -App.FIELD_SIZE / 2 + App.TILE_SIZE / 2 + App.TILE_SIZE * (j + 1);

                // corner points
                p1 = new Vector3(a, Terrain.getHeight(a, b), b);
                p2 = new Vector3(a, Terrain.getHeight(a, d), d);
                p3 = new Vector3(c, Terrain.getHeight(c, b), b);
                p4 = new Vector3(c, Terrain.getHeight(c, d), d);

                // apply a checker-style pattern 
                checkerPattern = (i + j) % 2 == 0;

                // apply the correct texture
                avg = averageVector(p1, p2, p3, p4);
                if (avg.y < 0) { // water texture
                    texture = new Material(ColorAttribute.createDiffuse(Math.random() < 0.5 ? Input.THEME.waterColorLight() : Input.THEME.waterColorDark()));
                }
                else if (Terrain.collision.isInSandPit(avg.x, avg.z)) { // sandpit texture
                    texture = new Material(ColorAttribute.createDiffuse(Math.random() < 0.8 ? Input.THEME.sandColorLight() : Input.THEME.sandColorDark()));
                }
                else { // grass texture (depending on height)
                    texture = new Material(ColorAttribute.createDiffuse(checkerPattern ? Input.THEME.grassColorLight(avg.y) : Input.THEME.grassColorDark(avg.y)));
                }

                // build the tile out of 2 triangle meshes
                MeshPartBuilder meshBuilder = super.part("triangle", GL20.GL_TRIANGLES, Usage.Position, texture);
                meshBuilder.triangle(p1, p2, p3);
                meshBuilder.triangle(p3, p2, p4);
            }
        }
    }

    @Override
    public Model end() {
        return super.end();
    } 
    
    /**
     * Calculates the average vector from an array of vectors.
     * @param p input vectors
     * @return
     */
    private Vector3 averageVector(Vector3... p) {
        float sumX = 0, sumY = 0, sumZ = 0;
        for (Vector3 v : p) {
            sumX += v.x;
            sumY += v.y;
            sumZ += v.z;
        }
        return new Vector3(sumX, sumY, sumZ).scl(1f / p.length);
    }
}
