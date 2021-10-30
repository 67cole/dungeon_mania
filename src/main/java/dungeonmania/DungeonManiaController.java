package dungeonmania;

import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.entities.Spider;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.BuildableEntities.*;
import dungeonmania.entities.CollectableEntities.*;
import dungeonmania.entities.RareCollectableEntities.*;
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
import java.util.Random;

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
    private Dungeon currDungeon;

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
        currDungeon = main;

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
            
            for (int i = 0; i < entitiesList.size(); i++) {
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
        List<Entity> entitiesToBeRemoved = new ArrayList<Entity>();

        currDungeon.setTickCounter(currDungeon.getTickCounter() + 1);
        Spider spid = null;
        int spiderSpawned = 0;
        ZombieToast zombieHolder = null;
        Mercenary mercenaryHolder = null;
        Bomb bombHolder = null;
        int zombieAddedLater = 0;
        int mercenaryAddedLater = 0;
        int EnemyCheck = 0;

        Position playerSpawnPosition = null;
        main = currDungeon;
        List<Entity> entities = currDungeon.getEntities();
        // Mercenary Movement goes last
        mercenaryMovement(entities, movementDirection);

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
                // Potion Checker
                
                // If the inital direction is NONE then an item has been used
                if (movementDirection == Direction.NONE) {
                    // Sets the status of player depending on potion
                    useItem(temp2, main, itemUsed);
                    // Sets the bomb 
                    bombHolder = useBomb(temp2, main, itemUsed);
                    continue;
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
                
                Entity interactingEntity = temp.checkNext(movementDirection, entities);
                // Checking the bomb
                if (interactingEntity != null) {
                    if (isBombActivated(interactingEntity)) {
                        continue;
                    }
                }
                
                // If it is here movement is allowed and
                // it might need to interact with an entity.
                temp.moveEntity(movementDirection);

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
                        entitiesToBeRemoved.add(interactingEntity);
                        // Accounting for chance to receive TheOneRing
                        if (interactingEntity.getClass().getSuperclass().getName().equals("dungeonmania.entities.MovingEntity")) {
                            Random random = new Random();
                            int chance = random.nextInt(21);
                            if (chance == 10) {
                                String entityId =  String.format("entity%d", entityCounter);
                                entityCounter += 1;
                                // Position needs to be stated as checkNext requires a position to run
                                Position tempPos = new Position(-1, -1);
                                TheOneRing oneRing = new TheOneRing(tempPos, "one_ring", entityId, true);
                                main.inventory.add(oneRing);
                            }
                        }
                    }
                }
                if (main.getDungeonGoals().contains("exit")) {
                    checkExitGoal(entities, main, temp);
                }
            }
            
            // Zombie Spawner Ticks
            if (entity.getType().equals("zombie_toast_spawner") && currDungeon.getTickCounter() % 20 == 0) {
                Position zombieSpawn = checkWhiteSpace(entity.getPosition(), entities);
                // If there is no white space around zombie spawner, don't spawn zombie
                if (zombieSpawn == null) continue;

                String entityId =  String.format("entity%d", entityCounter);
                ZombieToast zombieToastEntity = new ZombieToast(zombieSpawn, "zombie_toast", entityId, true);
                zombieHolder = zombieToastEntity;
                zombieAddedLater = 1;
            }
            // Zombie Movement (moves the same for character but can't interact)
            if (entity.getType().equals("zombie_toast")) {
                ZombieToast temp = (ZombieToast) entity;
                temp.moveEntity(entities);
            }         
            // Spider Movement
            if (entity.getType().equals("spider")) {
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

                    // if blocked, set dir to opposite
                    for (Entity currEnt: entities) {
                        Position nextPos = spider.getPosition().translateBy(dir);

                        if (currEnt.getPosition().equals(nextPos) && currEnt.getType().equals("boulder")) {
                            spider.setClockwise(!spider.getClockwise());
                        }
                    }

                    // double check if movement direction changed
                    if (spider.getClockwise() == false) {
                        dir = negLoop.get(loopPos);
                    } else {
                        dir = posLoop.get(loopPos);
                    }

                    spider.moveSpider(dir);

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
        // Every 75 ticks of the game causes a new mercenary to spawn
        if (currDungeon.getTickCounter() % 75 == 0 && EnemyCheck == 1) {
            String entityId =  String.format("entity%d", entityCounter);
            entityCounter += 1;

            Mercenary mercenaryEntity = new Mercenary(playerSpawnPosition, "mercenary", entityId, true);
            mercenaryHolder = mercenaryEntity;
            mercenaryAddedLater = 1;
        }

        // Spider spawner ticks
        if ((checkMaxSpiders(entities) == false) && (currDungeon.getTickCounter() % 10 == 0)) {
            Position spiderSpawn = getSpiderSpawn(entities);
            String entityId =  String.format("entity%d", entityCounter);
            entityCounter += 1;
            Spider newSpider = new Spider(spiderSpawn, "spider", entityId, true);                   
            spid = newSpider;
            spiderSpawned = 1;
        }
                 
        if (zombieAddedLater == 1) main.addEntities(zombieHolder);
        if (mercenaryAddedLater == 1) main.addEntities(mercenaryHolder);
        if (spiderSpawned == 1) main.addEntities(spid);

        // Remove the collectible from the map
        entityRemover(entitiesToBeRemoved, main);
        
        // Adding the bomb to the map
        if (bombHolder != null) {
            main.addEntities(bombHolder);
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
            int x = getRandomNumber(1, 16);
            int y = getRandomNumber(1, 14);
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

    public Position getPlayerPosition(List<Entity> entities) {
        for (Entity player: entities) {
            if (player.getType().equals("player")) {
                return player.getPosition();
            } 
        }
        return null;
    }

    /**
     * Moves the mercenary around
     */
    public void mercenaryMovement(List<Entity> entities, Direction direction) {
        for (Entity entity : entities) {
            if (entity.getType().equals("mercenary")) {
                Mercenary temp = (Mercenary) entity;
                Position player = getPlayerPosition(entities);
                player = player.translateBy(direction);
                temp.moveEntity(entities, player);
            }
        }
    }

    
    
    /**
     * Searches for a key
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
     */
     public void entityRemover(List<Entity> entityList, Dungeon main) {
        for (Entity entityToBeRemoved : entityList) {
            if (entityToBeRemoved != null) {
                if (entityToBeRemoved.getClass().getSuperclass().getName().equals("dungeonmania.entities.CollectableEntity")) {
                    if (entityToBeRemoved.getType().equals("key")) {
                        if (main.getKeyStatus()) {
                            main.setKeyStatus(false);;
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
            }
        }
    }
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
                }
            }
        }
    }

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

    public boolean isBombActivated(Entity entity) {
        if (entity.getType().equals("bomb")) {
            Bomb bomb = (Bomb) entity;
            if (bomb.isActivated()) {
                return true;
            }
        }
        return false;
    }
}

