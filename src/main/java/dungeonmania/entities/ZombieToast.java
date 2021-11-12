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
        Random random = new Random();
        int chance = random.nextInt(6);
        if (chance == 3) {
            setArmour(true);
        }
    }

    /**
     * Moves the zombie around
     * @param entities - The list of all entities in the dungeon
     * @param direction - The direction of the character
     */
    public static void zombieMovement (List <Entity> entities, Direction direction) {
        Position player = Character.getPlayerPosition(entities);
        player = player.translateBy(direction);
        boolean swampMove = true;
        for (Entity entity : entities) {
            if (entity.getType().equals("zombie_toast")) {
                ZombieToast temp = (ZombieToast) entity;
                swampMove = SwampTile.swampCanMove(temp, entities);
                if (swampMove == false) {
                    continue;
                }
                temp.moveEntity(entities, player);
            }
        }
    }

    /**
     * Moves the zombie around
     * @param entities - The list of all entities in the dungeon
     * @param playerPosition - The position of the player where they are walking to
     */
    public void moveEntity(List<Entity> entities, Position playerPosition) {
        // For now, zombies travel randomely
        Random random = new Random();
        int randDirection = random.nextInt(4);

        if (playerPosition.equals(super.getPosition())) return;

        switch(randDirection) {
            case 0:
                if (checkMovement(super.getPosition().translateBy(0, -1), entities)) {
                    // If its netiher a wall nor a boulder, check if its a door
                    Door doorEntity = checkDoor(super.getPosition().translateBy(0,-1), entities); 
                    if (doorEntity != null) {
                        //If it is a door, check if its locked or not, if it isnt locked, move into it
                        if (doorEntity.getLocked() == true) {
                            break;
                        }
                    }
                    super.moveUpward();
                    break;
                }
            
            case 1:
                if (checkMovement(super.getPosition().translateBy(0, 1), entities)) {
                    // If its netiher a wall nor a boulder, check if its a door
                    Door doorEntity = checkDoor(super.getPosition().translateBy(0,1), entities); 
                    if (doorEntity != null) {
                        //If it is a door, check if its locked or not, if it isnt locked, move into it
                        if (doorEntity.getLocked() == true) {
                            break;
                        }
                    }
                    super.moveDownward();
                    break;
                }
            
            case 2:      
                if (checkMovement(super.getPosition().translateBy(-1, 0), entities)) {
                    // If its netiher a wall nor a boulder, check if its a door
                    Door doorEntity = checkDoor(super.getPosition().translateBy(-1, 0), entities); 
                    if (doorEntity != null) {
                        //If it is a door, check if its locked or not, if it isnt locked, move into it
                        if (doorEntity.getLocked() == true) {
                            break;
                        }
                    }
                    super.moveLeft();
                    break;
                }
            
            case 3:
                if (checkMovement(super.getPosition().translateBy(1, 0), entities)) {
                    // If its netiher a wall nor a boulder, check if its a door
                    Door doorEntity = checkDoor(super.getPosition().translateBy(1,0), entities); 
                    if (doorEntity != null) {
                        //If it is a door, check if its locked or not, if it isnt locked, move into it
                        if (doorEntity.getLocked() == true) {
                            break;
                        }
                    }
                    super.moveRight();
                    break;
                }
        }   
    }

}

