package dungeonmania;

import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    public void setBuildables(List<String> buildables) {
        this.buildables = buildables;
        return;
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

    /**
     * This function checks whether the dungeon exists
     * @param dungeonName - this is the dungeon name
     */
    public static boolean dungeonNotValid(String dungeonName) {
       
        System.out.println("xdxddxd");
        try {
            String json = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
            System.out.println("bppga");
        } catch (IOException FileNotFoundException) {

        }
    

        return true;
    }

    /**
     * This function checks whether the gamemode is valid
     * @param gameMode - this is the gameMode
     */
    public static boolean gameModeNotValid(String gameMode) {
        for (String gamemodeState : DungeonManiaController.getGameModes()) {
            if (gamemodeState.equals(gameMode)) return true;
        }

        return false;
    }


    /**
     * Checks whether the entityID is valid or not
     * @param entityId - the id given in the argument 
     * @param entities - the list of all entities in the dungeon
     */
    public static boolean entityIdCheck(String entityId, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getID().equals(entityId)) return true;
        }

        return false; 
    }

    /**
     * Return the entity we are interacting with
     * @param entityId
     * @param entities
     * @return entity
     */
    public static Entity IdToEntity(String entityId, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getID().equals(entityId)) return entity; 
        }

        // Returning null will never occur
        return null;
    }



    
}