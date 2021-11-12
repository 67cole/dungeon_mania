package dungeonmania;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import dungeonmania.util.Position;

import java.util.Arrays;

public class Maze {
    public static final boolean WALL = false;
    public static final boolean EMPTY = true;
    private static final int WIDTH_SIZE = 50;
    private static final int HEIGHT_SIZE = 50;

    /**
     * The map of the randomely generated dungeon
     */
    private boolean map[][];

    /**
     * The width of the map
     */
    private int width;

    /**
     * The height of the map
     */
    private int height;


    public Maze(int xStart, int yStart, int xFinal, int yFinal) {
        this.width = WIDTH_SIZE;
        this.height = HEIGHT_SIZE;
        this.map = new boolean[width][height];

        // Let maze be a 2D array of booleans - set all to WALL (false)
        for (boolean[] row: map) {
            Arrays.fill(row, WALL);
        }

        map[xStart][yStart] = EMPTY;

        // Let options be a list of positions
        List<Position> options = new ArrayList<Position>();
        options = checkAdjacentWall(xStart, yStart, options);

        while (!options.isEmpty()) {

            // next is a random position from the available options
            Random random = new Random();
            Position next = options.get(random.nextInt(options.size()));
            options.remove(next);

            // neighbours is positions 2 away and are empty
            List<Position> neighbours = checkAdjacentEmpty(next.getX(), next.getY(), new ArrayList<Position>());

            if (!neighbours.isEmpty()) {
                Position neighbour = neighbours.get(random.nextInt(neighbours.size()));
                map[next.getX()][next.getY()] = EMPTY;
                
                // Get the position between the neighbour and next
                int inBetweenX = (next.getX() + neighbour.getX()) / 2;
                int inBetweenY = (next.getY() + neighbour.getY()) / 2;

                map[inBetweenX][inBetweenY] = EMPTY;
                map[neighbour.getX()][neighbour.getY()] = EMPTY;
            }

            List<Position> temp = checkAdjacentWall(next.getX(), next.getY(), new ArrayList<Position>());
            options.addAll(temp);

        }

        // Check if the end is a wall
        if (map[xFinal][yFinal] == WALL) {
            
            map[xFinal][yFinal] = EMPTY;
            List<Position> neighbours = getDirectAdjacents(xFinal, yFinal, new ArrayList<Position>());

            // Checking if any neighbours of the end position is empty
            int emptyCheck = 0;
            for (Position neighbour : neighbours) {
                if (map[neighbour.getX()][neighbour.getY()] == EMPTY) {
                    emptyCheck++;
                    break;
                }
            }

            if (emptyCheck == 0) {

                // Connecting to grid
                Random random = new Random();
                Position neighbour = neighbours.get(random.nextInt(neighbours.size()));
                map[neighbour.getX()][neighbour.getY()] = EMPTY;
            }
        }

    }

    public boolean[][] getMap() {
        return map;
    }

    public List<Position> checkAdjacentWall(int x, int y, List<Position> options) {
        
        // 2 Positions Forward
        if (y - 2 > 0 && y - 2 < height) {
            if (map[x][y - 2] == WALL) {
                options.add(new Position(x, y - 2));
            }
        }
        
        // 2 Positions Right
        if (x + 2 > 0 && x + 2 < width) {
            if (map[x + 2][y] == WALL) {
                options.add(new Position(x + 2, y));
            }
        }

        // 2 Positions Below
        if (y + 2 > 0 && y + 2 < height) {
            if (map[x][y + 2] == WALL) {
                options.add(new Position(x, y + 2));
            }
        }

        // 2 Positions Left
        if (x - 2 > 0 && x - 2 < width) {
            if (map[x - 2][y] == WALL) {
                options.add(new Position(x - 2, y));
            }
        }

        return options; 
    }

    public List<Position> checkAdjacentEmpty(int x, int y, List<Position> options) {
        
        // 2 Positions Forward
        if (y - 2 > 0 && y - 2 < height) {
            if (map[x][y - 2] == EMPTY) {
                options.add(new Position(x, y - 2));
            }
        }
        
        // 2 Positions Right
        if (x + 2 > 0 && x + 2 < width) {
            if (map[x + 2][y] == EMPTY) {
                options.add(new Position(x + 2, y));
            }
        }

        // 2 Positions Below
        if (y + 2 > 0 && y + 2 < height) {
            if (map[x][y + 2] == EMPTY) {
                options.add(new Position(x, y + 2));
            }
        }

        // 2 Positions Left
        if (x - 2 > 0 && x - 2 < width) {
            if (map[x - 2][y] == EMPTY) {
                options.add(new Position(x - 2, y));
            }
        }

        return options; 
    }

    public List<Position> getDirectAdjacents(int x, int y, List<Position> options) {
        // 1 Positions Forward
        if (y - 1 > 0 && y - 1 < height) {
            options.add(new Position(x, y - 1));
        }
        
        // 1 Positions Right
        if (x + 1 > 0 && x + 1 < width) {
            options.add(new Position(x + 1, y));
        }

        // 1 Positions Below
        if (y + 1 > 0 && y + 1 < height) {
            options.add(new Position(x, y + 1));
        }

        // 1 Positions Left
        if (x - 1 > 0 && x - 1 < width) {
           options.add(new Position(x - 1, y));
        }

        return options; 
    }
}
