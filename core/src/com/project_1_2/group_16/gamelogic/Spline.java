package com.project_1_2.group_16.gamelogic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.project_1_2.group_16.App;

public class Spline {

    /**
     * Represents the X-axis.
     */
    public static final int X = 1;

    /**
     * Represents the Y-axis.
     */
    public static final int Y = 2;

    /**
     * Size of the grid that makes up the spline.
     * Must be a minimum of 4.
     */
    public static final int SPLINE_SIZE = 4;

    /**
     * First matrix of coefficients used for the spline creation.
     */
    private static final float[][] C1 = {
        { 1,  0,  0,  0},
        { 0,  0,  1,  0},
        {-3,  3, -2, -1},
        { 2, -2,  1,  1}
    };

    /**
     * Second matrix of coefficients used for the spline creation.
     */
    private static final float[][] C2 = {
        {1, 0, -3,  2},
        {0, 0,  3, -2},
        {0, 1, -2,  1},
        {0, 0, -1,  1}
    };

    /**
     * All data points that make up the base of the spline.
     */
    private DataPoint[][] data;

    /**
     * All quadrants that you can create using the data points.
     */
    private CubicQuadrant[] quadrants;
    
    /**
     * Evaluates the height of a pair of coordinates.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return max(z-coordinate, -0.01)
     */
    public float getHeight(float x, float y) {
        for (Quadrant q : this.quadrants) {
            if (q.contains(x, y)) {
                return q.getHeight(x, y);
            }
        }
        return 0;
    }

    /**
     * Create the terrain-spline.
     * @param splineFile the spline file
     */
    public void createSpline(File splineFile) {
        // read the spline file, create the basis and calculate the derivatives
        this.data = this.createBasis(this.readSplineFile(splineFile));
        this.assignDerivatives(this.data);

        // create all quadrants
        this.quadrants = new CubicQuadrant[(SPLINE_SIZE - 1) * (SPLINE_SIZE - 1)];
        for (int x, y, i = 0; i < this.quadrants.length; i++) {
            y = i / (SPLINE_SIZE - 1);
            x = i % (SPLINE_SIZE - 1);
            this.quadrants[i] = new CubicQuadrant(this.data[y][x], this.data[y+1][x], this.data[y][x+1], this.data[y+1][x+1]);
            this.quadrants[i].A = multiplyMatrix(C1, multiplyMatrix(this.quadrants[i].getInitialValues(), C2));
        }
    }

    /**
     * Read the spline file.
     * @param splineFile
     * @return an array of all points from the spline file.
     * first 4 points are the corners, the rest is additional points.
     */
    private Vector3[] readSplineFile(File splineFile) {
        JsonReader reader = new JsonReader();
        List<Vector3> out = new ArrayList<Vector3>();

        // corners
        JsonValue corners = reader.parse(new FileHandle(splineFile)).child();
        for (JsonValue corner : corners.get("points")) {
            switch (corner.getString("name")) {
                case "negative-negative": {
                    out.add(new Vector3(-App.FIELD_SIZE/2, -App.FIELD_SIZE/2, corner.getFloat("height")));
                } break;
                case "negative-positive": {
                    out.add(new Vector3(-App.FIELD_SIZE/2, App.FIELD_SIZE/2, corner.getFloat("height")));
                } break;
                case "positive-negative": {
                    out.add(new Vector3(App.FIELD_SIZE/2, -App.FIELD_SIZE/2, corner.getFloat("height")));
                } break;
                case "positive-positive": {
                    out.add(new Vector3(App.FIELD_SIZE/2, App.FIELD_SIZE/2, corner.getFloat("height")));
                } break;
            }
        }

        // additional points
        JsonValue points = corners.next(); float x, y, z, i;
        for (JsonValue point : points.get("points")) {
            x = 0; y = 0; z = 0; i = 0;
            for (JsonValue coordinate : point.get("coordinates")) {
                switch ((int)i) {
                    case 0: {
                        x = Float.parseFloat(coordinate.toString());
                        i++;
                    } break;
                    case 1: {
                        y = Float.parseFloat(coordinate.toString());
                        i++;
                    } break;
                    case 2: {
                        z = Float.parseFloat(coordinate.toString());
                        i++;
                    } break;
                }
            }
            out.add(new Vector3(x, y, z));
        }
        System.out.println("spline: "+out); // debug
        return out.toArray(new Vector3[out.size()]);
    }

