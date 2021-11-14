package dungeonmania.entities;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import dungeonmania.entities.CollectableEntities.Sword;
import dungeonmania.entities.BuildableEntities.Bow;
import dungeonmania.entities.BuildableEntities.Shield;
import dungeonmania.entities.CollectableEntities.Armour;
import dungeonmania.entities.CollectableEntities.Key;
import dungeonmania.entities.CollectableEntities.Bomb;
import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController;
import dungeonmania.util.Direction;

public abstract class MovingEntity implements Entity {
    /**
     * Health of movingEntity
     */
    private int health;

    /**
     * Attack of movingEntity
     */
    private int attack;

    /**
     * Condition of movingEntity
     */
    private boolean alive;

    /**
     * Position in the path
     */
    private Position position;

    /**
     * Type of moving entity
     */
    private String type;

    /**
     * ID of moving entity
     */
    private String ID;

    /**
     * If it is interactable
     */
    private boolean isInteractable;

    /**
     * Armour of the entity
     */
    private boolean armour = false;
    private int totalMovement = 1;

    /**
     * Creates a moving entity that can be moved up, down, left and right into cardinally adjacent square
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public MovingEntity(Position position, String type, String ID, boolean isInteractable) {
        this.position = position;
        this.type = type;
        this.ID = ID;
        this.isInteractable = isInteractable;
        this.alive = true;
    }

    
    /**
     * Move the entity around
     */
    public void moveEntity(Direction direction) {}
    
    public void moveEntity(Position direction) {}

    public void moveSpider(List<Entity> entities, Entity entity){}
    /**
     * Move the position by one square up
     */
    public void moveUpward() {
        setPosition(position.translateBy(0, -1));
    }

    /**
     * Move the position by one square down
     */
    public void moveDownward() {
        setPosition(position.translateBy(0, 1));
    }

    /**
     * Move the position by one square left
     */
    public void moveLeft() {
        setPosition(position.translateBy(-1, 0));
    }

    /**
     * Move the position by one square right
     */
    public void moveRight() {
        setPosition(position.translateBy(1, 0));
    }

    /**
     * Get position
     */
     @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Get type
     */
     @Override
    public String getType() {
        return type;
    }

    /**
     * Get ID
     */
    @Override
    public String getID() {
        return ID;
    }

    /**
     * Get isInteractable
     */
    @Override
    public boolean getIsInteractable() {
        return isInteractable;
    }

    /**
     * Getting the armour status of entity
     * @return boolean
     */
    public boolean getArmour() {
        return this.armour;
    }

    /**
     * Setting the armour status of entity
     * @return boolean
     */
    public void setArmour(boolean armour) {
        this.armour = armour;
    }

    /**
     * Sets the position
     * @param position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Get Alive
     */
    public boolean isAlive() {
        return this.alive;
    }
    /**
     * Set Alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    /**
     * Get Attack
     */
    public int getAttack() {
        return this.attack;
    }

    /**
     * Set Attack
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }
    /**
     * Get Health
     */
    public int getHealth() {
        return this.health;
    }
    /**
     * Set Health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Getter for position in loop
     */
    public int getLoopPos() {
        return 0;
    }

    /**
     * Setter for position in loop
     * @param alive
     */
    public void setLoopPos(int loopPos) {
    }

    /**
     * Getter for clockwiseLoop
     */
    public List<Position> getClockwiseLoop() {
        return null;
    }

    /**
     * Getter for anticlockwiseLoop
     */
    public List<Position> getAnticlockwiseLoop() {
        return null;
    }

    /**
     * Getter for clockwise
     */
    public boolean getClockwise() {
        return true;
    }
    
    /**
     * Setter for clockwise
     * @param clockwise
     */
    public void setClockwise(boolean clockwise) {
    }

    /**
     * Adds onto totalmovement 
     */
    public void swampMove() {
        totalMovement++;
    }

    /**
     * Getter for total movement
     */
    public int getTotalMovement() {
        return this.totalMovement;
    }
    /**
     * Resets total movement back to original 

     */
    public void resetTotalMovement() {
        this.totalMovement = 1;
    }


