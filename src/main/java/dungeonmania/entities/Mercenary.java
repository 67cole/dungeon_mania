package dungeonmania.entities;

import dungeonmania.util.Position;

public class Mercenary extends MovingEntity {
    private final static int STARTING_HEALTH = 3;
    private final static int ATTACK = 2;

    /**
     * Health of Mercenary
     */
    private int health;

    /**
     * Attack of Mercenary
     */
    private int attack;

    /**
     * Condition of mercenary
     */
    private boolean alive;


    /**
     * Creates the mercenary
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Mercenary(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
    }
    
}
