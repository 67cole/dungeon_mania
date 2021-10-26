package dungeonmania;

import dungeonmania.entities.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.io.FileReader;
import java.io.IOException;
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
    private int dungeonCounter = '0';

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

        // To do: Inventory, Entities, Buildables, Goals
        // Need a way to add the entity position location from the json into the dungeon object.
        //Open up the json file and obtain information on x,y, and type. Depending on the type, we will create that corresponding
        //Class and add it into the entities list for the dungeon.
        String filename = "src\\main\\resources\\dungeons\\" + dungeonName + ".json";
        try {
            JsonObject json_object = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
            JsonArray entities_list = json_object.get("entities").getAsJsonArray();
            System.out.println(entities_list.size());
            
            for (int i = 0; i < entities_list.size(); i++) {
                JsonObject entity = entities_list.get(i).getAsJsonObject();
                String type = entity.get("type").getAsString();
                int x = entity.get("x").getAsInt();
                int y = entity.get("y").getAsInt();
                System.out.println("HI");

                switch(type) {
                    case "wall":
                        Wall wall_entity = new Wall(x, y, type);
                        main.addEntities(wall_entity);
                        break;
                    case "exit":
                        Exit exit_entity = new Exit(x,y,type);
                        main.addEntities(exit_entity);
                        break;
                    case "boulder":
                        Boulder boulder_entity = new Boulder(x,y,type);
                        main.addEntities(boulder_entity);
                        break;
                    case "switch":
                        Switch switch_entity = new Switch(x,y,type);
                        main.addEntities(switch_entity);
                        break;
                    case "door":
                        Door door_entity = new Door(x,y,type);
                        main.addEntities(door_entity);
                        break;
                    case "portal":
                        Portal portal_entity = new Portal(x,y,type);
                        main.addEntities(portal_entity);
                        break;
                    case "zombie_toast_spawner":
                        ZombieToastSpawner zombie_toast_entity = new ZombieToastSpawner(x,y,type);
                        main.addEntities(zombie_toast_entity);
                        break;
                    
                }


            }
        } catch (Exception e) {

        }
        return null;
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