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
import com.project_1_2.group_16.themes.DefaultTheme;

// This class will create and color triangle models for the new terrain generation

public class TriangleModel extends Model {


    // Four points forming one rectangle which will be divided into two triangles later
    static Vector3 P1,P2,P3,P4;

    // The average height of the triangle which will be used later to determine which color the triangle has.
    static float avgHeight;
    static float a,b;
    static Material texture;
    Model triangle;
    static DefaultTheme defaultTheme = new DefaultTheme();

    /**
     * The constructor will take 4 points (3D vectors) that form rectangle and break it into two triangles
     * It will also give a color to the triangles based on the average height of the points.
     * @param p1 Top-left corner of the rectangle
     * @param p2 Top-right corner of the rectangle
     * @param p3 Bottom-left corner of the rectangle
     * @param p4 Bottom-right corner of the rectangle
     */
    public TriangleModel(Vector3 p1, Vector3 p2 , Vector3 p3 , Vector3 p4, float x, float z){

        P1 = p1;
        P2 = p2;
        P3 = p3;
        P4 = p4;
        this.a = x;
        this.b = z;

        // We use model builder to create a custom model which is two connected triangles
        ModelBuilder triangleBuilder = new ModelBuilder();
        triangleBuilder.begin();
        calculateAvgHeight(P1, P2, P3);
        setTriangleColor(avgHeight);
        // mesh builder will fill the triangles with the color
        MeshPartBuilder meshBuilder = triangleBuilder.part("triangle1", GL20.GL_TRIANGLES, Usage.Position,texture );
        meshBuilder.triangle(P1, P2, P3);

        /*calculateAvgHeight(P3, P2, P4);
        setTriangleColor(avgHeight);
        meshBuilder = triangleBuilder.part("triangle2", GL20.GL_TRIANGLES, Usage.Position,texture );*/
        meshBuilder.triangle(P3, P2, P4);

        //PLEASE DO NOT REMOVE THIS CODE
        // I will use it later for taking pictures for the presentation.
    /*    builder.setColor(Color.BLUE);
        builder.triangle(p1,p2,p3);
        builder.triangle(p4,p2,p3);*/

        // assign the custom model to the field (triangle)
        triangle = triangleBuilder.end();
    }

    /**
     * This method will assign a color for each triangle based on the average height
     * @param avgHeight the average height of triangle
     */
    public static void setTriangleColor (float avgHeight){
        if (avgHeight < 0){
            // if water
            texture = new Material(ColorAttribute.createDiffuse(defaultTheme.waterColor()));
        }
        else if (Collision.isInSandPit(a, b)) {
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
        return triangle;
    }
}
