package dungeonmania.entities;

import dungeonmania.util.*;

import java.util.List;
import java.util.Random;


public class ZombieToast extends MovingEntity {
    private final static int STARTING_HEALTH = 3;
    private final static int ATTACK = 1;

    /**
     * Creates the main zombie
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public ZombieToast(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        setHealth(STARTING_HEALTH);
        setAttack(ATTACK);
    }

    public void moveEntity(List<Entity> entities) {
        // For now, zombies travel randomely
        Random random = new Random();
        int randDirection = random.nextInt(5);

        switch(randDirection) {
            case 1:
                if (checkMovement(super.getPosition().translateBy(0, -1), entities)) {
                    super.moveUpward();
                    break;
                }
            
            case 2:
                if (checkMovement(super.getPosition().translateBy(0, 1), entities)) {
                    super.moveUpward();
                    break;
                }
            
            case 3:
                if (checkMovement(super.getPosition().translateBy(-1, 0), entities)) {
                    super.moveUpward();
                    break;
                }
            
            case 4:
                if (checkMovement(super.getPosition().translateBy(1, 0), entities)) {
                    super.moveUpward();
                    break;
                }
        }   
    }

}

