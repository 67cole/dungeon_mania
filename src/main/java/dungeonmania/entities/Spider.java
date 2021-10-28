package dungeonmania.entities;

import dungeonmania.util.*;

public class Spider extends MovingEntity {
    private final static int STARTING_HEALTH = 1;
    private final static int ATTACK = 1;

    /**
     * Health of spider
     */
    private int health;

    /**
     * Attack of spider
     */
    private int attack;

    /**
     * Condition of spider
     */
    private boolean alive;

    /**
     * Creates the main zombie
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Spider(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        this.health = STARTING_HEALTH;
        this.attack = ATTACK;
        this.alive = true;
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
     * If the health is less than 1, i.e. <= 0, the spider is dead
     * @param health
     */
    public void setHealth(int health) {
        this.health = Math.min(health, STARTING_HEALTH);

        if (health < 1) {
            this.setAlive(false);
        }
    }

    /**
     * Setter for spider's condition
     * @param alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Movement for the spider
     */
    @Override
    public void moveEntity(Direction direction) {
        
    }



}
