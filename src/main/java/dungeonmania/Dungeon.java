package dungeonmania;

import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap; 

import dungeonmania.response.models.ItemResponse;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.Entity;



public class Dungeon {

    // Keep track of all entities in the dungeon
    private List<Entity> entities = new ArrayList<Entity>();
    private int[][] dungeonMap = new int[16][18];
    private int[][] adjMap = new int[288][288];

    public  List<CollectableEntity> inventory = new ArrayList<CollectableEntity>();
    public  List<String> buildables = new ArrayList<String>();

    private String dungeonName;
    private String dungeonId;
    private String dungeonGoals;
    private int keyCounter;
    private boolean keyStatus = true;
    private int tickCounter;
    private int entityCounter;
    private boolean peaceful;
    private boolean hard; 
    private int invisibilityPotionCounter;
    private int invincibilityPotionCounter;

    public Dungeon(String dungeonName, String dungeonId, String dungeonGoals) {
        this.dungeonName = dungeonName;
        this.dungeonId = dungeonId;
        this.dungeonGoals = dungeonGoals;
        this.keyCounter = 0;
    }

    public int getEntityCounter() {
        return entityCounter;
    }


    public void setEntityCounter(int entityCounter) {
        this.entityCounter = entityCounter;
    }

    public int getInvincibilityPotionCounter() {
        return invincibilityPotionCounter;
    }

    public void setInvincibilityCounter(int invincibilityPotionCounter) {
        this.invincibilityPotionCounter = invincibilityPotionCounter;
    }

    public int getInvisibilityPotionCounter() {
        return invisibilityPotionCounter;
    }

    public void setInvisibilityPotionCounter(int invisibilityPotionCounter) {
        this.invisibilityPotionCounter = invisibilityPotionCounter;
    }

    public int getTickCounter() {
        return tickCounter;
    }

    public void setTickCounter(int tickCounter) {
        this.tickCounter = tickCounter;
    }

    public String getDungeonGoals() {
        return dungeonGoals;
    }

    public void setDungeonGoals(String dungeonGoals) {
        this.dungeonGoals = dungeonGoals;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public void setDungeonName(String dungeonName) {
        this.dungeonName = dungeonName;
    }

    public String getDungeonId() {
        return dungeonId;
    }

    public void setDungeonId(String dungeonId) {
        this.dungeonId = dungeonId;
    }

    public int getKeyCounter() {
        return this.keyCounter;
    }

    public void setKeyCounter(int keyCounter) {
        this.keyCounter = keyCounter;
    }

    public List<CollectableEntity> getInventory() {
        return inventory;
    }

    public List<String> getBuildables() {
        return buildables;
    }

    public boolean getPeaceful() {
        return peaceful; 
    }

    public void setPeaceful(boolean peaceful) {
        this.peaceful = peaceful;
    }

    public boolean getHard() {
        return hard;
    }

    public void setHard(boolean hard) {
        this.hard = hard;
    }


    /**
     * Adds an entity into the entities list
     * @param entity
     */
    public void addEntities(Entity entity) {
        entities.add(entity);
    }

    /**
     * Removes an entity from the entities list
     * @param entity
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
    
    public boolean getKeyStatus() {
        return this.keyStatus;
    }
    public void setKeyStatus(boolean keyStatus) {
        this.keyStatus = keyStatus;
    }

    public List<Entity> getEntitiesAtPos(Position pos) {

        List<Entity> entitiesAtPos = new ArrayList<Entity>();
        for (Entity entity : entities) {
            if (entity.getPosition().equals(pos)) {
                entitiesAtPos.add(entity);
            }
        }

        return entitiesAtPos;
    }

    
	public int[][] getDungeonMap() {
		return this.dungeonMap;
	}

	public void setDungeonMap(List<Entity> entities) {
		this.dungeonMap = dungeonArray(entities);
	}

	public int[][] getAdjMap() {
		return this.adjMap;
	}

	public void setAdjMap(int[][] grid) {
		this.adjMap = createAdjacencyMatrix(grid);
	}


    public int[][] dungeonArray(List<Entity> entities) {
        int[][] array = new int[16][18];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 18; j++) {
                array[i][j] = 0;
            }
        }
        for (Entity entity : entities) {
            if (entity.getType().equals("wall") || entity.getType().equals("door")) {
                
                Position pos = entity.getPosition();

                array[pos.getY()][pos.getX()] = 1;
                
                
            }
        }
        return array;
    }

    public Position[][] posGrid(List<Entity> entities) {
        Position[][] array = new Position[16][18];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 18; j++) {
                array[i][j] = new Position(j, i);
            }
        }
        return array;
    }

    

    public void printDungeon(int[][] dungeonArray) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 18; j++) {
                if (dungeonArray[i][j] == 1) {
                    System.out.printf("☐ ");
                } else {
                    System.out.printf("  ");
                }
                
            }
            System.out.println();
        }
        System.out.println();
    }
    

    public int[][] createAdjacencyMatrix(int[][] grid) {
        int [][] adjMatrix = new int[288][288];
        for (int row = 0; row < 288; row++) {
            for (int col = 0; col < 288; col++) {
                adjMatrix[row][col] = 99;
                if (col == row) adjMatrix[row][col] = 0;
            }
        }
        int node = 0;
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 18; col++) {
                if (grid[row][col] == 0) {
                    // check up
                    if (row > 0 && grid[row - 1][col] == 0) {
                        adjMatrix[node][node - 18] = 1;
                        adjMatrix[node - 18][node] = 1;
                    }

                    // check down
                    if (row < 16 && grid[row + 1][col] == 0) {
                        adjMatrix[node][node + 18] = 1;
                        adjMatrix[node + 18][node] = 1;
                    }

                    // check right
                    if (col < 18 && grid[row][col + 1] == 0) {
                        adjMatrix[node][node + 1] = 1;
                        adjMatrix[node + 1][node] = 1;
                    }
                    
                    // check left
                    if (col > 0 && grid[row][col - 1] == 0) {
                        adjMatrix[node][node - 1] = 1;
                        adjMatrix[node - 1][node] = 1;
                    }
                    
                }
                node++;
            }
        }
        


        return adjMatrix;
    }

    

  

    public List<Integer> unvisitedNeighbours(Queue<Integer> unvisited, int[][] adj, int currNode) {

        List<Integer> unvNeigh = new ArrayList<Integer>();
        // if not top row
        if (currNode > 17) unvNeigh.add(currNode - 18);
        // if not left side
        if (!(currNode % 18 == 0)) unvNeigh.add(currNode - 1);
        // if not right side
        if (!((currNode + 1) % 18 == 0)) unvNeigh.add(currNode + 1);
        // if not bottom row
        if (currNode + 18 < 288) unvNeigh.add(currNode + 18);

        return unvNeigh;
    }

    public boolean isUnvisited(Queue<Integer> unvisited, int node) {

        return false;
    }


}