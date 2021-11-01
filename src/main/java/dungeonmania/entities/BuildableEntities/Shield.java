package dungeonmania.entities.BuildableEntities;

import dungeonmania.entities.BuildableEntity;
import dungeonmania.util.Position;

public class Shield extends BuildableEntity{
    /**
     * Durability of shield
     */
    private int durability;

    public Shield(Position position, String type, String ID, boolean isInteractable) {
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
     * Checks durability of shield
     * @return Shield
     */
    public Shield checkDurability() {
        if (this.durability == 0) {
            return this;
        }
        return null;
    }
}
