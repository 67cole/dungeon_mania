package dungeonmania;

import dungeonmania.entities.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;

public class DungeonManiaController {
    
    // List to store information about dungeons 
    private List<Dungeon> dungeons = new ArrayList<Dungeon>();
    private int dungeonCounter = 0;
    private int entityCounter = 0;

    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("Standard", "Peaceful", "Hard");
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        DungeonResponse dr = null;
        // Plan
        // First: Have to create a new dungeon by using the json file in the dungeons folder, and inserting the entitys on to the map.
        
        // Create the unique identifier for the new dungeon
        String dungeonId = String.format("dungeon%d", dungeonCounter);
        dungeonCounter += 1;

        // Make a new dungeon object and add it to the dungeons list
        Dungeon main = new Dungeon(dungeonName, dungeonId);
        dungeons.add(main);

        addEntitiesToList(dungeonName, main);

        // To do: Inventory, Entities, Buildables, Goals
        // Need a way to add the entity position location from the json into the dungeon object.
        //Open up the json file and obtain information on x,y, and type. Depending on the type, we will create that corresponding
        //Class and add it into the entities list for the dungeon.
        
        return null;
    }
    
    public void addEntitiesToList(String dungeonName, Dungeon main) {

        String filename = "src\\main\\resources\\dungeons\\" + dungeonName;
        try {
            JsonObject json_object = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
            JsonArray entities_list = json_object.get("entities").getAsJsonArray();
           
            for (int i = 0; i < entities_list.size(); i++) {
                JsonObject entity = entities_list.get(i).getAsJsonObject();
                String type = entity.get("type").getAsString();
                int x = entity.get("x").getAsInt();
                int y = entity.get("y").getAsInt();
                Position position;
                String entityId;

                switch(type) {
                    case "wall":
                        position = new Position(x,y);
                        entityId =  String.format("entity%d", entityCounter);
                        Wall wall_entity = new Wall(position, type, entityId , false);
                        main.addEntities(wall_entity);
                        break;
                    case "exit":
                        position = new Position(x,y);
                        entityId =  String.format("entity%d", entityCounter);
                        Exit exit_entity = new Exit(position, type, entityId, true);
                        main.addEntities(exit_entity);
                        break;
                    case "boulder":
                        position = new Position(x,y);
                        entityId =  String.format("entity%d", entityCounter);
                        Boulder boulder_entity = new Boulder(position, type, entityId, true);
                        main.addEntities(boulder_entity);
                        break;
                    case "switch":
                        position = new Position(x,y);
                        entityId =  String.format("entity%d", entityCounter);
                        Switch switch_entity = new Switch(position, type, entityId, true);
                        main.addEntities(switch_entity);
                        break;
                    case "door":
                        position = new Position(x,y);
                        entityId =  String.format("entity%d", entityCounter);
                        Door door_entity = new Door(position, type, entityId, true);
                        main.addEntities(door_entity);
                        break;
                    case "portal":
                        position = new Position(x,y);
                        entityId =  String.format("entity%d", entityCounter);
                        Portal portal_entity = new Portal(position, type, entityId, true);
                        main.addEntities(portal_entity);
                        break;
                    case "zombie_toast_spawner":
                        position = new Position(x,y);
                        entityId =  String.format("entity%d", entityCounter);
                        ZombieToastSpawner zombie_toast_entity = new ZombieToastSpawner(position, type, entityId, true);
                        main.addEntities(zombie_toast_entity);
                        break;
                    
                }
            }
        } catch (Exception e) {

        }    
    }

    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        return null;
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        return null;
    }

    public List<String> allGames() {
        return new ArrayList<>();
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        return null;
    }
}