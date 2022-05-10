package com.project_1_2.group_16.gamelogic;
 
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.misc.ANSI;

import bsh.EvalError;
import bsh.Interpreter;

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
    public static final int SPLINE_SIZE = 16;

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
     * String interpreter.
     */
    private static final Interpreter BSH = new Interpreter();

    /**
     * All knot points that make up the base of the spline.
     */
    private KnotPoint[][] knots;

    /**
     * All quadrants that you can create using the knot points.
     */
    private Quadrant[] quadrants;

    /**
     * The height function used for the terrain.
     */
    private String heightFunction;

    /**
     * All input points that describe any custom elements of the terrain.
     */
    private float[][] input;

    public Spline(String heightFunction, float[][] input) {
        this.heightFunction = heightFunction;
        this.input = input;
    }
    
    /**
     * Evaluates the height of a pair of coordinates.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return max(z-coordinate, -0.01)
     */
    public float getHeight(float x, float y) {
        // make everything outside of the rendered area water
        if (Math.abs(x) > Terrain.WATER_EDGE || Math.abs(y) > Terrain.WATER_EDGE) {
            return -1;
        }

        // calculate the height within the relevant quadrant
        for (Quadrant q : this.quadrants) {
            if (q.contains(x, y)) {
                return q.getHeight(x, y);
            }
        }
        return 0;
    }

    /**
     * Evaluates the height of a pair of coordinates using the height function.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return max(z-coordinate, -0.01)
     */
    public float getHeightFunction(float x, float y) {
        // make everything outside of the rendered area water
        if (Math.abs(x) > Terrain.WATER_EDGE || Math.abs(y) > Terrain.WATER_EDGE) {
            return -1;
        }

        // evaluate height function
        String eval = ((("float x = "+x).concat("; float y = ")+y).concat("; ")+this.heightFunction).concat(";");
        try {
            return Math.max((float)(double)(BSH.eval(eval)), -0.01f);
        } catch (EvalError e) {
            System.out.println(ANSI.RED+"eval error"+ANSI.RESET+", interpreted: "+eval); 
            System.exit(0);
        } catch (NullPointerException e) {
            System.out.println(ANSI.RED+"return error"+ANSI.RESET+", height function doesn't return anything");
            System.exit(0);
        } catch (ClassCastException e) {
            System.out.println(ANSI.RED+"cast exception"+ANSI.RESET+", please use double values for height function"); 
            System.exit(0);
        } catch (RuntimeException e) {
            System.out.println(ANSI.RED+"unexpected error"+ANSI.RESET+", please re-evaluate the height function");
            System.out.print(ANSI.RED+"error stack: "+ANSI.RESET);
            e.printStackTrace();
            System.exit(0);
        }
        return 0;
    }

    /**
     * Create the terrain-spline using the current function and input array.
     */
    public void createSpline() {
        // compute knot points
        this.knots = new KnotPoint[SPLINE_SIZE][SPLINE_SIZE];
        float x, y, z;
        for (int i = 0; i < SPLINE_SIZE; i++) {
            for (int j = 0; j < SPLINE_SIZE; j++) {
                x = -App.FIELD_SIZE/2 + j*(App.FIELD_SIZE*1f/(SPLINE_SIZE-1));
                y = -App.FIELD_SIZE/2 + i*(App.FIELD_SIZE*1f/(SPLINE_SIZE-1));
                z = this.getHeightFunction(x, y) + this.input[i][j];
                this.knots[i][j] = new KnotPoint(x, y, z);
            }
        }
        
        // calculate derivatives of the knot points
        this.assignDerivatives(this.knots);

        // create all quadrants
        this.quadrants = new Quadrant[(SPLINE_SIZE - 1) * (SPLINE_SIZE - 1)];
        for (int i = 0, a, b; i < this.quadrants.length; i++) {
            a = i / (SPLINE_SIZE - 1);
            b = i % (SPLINE_SIZE - 1);
            this.quadrants[i] = new Quadrant(this.knots[a][b], this.knots[a+1][b], this.knots[a][b+1], this.knots[a+1][b+1]);
            this.quadrants[i].A = multiplyMatrix(C1, multiplyMatrix(this.quadrants[i].I, C2));
        }
    }

    /**
     * Calculate the dX, dY and dXY for all knot points.
     * Calculates derivatives using the forward-, backward-, and center-difference formulas.
     * @param data the knot points
     */
    private void assignDerivatives(KnotPoint[][] data) {
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
    private float forwardDifference(Vector3 p1, Vector3 p2, Vector3 p3, int wrt) {
        return (-p3.z + 4 * p2.z - 3 * p1.z) / (wrt == X ? (p3.x - p1.x) : (p3.y - p1.y));
    }

    /**
     * Calculates the derivative at p1 using the backward-difference formula.
     * @param p1 the starting (target) point
     * @param p2 the previous point of p1
     * @param p3 the previous point of p2
     * @return the derivative at p1
     */
    private float backwardDifference(Vector3 p1, Vector3 p2, Vector3 p3, int wrt) {
        return (p3.z - 4 * p2.z + 3 * p1.z) / (wrt == X ? (p1.x - p3.x) : (p1.y - p3.y));
    }

    /**
     * Calculates the derivative at p1 using the center-difference formula.
     * @param p1 the starting (target) point
     * @param p2 the previous point of p1
     * @param p3 the next point after p1
     * @return the derivative at p1
     */
    private float centerDifference(Vector3 p1, Vector3 p2, Vector3 p3, int wrt) {
        return (p3.z - p2.z) / (wrt == X ? (p3.x - p2.x) : (p3.y - p2.y));
    }

    /**
     * Multiply 2 matrices together.
     * Adapted from: https://www.baeldung.com/java-matrix-multiplication
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
    static class KnotPoint extends Vector3 {

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

        public KnotPoint(float x, float y, float z) {
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
         * Corners of the quadrant.
         */
        public KnotPoint nn, np, pn, pp;

        /**
         * Quadrant-coefficients.
         */
        public float[][] A;

        /**
         * Initial values.
         */
        public float[][] I;

        public Quadrant(KnotPoint c1, KnotPoint c2, KnotPoint c3, KnotPoint c4) {
            this.nn = c1;
            this.np = c2;
            this.pn = c3;
            this.pp = c4;
            this.I = new float[][] {
                {c1.z, c2.z, c1.dY, c2.dY},
                {c3.z, c4.z, c3.dY, c4.dY},
                {c1.dX, c2.dX, c1.dXY, c2.dXY},
                {c3.dX, c4.dX, c3.dXY, c4.dXY}
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
