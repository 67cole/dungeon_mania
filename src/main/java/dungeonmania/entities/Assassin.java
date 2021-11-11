package dungeonmania.entities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class Assassin extends MovingEntity {
    private final static int STARTING_HEALTH = 6;
    private final static int ATTACK = 6;

    /**
     * Creates the mercenary
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Assassin(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        setHealth(STARTING_HEALTH);
        setAttack(ATTACK);
    }

    
    /**
     * Moves the assassin around
     * @param entities - The list of all entities in the dungeon
     * @param direction - The direction of the character
     */
    public static void assassinMovement(List<Entity> entities, Direction direction) {
        Position player = Character.getPlayerPosition(entities);
        player = player.translateBy(direction);
        boolean swampMove = true;
        for (Entity entity : entities) {
            if (entity.getType().equals("assassin")) {
                Assassin temp = (Assassin) entity;
                swampMove = SwampTile.swampCanMove(temp, entities);
                if (swampMove == false) {
                    continue;
                }
                temp.moveEntity();
            }
        }
    }  
    
    
    /**
     * Moving the assassin, this should follow a djikstra's algorithm 
     */
    public void moveEntity () {
        
    }


}