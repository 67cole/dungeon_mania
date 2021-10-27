package dungeonmania.entities;

import dungeonmania.util.*;

import java.util.Random;
import java.util.List;

public class ZombieToast extends MovingEntity {
    private final static int STARTING_HEALTH = 3;
    private final static int ATTACK = 1;

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
    public ZombieToast(Position position, String type, String ID, boolean isInteractable) {
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

    @Override
    public void moveEntity(Direction direction, List<Entity> entities) {

        // For now, zombies travel randomely
        Random random = new Random();
        int randDirection = random.nextInt(5);

        boolean check = super.checkMovement(direction, entities);

        switch(randDirection) {
            case 1:
                if (check) super.moveUpward();
                break;
            
            case 2:
                if (check) super.moveDownward();
                break;
            
            case 3:
                if (check) super.moveLeft();
                break;
            
            case 4:
                if (check) super.moveRight();
                break;
        }   
    }
}

