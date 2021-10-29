package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.*;

public class Character extends MovingEntity {
    private final static int STARTING_HEALTH = 10;
    private final static int ATTACK = 2;
    private Position spawnPosition = null;

    /**
     * Creates the main character
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Character(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        setHealth(STARTING_HEALTH);
        setAttack(ATTACK);
    }

    public Position getSpawn() {
        return spawnPosition;
    }
    public void setSpawn(Position playerSpawn) {
        spawnPosition = playerSpawn;
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
