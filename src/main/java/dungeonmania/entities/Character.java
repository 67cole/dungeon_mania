package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.*;

public class Character extends MovingEntity {
    private final static int STARTING_HEALTH = 10;
    private final static int ATTACK = 2;

    private Position spawnPosition = null;
    private boolean invincible;
    private boolean invisible;

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
        this.invincible = false;
        this.invisible = false;
    }

    public Position getSpawn() {
        return spawnPosition;
    }
    public void setSpawn(Position playerSpawn) {
        spawnPosition = playerSpawn;
    }

    public boolean isInvincible() {
        return this.invincible;
    }

    public void setIsInvincible(boolean isInvincible) {
        this.invincible = isInvincible;
    }

    public boolean isInvisible() {
        return this.invisible;
    }

    public void setIsInvisible(boolean isInvisible) {
        this.invisible = isInvisible;
    }

    public void restoreHealth() {
        setHealth(STARTING_HEALTH);
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

    /**
     * returns the character
     * @param entities
     * @return Character
     */
    public static Character getCharacter(List <Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals("player")) {
                return (Character) entity; 
            }
        }

        return null;
    }


    /**
     * Helper Function that returns the players position 
     * @param entities
     * @return position
     */
    public static Position getPlayerPosition(List<Entity> entities) {
        for (Entity player: entities) {
            if (player.getType().equals("player")) {
                return player.getPosition();
            } 
        }

        return null;
    }
 
}
