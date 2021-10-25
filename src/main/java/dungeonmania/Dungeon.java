package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;


public class Dungeon {

    // Keep track of all entities in the dungeon
    private List<Entity> entities = new ArrayList<Entity>();

    private String dungeonName;
    private String dungeonId;

    public Dungeon(String dungeonName, String dungeonId) {
        this.dungeonName = dungeonName;
        this.dungeonId = dungeonId;
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

}