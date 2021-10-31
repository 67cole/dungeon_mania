package dungeonmania.entities;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.entities.CollectableEntities.Sword;
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

    public void moveSpider(Position position) {}

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
     * checkMovement checks for the next square if it's a wall/boulder.
     * This uses direction args.
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
     */
    public boolean checkMovement(Position position, List<Entity> entities) {
        
        for (Entity entity : entities) {
            if (entity.getPosition().equals(position) && !entity.getType().equals("door") && !entity.getType().equals("switch") && !entity.getType().equals("player")) {
                return false;
            // If the square contains a door, check if its locked or not
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
    /* 
    * checkMovement checks for the next square if it's a door. If the door is locked,
     * it should check for the specific key inside the characters inventory and open the door 
     * if the key matches the door. Returns true if the door is open and false if not
     */
    public boolean checkDoorLock(Door entityDoor, List<Entity> entities, Dungeon main) {

        // If the door is locked, look for the key inside the inventory. Unlock the door if its found
        if (entityDoor.getLocked() == true) {
            int keyType = entityDoor.getKeyType();
            int keyNum = 0;
            int remove = 0;
            CollectableEntity itemKey = null;

            for (CollectableEntity item : main.inventory) {
                if (item.getType().equals("key")) {
                    Key key = (Key) item;
                    keyNum =  key.getKeyNum();
                    //If the key and door match, open the door
                    if (keyType == keyNum) {
                        entityDoor.setLocked(false);
                        remove = 1;
                        itemKey = item;
                    }
                }
            }
            if (remove == 1) {
                main.inventory.remove(itemKey);
                main.setKeyStatus(true);
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    //Checks if the position to be moved in is a door, if it is, return that door
    public Door checkDoor(Direction movementDirection, List<Entity> entities) {
        Position entityPosition = position.translateBy(movementDirection);

        Door entityDoor = null;
        for (Entity entity: entities) {
            if (entity.getPosition().equals(entityPosition) && entity.getType().equals("door")) {
                entityDoor = (Door) entity;
                return entityDoor;
            }
        }
        return entityDoor;

    }

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

    public boolean checkBattleState(int characterHealth, int enemyHealth) {
        if (characterHealth <= 0) {
            return false;
        }
        if (enemyHealth <= 0) {
            return false;
        }
        return true;
    }

    public boolean checkAlive(int health) {
        if (health <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public void entityFunction(List<Entity> entities, Character player, Direction direction, Dungeon main) {
        while (checkBattleState(player.getHealth(), this.getHealth())) {
            // If character is invincible, set enemy dead, return.
            if (player.isInvincible()) {
                this.setAlive(false);
                return;
            }
            // If character is invisible, return.
            if (player.isInvisible()) {
                return;
            }
            // Simulate a round of battle
            int weaponAtk = 0;
            boolean charArmour = false;
            boolean enemyArmour = false;
            if (this.getArmour()) {
                enemyArmour = true;
            }
            Sword swordHolder = null;
            Armour armourHolder = null;
            for (CollectableEntity item: main.inventory) {
                if (item.getType().equals("sword")) {
                    Sword sword = (Sword) item;
                    weaponAtk = sword.getAttack();
                    sword.reduceDurability();
                    swordHolder = sword.checkDurability();
                    break;
                }
                if (item.getType().equals("armour")) {
                    Armour armour = (Armour) item;
                    charArmour = true;
                    armour.reduceDurability();
                    armourHolder = armour.checkDurability();
                    break;
                }
            }
            // Remove the sword if durability runs out
            if (swordHolder != null) {
                main.inventory.remove(swordHolder);
            }
            // Remove the armour if durability runs out
            if (armourHolder != null) {
                main.inventory.remove(armourHolder);
            }
            int characterHealth = player.getHealth();
            int characterAD = player.getAttack();
            int enemyHealth = this.getHealth();
            int enemyAD = this.getAttack();
            if (charArmour) {
                characterHealth = characterHealth - ((enemyHealth * (enemyAD / 2)) / 10);
            }
            else {
                characterHealth = characterHealth - ((enemyHealth * (enemyAD)) / 10);
            }
            if (enemyArmour) {
                enemyHealth = enemyHealth - ((characterHealth * ((characterAD + weaponAtk) / 2)) / 5);
            }
            else {
                enemyHealth = enemyHealth - ((characterHealth * (characterAD + weaponAtk)) / 5);
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
    


}
