package dungeonmania.entities;

import dungeonmania.util.Position;

import java.util.List;

import dungeonmania.Dungeon;
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
    public Position getPosition() {
        return position;
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
     * Get type
     */
    public String getType() {
        return type;
    }

    /**
     * Get ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Get isInteractable
     */
    public boolean getIsInteractable() {
        return isInteractable;
    }

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

    public Entity checkNext(Direction direction, List<Entity> entities) {
        switch (direction) {
            case UP:
                Position attemptedMove = position.translateBy(0, -1);
            
                for (Entity entity : entities) {
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(attemptedMove)) {
                        return entity;
                    }
                }
                break;

            case DOWN:
                Position attemptedMove1 = position.translateBy(0, 1);

                for (Entity entity : entities) {
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(attemptedMove1)) {
                        return entity;
                    }
                }
                break;

            case LEFT:
                Position attemptedMove2 = position.translateBy(-1, 0);

                for (Entity entity : entities) {
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(attemptedMove2)) {
                        return entity;
                    }
                }
                break;

            case RIGHT:
                Position attemptedMove3 = position.translateBy(1, 0);

                for (Entity entity : entities) {
                    if (!entity.getType().equals("switch") && entity.getPosition().equals(attemptedMove3)) {
                        return entity;
                    }
                }
                break; 

            case NONE:
                return null;
        }

        // If it's a white square, you can move
        return null; 
    }   

    public boolean boulderBlocked(Direction direction, Position attemptedMove, List<Entity> entities, Entity entity) {

        if (entity.getType().equals("boulder") && entity.getPosition().equals(attemptedMove)) {
            
            StaticEntity main = (StaticEntity) entity;
            System.out.println("entered here");

            if (main.checkNext(direction, entities) == null) {
                return false;
            }

            if (main.checkNext(direction, entities).getType().equals("boulder") ||
                main.checkNext(direction, entities).getType().equals("wall")) {
                
                System.out.println("entered here2");
                // Next entity is a wall or boulder. Must block it.
                return true;
            }
        }


        return false;
    }

    public boolean checkBS(int characterHealth, int enemyHealth) {
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
        while (checkBS(player.getHealth(), this.getHealth())) {
            // Simulate a round of battle
            int characterHealth = player.getHealth();
            int characterAD = player.getAttack();
            int enemyHealth = this.getHealth();
            int enemyAD = this.getAttack();
            int newCharacterHealth = characterHealth - ((enemyHealth * enemyAD) / 10);
            int newEnemyHealth = enemyHealth - ((newCharacterHealth * characterAD) / 5);
            // Check if character dies
            if (!checkAlive(characterHealth)) {
                player.setAlive(false);
                player.setHealth(0);
            }
            else {
                player.setHealth(newCharacterHealth);
            }
            // Check if enemy dies
            if (!checkAlive(enemyHealth)) {
               this.setAlive(false);
               this.setHealth(0);
            }
            else {
                this.setHealth(newEnemyHealth);
            }
            // If none are dead, repeat the round
        }
    }
    


}
