package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.*;

public class Character extends MovingEntity {
    private final static int STARTING_HEALTH = 10;
    private final static int ATTACK = 2;
    private Position spawnPosition = null;

    /**
     * Health of character
     */
    private int health;

    /**
     * Attack of character
     */
    private int attack;

    /**
     * Condition of character
     */
    private boolean alive;

    /**
     * Creates the main character
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Character(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        this.health = STARTING_HEALTH;
        this.attack = ATTACK;
        this.alive = true;
    }


    public Position getSpawn() {
        return spawnPosition;
    }
    public void setSpawn(Position playerSpawn) {
        spawnPosition = playerSpawn;
    }

    /**
     * Getter for health
     */
    public int getHealth() {
        return health; 
    }

    /**
     * Getter for attack
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Getter for alive
     */
    public boolean getAliveStatus() {
        return alive;
    }

    /**
     * Setter for attack
     * If the health is less than 1, i.e. <= 0, the character is dead
     * @param health
     */
    public void setHealth(int health) {
        this.health = Math.min(health, STARTING_HEALTH);

        if (health < 1) {
            this.setAlive(false);
        }
    }

    /**
     * Setter for character's condition
     * @param alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Moving the entity
     */
    @Override
    public void moveEntity(Direction direction) {
        switch(direction) {
            case UP:
                super.moveUpward();
                break;

            case DOWN:
                super.moveDownward();
                break;
            
            case LEFT:
                super.moveLeft();
                break;
            
            case RIGHT:
                super.moveRight();
                break;
            
            case NONE:
                break;
        }   
    }
 
}