    /**
     * Create the basis of the spline.
     * @param input all input points (spline file)
     * @return the data matrix
     */
    private DataPoint[][] createBasis(Vector3[] input) {
        float size = App.FIELD_SIZE;
        DataPoint[][] data = new DataPoint[SPLINE_SIZE][SPLINE_SIZE];

        // configure corner points
        Vector3 corner_nn = new Vector3(), corner_np = new Vector3();
        Vector3 corner_pn = new Vector3(), corner_pp = new Vector3();
        for (int i = 0; i < 4; i++) {
            if (input[i].x < 0 && input[i].y < 0) corner_nn.set(input[i]);
            else if (input[i].x < 0 && input[i].y > 0) corner_np.set(input[i]);
            else if (input[i].x > 0 && input[i].y < 0) corner_pn.set(input[i]);
            else if (input[i].x > 0 && input[i].y > 0) corner_pp.set(input[i]);
        }

        // configure additional points
        Vector3[] points = new Vector3[input.length-4];
        for (int i = 4; i < input.length; i++) {
            points[i-4] = input[i];
        }

        // TODO temporary
        data[0][0] = new DataPoint(-size / 2, -size / 2, 0.5f);
        data[0][1] = new DataPoint(-size / 2 + size / 3, -size / 2, 0.1f);
        data[0][2] = new DataPoint(size / 2 - size / 3, -size / 2, 0.1f);
        data[0][3] = new DataPoint(size / 2, -size / 2, 0.5f);

        data[1][0] = new DataPoint(-size / 2, -size / 2 + size / 3, 0.1f);
        data[1][1] = new DataPoint(-size / 2 + size / 3, -size / 2 + size / 3, 0.1f);
        data[1][2] = new DataPoint(size / 2 - size / 3, -size / 2 + size / 3, 0.1f);
        data[1][3] = new DataPoint(size / 2, -size / 2 + size / 3, 0.1f);

        data[2][0] = new DataPoint(-size / 2, size / 2 - size / 3, 0.1f);
        data[2][1] = new DataPoint(-size / 2 + size / 3, size / 2 - size / 3, 0.1f);
        data[2][2] = new DataPoint(size / 2 - size / 3, size / 2 - size / 3, 0.1f);
        data[2][3] = new DataPoint(size / 2, size / 2 - size / 3, 0.1f);

        data[3][0] = new DataPoint(-size / 2, size / 2, 0.5f);
        data[3][1] = new DataPoint(-size / 2 + size / 3, size / 2, 0.1f);
        data[3][2] = new DataPoint(size / 2 - size / 3, size / 2, 0.1f);
        data[3][3] = new DataPoint(size / 2, size / 2, 0.5f);
        
        return data;
    }

