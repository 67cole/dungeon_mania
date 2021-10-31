package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.response.models.ItemResponse;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.Entity;


public class Dungeon {

    // Keep track of all entities in the dungeon
    private List<Entity> entities = new ArrayList<Entity>();
    public  List<CollectableEntity> inventory = new ArrayList<CollectableEntity>();
    public  List<String> buildables = new ArrayList<String>();

    private String dungeonName;
    private String dungeonId;
    private String dungeonGoals;
    private int keyCounter;
    private boolean keyStatus = true;
    private int tickCounter;
    private int entityCounter;
    private int height;
    private int width;

    public Dungeon(String dungeonName, String dungeonId, String dungeonGoals) {
        this.dungeonName = dungeonName;
        this.dungeonId = dungeonId;
        this.dungeonGoals = dungeonGoals;
        this.keyCounter = 0;
    }

    

    public int getHeight() {
        return height;
    }



    public void setHeight(int height) {
        this.height = height;
    }



    public int getWidth() {
        return width;
    }



    public void setWidth(int width) {
        this.width = width;
    }



    public int getEntityCounter() {
        return entityCounter;
    }


    public void setEntityCounter(int entityCounter) {
        this.entityCounter = entityCounter;
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
}