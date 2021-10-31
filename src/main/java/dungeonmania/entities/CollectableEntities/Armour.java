package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity{

    /**
     * Durability of armour
     */
    private int durability;

    /**
     * Creates the armour
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Armour(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
        this.durability = 5; 
    }
    /**
     * Getter for durability
     * @return int
     */
    public int getDurability() {
        return this.durability;
    }
    /**
     * Reduces durability by 1
     */
    public void reduceDurability() {
        this.durability -= 1;
    }
    /**
     * Checks durability of armour
     * @return Armour
     */
    public Armour checkDurability() {
        if (this.durability == 0) {
            return this;
        }
        return null;
    }
}

