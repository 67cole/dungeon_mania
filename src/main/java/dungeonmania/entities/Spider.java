package dungeonmania.entities;

import dungeonmania.util.*;

public class Spider extends MovingEntity {
    private final static int STARTING_HEALTH = 1;
    private final static int ATTACK = 1;

    /**
     * Creates the main zombie
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Spider(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        setHealth(STARTING_HEALTH);
        setAttack(ATTACK);
    }

    /**
     * Movement for the spider
     */
    @Override
    public void moveEntity(Direction direction) {
        
    }



}
