package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.project_1_2.group_16.gamelogic.Collision;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.themes.DefaultTheme;

// This class will generate a good-looking and smooth terrain using triangulated mesh technique

public class TerrainGenerator extends Model {

    // The average height of the triangle which will be used later to determine which color each triangle has.
    static float avgHeight;
    static Material texture;
    public static Model terrainModel;
    static DefaultTheme defaultTheme = new DefaultTheme();

    /**
     * This method will draw a number of triangles to form a smooths terrain.
     * It will also give a color to the triangles based on the average height of the points.
     * @param FIELD_SIZE is the size of the whole map
     * @param TRIANGLE_BASE_SIZE controls the size of the triangle
     * @param FIELD_DETAIL is the number of rectangle which will be divided into two triangles
     */
    public static void generateTerrain(float FIELD_SIZE, float TRIANGLE_BASE_SIZE, float FIELD_DETAIL ){

        // We use model builder to create a custom terrain model which consist of large number of triangles connected together
        ModelBuilder triangleBuilder = new ModelBuilder();
        triangleBuilder.begin();

        // Those vars will determine the x,y and z coordinates of the corners of the triangles
        float a, b,c,d;
        for(int i = 0; i < FIELD_DETAIL-1; i++) {
            for(int j = 0; j < FIELD_DETAIL-1; j++) {
                a = -FIELD_SIZE / 2 + TRIANGLE_BASE_SIZE / 2 + TRIANGLE_BASE_SIZE * (i );
                b = -FIELD_SIZE / 2 + TRIANGLE_BASE_SIZE / 2 + TRIANGLE_BASE_SIZE * (j );
                c = -FIELD_SIZE / 2 + TRIANGLE_BASE_SIZE / 2 + TRIANGLE_BASE_SIZE * (i + 1);
                d = -FIELD_SIZE / 2 + TRIANGLE_BASE_SIZE / 2 + TRIANGLE_BASE_SIZE * (j + 1);

                /* p1 Top-left corner of the rectangle
                p2 Top-right corner of the rectangle
                p3 Bottom-left corner of the rectangle
                p4 Bottom-right corner of the rectangle*/
                Vector3 p1 = new Vector3(a, Terrain.getHeight(a, b),b);
                Vector3 p2 = new Vector3(a,Terrain.getHeight(a, d),d);
                Vector3 p3 = new Vector3(c,Terrain.getHeight(c, b),b);
                Vector3 p4 = new Vector3(c,Terrain.getHeight(c, d),d);

                // set a color for the triangle based on the average height
                calculateAvgHeight(p1, p2, p3);
                setTriangleColor(avgHeight,a,b);
                // mesh builder will fill the triangles with the color
                MeshPartBuilder meshBuilder = triangleBuilder.part("triangle", GL20.GL_TRIANGLES, Usage.Position,texture );
                meshBuilder.triangle(p1, p2, p3);
                calculateAvgHeight(p3, p2, p4);
                setTriangleColor(avgHeight,c,b);
                meshBuilder.triangle(p3, p2, p4);

                //PLEASE DO NOT REMOVE THIS CODE
                //I will use it later for taking pictures for the presentation.
              /* builder.setColor(Color.BLUE);
                 builder.triangle(p1,p2,p3);
                 builder.triangle(p4,p2,p3);*/
            }
        }
        
        // assign the custom terrain model to this field (terrainModel)
        terrainModel = triangleBuilder.end();
    }

    /**
     * This method will assign a color for each triangle based on the average height
     * @param avgHeight the average height of triangle
     * @param xCoor the x coordinate that is used to check weather this triangle is sand or not
     * @param zCoor the y coordinate that is used to check weather this triangle is sand or not
     */
    public static void setTriangleColor (float avgHeight, float xCoor, float zCoor){
        if (avgHeight < 0){
            // if water
            texture = new Material(ColorAttribute.createDiffuse(defaultTheme.waterColor()));
        }
        else if (Collision.isInSandPit(xCoor, zCoor)) {
            // sandpit texture
            texture = new Material(ColorAttribute.createDiffuse(defaultTheme.sandColor()));
        }
        else {
            // then it is grass
            texture = new Material(ColorAttribute.createDiffuse(defaultTheme.grassColor(avgHeight)));

        }

    }

    /**
     * It will calculate the average height of three 3D vectors (points) using their Y value (which is the height of each of them)
     * @param p1 3D vector which represents the first angle of a triangle.
     * @param p2 3D vector which represents the second angle of a triangle.
     * @param p3 3D vector which represents the third angle of a triangle.
     */
    public static void calculateAvgHeight(Vector3 p1 , Vector3 p2, Vector3 p3) {
        float temp = p1.y + p2.y + p3.y ;
        avgHeight = temp / 3;
    }

    /**
     * This method will return the model (two triangles) which is needed to be added to a Model instance for rendering
     * @return the two triangles model
     */
    public Model getModel() {
        return terrainModel;
    }
}
