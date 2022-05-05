package com.project_1_2.group_16.gamelogic;

import java.io.File;

import com.badlogic.gdx.math.Vector3;
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
    private Quadrant[] quadrants;
    
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
     * 
     * @throws ...
     */
    public void createSpline(File splineFile) {
        // read the spline file and calculate the derivatives
        this.data = getData(splineFile);
        this.assignDerivatives(this.data);

        // create all quadrants
        this.quadrants = new Quadrant[(SPLINE_SIZE - 1) * (SPLINE_SIZE - 1)];
        for (int x, y, i = 0; i < this.quadrants.length; i++) {
            y = i / 3;
            x = i % 3;
            this.quadrants[i] = new Quadrant(this.data[y][x], this.data[y+1][x], this.data[y+1][x+1], this.data[y][x+1]);
            this.quadrants[i].A = multiplyMatrix(C1, multiplyMatrix(this.quadrants[i].getInitialValues(), C2));
        }
    }

    /**
     * Reads the spline file. TODO
     * @param splineFile
     * @return an array containing all data from the spline file
     */
    private DataPoint[][] getData(File splineFile) {
        float size = App.FIELD_SIZE;
        DataPoint[][] data = new DataPoint[SPLINE_SIZE][SPLINE_SIZE]; 

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
    static class Quadrant {

        /**
         * Corner of the quadrant.
         */
        public DataPoint nn, np, pn, pp;

        /**
         * Quadrant-coefficients.
         */
        public float[][] A;

        public Quadrant(DataPoint c1, DataPoint c2, DataPoint c3, DataPoint c4) {
            this.nn = c1;
            this.np = c2;
            this.pp = c3;
            this.pn = c4;
        }

        /**
         * Get the initial values from the quadrant.
         * @return a 4x4 matrix made from the corner points
         */
        public float[][] getInitialValues() {
            return new float[][] {
                {nn.z, np.z, nn.dY, np.dY},
                {pn.z, pp.z, pn.dY, pp.dY},
                {nn.dX, np.dX, nn.dXY, np.dXY},
                {pn.dX, pp.dX, pn.dXY, pp.dXY}
            };
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
            x = (x - nn.x) / (pp.x - nn.x);
            y = (y - nn.y) / (pp.y - nn.y);
            float[][] x_matrix = {{1, x, (float)Math.pow(x, 2), (float)Math.pow(x, 3)}};
            float[][] y_matrix = {{1}, {y}, {(float)Math.pow(y, 2)}, {(float)Math.pow(y, 3)}};
            return Math.max(multiplyMatrix(x_matrix, multiplyMatrix(this.A, y_matrix))[0][0], -0.01f);
        }
    }
}
