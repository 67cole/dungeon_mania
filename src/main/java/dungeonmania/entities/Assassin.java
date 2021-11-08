package dungeonmania.entities;

import dungeonmania.util.Position;

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
     * Moving the assassin, this should follow a djikstra's algorithm 
     */
    public void moveEntity () {
        
    }


}