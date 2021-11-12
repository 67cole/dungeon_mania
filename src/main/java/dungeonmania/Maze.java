package dungeonmania;

import java.util.LinkedList;
import java.util.Random;

public class Maze {
    public static final boolean WALL = false;
    public static final boolean EMPTY = true;

    /**
     * The map of the randomely generated dungeon
     */
    private boolean map[][];


    /**
     * The map's width
     */
    private int width;

    /**
     * The map's length
     */
    private int length;

    public Maze(int x, int y, int xFinal, int yFinal, String gameMode) {
        this.width = xFinal - x;
        this.length = yFinal - y;
        this.map = new boolean[width][length];
        
        // Setting up the linked lists
        LinkedList<int[]> temp = new LinkedList<>();
        
        Random random = new Random();
        

    }
}
