package com.project_1_2.group_16.gamelogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.models.Wall;

/**
 * Auxiliary class that generates random mazes.
 * Adapted from: https://rosettacode.org/wiki/Maze_generation
 */
public class MazeGenerator {

    /**
     * Width of the walls of the maze.
     */
    public static final float WALL_WIDTH = 0.5f;

    private static int complexity;

    private static Node[][] maze;

    /**
     * Generate a random maze with walls. Sets the start and end positions to solvable locations.
     * @param complexity in how many cells the maze will be divided
     */
    public static void generateMaze(int complexity) {
        // set complexity
        MazeGenerator.complexity = complexity;

        // reset maze
        maze = new Node[complexity][complexity];
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                maze[i][j] = new Node(i, j, complexity, App.FIELD_SIZE);
            }
        }

        // set start and end positions.
        Node start, end;
        int rand = (int)(Math.random()*4);
        switch (rand) {
            case 0: start = maze[0][0]; end = maze[maze.length - 1][maze.length - 1]; break;
            case 1: start = maze[maze.length - 1][maze.length - 1]; end = maze[0][0]; break;
            case 2: start = maze[maze.length - 1][0]; end = maze[0][maze.length - 1]; break;
            default: start = maze[0][maze.length - 1]; end = maze[maze.length - 1][0];
        }
        Input.V0 = start.center;
        Input.VT = end.center;
        Input.H = "0.1";

        // create the maze
        createMaze(0, 0);

        // create walls
        Input.WALLS = new ArrayList<Wall>();
        Node n;
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                n = maze[i][j];
                if (n.hasWallTop()) {
                    Input.WALLS.add(new Wall(new Vector2(n.center.x - n.size/2 + WALL_WIDTH/4, n.center.y - n.size/2 + WALL_WIDTH/2), n.size - WALL_WIDTH, WALL_WIDTH, Wall.MAZE_WALL));
                    
                    // water walls
                    Input.WALLS.add(new Wall(new Vector2(n.center.x - n.size/2 + WALL_WIDTH/4 - 1.05f*WALL_WIDTH, n.center.y - n.size/2 + 0.55f*WALL_WIDTH), 1.1f*WALL_WIDTH, 1.1f*WALL_WIDTH, Wall.WATER));
                    Input.WALLS.add(new Wall(new Vector2(n.center.x + n.size/2 + WALL_WIDTH/4 - 1.05f*WALL_WIDTH, n.center.y - n.size/2 + 0.55f*WALL_WIDTH), 1.1f*WALL_WIDTH, 1.1f*WALL_WIDTH, Wall.WATER));
                }
            }
            for (int j = 0; j < complexity; j++) {
                n = maze[i][j];
                if (n.hasWallLeft()) {
                    Input.WALLS.add(new Wall(new Vector2(n.center.x - n.size/2 - WALL_WIDTH/4 - WALL_WIDTH/2, n.center.y + n.size/2 - WALL_WIDTH/2), WALL_WIDTH, n.size - WALL_WIDTH, Wall.MAZE_WALL));

                    // water walls
                    Input.WALLS.add(new Wall(new Vector2(n.center.x - n.size/2 - WALL_WIDTH/4 - WALL_WIDTH/2 - 0.05f*WALL_WIDTH, n.center.y + n.size/2 + 0.55f*WALL_WIDTH), 1.1f*WALL_WIDTH, 1.1f*WALL_WIDTH, Wall.WATER));
                    Input.WALLS.add(new Wall(new Vector2(n.center.x - n.size/2 - WALL_WIDTH/4 - WALL_WIDTH/2 - 0.05f*WALL_WIDTH, n.center.y - n.size/2 + 0.55f*WALL_WIDTH), 1.1f*WALL_WIDTH, 1.1f*WALL_WIDTH, Wall.WATER));

                }
            }
        }
    }

    /**
     * Recursive method for generating a maze.
     * Uses DFS.
     * @param currentX x the DFS is currently visiting
     * @param currentY y the DFS is currently visiting
     */
    private static void createMaze(int currentX, int currentY) {
        // store directions in a random order
		Direction[] directions = Direction.values();
		Collections.shuffle(Arrays.asList(directions));

        // start a depth first search
		for (Direction d : directions) {
			int nextX = currentX + d.dx;
			int nextY = currentY + d.dy;

            // only visit if valid coordinate and not visited yet
			if (isValidIndex(nextX) && isValidIndex(nextY) && maze[nextX][nextY].bit == 0) {
				maze[currentX][currentY].bit += d.bit;
				maze[nextX][nextY].bit += d.opposite.bit;
				createMaze(nextX, nextY);
			}
		}
	}

    /**
     * Checks if the index is valid. 
     * @param i index
     * @return
     */
    private static boolean isValidIndex(int i) {
        return i >= 0 && i < complexity;
    }

    /**
     * Get the complexity of the current maze.
     * @return
     */
    public static int getComplexity() {
        return complexity;
    }



    /**
     * Auxiliary class the represents a node of the maze.
     */
    static class Node {

        /**
         * 'Score' that this node has.
         */
        public int bit;

        /**
         * Center of this node (game coordinates).
         */
        public final Vector2 center;

        /**
         * Length and width of this node.
         */
        public final float size;

        private final int i, j;

        public Node(int i, int j, int complexity, float fieldSize) {
            this.i = i;
            this.j = j;
            this.size = fieldSize / complexity;
            this.center = new Vector2(-fieldSize / 2 + (i + 0.5f) * this.size, -fieldSize / 2 + (j + 0.5f) * this.size);
        }

        /**
         * If this node has a wall on its west side.
         * @return
         */
        public boolean hasWallLeft() {
            if (this.i == 0) return false;
            return (this.bit & 8) == 0;
        }

        /**
         * If this node has a wall on its north side.
         * @return
         */
        public boolean hasWallTop() {
            if (this.j == 0) return false;
            return (this.bit & 1) == 0;
        }
    }

    /**
     * Enumerated type that represents a direction.
     */
    private enum Direction {

		NORTH(1, 0, -1), 
        SOUTH(2, 0, 1), 
        EAST(4, 1, 0),
        WEST(8, -1, 0);

		private final int bit;
		private final int dx;
		private final int dy;
		private Direction opposite;
 
		static {
			NORTH.opposite = SOUTH;
			SOUTH.opposite = NORTH;
			EAST.opposite = WEST;
			WEST.opposite = EAST;
		}
 
		private Direction(int bit, int dx, int dy) {
			this.bit = bit;
			this.dx = dx;
			this.dy = dy;
		}
	};

    public static void display() {
        for (int i = 0; i < complexity; i++) {
            // draw the north edge
            for (int j = 0; j < complexity; j++) {
                System.out.print((maze[j][i].bit & 1) == 0 ? "+---" : "+   ");
            }
            System.out.println("+");
            // draw the west edge
            for (int j = 0; j < complexity; j++) {
                System.out.print((maze[j][i].bit & 8) == 0 ? "|   " : "    ");
            }
            System.out.println("|");
        }
        // draw the bottom line
        for (int j = 0; j < complexity; j++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }
}