    /**
     * checkMovement checks for the next square if it's a wall/boulder.
     * This uses direction args.
     * @param direction
     * @param entities
     * @return boolean
     */
    public boolean checkMovement(Direction direction, List<Entity> entities) {
        switch (direction) {
            case UP:
                Position attemptedMove = position.translateBy(0, -1);

                for (Entity entity : entities) {

                    if (boulderBlocked(direction, attemptedMove, entities, entity)) {
                        return false;
                    }

                    if (entity.getType().equals("wall") && entity.getPosition().equals(attemptedMove)) {
                        return false;
                    }

                    if (entity.getType().equals("switch_door") && entity.getPosition().equals(attemptedMove)) {
                        return false;
                    }

                }
                break;

            case DOWN:
                Position attemptedMove1 = position.translateBy(0, 1);

                for (Entity entity : entities) {
                    if (boulderBlocked(direction, attemptedMove1, entities, entity)) {
                        return false;
                    }

                    if (entity.getType().equals("wall") && entity.getPosition().equals(attemptedMove1)) {
                        return false;
                    }
                    if (entity.getType().equals("switch_door") && entity.getPosition().equals(attemptedMove1)) {
                        return false;
                    }

                }
                break;

            case LEFT:
                Position attemptedMove2 = position.translateBy(-1, 0);

                for (Entity entity : entities) {
                    if (boulderBlocked(direction, attemptedMove2, entities, entity)) {
                        return false;
                    }

                    if (entity.getType().equals("wall") && entity.getPosition().equals(attemptedMove2)) {
                        return false;
                    }
                    if (entity.getType().equals("switch_door") && entity.getPosition().equals(attemptedMove2)) {
                        return false;
                    }

                }
                break;

            case RIGHT:
                Position attemptedMove3 = position.translateBy(1, 0);

                for (Entity entity : entities) {

                    if (boulderBlocked(direction, attemptedMove3, entities, entity)) {
                        return false;
                    }

                    if (entity.getType().equals("wall") && entity.getPosition().equals(attemptedMove3)) {
                        return false;
                    }
                    if (entity.getType().equals("switch_door") && entity.getPosition().equals(attemptedMove3)) {
                        return false;
                    }

                }
                break; 

            case NONE:
                return true;
        }

        // If it's a white square, you can move
        return true; 
    }

    /**
     * checkMovement checks for the next square if it's a wall/boulder.
     * This gives a position already as an arg
     * @param position
     * @param entities
     * @return boolean
     */
    public boolean checkMovement(Position position, List<Entity> entities) {
        
        for (Entity entity : entities) {
            if (entity.getPosition().equals(position) && !entity.getType().equals("door") && !entity.getType().equals("switch") && !entity.getType().equals("player") 
            && !entity.getClass().getSuperclass().getName().equals("dungeonmania.entities.CollectableEntity") && !entity.getType().equals("swamp_tile")) {
                return false;
            } 
        }
        return true;
    }

    /**
     * Checks if there is a spider in the attempted move position
     * @param direction
     * @param entities
     * @param attemptedMove
     * @return Entity
     */
    public Entity checkSpider(Direction direction, List<Entity> entities, Position attemptedMove) {
        for (Entity entity : entities) {
            if (entity.getType().equals("spider")) {

                MovingEntity spider = (Spider) entity;
                List<Position> loop = spider.getAnticlockwiseLoop();
                if (spider.getClockwise() == (true)) {
                    loop = spider.getClockwiseLoop();
                } 
                int loopPos = spider.getLoopPos();
                Position dir = loop.get(loopPos);
                if (spider.getPosition().translateBy(dir).equals(attemptedMove)) {
                    return spider;
                }
            }
        }

        return null;
    }

    /**
     * Checks if the position to be moved in is a door, if it is, return that door
     * @param movementDirection
     * @param entities
     * @return Door
     */
    public Door checkDoor(Direction movementDirection, List<Entity> entities) {
        Position entityPosition = position.translateBy(movementDirection);

        Door entityDoor = null;
        for (Entity entity: entities) {
            if (entity.getPosition().equals(entityPosition)) {
                if (entity.getType().equals("door") || entity.getType().equals("door_unlocked")) {
                    entityDoor = (Door) entity;
                    return entityDoor;
                }        
            }
        }
        return entityDoor;
    }

    /**
     * Checks if the position to be moved in is a door, if it is, return that door
     * @param position
     * @param entities
     * @return Door
     */
    public Door checkDoor(Position position, List<Entity> entities) {
        Door entityDoor = null;
        for (Entity entity: entities) {
            if (entity.getPosition().equals(position)) {
                if (entity.getType().equals("door") || entity.getType().equals("door_unlocked")) {
                    entityDoor = (Door) entity;
                    return entityDoor;
                }        
            }
        }
        return entityDoor;
    }

