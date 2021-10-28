package dungeonmania;

import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    // This will be changed based on negame or loadgame
    private String currDungeon;

    private int dungeonCounter = 0;
    private int entityCounter = 0;
    private static int tickCounter = 0;

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

        List<ItemResponse> emptyInventory = new ArrayList<ItemResponse>();
        List<String> emptyBuildables = new ArrayList<String>();
        // Plan
        // First: Have to create a new dungeon by using the json file in the dungeons folder, and inserting the entitys on to the map.
        
        // Create the unique identifier for the new dungeon
        String dungeonId = String.format("dungeon%d", dungeonCounter);
        dungeonCounter += 1;

        // Make a new dungeon object and add it to the dungeons list
        String goals = getGoalsFromJson(dungeonName);
        Dungeon main = new Dungeon(dungeonName, dungeonId, goals);
        dungeons.add(main);
        currDungeon = dungeonId;

        addEntitiesToList(dungeonName, main);
        

        // To do: Inventory, Entities, Buildables, Goals
        // Need a way to add the entity position location from the json into the dungeon object.
        //Open up the json file and obtain information on x,y, and type. Depending on the type, we will create that corresponding
        //Class and add it into the entities list for the dungeon.
        List<EntityResponse> erList = new ArrayList<EntityResponse>();
        for(Entity entity: main.getEntities()) {
            EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
            erList.add(er);
        }
        
        DungeonResponse dr = new DungeonResponse(dungeonId, dungeonName, erList, emptyInventory, emptyBuildables, goals);

        
        return dr;
    }
    
    public void addEntitiesToList(String dungeonName, Dungeon main) {

        String filename = "src\\main\\resources\\dungeons\\" + dungeonName + ".json";
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
            JsonArray entitiesList = jsonObject.get("entities").getAsJsonArray();
            
            for (int i = 0; i < entitiesList .size(); i++) {
                JsonObject entity = entitiesList .get(i).getAsJsonObject();
                String type = entity.get("type").getAsString();
                int x = entity.get("x").getAsInt();
                int y = entity.get("y").getAsInt();
                Position position = new Position(x,y);;
                String entityId =  String.format("entity%d", entityCounter);
                entityCounter += 1;
                switch(type) {
                    case "player":
                        Character characterEntity = new Character(position, type, entityId , false);
                        main.addEntities(characterEntity);  
                        break;
                    case "wall":                       
                        Wall wallEntity = new Wall(position, type, entityId , false);
                        main.addEntities(wallEntity);  
                        break;
                    case "exit":
                        Exit exitEntity = new Exit(position, type, entityId, true);
                        main.addEntities(exitEntity);
                        break;
                    case "boulder":
                        Boulder boulderEntity= new Boulder(position, type, entityId, true);
                        main.addEntities(boulderEntity);
                        break;
                    case "switch":
                        Switch switchEntity = new Switch(position, type, entityId, true);
                        main.addEntities(switchEntity);
                        break;
                    case "door":
                        Door doorEntity = new Door(position, type, entityId, true);
                        main.addEntities(doorEntity);
                        break;
                    case "portal":
                        String colour = entity.get("colour").getAsString();
                        Portal portalEntity = new Portal(position, type, entityId, true, colour);
                        main.addEntities(portalEntity);
                        break;
                    case "zombie_toast_spawner":
                        ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(position, type, entityId, true);
                        main.addEntities(zombieToastSpawner);
                        break;
                    case "spider":
                        Spider spiderEntity = new Spider(position, type, entityId, true);
                        main.addEntities(spiderEntity);
                        break;
                }
            }
        } catch (Exception e) {

        }   
    }

    public String getGoalsFromJson(String dungeonName)  {
        String returnGoal = "";
        String filename = "src\\main\\resources\\dungeons\\" + dungeonName + ".json";
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
            JsonObject goalCondition = jsonObject.get("goal-condition").getAsJsonObject();
            String goal = goalCondition.get("goal").getAsString();
            switch(goal) {
                case "AND":
                    JsonArray subgoals = goalCondition.get("subgoals").getAsJsonArray();
                    for (int i = 0; i < subgoals.size(); i++) {
                        JsonObject goals = subgoals.get(i).getAsJsonObject();
                        if (goals.get("goal").getAsString().equals("enemies")) {
                            if (findEnemies(filename, "mercenary") && findEnemies(filename, "spider")) {
                                returnGoal += ":mercenary AND :spider";    
                            } else if (findEnemies(filename, "spider")) {
                                returnGoal += ":spider";
                            } else if (findEnemies(filename, "mercenary")) {
                                returnGoal += ":mercenary";
                            }
                        } 
                        else {
                            returnGoal += ":" + goals.get("goal").getAsString();
                        }                   
                        if (i + 1 != subgoals.size()) {
                            returnGoal += " AND ";
                        }
                    }
                    break;
                case "exit":
                    returnGoal = ":exit";
                    break;
                case "boulders":
                    returnGoal = ":boulder";
                    break;

            }
        } catch (Exception e) {

        }
        return returnGoal;
        
    }

    public boolean findEnemies(String filename, String enemy) {
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
            JsonArray entitiesList = jsonObject.get("entities").getAsJsonArray();
            for (int i = 0; i < entitiesList.size(); i++) {
                JsonObject entity = entitiesList.get(i).getAsJsonObject();
                if (entity.get("type").getAsString().equals(enemy)) {
                    return true;
                }
            }

        } catch (Exception e) {

        }     
        return false;

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

        Dungeon main = null;
        DungeonManiaController.tickCounter++;
        ZombieToast holder = null;
        int con = 0;

         
        for (Dungeon dungeon : dungeons) {
            if (dungeon.getDungeonId().equals(currDungeon)) {
                main = dungeon;
                List<Entity> entities = dungeon.getEntities();


                for (Entity entity : entities) {

                    // Character Movement
                    if (entity.getType().equals("player")) {
                        MovingEntity temp = (MovingEntity) entity;

                        // Either the character moves or it doesnt.

                        // Check if it it blocked by a wall, in which it doesnt move
                        // or if theres 2 boulders next to each other
                        if (!temp.checkMovement(movementDirection, entities)) continue;
                        
                        Entity intEntity = temp.checkNext(movementDirection, entities);
                        
                        // If it is here movement is allowed and
                        // it might need to interact with an entity.
                        temp.moveEntity(movementDirection);

                        // Check if it is empty square or an entity
                        
                        
                        if (intEntity != null) {
                            // we have an interactable
                            intEntity.entityFunction(entities, (Character) temp, movementDirection);
                        }

                        if (main.getDungeonGoals().contains("exit")) {
                            checkExitGoal(entities, main, temp);
                        }

                    }

                    // Zombie Spawner Ticks
                    if (entity.getType().equals("zombie_toast_spawner") && DungeonManiaController.tickCounter % 20 == 0) {
                        Position zombieSpawn = checkWhiteSpace(entity.getPosition(), entities);


                        // If there is no white space around zombie spawner, don't spawn zombie
                        if (zombieSpawn == null) continue;

                        String entityId =  String.format("entity%d", entityCounter);
                        entityCounter += 1;

                        ZombieToast zombieToastEntity = new ZombieToast(zombieSpawn, "zombie_toast", entityId, true);
                        holder = zombieToastEntity;
                        con = 1;
                    }

                    // Zombie Movement
                    if (entity.getType().equals("zombie_toast")) {
                        MovingEntity temp = (MovingEntity) entity;

                        // Either the character moves or it doesnt.

                        // Check if it it blocked by a wall, in which it doesnt move
                        // or if theres 2 boulders next to each other
                        if (!temp.checkMovement(movementDirection, entities)) continue;
                        
                        Entity intEntity = temp.checkNext(movementDirection, entities);
                        // If it is here movement is allowed and
                        // it might need to interact with an entity.

                        if (intEntity.getType().equals("portal")) continue;

                        temp.moveEntity(movementDirection);
                        // Check if it is empty square or an entity
                        
                        if (intEntity != null) {
                            // we have an interactable
                            intEntity.entityFunction(entities, (Character) temp, movementDirection);
                        }
                    }

                    // Spider Movement
                    if (entity.getType().equals("spider")) {
                        MovingEntity temp = (MovingEntity) entity;

                        if (DungeonManiaController.tickCounter == 1) {
                            temp.moveUpward();
                            continue;
                        }

                        
                    }
                }
                // update goals
                if (main.getDungeonGoals().contains("boulder")) {
                    checkBoulderGoal(entities, main);
                }

            }
        }
        
        if (con == 1) main.addEntities(holder);
    
        List<ItemResponse> emptyInventory = new ArrayList<ItemResponse>();
        List<String> emptyBuildables = new ArrayList<String>();

        List<EntityResponse> erList= new ArrayList<EntityResponse>();
        for(Entity entity: main.getEntities()) {
            EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
            erList.add(er);
        }

        DungeonResponse dr = new DungeonResponse(main.getDungeonId(), main.getDungeonName(),
            erList, emptyInventory, emptyBuildables, main.getDungeonGoals());
        return dr;
    }


    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        return null;
    }



    /**
     * 
     * 
     *              HELPER FUNCTIONS
     * 
     * 
     */


    /**
     * Check if white space is empty or not
     */
    public Position checkWhiteSpace(Position position, List<Entity> entities) {
        List<Position> adjacents = position.getAdjacentPositions();

        // List Position 1, 3, 5, 7 are adjacent
        for (int i = 1; i < adjacents.size(); i = i + 2) {
            Position temp = adjacents.get(i);
            int check = 0;

            for (Entity entity : entities) {
                if (entity.getPosition().equals(temp)) {
                    check = 1; 
                    break;
                } 
            }

            if (check == 0) {
                return temp;
            }
        }   

        return null;
    }

    public void checkBoulderGoal(List<Entity> entities, Dungeon dungeon) {

        HashMap<Position, Integer> map = new HashMap<Position, Integer>();
        // key, value
        
        for (Entity entity : entities) {

            if (entity.getType().equals("switch")) {
                if (!map.containsKey(entity.getPosition())) {
                    map.put(entity.getPosition(), 1);
                }
                else {
                    map.put(entity.getPosition(), 2);
                }
            }
            
            if (entity.getType().equals("boulder")) {
                if (!map.containsKey(entity.getPosition())) {
                    map.put(entity.getPosition(), 1);
                }
                else {
                    map.put(entity.getPosition(), 2);
                }
            }
        }

        for (Integer amt : map.values()) {
            // even
            if (amt != 2) {
                return; // not finished with boulders goal 
            }
        }
        dungeon.setDungeonGoals("");

    }

    public void checkExitGoal(List<Entity> entities, Dungeon dungeon, MovingEntity player) {

        for (Entity entity : entities) {

            if (entity.getType().equals("exit")) {
                if (entity.getPosition().equals(player.getPosition())) {
                    dungeon.setDungeonGoals("");
                }
            }
        }
    }





}