    /**
     * Calculate the dX, dY and dXY for all data points.
     * Calculates derivatives using the forward-, backward-, and center-difference formulas.
     * @param data the data points
     */
    private void assignDerivatives(DataPoint[][] data) {
        // x-derivative
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data.length; x++) {
                if (x == 0) {
                    data[y][x].dX = forwardDifference(data[y][x], data[y][x + 1], data[y][x + 2], X);
                }
                else if (x == data.length - 1) {
                    data[y][x].dX = backwardDifference(data[y][x], data[y][x - 1], data[y][x - 2], X);
                }
                else {
                    data[y][x].dX = centerDifference(data[y][x], data[y][x - 1], data[y][x + 1], X);
                }
            }
        }

        // y-derivative
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data.length; x++) {
                if (y == 0) {
                    data[y][x].dY = forwardDifference(data[y][x], data[y + 1][x], data[y + 2][x], Y);
                }
                else if (y == data.length - 1) {
                    data[y][x].dY = backwardDifference(data[y][x], data[y - 1][x], data[y - 2][x], Y);
                }
                else {
                    data[y][x].dY = centerDifference(data[y][x], data[y - 1][x], data[y + 1][x], Y);
                }
            }
        }

        // mixed derivative
        // dXY = d(dX)Y
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data.length; x++) {
                if (y == 0) { // forward
                    data[y][x].dXY = (-data[y+2][x].dX + 4*data[y+1][x].dX - 3*data[y][x].dX) / (data[y+2][x].y - data[y][x].y);
                }
                else if (y == data.length - 1) { // backward
                    data[y][x].dXY = (data[y-2][x].dX - 4*data[y-1][x].dX + 3*data[y][x].dX) / (data[y][x].y - data[y-2][x].y);
                }
                else { // center
                    data[y][x].dXY = (data[y+1][x].dX - data[y-1][x].dX) / (data[y+1][x].y - data[y-1][x].y);
                }
            }
        }
    }

    /**
     * Calculates the derivative at p1 using the forward-difference formula.
     * @param p1 the starting (target) point
     * @param p2 the next point after p1
     * @param p3 the next point after p2
     * @return the derivative at p1
     */
    private float forwardDifference(DataPoint p1, DataPoint p2, DataPoint p3, int wrt) {
        return (-p3.z + 4 * p2.z - 3 * p1.z) / (wrt == X ? (p3.x - p1.x) : (p3.y - p1.y));
    }

    /**
     * Calculates the derivative at p1 using the backward-difference formula.
     * @param p1 the starting (target) point
     * @param p2 the previous point of p1
     * @param p3 the previous point of p2
     * @return the derivative at p1
     */
    private float backwardDifference(DataPoint p1, DataPoint p2, DataPoint p3, int wrt) {
        return (p3.z - 4 * p2.z + 3 * p1.z) / (wrt == X ? (p1.x - p3.x) : (p1.y - p3.y));
    }

    /**
     * Calculates the derivative at p1 using the center-difference formula.
     * @param p1 the starting (target) point
     * @param p2 the previous point of p1
     * @param p3 the next point after p1
     * @return the derivative at p1
     */
    private float centerDifference(DataPoint p1, DataPoint p2, DataPoint p3, int wrt) {
        return (p3.z - p2.z) / (wrt == X ? (p3.x - p2.x) : (p3.y - p2.y));
    }

    /**
     * Multiply 2 matrices together.
     * https://www.baeldung.com/java-matrix-multiplication (adapted)
     * @param A first matrix
     * @param B second matrix
     * @return resulting matrix
     */
    private static float[][] multiplyMatrix(float[][] A, float[][] B) {
        float[][] C = new float[A.length][B[0].length];
        float cell;
        for (int row = 0; row < C.length; row++) {
            for (int col = 0; col < C[row].length; col++) {
                cell = 0;
                for (int i = 0; i < B.length; i++) {
                    cell += A[row][i] * B[i][col];
                }
                C[row][col] = cell;
            }
        }
        return C;
    }











    

    /**
     * Represents a data point used in the base of a spline.
     */
    static class DataPoint extends Vector3 {

        /**
         * Derivative wrt x.
         */
        public float dX;

        /**
         * Derivative wrt y.
         */
        public float dY;

        /**
         * Mixed derivative.
         */
        public float dXY;

        public DataPoint(float x, float y, float z) {
            super(x, y, z);
        }

        @Override
        public String toString() {
            return "x: "+x+" y: "+y+" z: "+z+" dX: "+dX+" dY: "+dY+" dXY: "+dXY;
        }
    }

    /**
     * Represents a quadrant of the terrain (square bound by 4 data points).
     */
    static abstract class Quadrant {

        /**
         * Corners of the quadrant.
         */
        public Vector3 nn, np, pn, pp;

        public Quadrant(Vector3 c1, Vector3 c2, Vector3 c3, Vector3 c4) {
            this.nn = c1;
            this.np = c2;
            this.pn = c3;
            this.pp = c4;
        }

        /**
         * Checks if the specified coordinates lie within this quadrant
         * @param x x-coordinate
         * @param y y-coordinate
         * @return
         */
        public boolean contains(float x, float y) {
            if (x < this.nn.x) return false;
            if (x > this.pp.x) return false;
            if (y < this.nn.y) return false;
            if (y > this.pp.y) return false;
            return true;
        }

        /**
         * Get the height of a pair of coordinates that lie within this quadrant
         * @param x x-coordinate
         * @param y y-coordinate
         * @return z-coordinate
         */
        public float getHeight(float x, float y) {
            return 0;
        }
    }

    static class CubicQuadrant extends Quadrant {

        /**
         * Quadrant-coefficients.
         */
        public float[][] A;

        /**
         * Initial values.
         */
        public float[][] I;

        public CubicQuadrant(DataPoint c1, DataPoint c2, DataPoint c3, DataPoint c4) {
            super(c1, c2, c3, c4);
            this.I = new float[][] {
                {c1.z, c2.z, c1.dY, c2.dY},
                {c3.z, c4.z, c3.dY, c4.dY},
                {c1.dX, c2.dX, c1.dXY, c2.dXY},
                {c3.dX, c4.dX, c3.dXY, c4.dXY}
            };
        }

        /**
         * Get the initial values from the quadrant.
         * @return a 4x4 matrix made from the corner points
         */
        public float[][] getInitialValues() {
            return this.I;
        }
        
        @Override
        public float getHeight(float x, float y) {
            x = (x - nn.x) / (pp.x - nn.x);
            y = (y - nn.y) / (pp.y - nn.y);
            float[][] x_matrix = {{1, x, (float)Math.pow(x, 2), (float)Math.pow(x, 3)}};
            float[][] y_matrix = {{1}, {y}, {(float)Math.pow(y, 2)}, {(float)Math.pow(y, 3)}};
            return Math.max(multiplyMatrix(x_matrix, multiplyMatrix(this.A, y_matrix))[0][0], -0.01f);
        }
    }
}
