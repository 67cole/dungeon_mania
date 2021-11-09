package dungeonmania;

import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.entities.Spider;
import dungeonmania.entities.MovingEntity;
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

    /**
     * creates a new game
     * @param dungeonName
     * @param gameMode
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {    
        // INCOMPLETE *********
        if (!dungeonNotValid(dungeonName)) {
            throw new IllegalArgumentException("This dungeon does not exist.");
        }

        if (!gameModeNotValid(gameMode)) {
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
            case "Peaceful":
                currDungeon.setPeaceful(true);
                break;

            case "Hard":
                currDungeon.setHard(true);
                break;
        }
        addEntitiesToList(dungeonName, main);
        
        List<EntityResponse> erList = new ArrayList<EntityResponse>();
        for (Entity entity: main.getEntities()) {
            EntityResponse er = new EntityResponse(entity.getID(), entity.getType(), entity.getPosition(), entity.getIsInteractable());
            erList.add(er);
        }
        
        // TODO: Sample goals

        DungeonResponse dr = new DungeonResponse(dungeonId, dungeonName, erList, emptyInventory, emptyBuildables, goals);
        lastTick = dr;
        
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
            if (entity.getType().equals("door") || entity.getType().equals("portal")) {
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
                    int keyType = entity.get("keyType").getAsInt();
                    boolean locked = entity.get("locked").getAsBoolean();

                    Door doorEntity = new Door(position, type, entityId, true, keyType, locked);

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
                case "key":
                    int keyNum = entity.get("keyNum").getAsInt();
                    Key key = new Key(position, type, entityId, true, keyNum);
                    main.addEntities(key);
                    break;
                case "arrow":
                    Arrows arrows = new Arrows(position, type, entityId, true);
                    main.addEntities(arrows);
                    break;
                case "bomb":
                    Bomb bomb = new Bomb(position, type, entityId, true);
                    bomb.setActivated(entity.get("activated").getAsBoolean());
                    main.addEntities(bomb);
                    break;
                case "health_potion":
                    HealthPotion healthPotion = new HealthPotion(position, type, entityId, true);
                    main.addEntities(healthPotion);
                    break;
                case "invincibility_potion":
                    InvincibilityPotion invincibilityPotion = new InvincibilityPotion(position, type, entityId, true);
                    main.addEntities(invincibilityPotion);
                    break;
                case "invisibility_potion":
                    InvisibilityPotion invisibilityPotion = new InvisibilityPotion(position, type, entityId, true);
                    main.addEntities(invisibilityPotion);
                    break;
                case "sword":
                    Sword sword = new Sword(position, type, entityId, true);
                    main.addEntities(sword);
                    sword.setAttack(entity.get("attack").getAsInt());
                    sword.setDurability(entity.get("durability").getAsInt());
                    break;
                case "treasure":
                    Treasure treasure = new Treasure(position, type, entityId, true);
                    main.addEntities(treasure);
                    break;
                case "wood":
                    Wood wood = new Wood(position, type, entityId, true);
                    main.addEntities(wood);
                    break;
                case "spider":
                    Spider spiderEntity = new Spider(position, type, entityId, true);
                    spiderEntity.setAttack(entity.get("attack").getAsInt());
                    spiderEntity.setHealth(entity.get("health").getAsInt());
                    main.addEntities(spiderEntity);
                    break;
                case "zombie_toast":
                    ZombieToast zombieToast = new ZombieToast(position, type, entityId, true);
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
                
            }
        }

        for (int i = 0; i < inventoryList.size(); i++) {
            JsonObject entity = inventoryList.get(i).getAsJsonObject();
            String type = entity.get("type").getAsString();
            String entityId = entity.get("id").getAsString();
            Position position = new Position(-1, -1);
            switch (type) {
                case "sword":
                    Sword sword = new Sword(position, type, entityId, true);
                    main.inventory.add(sword);
                    sword.setAttack(entity.get("attack").getAsInt());
                    sword.setDurability(entity.get("durability").getAsInt());
                    break;
                case "bomb":
                    Bomb bomb = new Bomb(position, type, entityId, true);
                    bomb.setActivated(entity.get("activated").getAsBoolean());
                    main.inventory.add(bomb);
                    break;
                case "health_potion":
                    HealthPotion healthPotion = new HealthPotion(position, type, entityId, true);
                    main.inventory.add(healthPotion);
                    break;
                case "invincibility_potion":
                    InvincibilityPotion invincibilityPotion = new InvincibilityPotion(position, type, entityId, true);
                    main.inventory.add(invincibilityPotion);
                    break;
                case "invisibility_potion":
                    InvisibilityPotion invisibilityPotion = new InvisibilityPotion(position, type, entityId, true);
                    main.inventory.add(invisibilityPotion);
                    break;
                case "armour":
                    Armour armour = new Armour(position, type, entityId, true);
                    armour.setDurability(entity.get("durability").getAsInt());
                    main.inventory.add(armour);
                    break;
                case "key":
                    int keyNum = entity.get("keyNum").getAsInt();
                    Key key = new Key(position, type, entityId, true, keyNum);
                    main.inventory.add(key);
                    break;
                case "treasure":
                    Treasure treasure = new Treasure(position, type, entityId, true);
                    main.inventory.add(treasure);
                    break;
                case "wood":
                    Wood wood = new Wood(position, type, entityId, true);
                    main.inventory.add(wood);
                    break;
                case "arrow":
                    Arrows arrows = new Arrows(position, type, entityId, true);
                    main.inventory.add(arrows);
                    break;
                case "one_ring":
                    TheOneRing one = new TheOneRing(position, type, entityId, true);
                    main.inventory.add(one);
                    break;
                case "bow":
                    Bow bow = new Bow(position, type, entityId, true);
                    bow.setDurability(entity.get("durability").getAsInt());
                    main.inventory.add(bow);
                    break;
                case "shield":
                    Shield shield = new Shield(position, type, entityId, true);
                    shield.setDurability(entity.get("durability").getAsInt());
                    main.inventory.add(shield);
                    break;
            }
        }

        for (int i = 0; i < buildableList.size(); i++) {
            String entity = buildableList.get(i).getAsString();
            main.buildables.add(entity);
        }

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
        List<Entity> allNearbyEntities = new ArrayList<Entity>();
        currDungeon.setTickCounter(currDungeon.getTickCounter() + 1);
        Spider spid = null;
        int spiderSpawned = 0;
        ZombieToast zombieHolder = null;
        Mercenary mercenaryHolder = null;
        Hydra hydraHolder = null;
        Assassin assassinHolder = null;
        Bomb bombHolder = null;
        int zombieAddedLater = 0;
        int mercenaryAddedLater = 0;
        int hydraAddedLater = 0;
        int assassinAddedLater = 0;
        int EnemyCheck = 0;
        boolean invincibilityActive = false; 
        Character tempChar = null;
        Position playerSpawnPosition = null;
        main = currDungeon;

        // Get the character class
        Character character = getCharacter(entities);
        
        // Check potion duration and set it off if it expires
        potionTickAdder(character);
        potionChecker(character);

        // If the gamemode is peaceful, act as if the character has invisibility
        if (currDungeon.getPeaceful()) character.setIsInvisible(true);

        // If the gamemode is hard, always turn off invincibility
        if (currDungeon.getHard()) character.setIsInvincible(false);


        // Checks if the character is invincible, then move the enemies
        if (character.isInvincible()) {
            invincibilityPhase(character, entities, movementDirection);
            invincibilityActive = true;
        }

        // Enemy movement goes first
        if (!invincibilityActive) hydraMovement(entities, movementDirection);
        if (!invincibilityActive) mercenaryMovement(entities, movementDirection);
        if (!invincibilityActive) assassinMovement(entities, movementDirection);
        if (!invincibilityActive) zombieMovement(entities, movementDirection);



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
                    bombHolder = useBomb(temp2, main, itemUsed);
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
                    if(!temp.checkDoorLock(doorEntity, entities, main)) continue;
                }
                
                List<Entity> interactingEntities = temp.checkNext(movementDirection, entities);
                // Checking the bomb
                boolean bombStatus = checkBomb(interactingEntities);
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
                                    main.addEntities(respawnedCharacter);
                                    break;
                                }
                            }
                            entitiesToBeRemoved.add(temp);
                            break;
                        }
                        // If the character is invisible
                        else if (temp2.isInvisible()) {
                            continue;
                        }
                        // If the character isnt dead, then the enemy has to have died in the case of battle
                        // Takes into the account of collectable item
                        else {
                            List<String> nonRemovable = Arrays.asList("boulder", "portal", "switch", "door", "exit", "swamp_tile");
                            int dontRemove = 0;
                            for (String curr : nonRemovable) {
                                if (interactingEntity.getType().equals(curr)) dontRemove = 1;
                            }
                            if (dontRemove == 0) entitiesToBeRemoved.add(interactingEntity);

                            // if (!interactingEntity.getType().equals("boulder")) entitiesToBeRemoved.add(interactingEntity);
                            // Accounting for chance to receive TheOneRing
                            if (interactingEntity.getClass().getSuperclass().getName().equals("dungeonmania.entities.MovingEntity")) {
                                Random random = new Random();
                                int chance = random.nextInt(21);
                                if (chance == 10) {
                                    String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
                                    currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1);
                                    // Position needs to be stated as checkNext requires a position to run
                                    Position tempPos = new Position(-1, -1);
                                    TheOneRing oneRing = new TheOneRing(tempPos, "one_ring", entityId, true);
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
                                        Armour armour = new Armour(tempPos, "armour", entityId, true);
                                        main.inventory.add(armour);
                                    }
                                }
                                mercenaryBattleRadiusChecker(temp, entities);
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
                    Position zombieSpawn = checkWhiteSpace(entity.getPosition(), entities);

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
                MovingEntity temp = (MovingEntity) entity;
                MovingEntity spider = (Spider) entity;
                int loopPos = spider.getLoopPos();
                // if just spawned, move upward. do not need to check for
                // boulder above since cannot spawn below a boulder
                if (loopPos == 0) {
                    temp.moveUpward();
                    // if finished a loop, reset
                    if (loopPos == 9) {
                        loopPos = 0;
                    }
                    spider.setLoopPos(loopPos + 1);
                } else {
                    // 1. get currLoop based on movement direction
                    // 2. check if next pos is a boulder
                    //      if boulder, setClockwise to opposite
                    // 3. move
                    List<Position> posLoop = spider.getClockwiseLoop();
                    List<Position> negLoop = spider.getAnticlockwiseLoop();

                    // get direction of movement based on whether moving clockwise
                    Position dir = posLoop.get(loopPos);                           
                    if (spider.getClockwise() == false) {
                        dir = negLoop.get(loopPos);
                    }
                    int spiderBlocked = 0;
                    // if blocked, set dir to opposite
                    for (Entity currEnt: entities) {
                        Position nextPos = spider.getPosition().translateBy(dir);

                        if (currEnt.getPosition().equals(nextPos) && currEnt.getType().equals("boulder")) {
                            spider.setClockwise(!spider.getClockwise());
                            
                            spiderBlocked = 1;
                            
                        }
                        else if (currEnt.getPosition().equals(nextPos) && currEnt.getType().equals("door")) {
                            spider.setClockwise(!spider.getClockwise());
                            
                            spiderBlocked = 1;
                            
                        }
                    }

                    // double check if movement direction changed
                    if (spider.getClockwise() == false) {
                        dir = negLoop.get(loopPos);
                    } else {
                        dir = posLoop.get(loopPos);
                    }
                    if (spiderBlocked == 0) spider.moveEntity(dir);
                    
                    // update loopPos
                    if (spider.getClockwise() == true) {
                        if (loopPos == 8) {
                            loopPos = 0;
                        }
                        spider.setLoopPos(loopPos + 1);
                    } else {
                        if (loopPos == 1) {
                            loopPos = 9;
                        }
                        spider.setLoopPos(loopPos - 1);
                    }

                }
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

            Position hydraSpawn = checkWhiteSpace(character.getPosition(), entities);
            Hydra hydraEntity = new Hydra(hydraSpawn, "hydra", entityId, true);
            hydraHolder = hydraEntity;
            hydraAddedLater = 1;
        }

        // Spider spawner ticks
        if ((checkMaxSpiders(entities) == false) && (currDungeon.getTickCounter() % 25 == 0)) {
            Position spiderSpawn = getSpiderSpawn(entities);
            String entityId =  String.format("entity%d", currDungeon.getEntityCounter());
            currDungeon.setEntityCounter(currDungeon.getEntityCounter() + 1); 
            Spider newSpider = new Spider(spiderSpawn, "spider", entityId, true);                   
            spid = newSpider;
            spiderSpawned = 1;
        }
                 
        if (zombieAddedLater == 1) main.addEntities(zombieHolder);
        if (mercenaryAddedLater == 1) main.addEntities(mercenaryHolder);
        if (hydraAddedLater == 1) main.addEntities(hydraHolder);
        if (assassinAddedLater == 1) main.addEntities(assassinHolder);
        if (spiderSpawned == 1) main.addEntities(spid);

        
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
                Position entPos = enti.getPosition();
                Position up = new Position(0, -1);
                Position down = new Position(0, 1);
                Position left = new Position(-1, 0);
                Position right = new Position(1, 0);
                if (playerPos.equals(entPos.translateBy(up)) || playerPos.equals(entPos.translateBy(down)) || playerPos.equals(entPos.translateBy(left)) || playerPos.equals(entPos.translateBy(right))) {
                    doExplode(entities, (Character) tempChar, main, enti, allNearbyEntities);    
                }
            }
        }
        // add all nearby entities to the bomb to entiitesToBeRemoved
        entitiesToBeRemoved.addAll(allNearbyEntities);
        // Remove the collectible from the map
        entityRemover(entitiesToBeRemoved, main);
        
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
        return dr;
    }


    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        System.out.println("passing thru interact");
        // Get entity list
        List<Entity> entities = currDungeon.getEntities();

        // Get inventory
        List<CollectableEntity> inventory = currDungeon.getInventory();

        // Get the character class
        Character character = getCharacter(entities);

        // Check if the entity exists within the dungeon
        if (!entityIdCheck(entityId, entities)) {
            throw new IllegalArgumentException("The entity does not exist.");
        }

        // Check what we are interacting with 
        Entity interaction = IdToEntity(entityId, entities);

        // Interaction with the mercenary
        if (interaction.getType().equals("mercenary")) {
            
            // Check whether the player is close enough to the mercenary
            if (!playerProximityMercenary(character, interaction)) {
                throw new InvalidActionException("The player is not close enough to the mercenary.");
            }

            // Check whether the player has enough gold to bribe the mercenary
            if (!playerHasEnoughGold(inventory)) {
                throw new InvalidActionException("The player does not have enough gold to bribe the mercenary.");
            }



        }

        // Interaction with the spawner
        if (interaction.getType().equals("zombie_toast_spawner")) {

            // Check whether the player is close enough to the spawner
            if (!playerProximitySpawner(character, interaction)) {
                throw new InvalidActionException("The player is not close enough to the spawner.");
            }

            // Check whether the player has a weapon to destroy the spawner
            if (!playerHasSword(inventory)) {
                throw new InvalidActionException("The player does not have a weapon to destory the spawner.");
            }

            System.out.println("lolfewlfw");
            ZombieToastSpawner.interactWithSpawner(inventory, interaction, currDungeon);
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
                if (item.getType().equals("wood") && wood < 2) {
                    itemsToBeRemoved.add(item);
                    wood++;
                }
                if (item.getType().equals("arrow") && arrow < 4) {
                    itemsToBeRemoved.add(item);
                    arrow++;
                }
            }
            // Position needs to be stated as checkNext requires a position to run
            Position tempPos = new Position(-1, -1);
            Bow bow = new Bow(tempPos, "bow", entityId, true);
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
                if (wood >= 2 && treasure >= 1 && key >= 1) {
                    break;
                }
                if (item.getType().equals("wood") && wood < 3) {
                    itemsToBeRemoved.add(item);
                    wood++;
                }
                if (item.getType().equals("key") && key < 2 && treasure < 2) {
                    itemsToBeRemoved.add(item);
                    currDungeon.setKeyStatus(true);
                    key++;
                }
                 if (item.getType().equals("treasure") && treasure < 2 && key < 2) {
                    itemsToBeRemoved.add(item);
                    treasure++;
                }
            }
            // Position needs to be stated as checkNext requires a position to run
            Position tempPos = new Position(-1, -1);
            Shield shield = new Shield(tempPos, "shield", entityId, true);
            currDungeon.inventory.add(shield);
            currDungeon.buildables.remove(buildable);
        }

        for (CollectableEntity item: itemsToBeRemoved) {
            currDungeon.inventory.remove(item);
        }

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
        return dr;
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

    public boolean checkMaxSpiders(List<Entity> entities) {
        int noSpiders = 0;
        for (Entity entity: entities) {
            if (entity.getType().equals("spider")) {
                noSpiders += 1;
            }
        }
        if (noSpiders < 8) {
            return false;
        } else {
            return true;
        }
    }

    public Position getSpiderSpawn(List<Entity> entities) {

        boolean posFound = false;
        while (posFound == false) {
            int x = getRandomNumber(0, 15);
            int y = getRandomNumber(0, 15);
            int check = 0;
            Position pos = new Position(x, y);
            Position posAbove = new Position(x, y + 1);
            for (Entity entity : entities) {
                // if the square is a boulder
                if ((entity.getPosition().equals(pos)) && (entity.getType().equals("boulder"))) {
                    check = 1;
                    break;
                } else if ((entity.getPosition().equals(posAbove)) && (entity.getType().equals("boulder"))) {
                    check = 1;
                    break;
                }
            }
            if (check == 0) {
                return pos;
            }
            // check if the square is a bouldeer
            // check if the square above is a boulder
        }
        


        return null;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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

        returnGoal = returnGoal.replace("(" + goal + ")", "");
        returnGoal = returnGoal.replace(goal + " AND ", "");
        returnGoal = returnGoal.replace(" AND" + goal, "");

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

        System.out.println(returnGoal);
        System.out.println(goal);

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
     * Helper Function that returns the players position 
     * @param entities
     * @return position
     */
    public Position getPlayerPosition(List<Entity> entities) {
        for (Entity player: entities) {
            if (player.getType().equals("player")) {
                return player.getPosition();
            } 
        }
        return null;
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
     * Moves the hydra around
     * @param entities - The list of all entities in the dungeon
     * @param direction - The direction of the character
     */
    public void hydraMovement (List<Entity> entities, Direction direction) {
        Position player = getPlayerPosition(entities);
        player = player.translateBy(direction);

        for (Entity entity : entities) {
            if (entity.getType().equals("hydra")) {
                Hydra temp = (Hydra) entity;
                temp.moveEntity(entities, player);

            }
        }
    }

    /**
     * Moves the zombie around
     * @param entities - The list of all entities in the dungeon
     * @param direction - The direction of the character
     */
    public void zombieMovement (List <Entity> entities, Direction direction) {
        Position player = getPlayerPosition(entities);
        player = player.translateBy(direction);

        for (Entity entity : entities) {
            if (entity.getType().equals("zombie_toast")) {
                ZombieToast temp = (ZombieToast) entity;
                temp.moveEntity(entities, player);

            }
        }
    }

    /**
     * Moves the mercenary around
     * @param entities - The list of all entities in the dungeon
     * @param direction - The direction of the character
     */
    public void mercenaryMovement(List<Entity> entities, Direction direction) {
        Position player = getPlayerPosition(entities);
        player = player.translateBy(direction);

        for (Entity entity : entities) {
            if (entity.getType().equals("mercenary")) {
                Mercenary temp = (Mercenary) entity;
                temp.moveEntity(entities, player);
            }
        }
    }

    /**
     * Moves the assassin around
     * @param entities - The list of all entities in the dungeon
     * @param direction - The direction of the character
     */
    public void assassinMovement(List<Entity> entities, Direction direction) {
        Position player = getPlayerPosition(entities);
        player = player.translateBy(direction);

        for (Entity entity : entities) {
            if (entity.getType().equals("assassin")) {
                Assassin temp = (Assassin) entity;
                temp.moveEntity();
            }
        }
    }



    /**
     * Moves the enemies around if the character is invincible
     * @param character - The character class
     * @param entities - The list of all entities
     */
    public void invincibilityPhase(Character character, List<Entity> entities, Direction direction) {
        Position player = getPlayerPosition(entities);
        player = player.translateBy(direction);

        for (Entity entity : entities) {
            if (entity.getType().equals("zombie_toast") || entity.getType().equals("spider") || entity.getType().equals("mercenary")) {
                MovingEntity temp = (MovingEntity) entity;
                temp.runEnemy(entities, player);
            }
        }
    }

    
    
    /**
     * Searches for a key, returns true if the key could be found
     * @param inventory
     */
    public boolean keyChecker(List<CollectableEntity> inventory) {
        for (CollectableEntity item: inventory) {
            if (item.getType().equals("key")) {
                return true;
            }
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
                        else if (keyChecker(main.inventory)) {
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
     * Checks if the bomb is activated
     * @param player - the character
     * @param main - the dungeon
     * @param itemUsed - the item that is used
     * @return Bomb
     */
    public Bomb useBomb(Character player, Dungeon main, String itemUsed) {
        if (itemUsed != null) {
            for (CollectableEntity entity2: main.inventory) {
                if (entity2.getID().equals(itemUsed)) {
                    if (entity2.getType().equals("bomb")) {
                        Bomb bomb = (Bomb) entity2;
                        Bomb newBomb = new Bomb(player.getPosition(), bomb.getType(), bomb.getID(), bomb.getIsInteractable());
                        newBomb.setActivated(true);
                        main.inventory.remove(bomb);
                        return newBomb;
                    }
                }
            }
        }
        return null;
    }
    /**
     * Checks if the bomb is activated
     * @param entity - the item
     * @return boolean 
     */
    public boolean isBombActivated(Entity entity) {
        if (entity.getType().equals("bomb")) {
            Bomb bomb = (Bomb) entity;
            if (bomb.isActivated()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Helper Function that takes in the json file and adds all entities into entities list
     * @param dungeonName
     * @param main
     */
    public void addEntitiesToList(String dungeonName, Dungeon main) {

        String filename = "src\\main\\resources\\dungeons\\" + dungeonName + ".json";
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
            
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
                        int keyType = entity.get("key").getAsInt();
                        Door doorEntity = new Door(position, type, entityId, true, keyType, true);
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
                    case "key":
                        main.setKeyCounter(main.getKeyCounter() + 1);
                        Key key = new Key(position, type, entityId, true, main.getKeyCounter());
                        main.addEntities(key);
                        break;
                    case "arrow":
                        Arrows arrows = new Arrows(position, type, entityId, true);
                        main.addEntities(arrows);
                        break;
                    case "bomb":
                        Bomb bomb = new Bomb(position, type, entityId, true);
                        main.addEntities(bomb);
                        break;
                    case "health_potion":
                        HealthPotion healthPotion = new HealthPotion(position, type, entityId, true);
                        main.addEntities(healthPotion);
                        break;
                    case "invincibility_potion":
                        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(position, type, entityId, true);
                        main.addEntities(invincibilityPotion);
                        break;
                    case "invisibility_potion":
                        InvisibilityPotion invisibilityPotion = new InvisibilityPotion(position, type, entityId, true);
                        main.addEntities(invisibilityPotion);
                        break;
                    case "sword":
                        Sword sword = new Sword(position, type, entityId, true);
                        main.addEntities(sword);
                        break;
                    case "treasure":
                        Treasure treasure = new Treasure(position, type, entityId, true);
                        main.addEntities(treasure);
                        break;
                    case "wood":
                        Wood wood = new Wood(position, type, entityId, true);
                        main.addEntities(wood);
                        break;
                    case "spider":
                        Spider spiderEntity = new Spider(position, type, entityId, true);
                        main.addEntities(spiderEntity);
                        break;
                    case "zombie_toast":
                        ZombieToast zombieToast = new ZombieToast(position, type, entityId, true);
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
        String filename = "src\\main\\resources\\dungeons\\" + dungeonName + ".json";
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
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
     * checks if a bomb is activated
     * @param interactingEntities
     * @return boolean
     */
    public boolean checkBomb(List<Entity> interactingEntities) {
        for (Entity interactingEntity: interactingEntities) {
            if (interactingEntity != null) {
                if (isBombActivated(interactingEntity)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * returns the character
     * @param entities
     * @return Character
     */
    public Character getCharacter(List <Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals("player")) {
                return (Character) entity; 
            }
        }

        return null;
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
     * attempts to explode bombs given a boulder position
     * @param entities
     * @param player
     * @param main
     * @param bou
     * @param nearby
     */
    public void doExplode(List<Entity> entities, Character player,  Dungeon main, Entity bou, List<Entity> nearby) {
        // pos = boulder position
        Position boulderPos = bou.getPosition();
        // find a switch

        List<Entity> entitiesAtBoulder = main.getEntitiesAtPos(boulderPos);
        Position N = boulderPos.translateBy(0, -1);
        Position E = boulderPos.translateBy(1, 0);
        Position S = boulderPos.translateBy(0, 1);
        Position W = boulderPos.translateBy(-1, 0);
        
        List<Entity> entsAbove = main.getEntitiesAtPos(N);
        List<Entity> entsRight = main.getEntitiesAtPos(E);
        List<Entity> entsBelow = main.getEntitiesAtPos(S);
        List<Entity> entsLeft = main.getEntitiesAtPos(W);

        for (Entity currEnt : entitiesAtBoulder) {
            if (currEnt.getType().equals("switch")) {
                // see if there are bombs cardinally adjacent, if so, explode any adjacent bombs
                if (isBombAtPos(entsAbove)) {
                    explode(entsAbove, entities, N, main, player, nearby);
                }
                if (isBombAtPos(entsRight)) {
                    explode(entsRight, entities, E, main, player, nearby);
                    
                } 
                if (isBombAtPos(entsBelow)) {
                    explode(entsBelow, entities, S, main, player, nearby);
                    
                } 
                if (isBombAtPos(entsLeft)) {
                    explode(entsLeft, entities, W, main, player, nearby);
                }
                
            }
        }
    }
    
    /**
     * returns whether a bomb is in the list of entities at a given position
     * @param entities
     * @return Boolean
     */
    public Boolean isBombAtPos(List<Entity> entities) {
        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("bomb")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Explodes any bombs adjacent to a given bomb position
     * @param entitiesAtPos
     * @param entities
     * @param pos
     * @param main
     * @param player
     * @param NearbyEntities
     */
    public void explode(List<Entity> entitiesAtPos, List<Entity> entities, Position pos, Dungeon main, Character player, List<Entity> NearbyEntities) {
        
        Position N = pos.translateBy(0, -1);
        Position NE = pos.translateBy(1, -1);
        Position E = pos.translateBy(1, 0);
        Position SE = pos.translateBy(1, 1);
        Position S = pos.translateBy(0, 1);
        Position SW = pos.translateBy(-1, 1);
        Position W = pos.translateBy(-1, 0);
        Position NW = pos.translateBy(-1, -1);
        
        List<Entity> entsN = main.getEntitiesAtPos(N);
        List<Entity> entsNE = main.getEntitiesAtPos(NE);
        List<Entity> entsE = main.getEntitiesAtPos(E);
        List<Entity> entsSE = main.getEntitiesAtPos(SE);
        List<Entity> entsS = main.getEntitiesAtPos(S);
        List<Entity> entsSW = main.getEntitiesAtPos(SW);
        List<Entity> entsW = main.getEntitiesAtPos(W);
        List<Entity> entsNW = main.getEntitiesAtPos(NW);
        List<Entity> entsO = entitiesAtPos;
        // add all nearby non-player entities to the list of entities to be removed 
        for (Entity ent : entsN) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsNE) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsE) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsSE) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsS) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsSW) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsW) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsNW) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsO) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
    }

    /**
     * This function checks whether the dungeon exists
     * @param dungeonName - this is the dungeon name
     */
    public boolean dungeonNotValid(String dungeonName) {
       
        return true;
    }

    /**
     * This function checks whether the gamemode is valid
     * @param gameMode - this is the gameMode
     */
    public boolean gameModeNotValid(String gameMode) {
        for (String gamemodeState : getGameModes()) {
            if (gamemodeState.equals(gameMode)) return true;
        }

        return false;
    }

    /**
     * This function checks whether there are any players in the mercenary's battle radius
     * @param character - the player
     */
    public void mercenaryBattleRadiusChecker(MovingEntity character, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals("mercenary")) {
                Position vector = Position.calculatePositionBetween(character.getPosition(), entity.getPosition());
                double distance = Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2));

                if (distance < 4) {
                    Mercenary temp = (Mercenary) entity;
                    temp.moveEntity(entities, character.getPosition());
                }
            }
        }
    }

    /**
     * Checks whether the entityID is valid or not
     * @param entityId - the id given in the argument 
     * @param entities - the list of all entities in the dungeon
     */
    public boolean entityIdCheck(String entityId, List<Entity> entities) {
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
    public Entity IdToEntity(String entityId, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getID().equals(entityId)) return entity; 
        }

        // Returning null will never occur
        return null;
    }


    /**
     * Checks whether the place is close enough to the mercenary to bribe
     * @param character - the character class
     * @param interaction - the entity the character is interacting with, mercenary in this case
     */
    public boolean playerProximityMercenary(Character character, Entity interaction) {
        // First, wrap the entity and get its position
        Mercenary mercenary = (Mercenary) interaction;
        Position mercenaryPosition = mercenary.getPosition();

        // Checking the positions around the mercenary
        List<Position> adjacent = mercenaryPosition.getAdjacentPositions();

        // Index 1, 3, 5, 7 are cardinally adjacent positions, so place into temporary list holder
        List<Position> validPositions = new ArrayList<>();

        // However, also add one tiles to the left, right, up and down as it is 2 cardinal tiles
        validPositions.add(adjacent.get(1));
        validPositions.add(adjacent.get(1).translateBy(0, -1));
        validPositions.add(adjacent.get(3));
        validPositions.add(adjacent.get(3).translateBy(1, 0));
        validPositions.add(adjacent.get(5));
        validPositions.add(adjacent.get(5).translateBy(0, 1));
        validPositions.add(adjacent.get(7));
        validPositions.add(adjacent.get(7).translateBy(-1, 0));

        // Now, get player position and check if they're in any of these squares
        Position characterPosition = character.getPosition();

        for (Position position : validPositions) {
            if (position.equals(characterPosition)) return true;
        }

        return false; 
    }

    /**
     * Checks whether the player is close enough to the spawner
     * @param character - the character class
     * @param interaction - the entity the character is interacting with, spawner in this case
     */
    public boolean playerProximitySpawner(Character character, Entity interaction) {
        // First, wrap the entity and get its position
        ZombieToastSpawner spawner = (ZombieToastSpawner) interaction;
        Position spawnerPosition = spawner.getPosition();

        // Checking the positions around the spawner
        List<Position> adjacent = spawnerPosition.getAdjacentPositions();

        // Index 1, 3, 5, 7 are cardinally adjacent positions, so place into temporary list holder
        List<Position> validPositions = new ArrayList<>();
        validPositions.add(adjacent.get(1));
        validPositions.add(adjacent.get(3));
        validPositions.add(adjacent.get(5));
        validPositions.add(adjacent.get(7));

        // Now, get player position and check if they're in any of these squares
        Position characterPosition = character.getPosition();

        for (Position position : validPositions) {
            if (position.equals(characterPosition)) return true;
        }

        return false; 
    }

    /**
     * Checks whether the player has enough gold to bribe the mercenary
     * @param inventory - the player's inventory
     */
    public boolean playerHasEnoughGold(List<CollectableEntity> inventory) {
        // Gold counter
        int totalGold = 0;

        // Checks for gold in the inventory
        for (CollectableEntity item : inventory) {
            if (item.getType().equals("treasure")) {
                totalGold++;
            }
        }

        if (totalGold >= 2) return true;

        return false;
    }

    /**
     * Checks whether the player has a sword to destory the spawner
     * @param inventory - the player's inventory
     */
    public boolean playerHasSword(List<CollectableEntity> inventory) {
        // Checks for a sword in the inventory
        for (CollectableEntity item : inventory) {
            if (item.getType().equals("sword")) return true;
        }

        return false;
    }


    /**
     * returns current dungeon
     * @return Dungeon
     */
    public Dungeon getCurrDungeon() {
        return this.currDungeon;
    }
}
