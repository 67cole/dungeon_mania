package dungeonmania;

import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.entities.BuildableEntities.*;
import dungeonmania.entities.CollectableEntities.*;
import dungeonmania.entities.RareCollectableEntities.*;
import dungeonmania.exceptions.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class DungeonManiaController {
    
    // List to store information about dungeons 
    
    private List<Dungeon> dungeons = new ArrayList<Dungeon>();

    // This will be changed based on newgame or loadgame
    private Dungeon currDungeon;
    private DungeonResponse lastTick;

    private int dungeonCounter = 0;
    

    public DungeonManiaController() {
    }

    /**
     * returns skin
     * @return String
     */
    public String getSkin() {
        return "default";
    }

    /**
     * returns localisation
     * @return String
     */
    public String getLocalisation() {
        return "en_US";
    }

    /**
     * returns gamemodes
     * @return List<String>
     */
    public static List<String> getGameModes() {
        return Arrays.asList("standard", "peaceful", "hard");
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

    /**
     * creates a new game
     * @param dungeonName
     * @param gameMode
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {    
        // INCOMPLETE *********
        if (!Dungeon.dungeonNotValid(dungeonName)) {
            throw new IllegalArgumentException("This dungeon does not exist.");
        }

        if (!Dungeon.gameModeNotValid(gameMode)) {
            throw new IllegalArgumentException("This gamemode is not valid.");
        }

        List<ItemResponse> emptyInventory = new ArrayList<ItemResponse>();
        List<String> emptyBuildables = new ArrayList<String>();
      

        // Create the unique identifier for the new dungeon
        String dungeonId = String.format("dungeon%d", dungeonCounter);
        dungeonCounter += 1;

        // Make a new dungeon object and add it to the dungeons list
        String goals = getGoalsFromJson(dungeonName);
        Dungeon main = new Dungeon(dungeonName, dungeonId, goals);
        dungeons.add(main);
        currDungeon = main;

        switch (gameMode) {
            case "peaceful":
                currDungeon.setPeaceful(true);
                break;

            case "hard":
                currDungeon.setHard(true);
                break;
        }
        addEntitiesToList(dungeonName, main);
        
        List<EntityResponse> erList = new ArrayList<EntityResponse>();
        for (Entity entity: main.getEntities()) {
            EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
            erList.add(er);
        }
        
        DungeonResponse dr = new DungeonResponse(dungeonId, dungeonName, erList, emptyInventory, emptyBuildables, goals);
        lastTick = dr;
        clearDatabase();
        addToRewindDatabase();
        
        return dr;
    }

    /**
     * saves the game
     * @param name
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {

        // Edit database.json file
        String filename = "src\\main\\java\\dungeonmania\\database.json";
        Gson gson = new Gson();
        String json = gson.toJson(lastTick);
        JsonObject jsonObj = gson.fromJson(json, JsonElement.class).getAsJsonObject();

        // Add loadName
        jsonObj.addProperty("saveName", name);
        jsonObj.addProperty("entityCounter", currDungeon.getEntityCounter());
        jsonObj.addProperty("tickCounter", currDungeon.getTickCounter());
        jsonObj.addProperty("keyStatus", currDungeon.getKeyStatus());
        jsonObj.addProperty("invisibilityPotionCounter", currDungeon.getInvisibilityPotionCounter());
        jsonObj.addProperty("invincibilityPotionCounter", currDungeon.getInvincibilityPotionCounter());

        JsonArray jsonEntities = jsonObj.get("entities").getAsJsonArray();
        JsonArray jsonInventory = jsonObj.get("inventory").getAsJsonArray();

        // List of entities that have extra attributes
        List<MovingEntity> mvList = new ArrayList<MovingEntity>();
        for (Entity entity : currDungeon.getEntities()) {
            if (entity.getType().equals("player") || entity.getType().equals("mercenary") ||
            entity.getType().equals("spider") || entity.getType().equals("zombie_toast") 
            || entity.getType().equals("hydra") || entity.getType().equals("assassin")) {
                MovingEntity mv = (MovingEntity) entity;
                mvList.add(mv);
            }
        }

        List<CollectableEntity> collectableList = new ArrayList<CollectableEntity>();
        for (Entity entity : currDungeon.getEntities()) {
            if (entity.getType().equals("sword") || entity.getType().equals("key") ||
            entity.getType().equals("armour") || entity.getType().equals("bomb") || 
            entity.getType().equals("bow") || entity.getType().equals("shield")) {
                CollectableEntity mv = (CollectableEntity) entity;
                collectableList.add(mv);
            }
        }

        List<StaticEntity> staticList = new ArrayList<StaticEntity>();
        for (Entity entity : currDungeon.getEntities()) {
            if (entity.getType().equals("door") || entity.getType().equals("BLUEportal") ||
            entity.getType().equals("REDportal") || entity.getType().equals("YELLOWportal") ||
            entity.getType().equals("GREYportal")) {
                StaticEntity mv = (StaticEntity) entity;
                staticList.add(mv);
            }
        }

        // Give any moving entities attack and health
        for (int i = 0; i < jsonEntities.size(); i++) {
            JsonObject jEntity = jsonEntities.get(i).getAsJsonObject();

            if (jEntity.get("type").getAsString().equals("player")) {
                for (MovingEntity mvEntity : mvList) {
                    if (mvEntity.getType().equals("player")) {
                        jEntity.addProperty("attack", mvEntity.getAttack());
                        jEntity.addProperty("health", mvEntity.getHealth());
                    }
                }
            }
            else if (jEntity.get("type").getAsString().equals("mercenary")) {
                for (MovingEntity mvEntity : mvList) {
                    if (mvEntity.getType().equals("mercenary")) {
                        jEntity.addProperty("attack", mvEntity.getAttack());
                        jEntity.addProperty("health", mvEntity.getHealth());
                    }
                }
            }
            else if (jEntity.get("type").getAsString().equals("hydra")) {
                for (MovingEntity mvEntity : mvList) {
                    if (mvEntity.getType().equals("hydra")) {
                        jEntity.addProperty("attack", mvEntity.getAttack());
                        jEntity.addProperty("health", mvEntity.getHealth());
                    }
                }
            }
            else if (jEntity.get("type").getAsString().equals("assassin")) {
                for (MovingEntity mvEntity : mvList) {
                    if (mvEntity.getType().equals("assassin")) {
                        jEntity.addProperty("attack", mvEntity.getAttack());
                        jEntity.addProperty("health", mvEntity.getHealth());
                    }
                }
            }
            else if (jEntity.get("type").getAsString().equals("spider")) {
                for (MovingEntity mvEntity : mvList) {
                    if (mvEntity.getType().equals("spider")) {
                        Spider spider = (Spider) mvEntity;
                        jEntity.addProperty("attack", mvEntity.getAttack());
                        jEntity.addProperty("health", mvEntity.getHealth());
                        jEntity.addProperty("loopPos", spider.getLoopPos());
                        jEntity.addProperty("clockwise", spider.getClockwise());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("sword")) {
                for (CollectableEntity coEntity : collectableList) {
                    if (coEntity.getType().equals("sword")) {
                        Sword temp = (Sword) coEntity;
                        jEntity.addProperty("attack", temp.getAttack());
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("key")) {
                for (CollectableEntity coEntity : collectableList) {
                    if (coEntity.getID().equals(jEntity.get("id").getAsString())) {
                        Key temp = (Key) coEntity;
                        jEntity.addProperty("keyNum", temp.getKeyNum());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("armour")) {
                for (CollectableEntity coEntity : collectableList) {
                    if (coEntity.getType().equals("armour")) {
                        Armour temp = (Armour) coEntity;
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("bomb")) {
                for (CollectableEntity coEntity : collectableList) {
                    if (coEntity.getType().equals("bomb")) {
                        Bomb temp = (Bomb) coEntity;
                        jEntity.addProperty("activated", temp.isActivated());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("door")) {
                for (StaticEntity stEntity : staticList) {
                    if (stEntity.getID().equals(jEntity.get("id").getAsString())) {
                        Door temp = (Door) stEntity;
                        jEntity.addProperty("keyType", temp.getKeyType());
                        jEntity.addProperty("locked", temp.getLocked());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("REDportal") ||
            jEntity.get("type").getAsString().equals("BLUEportal") || 
            jEntity.get("type").getAsString().equals("YELLOWportal") ||
            jEntity.get("type").getAsString().equals("GREYportal")) {
                for (StaticEntity stEntity : staticList) {
                    if (stEntity.getType().equals("REDportal") || 
                    stEntity.getType().equals("GREYportal") || 
                    stEntity.getType().equals("YELLOWportal") ||
                    stEntity.getType().equals("BLUEportal")) {
                        Portal temp = (Portal) stEntity;
                        jEntity.addProperty("colour", temp.getColour());
                    }
                }
            }
        }

        for (int i = 0; i < jsonInventory.size(); i++) {
            JsonObject jEntity = jsonInventory.get(i).getAsJsonObject();

            if (jEntity.get("type").getAsString().equals("sword")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("sword")) {
                        Sword temp = (Sword) coEntity;
                        jEntity.addProperty("attack", temp.getAttack());
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("key")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("key")) {
                        Key temp = (Key) coEntity;
                        jEntity.addProperty("keyNum", temp.getKeyNum());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("armour")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("armour")) {
                        Armour temp = (Armour) coEntity;
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("bomb")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("bomb")) {
                        Bomb temp = (Bomb) coEntity;
                        jEntity.addProperty("activated", temp.isActivated());
                    }
                }
            }
            else if (jEntity.get("type").getAsString().equals("bow")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("bow")) {
                        Bow temp = (Bow) coEntity;
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("shield")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("shield")) {
                        Shield temp = (Shield) coEntity;
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

        }
        
        File newFile = new File(filename);

        String json2 = "";

        if (newFile.length() == 0) {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(jsonObj);
            json2 = gson.toJson(jsonArray);
        }

        else {
            try {
                JsonArray jsonArray = JsonParser.parseReader(new FileReader(filename)).getAsJsonArray();
                jsonArray.add(jsonObj);
                json2 = gson.toJson(jsonArray);
            } catch (Exception e) {}
        }

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(filename, false);
            fileOutputStream.write(json2.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {}


        return lastTick;
    }


    /**
     * loads the game
     * @param name
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {

        boolean nameChecker = false;
        for (String game : allGames()) {
            if (game.equals(name)) {
                nameChecker = true;
            }
        }
        // Exception as reqd in the spec
        if (nameChecker == false) {
            throw new IllegalArgumentException("Id is not a valid saved game");
        }

        String filename = "src\\main\\java\\dungeonmania\\database.json";
        JsonObject dungeon;
        List<EntityResponse> erList= new ArrayList<EntityResponse>();
        List<ItemResponse> irList= new ArrayList<ItemResponse>();
        try {
            JsonArray jsonArray = JsonParser.parseReader(new FileReader(filename)).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                dungeon = jsonArray.get(i).getAsJsonObject();
                if (dungeon.get("saveName").getAsString().equals(name)) {
                    // in the correct dungeon object
                    Dungeon main = new Dungeon(dungeon.get("dungeonName").getAsString()
                    , dungeon.get("dungeonId").getAsString(), dungeon.get("goals").getAsString());
                    main.setTickCounter(dungeon.get("tickCounter").getAsInt());
                    main.setEntityCounter(dungeon.get("entityCounter").getAsInt());
                    main.setKeyStatus(dungeon.get("keyStatus").getAsBoolean());
                    main.setInvincibilityCounter(dungeon.get("invincibilityPotionCounter").getAsInt());
                    main.setInvisibilityPotionCounter(dungeon.get("invisibilityPotionCounter").getAsInt());


                    currDungeon = main;
                    JsonArray entitiesList = dungeon.get("entities").getAsJsonArray();
                    JsonArray inventoryList = dungeon.get("inventory").getAsJsonArray();
                    JsonArray buildableList = dungeon.get("buildables").getAsJsonArray();
                    addEntitiesInventory(main, entitiesList, inventoryList, buildableList);
                    for(Entity entity: main.getEntities()) {
                        EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
                        erList.add(er);
                    }
                    for(CollectableEntity coEntity: main.getInventory()) {
                        ItemResponse ir = new ItemResponse(coEntity.getID(), coEntity.getType());
                        irList.add(ir);
                    }
                }
            }
        
        
        } catch (Exception e) {}

        // TO DO inventory attributes e..g., armor durability

        DungeonResponse newGame = new DungeonResponse(currDungeon.getDungeonId(), currDungeon.getDungeonName(),
        erList, irList, currDungeon.getBuildables(), currDungeon.getDungeonGoals());

        lastTick = newGame;

        // clearDatabase();
        // addToRewindDatabase();

        return newGame;
    }

    public List<String> allGames() {

        String filename = "src\\main\\java\\dungeonmania\\database.json";
        List<String> games = new ArrayList<String>();

        try {
            JsonArray jsonArray = JsonParser.parseReader(new FileReader(filename)).getAsJsonArray();
            // go into dungeons
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject dungeon = jsonArray.get(i).getAsJsonObject();
                games.add(dungeon.get("saveName").getAsString());
            }
        } catch (Exception e) {}

        return games;
    }


    /***
     * initiates a tick of the game
     * @param itemUsed
     * @param movementDirection
     * @return DungeonResponse
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {   
        if (!itemUsedInvalid(itemUsed)) {
            throw new IllegalArgumentException("The item used is invalid.");
         }

        if (!itemUsedNotInInventory(itemUsed)) {
            throw new InvalidActionException("The item is not in the inventory.");
        }

        // Get entity list
        List<Entity> entities = currDungeon.getEntities();
        Dungeon main = null;
        List<Entity> entitiesToBeRemoved = new ArrayList<Entity>();
        List<Entity> entitiesToBeAdded = new ArrayList<Entity>();
        List<Entity> allNearbyEntities = new ArrayList<Entity>();
        currDungeon.setTickCounter(currDungeon.getTickCounter() + 1);
        Spider spid = null;
        int spiderSpawned = 0;
        ZombieToast zombieHolder = null;
        Mercenary mercenaryHolder = null;
        Hydra hydraHolder = null;
        Assassin assassinHolder = null;
        Bomb bombHolder = null;
        Door doorHolder = null;
        int zombieAddedLater = 0;
        int mercenaryAddedLater = 0;
        int hydraAddedLater = 0;
        int assassinAddedLater = 0;
        int EnemyCheck = 0;
        boolean doorAddedLater = false;
        boolean invincibilityActive = false; 
        Character tempChar = null;
        Position playerSpawnPosition = null;
        main = currDungeon;
        
        // Get the character class
        Character character = Character.getCharacter(entities);
        
        // Check potion duration and set it off if it expires
        potionTickAdder(character);
        potionChecker(character);

        // If the gamemode is peaceful, act as if the character has invisibility
        if (currDungeon.getPeaceful()) character.setIsInvisible(true);

        // If the gamemode is hard, always turn off invincibility
        if (currDungeon.getHard()) character.setIsInvincible(false);


        // Checks if the character is invincible, then move the enemies
        if (character.isInvincible()) {
            InvincibilityPotion.invincibilityPhase(character, entities, movementDirection);
            invincibilityActive = true;
        }

        // Enemy movement goes first
        if (!invincibilityActive) Hydra.hydraMovement(entities, movementDirection);
        if (!invincibilityActive) Mercenary.mercenaryMovement(entities, movementDirection);
        if (!invincibilityActive) Assassin.assassinMovement(entities, movementDirection);
        if (!invincibilityActive) ZombieToast.zombieMovement(entities, movementDirection);



        for (Entity entity : entities) {
            // Mercenary should only spawn if there is an enemy for the dungeon
            if (entity.getType().equals("zombie_toast") || entity.getType().equals("spider") || entity.getType().equals("mercenary")) {
                EnemyCheck = 1;
            }
            // Character Movement
            if (entity.getType().equals("player")) {
                MovingEntity temp = (MovingEntity) entity;
                Character temp2  = (Character) entity;
                playerSpawnPosition = temp2.getSpawn();
                
                // If the inital direction is NONE then an item has been used
                if (movementDirection == Direction.NONE) {
                    // Sets the status of player depending on potion
                    useItem(temp2, main, itemUsed);
                    // Sets the bomb 
                    bombHolder = Bomb.useBomb(temp2, main, itemUsed);
                }
                // Either the character moves or it doesnt.
                // Check if its blocked by a wall, in which it doesnt move
                // or if theres 2 boulders next to each other
                
                if (!temp.checkMovement(movementDirection, entities)) {
                    movementDirection = Direction.NONE;
                }

                // If its netiher a wall nor a boulder, check if its a door
                Door doorEntity = temp.checkDoor(movementDirection, entities); 
                if (doorEntity != null) {
                    //If it is a door, check if its locked or not
                    if (doorEntity.getLocked() == true) {
                        //check if a key exists, if it does, remove the key
                        if (doorEntity.checkKey(main)) {
                            doorHolder = doorEntity;
                            doorAddedLater = true;
                        }
                        //If key doesnt exist, dont move
                        else {
                            movementDirection = Direction.NONE;
                        } 
                    }
                }
                
                List<Entity> interactingEntities = temp.checkNext(movementDirection, entities);
                // Checking the bomb
                boolean bombStatus = Bomb.checkBomb(interactingEntities);
                if (bombStatus) {
                    continue;
                }
                
                // If it is here movement is allowed and
                // it might need to interact with an entity.
                temp.moveEntity(movementDirection);
                for (Entity interactingEntity : interactingEntities) {
                    // Check if it is empty square or an entity
                    if (interactingEntity != null) {
                        // EntityFunction that handles all interactions with player
                        interactingEntity.entityFunction(entities, (Character) temp, movementDirection, main);
                        
                        // If the character is dead
                        if (!temp.isAlive()) {
                            // Checking for the One-Ring
                            for (CollectableEntity item : main.inventory) {
                                if (item.getType().equals("one_ring")) {
                                    main.inventory.remove(item);
                                    entitiesToBeRemoved.add(item);
                                    Character respawnedCharacter = new Character(temp.getPosition(), temp.getType(), temp.getID(), temp.getIsInteractable());
                                    entitiesToBeAdded.add(respawnedCharacter);
                                    break;
                                }
                            }
                            entitiesToBeRemoved.add(temp);
                            break;
                        }
                        // If the character is invisible
                        else if (temp2.isInvisible()) {
                            // Item still needs to be removed if it is picked up
                            if (interactingEntity.getClass().getSuperclass().getName().equals("dungeonmania.entities.CollectableEntity")) {
                                entitiesToBeRemoved.add(interactingEntity);
                            }
                            continue;
                        }
                        // If the character isnt dead, then the enemy has to have died in the case of battle
                        // Takes into the account of collectable item
                        else {
                            List<String> nonRemovable = Arrays.asList("boulder", "BLUEportal", "REDportal","YELLOWportal","GREYportal",
                            "switch", "door", "door_unlocked", "exit", "swamp_tile", "zombie_toast_spawner", "light_bulb_on","light_bulb_off",
                            "switch_door", "switchdoor_unlocked", "wire");
                            int dontRemove = 0;
                            for (String curr : nonRemovable) {
                                if (interactingEntity.getType().equals(curr)) dontRemove = 1;
                            }
                            if (dontRemove == 0) entitiesToBeRemoved.add(interactingEntity);

                            // Accounting for chance to receive TheOneRing
                            if (interactingEntity.getClass().getSuperclass().getName().equals("dungeonmania.entities.MovingEntity")) {
                                Random random = new Random();
                                int chance = random.nextInt(21);
                                if (chance == 10) {
                                    String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                                    currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
                                    // Position needs to be stated as checkNext requires a position to run
                                    Position tempPos = new Position(-1, -1);
                                    TheOneRing oneRing = new TheOneRing(tempPos, "one_ring", entityId, false);
                                    main.inventory.add(oneRing);
                                }
                            }
                            // Accounting for chance to receive armour
                            if (interactingEntity.getClass().getSuperclass().getName().equals("dungeonmania.entities.MovingEntity")) {
                                MovingEntity mob = (MovingEntity) interactingEntity;
                                if (mob.getArmour()) {
                                    Random random = new Random();
                                    int chance = random.nextInt(11);
                                    if (chance == 5) {
                                        String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                                        currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
                                        // Position needs to be stated as checkNext requires a position to run
                                        Position tempPos = new Position(-1, -1);
                                        Armour armour = new Armour(tempPos, "armour", entityId, false);
                                        main.inventory.add(armour);
                                    }
                                }
                                Mercenary.mercenaryBattleRadiusChecker(temp, entities);
                            }
                        }
                    }
                }
                if (main.getDungeonGoals().contains("exit")) {
                    checkExitGoal(entities, main, temp);
                }
            }
            
            // Zombie Spawner Ticks
            if (entity.getType().equals("zombie_toast_spawner")) {
                if ((currDungeon.getHard() && currDungeon.getTickCounter() % 15 == 0) || (!currDungeon.getHard() && currDungeon.getTickCounter() % 20 == 0)) {
                    Position zombieSpawn = ZombieToastSpawner.checkWhiteSpace(entity.getPosition(), entities);

                    // If there is no white space around zombie spawner, don't spawn zombie
                    if (zombieSpawn == null) continue;

                    String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                    currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1); 

                    ZombieToast zombieToastEntity = new ZombieToast(zombieSpawn, "zombie_toast", entityId, true);
                    zombieHolder = zombieToastEntity;
                    zombieAddedLater = 1;
                }
            }

            // Spider Movement
            if (entity.getType().equals("spider") && !invincibilityActive) {
                boolean swampMove = true;
                Spider temp = (Spider) entity;
                swampMove = SwampTile.swampCanMove(temp, entities);
                if (swampMove == false) {
                    continue;
                }
                ((MovingEntity) entity).moveSpider(entities, entity);
            }
        }

        // update goals
        if (main.getDungeonGoals().contains("boulder")) {
            checkBoulderGoal(entities, main);
        }
        // Mercenary Spawn Ticks
        // Every 40 ticks of the game causes a new mercenary to spawn
        if (currDungeon.getTickCounter() % 40 == 0 && EnemyCheck == 1) {
            
            // Assassins have a chance of spawning in place of a mercenary (20%)
            Random random = new Random();
            int AssassinSpawn = random.nextInt(5);

            if (AssassinSpawn == 3) {
                String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1); 

                Assassin assassinEntity = new Assassin(playerSpawnPosition, "assassin", entityId, true);
                assassinHolder = assassinEntity;
                assassinAddedLater = 1;
            }

            else {
                String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1); 

                Mercenary mercenaryEntity = new Mercenary(playerSpawnPosition, "mercenary", entityId, true);
                mercenaryHolder = mercenaryEntity;
                mercenaryAddedLater = 1;
            }
        }

        // Hydra Spawn Ticks
        if (currDungeon.getTickCounter() % 50 == 0 && currDungeon.getHard()) {
            String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
            currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1); 

            Position hydraSpawn = ZombieToastSpawner.checkWhiteSpace(character.getPosition(), entities);
            Hydra hydraEntity = new Hydra(hydraSpawn, "hydra", entityId, true);
            hydraHolder = hydraEntity;
            hydraAddedLater = 1;
        }

        // Spider spawner ticks
        if ((Spider.checkMaxSpiders(entities) == false) && (currDungeon.getTickCounter() % 25 == 0)) {
            String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
            currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1); 
            Spider newSpider = new Spider(null, "spider", entityId, true);  
            newSpider.setPosition(newSpider.getSpiderSpawn(entities));                 
            spid = newSpider;
            spiderSpawned = 1;
        }
                 
        if (zombieAddedLater == 1) main.addEntities(zombieHolder);
        if (mercenaryAddedLater == 1) main.addEntities(mercenaryHolder);
        if (hydraAddedLater == 1) main.addEntities(hydraHolder);
        if (assassinAddedLater == 1) main.addEntities(assassinHolder);
        if (spiderSpawned == 1) main.addEntities(spid);
        if (doorAddedLater == true) {
            main.removeEntity(doorHolder);
            Door unlockedDoor = new Door(doorHolder.getPosition(),
             "door_unlocked", doorHolder.getID(), doorHolder.getIsInteractable(), doorHolder.getKeyType(), false);
            main.addEntities(unlockedDoor);
        }

        
        Position playerPos = new Position(0, 0);
        for (Entity ent : entities) {
            if (ent.getType().equals("player")) playerPos = ent.getPosition();
                
        }
        for (Entity currPlayer : entities) {
            if (currPlayer.getType().equals("player")) tempChar = (Character) currPlayer;
        }
        // find boulders to check explosion eligibility
        for (Entity enti : entities) {
            // if boulder, check that the boulder has a switch and explode any nearby bombs
            if (enti.getType().equals("boulder")) {
                Entity bombEnt = null;
                // checks if the square player is moving onto has a bomb, which shouldnt be picked up
                for (Entity currEnt : entities) {
                    if (currEnt.getType().equals("player")) {
                        Position playerPosition = currEnt.getPosition();
                        for (Entity bomb : entities) {
                            if (bomb.getType().equals("bomb") && playerPosition.equals(bomb.getPosition())) {
                                bombEnt = bomb;
                            }
                        }
                    }
                }
                Position entPos = enti.getPosition();
                ArrayList<Position> adjacentPos = entPos.getCardinallyAdjacentPositions();
                if (playerPos.equals(adjacentPos.get(0)) || playerPos.equals(adjacentPos.get(1)) || playerPos.equals(adjacentPos.get(2)) || playerPos.equals(adjacentPos.get(3))) {
                    ((Boulder) enti).doExplode(entities, (Character) tempChar, main, enti, allNearbyEntities);  
                    currDungeon.inventory.remove(bombEnt);  
                }
            }
        }


        // Find switches to check lightbulb light up eligibility
        for (Entity enti : entities) {
            if (enti.getType().equals("light_bulb_on") || enti.getType().equals("light_bulb_off")) {
                LightBulb bulbEntity = (LightBulb) enti;
                //String logic = bulbEntity.getLogic();
                //if (logic == null) {
                    //Check if there are any switches+boulders next to the lightbulb
                    if (bulbEntity.checkSwitchBoulder(main)) {
                        bulbEntity.lightOn();
                    //Otherwise, check if the lightbulb is next to any wires that are connected to switches+boulders
                    } else if (bulbEntity.checkWires(main)) {  
                        bulbEntity.lightOn();
                    } else {
                        bulbEntity.lightOff();
                    }              
            }
        }

        // Find switch doors to check unlock door eligibility
        for (Entity enti : entities) {
            if (enti.getType().equals("switch_door") || enti.getType().equals("switchdoor_unlocked")) {
                SwitchDoor doorEntity = (SwitchDoor) enti;
                if (doorEntity.checkSwitchBoulder(main)) {
                    doorEntity.doorUnlock();
                } else if (doorEntity.checkWires(main)) {
                    doorEntity.doorUnlock();
                } else {
                    doorEntity.doorLock();
                }
            }
        }

       
        // add all nearby entities to the bomb to entiitesToBeRemoved
        entitiesToBeRemoved.addAll(allNearbyEntities);
        // Remove the collectible from the map
        entityRemover(entitiesToBeRemoved, main);
        // Adding entities to the map
        entityAdder(entitiesToBeAdded, main);
        
        // Adding the bomb to the map
        if (bombHolder != null) {
            main.addEntities(bombHolder);
        }

        // The Goal Checker Central


        // Check boulders
        if (main.getDungeonGoals().contains("boulder")) {
            checkBoulderGoal(entities, main);
        }
        // Check exit
        // already done

        // Check treasure
        if (main.getDungeonGoals().contains("treasure")) {
            checkTreasureGoal(entities, main);
        }


        // Check enemies
        if (main.getDungeonGoals().contains("mercenary")) {
            checkEnemiesGoal(entities, main);
        } 




        List<EntityResponse> erList= new ArrayList<EntityResponse>();
        for(Entity entity: main.getEntities()) {
            EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
            erList.add(er);
        }

        List<ItemResponse> irList= new ArrayList<ItemResponse>();
        for(CollectableEntity collectableEntity: main.inventory) {
            ItemResponse ir = new ItemResponse(collectableEntity.getID(), collectableEntity.getType());
            irList.add(ir);
        }

        DungeonResponse dr = new DungeonResponse(currDungeon.getDungeonId(), currDungeon.getDungeonName(),
            erList, irList, currDungeon.buildables, currDungeon.getDungeonGoals());

        lastTick = dr;
        addToRewindDatabase();
        return dr;
    }


    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        System.out.println("passing thru interact");
        // Get entity list
        List<Entity> entities = currDungeon.getEntities();

        // Get inventory
        List<CollectableEntity> inventory = currDungeon.getInventory();
        System.out.println("847");
        // Get the character class
        Character character = Character.getCharacter(entities);
        System.out.println("850");
        // Check if the entity exists within the dungeon
        if (!Dungeon.entityIdCheck(entityId, entities)) {
            throw new IllegalArgumentException("The entity does not exist.");
        }
        System.out.println("855");
        // Check what we are interacting with 
        Entity interaction = Dungeon.IdToEntity(entityId, entities);
        System.out.println("858");
        System.out.println(interaction.getType());
        // Interaction with the mercenary
        if (interaction.getType().equals("mercenary")) {
            System.out.println("Interacting with mercenary");
            
            // Check whether the player is close enough to the mercenary
            if (!Mercenary.playerProximityMercenary(character, interaction)) {
                throw new InvalidActionException("The player is not close enough to the mercenary.");
            }

            // Check whether the player has enough gold to bribe the mercenary
            if (!Treasure.playerHasEnoughGold(inventory)) {
                throw new InvalidActionException("The player does not have enough gold to bribe the mercenary.");
            }



        }

        // Interaction with the spawner
        if (interaction.getType().equals("zombie_toast_spawner")) {

            // Check whether the player is close enough to the spawner
            if (!ZombieToastSpawner.playerProximitySpawner(character, interaction)) {
                throw new InvalidActionException("The player is not close enough to the spawner.");
            }

            // Check whether the player has a weapon to destroy the spawner
            if (!Sword.playerHasSword(inventory)) {
                throw new InvalidActionException("The player does not have a weapon to destory the spawner.");
            }

            interactWithSpawner(inventory, interaction);
        }

        List<EntityResponse> erList= new ArrayList<EntityResponse>();
        for (Entity entity: currDungeon.getEntities()) {
            EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
            erList.add(er);
        }

        List<ItemResponse> irList= new ArrayList<ItemResponse>();
        for (CollectableEntity collectableEntity: currDungeon.inventory) {
            ItemResponse ir = new ItemResponse(collectableEntity.getID(), collectableEntity.getType());
            irList.add(ir);
        }

        DungeonResponse dr = new DungeonResponse(currDungeon.getDungeonId(), currDungeon.getDungeonName(),
            erList, irList, currDungeon.buildables, currDungeon.getDungeonGoals());

        
        lastTick = dr;

        return dr;

    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        List<CollectableEntity> itemsToBeRemoved = new ArrayList<CollectableEntity>();
        if (buildable.equals("bow")) {
            String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
            currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
            // Removing the items
            int wood = 0;
            int arrow = 0;
            for (CollectableEntity item: currDungeon.inventory) {
                if (wood >= 1 && arrow >= 3) {
                    break;
                }
                if (item.getType().equals("wood") && wood < 1) {
                    itemsToBeRemoved.add(item);
                    wood++;
                }
                if (item.getType().equals("arrow") && arrow < 3) {
                    itemsToBeRemoved.add(item);
                    arrow++;
                }
            }
            // Position needs to be stated as checkNext requires a position to run
            Position tempPos = new Position(-1, -1);
            Bow bow = new Bow(tempPos, "bow", entityId, false);
            currDungeon.inventory.add(bow);
            currDungeon.buildables.remove(buildable);
        }
        if (buildable.equals("shield")) {
            String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
            currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
            // Removing the items
            int key = 0;
            int treasure = 0;
            int wood = 0;
            for (CollectableEntity item: currDungeon.inventory) {
                if (wood >= 2 && (treasure >= 1 || key >= 1)) {
                    break;
                }
                if (item.getType().equals("wood") && wood < 2) {
                    itemsToBeRemoved.add(item);
                    wood++;
                }
                if (item.getType().equals("key") && key < 1 && treasure < 1) {
                    itemsToBeRemoved.add(item);
                    currDungeon.setKeyStatus(true);
                    key++;
                }
                if (item.getType().equals("treasure") && treasure < 1 && key < 1) {
                    itemsToBeRemoved.add(item);
                    treasure++;
                }
            }
            // Position needs to be stated as checkNext requires a position to run
            Position tempPos = new Position(-1, -1);
            Shield shield = new Shield(tempPos, "shield", entityId, false);
            currDungeon.inventory.add(shield);
            currDungeon.buildables.remove(buildable);
        }
        if (buildable.equals("midnight_armour")) {
            String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
            currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
            for (CollectableEntity item: currDungeon.inventory) {
                if (item.getType().equals("armour")) {
                    itemsToBeRemoved.add(item);
                    break;
                }
            }
            // Position needs to be stated as checkNext requires a position to run
            Position tempPos = new Position(-1, -1);
            MidnightArmour mArmour = new MidnightArmour(tempPos, "midnight_armour", entityId, true);
            currDungeon.inventory.add(mArmour);
            currDungeon.buildables.remove(buildable);
        }
        if (buildable.equals("sceptre")) {
            String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
            currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
            // Removing the items
            int key = 0;
            int treasure = 0;
            int wood = 0;
            int arrow = 0;
            for (CollectableEntity item: currDungeon.inventory) {
                if ((wood >= 1 || arrow >= 2) && (key >= 1 || treasure >= 1)) {
                    break;
                }
                if (item.getType().equals("wood") && wood < 1 && arrow < 1) {
                    itemsToBeRemoved.add(item);
                    wood++;
                }
                if (item.getType().equals("arrow") && arrow <= 2 && wood < 1) {
                    itemsToBeRemoved.add(item);
                    arrow++;
                }
                if (item.getType().equals("key") && key < 1 && treasure < 1) {
                    itemsToBeRemoved.add(item);
                    currDungeon.setKeyStatus(true);
                    key++;
                }
                if (item.getType().equals("treasure") && treasure < 1 && key < 1) {
                    itemsToBeRemoved.add(item);
                    treasure++;
                }
            }
            Position tempPos = new Position(-1, -1);
            Sceptre sceptre = new Sceptre(tempPos, "sceptre", entityId, true);
            currDungeon.inventory.add(sceptre);
            currDungeon.buildables.remove(buildable);
        }
        // Removing the items
        for (CollectableEntity item: itemsToBeRemoved) {
            currDungeon.inventory.remove(item);
        }

        // Check if buildables can still be made
        int wood = 0;
        int arrow = 0;
        int key = 0;
        int armour = 0;
        int treasure = 0;
        int sunStone = 0;
        for (CollectableEntity item: currDungeon.inventory) {
            switch(item.getType()) {
                case "wood":
                    wood++;
                    break;
                case "arrow":
                    arrow++;
                    break;
                case "key":
                    key++;
                    break;
                case "treasure":
                    treasure++;
                    break;
                case "sun_stone":
                    sunStone++;
                    break;
                case "armour":
                    armour++;
                    break;
            }
        }
        List<String> newBuildables = new ArrayList<String>();
        // Checking Bow
        if (wood >= 1 && arrow >= 3) {
            newBuildables.add("bow");
        } 
        // Checking Shield
        if (wood >= 2 && (treasure >= 1 || key == 1)) {
            newBuildables.add("shield");
        } 
        // Checking Sceptre
        if ((wood >= 1 || arrow >= 2) && (key >= 1 || treasure >= 1) && sunStone >= 1) {
            newBuildables.add("sceptre");
        } 
        // Checking MidnightArmour
        if (armour >= 1 && sunStone >= 1) {
            if (zombieChecker(currDungeon.getEntities())) {
                newBuildables.add("midnight_armour");
            }
        }
        currDungeon.setBuildables(newBuildables);
        List<EntityResponse> erList= new ArrayList<EntityResponse>();
        for(Entity entity: currDungeon.getEntities()) {
            EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
            erList.add(er);
        }

        List<ItemResponse> irList= new ArrayList<ItemResponse>();
        for(CollectableEntity collectableEntity: currDungeon.inventory) {
            ItemResponse ir = new ItemResponse(collectableEntity.getID(), collectableEntity.getType());
            irList.add(ir);
        }

        DungeonResponse dr = new DungeonResponse(currDungeon.getDungeonId(), currDungeon.getDungeonName(),
            erList, irList, currDungeon.buildables, currDungeon.getDungeonGoals());
        
        lastTick = dr;
        addToRewindDatabase();
        return dr;
    }

    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String gameMode) throws IllegalArgumentException {
        if (!Dungeon.gameModeNotValid(gameMode)) {
            throw new IllegalArgumentException("This gamemode is not valid.");
        }

        List<ItemResponse> emptyInventory = new ArrayList<ItemResponse>();
        List<String> emptyBuildables = new ArrayList<String>();

        // Create the unique identifier for the new dungeon
        String dungeonName = "Maze" + dungeonCounter;
        String dungeonId = String.format("dungeon%d", dungeonCounter);
        dungeonCounter += 1;

        // Make a new dungeon object and add it to the dungeons list
        String goals = ":exit";
        Dungeon main = new Dungeon(dungeonName, dungeonId, goals);
        dungeons.add(main);
        currDungeon = main;

        switch (gameMode) {
            case "peaceful":
                currDungeon.setPeaceful(true);
                break;

            case "hard":
                currDungeon.setHard(true);
                break;
        }

        Maze maze = new Maze(xStart, yStart, xEnd, yEnd);
        boolean map[][] = maze.getMap();

        addEntitiesToMaze(main, map, xStart, yStart, xEnd, yEnd);


        List<EntityResponse> erList = new ArrayList<EntityResponse>();
        for (Entity entity: main.getEntities()) {
            EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
            erList.add(er);
        }
        


        DungeonResponse dr = new DungeonResponse(dungeonId, dungeonName, erList, emptyInventory, emptyBuildables, goals);
        lastTick = dr;
        
        return dr;
    }

    // either 1 tick or 5 ticks
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {

        if (ticks <= 0) {
            throw new IllegalArgumentException("Ticks must be greater than 0");
        }

        if (currDungeon.getTickCounter() - ticks < 0) {
            throw new IllegalArgumentException("Cannot go to a negative tick");
        }

        return rewindGame(currDungeon.getTickCounter() - ticks);
    }








    /**
     * 
     * 
     *              HELPER FUNCTIONS
     * 
     * 
     */

    public void clearDatabase() {

        String filename = "src\\main\\java\\dungeonmania\\rewindDatabase.json";
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filename, false);
            fileOutputStream.close();
        } catch (Exception e) {}


    }

    public DungeonResponse rewindGame(int ticks) {

        System.out.println("entered 1046");
        String filename = "src\\main\\java\\dungeonmania\\rewindDatabase.json";
        JsonObject dungeon;
        List<EntityResponse> erList= new ArrayList<EntityResponse>();
        List<ItemResponse> irList= new ArrayList<ItemResponse>();
        try {
            JsonArray jsonArray = JsonParser.parseReader(new FileReader(filename)).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                dungeon = jsonArray.get(i).getAsJsonObject();
                if (dungeon.get("tickCounter").getAsInt() == ticks) {
                    System.out.println("entered correct object here");
                    // in the correct dungeon object
                    Dungeon main = new Dungeon(dungeon.get("dungeonName").getAsString()
                    , dungeon.get("dungeonId").getAsString(), dungeon.get("goals").getAsString());
                    main.setTickCounter(dungeon.get("tickCounter").getAsInt());
                    main.setEntityCounter(dungeon.get("entityCounter").getAsInt());
                    main.setKeyStatus(dungeon.get("keyStatus").getAsBoolean());
                    main.setInvincibilityCounter(dungeon.get("invincibilityPotionCounter").getAsInt());
                    main.setInvisibilityPotionCounter(dungeon.get("invisibilityPotionCounter").getAsInt());


                    currDungeon = main;
                    JsonArray entitiesList = dungeon.get("entities").getAsJsonArray();
                    JsonArray inventoryList = dungeon.get("inventory").getAsJsonArray();
                    JsonArray buildableList = dungeon.get("buildables").getAsJsonArray();
                    addEntitiesInventory(main, entitiesList, inventoryList, buildableList);
                    for(Entity entity: main.getEntities()) {
                        EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
                        erList.add(er);
                    }
                    for(CollectableEntity coEntity: main.getInventory()) {
                        ItemResponse ir = new ItemResponse(coEntity.getID(), coEntity.getType());
                        irList.add(ir);
                    }
                    break;
                }
            }
        
        
        } catch (Exception e) {}

        // TO DO inventory attributes e..g., armor durability

        DungeonResponse newGame = new DungeonResponse(currDungeon.getDungeonId(), currDungeon.getDungeonName(),
        erList, irList, currDungeon.getBuildables(), currDungeon.getDungeonGoals());

        lastTick = newGame;

        // clearDatabase();
        // addToRewindDatabase();

        return newGame;
    }




    public void addToRewindDatabase() {


        String filename = "src\\main\\java\\dungeonmania\\rewindDatabase.json";

        Gson gson = new Gson();
        String json = gson.toJson(lastTick);
        JsonObject jsonObj = gson.fromJson(json, JsonElement.class).getAsJsonObject();

        // Add loadName
        jsonObj.addProperty("entityCounter", currDungeon.getEntityCounter());
        jsonObj.addProperty("tickCounter", currDungeon.getTickCounter());
        jsonObj.addProperty("keyStatus", currDungeon.getKeyStatus());
        jsonObj.addProperty("invisibilityPotionCounter", currDungeon.getInvisibilityPotionCounter());
        jsonObj.addProperty("invincibilityPotionCounter", currDungeon.getInvincibilityPotionCounter());

        JsonArray jsonEntities = jsonObj.get("entities").getAsJsonArray();
        JsonArray jsonInventory = jsonObj.get("inventory").getAsJsonArray();

        // List of entities that have extra attributes
        List<MovingEntity> mvList = new ArrayList<MovingEntity>();
        for (Entity entity : currDungeon.getEntities()) {
            if (entity.getType().equals("player") || entity.getType().equals("mercenary") ||
            entity.getType().equals("spider") || entity.getType().equals("zombie_toast")) {
                MovingEntity mv = (MovingEntity) entity;
                mvList.add(mv);
            }
        }

        List<CollectableEntity> collectableList = new ArrayList<CollectableEntity>();
        for (Entity entity : currDungeon.getEntities()) {
            if (entity.getType().equals("sword") || entity.getType().equals("key") ||
            entity.getType().equals("armour") || entity.getType().equals("bomb") || 
            entity.getType().equals("bow") || entity.getType().equals("shield")) {
                CollectableEntity mv = (CollectableEntity) entity;
                collectableList.add(mv);
            }
        }

        List<StaticEntity> staticList = new ArrayList<StaticEntity>();
        for (Entity entity : currDungeon.getEntities()) {
            if (entity.getType().equals("door") || entity.getType().equals("BLUEportal") ||
            entity.getType().equals("REDportal") || entity.getType().equals("YELLOWportal") ||
            entity.getType().equals("GREYportal")) {
                StaticEntity mv = (StaticEntity) entity;
                staticList.add(mv);
            }
        }

        // Give any moving entities attack and health
        for (int i = 0; i < jsonEntities.size(); i++) {
            JsonObject jEntity = jsonEntities.get(i).getAsJsonObject();

            if (jEntity.get("type").getAsString().equals("player")) {
                for (MovingEntity mvEntity : mvList) {
                    if (mvEntity.getType().equals("player")) {
                        jEntity.addProperty("attack", mvEntity.getAttack());
                        jEntity.addProperty("health", mvEntity.getHealth());
                    }
                }
            }
            else if (jEntity.get("type").getAsString().equals("mercenary")) {
                for (MovingEntity mvEntity : mvList) {
                    if (mvEntity.getType().equals("mercenary")) {
                        jEntity.addProperty("attack", mvEntity.getAttack());
                        jEntity.addProperty("health", mvEntity.getHealth());
                    }
                }
            }
            else if (jEntity.get("type").getAsString().equals("spider")) {
                for (MovingEntity mvEntity : mvList) {
                    if (mvEntity.getType().equals("spider")) {
                        jEntity.addProperty("attack", mvEntity.getAttack());
                        jEntity.addProperty("health", mvEntity.getHealth());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("sword")) {
                for (CollectableEntity coEntity : collectableList) {
                    if (coEntity.getType().equals("sword")) {
                        Sword temp = (Sword) coEntity;
                        jEntity.addProperty("attack", temp.getAttack());
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("key")) {
                for (CollectableEntity coEntity : collectableList) {
                    if (coEntity.getID().equals(jEntity.get("id").getAsString())) {
                        Key temp = (Key) coEntity;
                        jEntity.addProperty("keyNum", temp.getKeyNum());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("armour")) {
                for (CollectableEntity coEntity : collectableList) {
                    if (coEntity.getType().equals("armour")) {
                        Armour temp = (Armour) coEntity;
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("bomb")) {
                for (CollectableEntity coEntity : collectableList) {
                    if (coEntity.getType().equals("bomb")) {
                        Bomb temp = (Bomb) coEntity;
                        jEntity.addProperty("activated", temp.isActivated());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("door")) {
                for (StaticEntity stEntity : staticList) {
                    if (stEntity.getID().equals(jEntity.get("id").getAsString())) {
                        Door temp = (Door) stEntity;
                        jEntity.addProperty("keyType", temp.getKeyType());
                        jEntity.addProperty("locked", temp.getLocked());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("portal")) {
                for (StaticEntity stEntity : staticList) {
                    if (stEntity.getType().equals("portal")) {
                        Portal temp = (Portal) stEntity;
                        jEntity.addProperty("colour", temp.getColour());
                    }
                }
            }
        }

        for (int i = 0; i < jsonInventory.size(); i++) {
            JsonObject jEntity = jsonInventory.get(i).getAsJsonObject();

            if (jEntity.get("type").getAsString().equals("sword")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("sword")) {
                        Sword temp = (Sword) coEntity;
                        jEntity.addProperty("attack", temp.getAttack());
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("key")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("key")) {
                        Key temp = (Key) coEntity;
                        jEntity.addProperty("keyNum", temp.getKeyNum());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("armour")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("armour")) {
                        Armour temp = (Armour) coEntity;
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("bomb")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("bomb")) {
                        Bomb temp = (Bomb) coEntity;
                        jEntity.addProperty("activated", temp.isActivated());
                    }
                }
            }
            else if (jEntity.get("type").getAsString().equals("bow")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("bow")) {
                        Bow temp = (Bow) coEntity;
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

            else if (jEntity.get("type").getAsString().equals("shield")) {
                for (CollectableEntity coEntity : currDungeon.getInventory()) {
                    if (coEntity.getType().equals("shield")) {
                        Shield temp = (Shield) coEntity;
                        jEntity.addProperty("durability", temp.getDurability());
                    }
                }
            }

        }
        
        File newFile = new File(filename);

        String json2 = "";

        if (newFile.length() == 0) {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(jsonObj);
            json2 = gson.toJson(jsonArray);
        }

        else {
            try {
                JsonArray jsonArray = JsonParser.parseReader(new FileReader(filename)).getAsJsonArray();
                jsonArray.add(jsonObj);
                json2 = gson.toJson(jsonArray);
            } catch (Exception e) {}
        }

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(filename, false);
            fileOutputStream.write(json2.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {}

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
                    map.put(entity.getPosition(), 3);
                }
                else {
                    map.put(entity.getPosition(), 2);
                }
            }
        }

        for (Integer amt : map.values()) {
            // even
            if (amt == 1) {
                return; // not finished with boulders goal 
            }
        }

        // Otherwise Goal has been completed!
        // Need to remove it from the goals string

        dungeon.setDungeonGoals(removeGoal(":boulder", dungeon));

    }

    public String removeGoal(String goal, Dungeon dungeon) {

        // ((X AND Y) OR (Z OR (G OR F)))
        // ( AND (Z OR F))
        String returnGoal = dungeon.getDungeonGoals();

        System.out.println(returnGoal);
        returnGoal = returnGoal.replace("(" + goal + ")", "");
        returnGoal = returnGoal.replace(goal + " AND ", "");
        returnGoal = returnGoal.replace(" AND " + goal, "");

        returnGoal = returnGoal.replace(" AND (" + goal + " OR " + ":mercenary)", "");
        returnGoal = returnGoal.replace(" AND (" + goal + " OR " + ":treasure)", "");
        returnGoal = returnGoal.replace(" AND (" + goal + " OR " + ":exit)", "");
        returnGoal = returnGoal.replace(" AND (" + goal + " OR " + ":boulder)", "");

        returnGoal = returnGoal.replace("(:mercenary " + " OR " + goal +") AND ", "");
        returnGoal = returnGoal.replace("(:treasure " + " OR " + goal +") AND ", "");
        returnGoal = returnGoal.replace("(:exit " + " OR " + goal +") AND ", "");
        returnGoal = returnGoal.replace("(:boulder " + " OR " + goal +") AND", "");

        returnGoal = returnGoal.replace("(" + goal + " OR " + ":mercenary)", "");
        returnGoal = returnGoal.replace("(" + goal + " OR " + ":treasure)", "");
        returnGoal = returnGoal.replace("(" + goal + " OR " + ":exit)", "");
        returnGoal = returnGoal.replace("(" + goal + " OR " + ":boulder)", "");

        returnGoal = returnGoal.replace("(:mercenary " + " OR " + goal +")", "");
        returnGoal = returnGoal.replace("(:treasure " + " OR " + goal +")", "");
        returnGoal = returnGoal.replace("(:exit " + " OR " + goal +")", "");
        returnGoal = returnGoal.replace("(:boulder " + " OR " + goal +")", "");

        // case where no brackets
        returnGoal = returnGoal.replace(goal, "");

        return returnGoal;
    }




    public void checkExitGoal(List<Entity> entities, Dungeon dungeon, MovingEntity player) {
        for (Entity entity : entities) {
            if (entity.getType().equals("exit")) {
                if (entity.getPosition().equals(player.getPosition())) {
                    dungeon.setDungeonGoals(removeGoal(":exit", dungeon));
                }
            }
        }
    }

    public void checkTreasureGoal(List<Entity> entities, Dungeon dungeon) {

        boolean isThereTreasure = false;
        for (Entity entity : entities) {

            if (entity.getType().equals("treasure")) {
                isThereTreasure = true;
            }
        }

        if (isThereTreasure == false) {
            dungeon.setDungeonGoals(removeGoal(":treasure", dungeon));
        }
    }

    public void checkEnemiesGoal(List<Entity> entities, Dungeon dungeon) {

        boolean isThereEnemy = false;
        for (Entity entity : entities) {

            if (entity.getType().equals("mercenary") ||
                entity.getType().equals("spider") || entity.getType().equals("zombie_toast")) {
                
                isThereEnemy = true;

            }
        }

        if (isThereEnemy == false) {
            dungeon.setDungeonGoals(removeGoal(":mercenary", dungeon));
        }
    }

    /**
     * This function checks whether or not the item given in tick is valid
     * @param itemUsed
     */
    public boolean itemUsedInvalid(String itemUsed) {
        if (itemUsed == null) return true;

        String[] items = {"bomb", "health_potion", "invincibility_potion", "invisibility_potion"};
        List<String> itemAvailable = Arrays.asList(items);

        for (CollectableEntity collectables : currDungeon.getInventory()) {
            if (collectables.getID().equals(itemUsed)) {
                if (itemAvailable.contains(collectables.getType())) return true;
            }
        }
        
        return false;
    }

    /**
     * This function checks whether or not the item given in tick is in the inventory
     * @param itemUsed
     */
    public boolean itemUsedNotInInventory(String itemUsed) {
        if (itemUsed == null) return true;

        List<CollectableEntity> inventory = currDungeon.getInventory();

        for (CollectableEntity collectable : inventory) {
            if (collectable.getID().equals(itemUsed)) return true;
        }
            
        return false;
    }


    /**
     * Removes an entity from Entities List
     * @param entityList - the list of entities to be removed in the dungeon
     * @param main - the dungeon
     */
     public void entityRemover(List<Entity> entityList, Dungeon main) {
        for (Entity entityToBeRemoved : entityList) {
            if (entityToBeRemoved != null) {
                if (entityToBeRemoved.getClass().getSuperclass().getName().equals("dungeonmania.entities.CollectableEntity")) {
                    if (entityToBeRemoved.getType().equals("key")) {
                        if (main.getKeyStatus()) {
                            main.setKeyStatus(false);
                        }
                        else if (CollectableEntity.keyChecker(main.inventory)) {
                            return;
                        }
                    }
                    main.removeEntity(entityToBeRemoved);
                }
                else if (entityToBeRemoved.getClass().getSuperclass().getName().equals("dungeonmania.entities.MovingEntity")) {
                    main.removeEntity(entityToBeRemoved);
                }
                else if (entityToBeRemoved.getClass().getSuperclass().getName().equals("dungeonmania.entities.StaticEntity")) {
                    main.removeEntity(entityToBeRemoved);
                }
            }
        }
    }

     /**
     * Removes an entity from Entities List
     * @param entityList - the list of entities to be removed in the dungeon
     * @param main - the dungeon
     */
    public void entityAdder(List<Entity> entityList, Dungeon main) {
        for (Entity entityToBeAdded: entityList) {
            main.addEntities(entityToBeAdded);
        }
    }
    /**
     * Uses the item in the inventory
     * @param player - the character
     * @param main - the dungeon
     * @param itemUsed - the item that is used
     */
    public void useItem(Character player, Dungeon main, String itemUsed) {
        if (itemUsed != null) {
            for (CollectableEntity entity2: main.inventory) {
                if (entity2.getID().equals(itemUsed)) {
                    if (entity2.getType().equals("invisibility_potion")) {
                        player.setIsInvisible(true);
                        main.inventory.remove(entity2);
                        return;
                        
                    }
                    if (entity2.getType().equals("invincibility_potion")) {
                        player.setIsInvincible(true);
                        main.inventory.remove(entity2);
                        return;
                    }
                    if (entity2.getType().equals("health_potion")) {
                        player.restoreHealth();
                        main.inventory.remove(entity2);
                        return;
                    }
                }
            }
        }
    }


 
    /**
     * Helper Function that takes in the json file and adds all entities into entities list
     * @param dungeonName
     * @param main
     */
    public void addEntitiesToList(String dungeonName, Dungeon main) {

        try {
            Gson gson = new Gson();
            String json = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
            JsonObject jsonObject = gson.fromJson(json, JsonElement.class).getAsJsonObject(); 
            JsonArray entitiesList = jsonObject.get("entities").getAsJsonArray();
            
            for (int i = 0; i < entitiesList.size(); i++) {
                JsonObject entity = entitiesList .get(i).getAsJsonObject();
                String type = entity.get("type").getAsString();
                int x = entity.get("x").getAsInt();
                int y = entity.get("y").getAsInt();
                Position position = new Position(x,y);;
                String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
                switch(type) {
                    case "player":
                        Character characterEntity = new Character(position, type, entityId , false);
                        main.addEntities(characterEntity);  
                        characterEntity.setSpawn(position);
                        break;
                    case "wall":                       
                        Wall wallEntity = new Wall(position, type, entityId , false);
                        main.addEntities(wallEntity);  
                        break;
                    case "exit":
                        Exit exitEntity = new Exit(position, type, entityId, false);
                        main.addEntities(exitEntity);
                        break;
                    case "boulder":
                        Boulder boulderEntity= new Boulder(position, type, entityId, false);
                        main.addEntities(boulderEntity);
                        break;
                    case "switch":
                        Switch switchEntity = new Switch(position, type, entityId, false);
                        main.addEntities(switchEntity);
                        break;
                    case "door":
                        int keyType = entity.get("key").getAsInt();
                        Door doorEntity = new Door(position, type, entityId, false, keyType, true);
                        main.addEntities(doorEntity);
                        break;
                    case "portal":
                        String colour = entity.get("colour").getAsString();
                        Portal portalEntity = new Portal(position, colour+"portal", entityId, false, colour);
                        main.addEntities(portalEntity);
                        break;
                    case "zombie_toast_spawner":
                        ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(position, type, entityId, true);
                        main.addEntities(zombieToastSpawner);
                        break;
                    case "key":
                        main.setKeyCounter(main.getKeyCounter() + 1);
                        Key key = new Key(position, type, entityId, false, main.getKeyCounter());
                        main.addEntities(key);
                        break;
                    case "arrow":
                        Arrows arrows = new Arrows(position, type, entityId, false);
                        main.addEntities(arrows);
                        break;
                    case "bomb":
                        Bomb bomb = new Bomb(position, type, entityId, false);
                        main.addEntities(bomb);
                        break;
                    case "health_potion":
                        HealthPotion healthPotion = new HealthPotion(position, type, entityId, false);
                        main.addEntities(healthPotion);
                        break;
                    case "invincibility_potion":
                        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(position, type, entityId, false);
                        main.addEntities(invincibilityPotion);
                        break;
                    case "invisibility_potion":
                        InvisibilityPotion invisibilityPotion = new InvisibilityPotion(position, type, entityId, false);
                        main.addEntities(invisibilityPotion);
                        break;
                    case "sword":
                        Sword sword = new Sword(position, type, entityId, false);
                        main.addEntities(sword);
                        break;
                    case "treasure":
                        Treasure treasure = new Treasure(position, type, entityId, false);
                        main.addEntities(treasure);
                        break;
                    case "wood":
                        Wood wood = new Wood(position, type, entityId, false);
                        main.addEntities(wood);
                        break;
                    case "spider":
                        Spider spiderEntity = new Spider(position, type, entityId, false);
                        main.addEntities(spiderEntity);
                        break;
                    case "zombie_toast":
                        ZombieToast zombieToast = new ZombieToast(position, type, entityId, false);
                        main.addEntities(zombieToast);
                        break;
                    case "mercenary":
                        Mercenary mercenaryEntity = new Mercenary(position, type, entityId, true);
                        main.addEntities(mercenaryEntity);
                        break;
                    case "hydra":
                        Hydra hydraEntity = new Hydra(position, type, entityId, true);
                        main.addEntities(hydraEntity);
                        break;
                    case "assassin":
                        Assassin assassinEntity = new Assassin(position, type, entityId, true);
                        main.addEntities(assassinEntity);
                        break;
                    case "swamp_tile":
                        SwampTile swamp = new SwampTile(position, type, entityId, false);
                        swamp.setMovementFactor(entity.get("movement_factor").getAsInt());
                        main.addEntities(swamp);
                        break;
                    case "light_bulb_off":
                        LightBulb bulb  = new LightBulb(position, type, entityId, false);
                        main.addEntities(bulb);
                        break;
                    case "time_turner":
                        TimeTurner tt = new TimeTurner(position, type, entityId, false);
                        main.addEntities(tt);
                        break;
                    case "sun_stone":
                        SunStone sunStone  = new SunStone(position, type, entityId, false);
                        main.addEntities(sunStone);
                        break;
                    case "armour":
                        Armour armour  = new Armour(position, type, entityId, false);
                        main.addEntities(armour);
                        break;
                    case "switch_door":
                        SwitchDoor switchDoor = new SwitchDoor(position, type, entityId, false);
                        main.addEntities(switchDoor);
                        break;
                    case "wire":
                        Wire wire =  new Wire(position, type, entityId, false);
                        main.addEntities(wire);
                        break;
                }
            }
        } catch (Exception e) {

        }   
    }

    /**
     * Helper Function that returns the goals as a string
     * @param dungeonName
     * @return returnGoal
     */
    public String getGoalsFromJson(String dungeonName)  {
        String returnGoal = "";
        try {
            Gson gson = new Gson();
            String json = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
            JsonObject jsonObject = gson.fromJson(json, JsonElement.class).getAsJsonObject();
            JsonObject goalCondition = jsonObject.get("goal-condition").getAsJsonObject();
            String goal = goalCondition.get("goal").getAsString();
            // For the case of a double goal
            if (goal.equals("AND") || goal.equals("OR")) {
                JsonArray subGoals = goalCondition.get("subgoals").getAsJsonArray();
                String firstString = returnSubGoal(subGoals.get(0).getAsJsonObject()); 
                String secondString = returnSubGoal(subGoals.get(1).getAsJsonObject()); 
                returnGoal = String.format("(%s %s %s)", firstString, goal, secondString);
            }

            // we have a single goal, e.g., just exit
            else {
                returnGoal = jsonifyGoal(goal);
            }

        } catch (Exception e) {

        }
        return returnGoal;
        
    }

    public String jsonifyGoal(String goal) {

        String returnGoal = "";
        if (goal.equals("enemies")) {
            // leave mercenary to be representative of all enemies for now..
            returnGoal = ":mercenary";
        }
        else if (goal.equals("boulders")) {
            returnGoal = ":boulder";
        }
        else {
            // this covers case for treasure and exit
            returnGoal = ":" + goal;
        }
        return returnGoal;
    }

    // Applied recursively
    public String returnSubGoal(JsonObject goalObject) {

        String returnGoal = "";
        String goal = goalObject.get("goal").getAsString();

        if (goal.equals("AND") || goal.equals("OR")) {
            JsonArray subGoals = goalObject.get("subgoals").getAsJsonArray();
            String firstString = returnSubGoal(subGoals.get(0).getAsJsonObject()); 
            String secondString = returnSubGoal(subGoals.get(1).getAsJsonObject()); 
            returnGoal = String.format("(%s %s %s)", firstString, goal, secondString);
        }

        // we have a single goal, e.g., just exit
        else {
            returnGoal = jsonifyGoal(goal);
        }

        return returnGoal;
    }


    /**
     * Helper Function that returns true if an enemy goal could be found in the 
     * json file 
     * @param filename
     * @param enemy
     * @return boolean
     */
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


    /**
     * This function adds on a tick if the character is invincible or invisible
     * @param character
     */
    public void potionTickAdder(Character character) {
        // If the character is invisible, add onto the tick counter
        if (character.isInvisible()) {
            currDungeon.setInvisibilityPotionCounter(currDungeon.getInvisibilityPotionCounter() + 1);
        }

        // If the character is invincible, add onto the tick counter
        if (character.isInvincible()) {
            currDungeon.setInvincibilityCounter(currDungeon.getInvincibilityPotionCounter() + 1);
        }
    }

    /**
     * This function checks whether the potion should still be active or not
     * @param character
     */
    public void potionChecker(Character character) {
        // Remove the invisibility potion after 15 ticks
        if (currDungeon.getInvisibilityPotionCounter() % 15 == 0) {
            character.setIsInvisible(false);
        }

        // Remove the invincibility potion after 10 ticks
        if (currDungeon.getInvincibilityPotionCounter() % 10 == 0) {
            character.setIsInvincible(false);
        }
    }


    /**
     * returns current dungeon
     * @return Dungeon
     */
    public Dungeon getCurrDungeon() {
        return this.currDungeon;
    }

        
    /**
     * adds entities to inventory
     * @param main
     * @param entitiesList
     * @param inventoryList
     */
    public void addEntitiesInventory(Dungeon main, JsonArray entitiesList, JsonArray inventoryList, JsonArray buildableList) {

        for (int i = 0; i < entitiesList.size(); i++) {
            JsonObject entity = entitiesList.get(i).getAsJsonObject();
            String type = entity.get("type").getAsString();
            String entityId = entity.get("id").getAsString();
            JsonObject posObj = entity.get("position").getAsJsonObject();
            int x = posObj.get("x").getAsInt();
            int y = posObj.get("y").getAsInt();
            Position position = new Position(x,y);
            switch (type) {
                case "player":
                    Character characterEntity = new Character(position, type, entityId , false);
                    main.addEntities(characterEntity);  
                    characterEntity.setAttack(entity.get("attack").getAsInt());
                    
                    if (currDungeon.getHard()) {
                        characterEntity.setHealth((entity.get("health").getAsInt()) - 4);
                    }
                    else {
                        characterEntity.setHealth(entity.get("health").getAsInt());
                    }
                    
                    characterEntity.setSpawn(position);
                    break;
                case "wall":                       
                    Wall wallEntity = new Wall(position, type, entityId , false);
                    main.addEntities(wallEntity);  
                    break;
                case "exit":
                    Exit exitEntity = new Exit(position, type, entityId, false);
                    main.addEntities(exitEntity);
                    break;
                case "boulder":
                    Boulder boulderEntity= new Boulder(position, type, entityId, false);
                    main.addEntities(boulderEntity);
                    break;
                case "switch":
                    Switch switchEntity = new Switch(position, type, entityId, false);
                    main.addEntities(switchEntity);
                    break;
                case "door":    
                    int keyType = entity.get("keyType").getAsInt();
                    boolean locked = entity.get("locked").getAsBoolean();
                    Door doorEntity = new Door(position, type, entityId, false, keyType, locked);
                    main.addEntities(doorEntity);
                    break;
                case "REDportal":
                    String colour = entity.get("colour").getAsString();
                    Portal portalEntity = new Portal(position, type, entityId, false, colour);
                    main.addEntities(portalEntity);
                    break;
                case "BLUEportal":
                    String colour2 = entity.get("colour").getAsString();
                    Portal portalEntity2 = new Portal(position, type, entityId, false, colour2);
                    main.addEntities(portalEntity2);
                    break;
                case "GREYportal":
                    String colour3 = entity.get("colour").getAsString();
                    Portal portalEntity3 = new Portal(position, type, entityId, false, colour3);
                    main.addEntities(portalEntity3);
                    break;
                case "YELLOWportal":
                    String colour4 = entity.get("colour").getAsString();
                    Portal portalEntity4 = new Portal(position, type, entityId, false, colour4);
                    main.addEntities(portalEntity4);
                    break;
                case "zombie_toast_spawner":
                    ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(position, type, entityId, true);
                    main.addEntities(zombieToastSpawner);
                    break;
                case "key":
                    int keyNum = entity.get("keyNum").getAsInt();
                    Key key = new Key(position, type, entityId, false, keyNum);
                    main.addEntities(key);
                    break;
                case "arrow":
                    Arrows arrows = new Arrows(position, type, entityId, false);
                    main.addEntities(arrows);
                    break;
                case "bomb":
                    Bomb bomb = new Bomb(position, type, entityId, false);
                    bomb.setActivated(entity.get("activated").getAsBoolean());
                    main.addEntities(bomb);
                    break;
                case "health_potion":
                    HealthPotion healthPotion = new HealthPotion(position, type, entityId, false);
                    main.addEntities(healthPotion);
                    break;
                case "invincibility_potion":
                    InvincibilityPotion invincibilityPotion = new InvincibilityPotion(position, type, entityId, false);
                    main.addEntities(invincibilityPotion);
                    break;
                case "invisibility_potion":
                    InvisibilityPotion invisibilityPotion = new InvisibilityPotion(position, type, entityId, false);
                    main.addEntities(invisibilityPotion);
                    break;
                case "sword":
                    Sword sword = new Sword(position, type, entityId, false);
                    main.addEntities(sword);
                    sword.setAttack(entity.get("attack").getAsInt());
                    sword.setDurability(entity.get("durability").getAsInt());
                    break;
                case "treasure":
                    Treasure treasure = new Treasure(position, type, entityId, false);
                    main.addEntities(treasure);
                    break;
                case "wood":
                    Wood wood = new Wood(position, type, entityId, false);
                    main.addEntities(wood);
                    break;
                case "spider":
                    Spider spiderEntity = new Spider(position, type, entityId, false);
                    spiderEntity.setAttack(entity.get("attack").getAsInt());
                    spiderEntity.setHealth(entity.get("health").getAsInt());
                    spiderEntity.setLoopPos(entity.get("loopPos").getAsInt());
                    spiderEntity.setClockwise(entity.get("clockwise").getAsBoolean());
                    main.addEntities(spiderEntity);
                    break;
                case "zombie_toast":
                    ZombieToast zombieToast = new ZombieToast(position, type, entityId, false);
                    zombieToast.setAttack(entity.get("attack").getAsInt());
                    zombieToast.setHealth(entity.get("health").getAsInt());
                    main.addEntities(zombieToast);
                    break;
                case "mercenary":
                    Mercenary mercenaryEntity = new Mercenary(position, type, entityId, true);
                    mercenaryEntity.setAttack(entity.get("attack").getAsInt());
                    mercenaryEntity.setHealth(entity.get("health").getAsInt());
                    main.addEntities(mercenaryEntity);
                    break;
                case "time_turner":
                    TimeTurner tt = new TimeTurner(position, type, entityId, false);
                    main.addEntities(tt);
                    break;
                case "swamp_tile":
                    SwampTile swamp = new SwampTile(position, type, entityId, false);
                    main.addEntities(swamp);
                    break;
                case "light_bulb_off":
                    LightBulb bulb  = new LightBulb(position, type, entityId, false);
                    main.addEntities(bulb);
                    break;
                case "wire":
                    Wire wire =  new Wire(position, type, entityId, false);
                    main.addEntities(wire);
                    break;
                case "switch_door":
                    SwitchDoor switchDoor = new SwitchDoor(position, type, entityId, false);
                    main.addEntities(switchDoor);
                    break;
                case "hydra":
                    Hydra hydraEntity = new Hydra(position, type, entityId, true);
                    main.addEntities(hydraEntity);
                    break;
                case "assassin":
                    Assassin assassinEntity = new Assassin(position, type, entityId, true);
                    assassinEntity.setAttack(entity.get("attack").getAsInt());
                    assassinEntity.setHealth(entity.get("health").getAsInt());
                    main.addEntities(assassinEntity);
                    break;
            }
        }

        for (int i = 0; i < inventoryList.size(); i++) {
            JsonObject entity = inventoryList.get(i).getAsJsonObject();
            String type = entity.get("type").getAsString();
            String entityId = entity.get("id").getAsString();
            Position position = new Position(-1, -1);
            switch (type) {
                case "sword":
                    Sword sword = new Sword(position, type, entityId, false);
                    sword.setAttack(entity.get("attack").getAsInt());
                    sword.setDurability(entity.get("durability").getAsInt());
                    main.inventory.add(sword);
                    
                    break;
                case "bomb":
                    Bomb bomb = new Bomb(position, type, entityId, false);
                    bomb.setActivated(entity.get("activated").getAsBoolean());
                    main.inventory.add(bomb);
                    break;
                case "health_potion":
                    HealthPotion healthPotion = new HealthPotion(position, type, entityId, false);
                    main.inventory.add(healthPotion);
                    break;
                case "invincibility_potion":
                    InvincibilityPotion invincibilityPotion = new InvincibilityPotion(position, type, entityId, false);
                    main.inventory.add(invincibilityPotion);
                    break;
                case "invisibility_potion":
                    InvisibilityPotion invisibilityPotion = new InvisibilityPotion(position, type, entityId, false);
                    main.inventory.add(invisibilityPotion);
                    break;
                case "armour":
                    Armour armour = new Armour(position, type, entityId, false);
                    armour.setDurability(entity.get("durability").getAsInt());
                    main.inventory.add(armour);
                    break;
                case "key":
                    int keyNum = entity.get("keyNum").getAsInt();
                    Key key = new Key(position, type, entityId, false, keyNum);
                    main.inventory.add(key);
                    break;
                case "treasure":
                    Treasure treasure = new Treasure(position, type, entityId, false);
                    main.inventory.add(treasure);
                    break;
                case "wood":
                    Wood wood = new Wood(position, type, entityId, false);
                    main.inventory.add(wood);
                    break;
                case "arrow":
                    Arrows arrows = new Arrows(position, type, entityId, false);
                    main.inventory.add(arrows);
                    break;
                case "one_ring":
                    TheOneRing one = new TheOneRing(position, type, entityId, false);
                    main.inventory.add(one);
                    break;
                case "bow":
                    Bow bow = new Bow(position, type, entityId, false);
                    bow.setDurability(entity.get("durability").getAsInt());
                    main.inventory.add(bow);
                    break;
                case "shield":
                    Shield shield = new Shield(position, type, entityId, false);
                    shield.setDurability(entity.get("durability").getAsInt());
                    main.inventory.add(shield);
                    break;
                case "time_turner":
                    TimeTurner tt = new TimeTurner(position, type, entityId, false);
                    main.inventory.add(tt);
            }
        }

        for (int i = 0; i < buildableList.size(); i++) {
            String entity = buildableList.get(i).getAsString();
            main.buildables.add(entity);
        }

    }

    /**
     * Interacts with the spawner - destroying it and removing a durability off the sword
     * @param inventory - the player's inventory
     * @param interaction - the interactable entity
     */
    public void interactWithSpawner(List<CollectableEntity> inventory, Entity interaction) {
        // Remove the spawner
        currDungeon.removeEntity(interaction);

        // Take off one durability of the sword
        for (CollectableEntity item : inventory) {
            if (item.getType().equals("sword")) {
                Sword sword = (Sword) interaction;
                sword.reduceDurability();

                if (sword.checkDurability() != null) {
                    currDungeon.inventory.remove(item);
                }

                return;
            }
        }
    }
    /**
     * Searches for a zombie
     * @param entites - list of entities in the dungeon
     */
    public boolean zombieChecker(List<Entity> entities) {
        for (Entity entity: entities) {
            if (entity.getType().equals("zombie_toast")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper function that takes in the randomly generated maze and adds all entities into entities list
     * @param main
     * @param map[][]
     * @param xStart
     * @param yStart
     * @param xEnd
     * @param yEnd
     */
    public void addEntitiesToMaze(Dungeon main, boolean map[][], int xStart, int yStart, int xEnd, int yEnd) {
        for (int row = 0; row < map.length; row++) {

            for (int col = 0; col < map[row].length; col++) {

                Position position = new Position(col, row);

                if (col == yStart && row == xStart) {
                    String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                    currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
                    
                    Character characterEntity = new Character(position, "player", entityId, false);
                    main.addEntities(characterEntity);  
                    characterEntity.setSpawn(position);
                }

                else if (col == yEnd && row == xEnd) {
                    String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                    currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);

                    Exit exitEntity = new Exit(position, "exit", entityId, false);
                    main.addEntities(exitEntity);
                }

                else if (map[row][col] == false || row == 0 || row == 49 ||  col == 0 || col == 49) {
                    String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                    currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);

                    Wall wallEntity = new Wall(position, "wall", entityId , false);
                    main.addEntities(wallEntity);   
                }

            }
        }
    }

}