    /**
     * returns a list of entities in the next square the player is moving to
     * @param direction
     * @param entities
     * @return List<Entity>
     */
    public List<Entity> checkNext(Direction direction, List<Entity> entities) {
        List<Entity> interactingEntities = new ArrayList<Entity>();
        switch (direction) {
            case UP:
                Position attemptedMove = position.translateBy(0, -1);
                Entity spider = checkSpider(direction, entities, attemptedMove);
                if (spider != null) {
                    interactingEntities.add(spider);
                } 
                for (Entity entity : entities) {
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(attemptedMove)) {
                        interactingEntities.add(entity);
                    }
                }
                break;

            case DOWN:
                Position attemptedMove1 = position.translateBy(0, 1);
                Entity spider2 = checkSpider(direction, entities, attemptedMove1);
                if (spider2 != null) {
                    interactingEntities.add(spider2);
                } 
                for (Entity entity : entities) {
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(attemptedMove1)) {
                        interactingEntities.add(entity);
                    }
                }
                break;

            case LEFT:
                Position attemptedMove2 = position.translateBy(-1, 0);
                Entity spider3 = checkSpider(direction, entities, attemptedMove2);
                if (spider3 != null) {
                    interactingEntities.add(spider3);
                } 
                for (Entity entity : entities) {
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(attemptedMove2)) {
                        interactingEntities.add(entity);
                    }
                }
                break;

            case RIGHT:
                Position attemptedMove3 = position.translateBy(1, 0);
                Entity spider4 = checkSpider(direction, entities, attemptedMove3);
                if (spider4 != null) {
                    interactingEntities.add(spider4);
                } 
                for (Entity entity : entities) {
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(attemptedMove3)) {
                        interactingEntities.add(entity);
                    }
                }
                break; 

            case NONE:
                Position noMove = position;
                Entity spider5 = checkSpider(direction, entities, noMove);
                if (spider5 != null) {
                    interactingEntities.add(spider5);
                } 
                for (Entity entity : entities) {
                    if (entity.getType().equals("player")) continue;
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(noMove)) {
                        interactingEntities.add(entity);
                    }
                }
                break; 
        }

        // If it's a white square, you can move
        return interactingEntities; 
    }   

    /**
     * checks if the next position is blocked by a boulder
     * @param direction
     * @param attemptedMove
     * @param entities
     * @param entity
     * @return boolean
     */
    public boolean boulderBlocked(Direction direction, Position attemptedMove, List<Entity> entities, Entity entity) {

        if (entity.getType().equals("boulder") && entity.getPosition().equals(attemptedMove)) {
            
            StaticEntity main = (StaticEntity) entity;

            if (main.checkNext(direction, entities) == null) {
                return false;
            }

            if (main.checkNext(direction, entities).getType().equals("boulder") ||
                main.checkNext(direction, entities).getType().equals("wall")) {
            
                // Next entity is a wall or boulder. Must block it.
                return true;
            }
        }


        return false;
    }

    /**
     * checks if a enemy and character are battling
     * @param characterHealth
     * @param enemyHealth
     * @return boolean
     */
    public boolean checkBattleState(int characterHealth, int enemyHealth) {
        if (characterHealth <= 0) {
            return false;
        }
        if (enemyHealth <= 0) {
            return false;
        }
        return true;
    }

    /**
     * checks whether an entity is alive
     * @param health
     * @return boolean
     */
    public boolean checkAlive(int health) {
        if (health <= 0) {
            return false;
        }
        return true;
    }
    
    @Override
    public void entityFunction(List<Entity> entities, Character player, Direction direction, Dungeon main) {
        while (checkBattleState(player.getHealth(), this.getHealth())) {
            // If character is invisible, return.
            if (player.isInvisible()) {
                return;
            }
            // If character is invincible, set enemy dead, return.
            if (player.isInvincible()) {
                this.setAlive(false);
                return;
            }
            
            // Simulate a round of battle
            int weaponAtk = 0;
            boolean charHasArmour = false;
            boolean charHasMArmour = false;
            boolean charHasShield = false;
            boolean charHasBow = false;
            boolean charHasAnduril = false;
            boolean enemyArmour = false;
            Sword swordHolder = null;
            Armour armourHolder = null;
            Shield shieldHolder = null;
            Bow bowHolder = null;
            if (this.getArmour()) {
                enemyArmour = true;
            }
            for (CollectableEntity item: main.inventory) {
                if (item.getType().equals("sword")) {
                    Sword sword = (Sword) item;
                    weaponAtk = sword.getAttack();
                    sword.reduceDurability();
                    swordHolder = sword.checkDurability();
                }
                if (item.getType().equals("armour")) {
                    Armour armour = (Armour) item;
                    charHasArmour = true;
                    armour.reduceDurability();
                    armourHolder = armour.checkDurability();
                }
                if (item.getType().equals("midnight_armour")) {
                    charHasMArmour = true;
                }
                if (item.getType().equals("bow")) {
                    Bow bow = (Bow) item;
                    charHasBow = true;
                    bow.reduceDurability();
                    bowHolder = bow.checkDurability();
                }
                if (item.getType().equals("shield")) {
                    Shield shield = (Shield) item;
                    charHasShield = true;
                    shield.reduceDurability();
                    shieldHolder = shield.checkDurability();
                }
                if (item.getType().equals("anduril")) {
                    charHasAnduril = true;
                }
            }
            if (swordHolder != null) {
                main.inventory.remove(swordHolder);
            }
            if (armourHolder != null) {
                main.inventory.remove(armourHolder);
            }
            if (bowHolder != null) {
                main.inventory.remove(bowHolder);
            }
            if (shieldHolder != null) {
                main.inventory.remove(shieldHolder);
            }
            int characterHealth = player.getHealth();
            int characterAD = player.getAttack();
            int enemyHealth = this.getHealth();
            int enemyAD = this.getAttack();
            // Searching for the mercenaries
            // Applying ally damage to enemy first
            List<Mercenary> friendlyMercenaries = getMercenary(entities);
            for (Mercenary merc: friendlyMercenaries) {
                enemyHealth = enemyHealth - ((merc.getHealth() * (merc.getAttack())) / 5);
            }
            // Applying ally damage to enemy first
            List<Assassin> friendlyAssassin = getAssassin(entities);
            for (Assassin assassin: friendlyAssassin) {
                enemyHealth = enemyHealth - ((assassin.getHealth() * (assassin.getAttack())) / 5);
            }
            // Calculations for character
            if (charHasMArmour) {
                enemyAD = enemyAD / 4;
            }
            if (charHasArmour) {
                if (charHasShield) {
                    characterHealth = characterHealth - ((enemyHealth * (enemyAD / 4)) / 10);
                }
                else {
                    characterHealth = characterHealth - ((enemyHealth * (enemyAD / 2)) / 10);
                }
            }
            else {
                characterHealth = characterHealth - ((enemyHealth * enemyAD) / 10);
            }
            
            // Calculate if hydra will heal or not
            Random random = new Random();
            int HydraHealing = random.nextInt(2);

            // If andurill is in inventory, set healing to 0
            if (charHasAnduril) {
                HydraHealing = 0;
                // If enemy is a boss, character AD is tripled
                if (this.getType().equals("assassin") || this.getType().equals("hydra")) {
                    characterAD = characterAD * 3;
                }
            }
            // Calculations for if the enemy is a hydra and it heals 
            if (this.getType().equals("hydra") && HydraHealing == 1) {
                enemyHealth = hydraBattleHealing(enemyHealth, characterHealth, characterAD, weaponAtk, enemyArmour, charHasBow);
            }

            // Otherwise, calculate normally for an enemy, or hydra when it doesn't heal
            else {
                if (charHasMArmour) {
                    characterAD = characterAD + 6;
                }
                if (enemyArmour) {
                    enemyHealth = enemyHealth - ((characterHealth * ((characterAD + weaponAtk) / 2)) / 5);
                    if (charHasBow) {
                        enemyHealth = enemyHealth - ((characterHealth * ((characterAD + weaponAtk) / 2)) / 5);
                    }
                }
                else {
                    enemyHealth = enemyHealth - ((characterHealth * (characterAD + weaponAtk)) / 5);
                    if (charHasBow) {
                        enemyHealth = enemyHealth - ((characterHealth * ((characterAD + weaponAtk) / 2)) / 5);
                    }
                }
            }

            // Check if character dies
            if (!checkAlive(characterHealth)) {
                player.setAlive(false);
                player.setHealth(0);
            }
            else {
                player.setHealth(characterHealth);
            }
            // Check if enemy dies
            if (!checkAlive(enemyHealth)) {
               this.setAlive(false);
               this.setHealth(0);
            }
            else {
                this.setHealth(enemyHealth);
            }
            // If none are dead, repeat the round
        }
    }
    
    /**
     * Moving the enemy if the invincibility potion is active
     * @param entities
     * @param playerPosition
     */
    public void runEnemy (List<Entity> entities, Position playerPosition) {
        Position current = getPosition();

        // Get the adjacent positions around the enemy
        List<Position> adjacent = current.getAdjacentPositions();

        // Index 1, 3, 5 and 7 are adjacent position 
        List<Position> validPositions = new ArrayList<>();
        validPositions.add(adjacent.get(1));
        validPositions.add(adjacent.get(3));
        validPositions.add(adjacent.get(5));
        validPositions.add(adjacent.get(7));


        // Final placeholders for positions moved and longest distance
        double longestDistance = -99999999;
        Position destination = null; 

        // This looks through adjacent positions, checks whether the next square is movable
        // then checks for the longest distance between these squares
        for (Position position : validPositions) {
            if (checkMovement(position, entities) || getType().equals("spider")) {
                Position vector = Position.calculatePositionBetween(position, playerPosition);
                double distance = Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2));

                if (distance >= longestDistance) {
                    destination = position; 
                    longestDistance = distance;
                }
            }
        }   

        // Checks whether we need to move the enemy at all
        Position originalVector = Position.calculatePositionBetween(current, playerPosition);
        double originalDistance = Math.sqrt(Math.pow(originalVector.getX(), 2) + Math.pow(originalVector.getY(), 2));
   
        // Now moving the enemy. Also check the movement once more as spider can be on wall
        if (longestDistance > originalDistance) setPosition(destination);
    }

    public int hydraBattleHealing (int enemyHealth, int characterHealth, int characterAD, int weaponAtk, boolean enemyArmour, boolean charHasBow) {
        if (enemyArmour) {
            enemyHealth = enemyHealth + ((characterHealth * ((characterAD + weaponAtk) / 2)) / 5);
            if (charHasBow) {
                enemyHealth = enemyHealth + ((characterHealth * ((characterAD + weaponAtk) / 2)) / 5);
            }
        }
        else {
            enemyHealth = enemyHealth + ((characterHealth * (characterAD + weaponAtk)) / 5);
            if (charHasBow) {
                enemyHealth = enemyHealth + ((characterHealth * ((characterAD + weaponAtk) / 2)) / 5);
            }
        }

        return enemyHealth;
    }

    /**
     * Checks if the position to be moved in is a swamp tile, if it is, return that tile
     * @param position
     * @param entities
     * @return swamp tile
     */
    public SwampTile checkSwamp(Position position, List<Entity> entities) {
        SwampTile swampEntity =  null;
        for (Entity entity: entities) {
            if (entity.getPosition().equals(position) && entity.getType().equals("swamp_tile")) {
                swampEntity = (SwampTile) entity;
                return swampEntity;
            }
        }
        return swampEntity;
    }

    /**
     * Checks if the position to be moved in is a swamp tile, if it is, return that tile
     * @param movementDirection
     * @param entities
     * @return swamp tile
     */
    public SwampTile checkSwamp(Direction movementDirection, List<Entity> entities) {
        Position entityPosition = position.translateBy(movementDirection);

        SwampTile swampEntity = null;
        for (Entity entity: entities) {
            if (entity.getPosition().equals(entityPosition) && entity.getType().equals("swamp_tile")) {
                swampEntity = (SwampTile)entity;
                return swampEntity;
            }
        }
        return swampEntity;
    }

    /**
     * Get Friendly Mercenaries in the range of the entity
     * @param entities
     * @return List<Mercenary>
     */
    public List<Mercenary> getMercenary(List<Entity> entities) {
        List<Mercenary> mercenaryList = new ArrayList<Mercenary>();
        // Look for a friendly Mercenary
        for (Entity entityMercenary: entities) {
            if (entityMercenary.getType().equals("mercenary")) {
               if (((Mercenary) entityMercenary).getFriendly() == true && ((Mercenary) entityMercenary).mercenaryBattle(this)) {
                   mercenaryList.add((Mercenary) entityMercenary);
               }
            }
        }
        return mercenaryList;
    }

    /**
     * Get Friendly Assassins in the range of the entity
     * @param entities
     * @return List<Assassin>
     */
    public List<Assassin> getAssassin(List<Entity> entities) {
        List<Assassin> assassinList = new ArrayList<Assassin>();
        // Look for a friendly Mercenary
        for (Entity entityAssassin: entities) {
            if (entityAssassin.getType().equals("assassin")) {
               if (((Assassin) entityAssassin).getFriendly() == true && ((Assassin) entityAssassin).assassinBattle(this)) {
                   assassinList.add((Assassin) entityAssassin);
               }
            }
        }
        return assassinList;
    }
}
