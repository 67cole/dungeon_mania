package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;


public class Dungeon {

    // Keep track of all entities in the dungeon
    private List<Entity> entities = new ArrayList<Entity>();

    private String dungeonName;
    private String dungeonId;
    private String dungeonGoals;

    public Dungeon(String dungeonName, String dungeonId, String dungeonGoals) {
        this.dungeonName = dungeonName;
        this.dungeonId = dungeonId;
        this.dungeonGoals = dungeonGoals;
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

    






    /**
     * Adds an entity into the entities list
     * @param entity
     */
    public void addEntities(Entity entity) {
        entities.add(entity);
    }

}