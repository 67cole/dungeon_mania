package dungeonmania.entities.BuildableEntities;

import dungeonmania.entities.BuildableEntity;
import dungeonmania.util.Position;

public class Bow extends BuildableEntity{
     /**
     * Durability of bow
     */
    private int durability;

    /**
     * Creates the Bow
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Bow(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
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
     * Setter for durability
     * @param durability
     * @return void
     */
    public void setDurability(int durability) {
        this.durability = durability;
    }

    /**
     * Reduces durability by 1
     */
    public void reduceDurability() {
        this.durability -= 1;
    }
    /**
     * Checks durability of bow
     * @return Bow
     */
    public Bow checkDurability() {
        if (this.durability == 0) {
            return this;
        }
        return null;
    }
}
