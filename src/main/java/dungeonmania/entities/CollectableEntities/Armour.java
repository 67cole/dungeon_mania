package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity{

    /**
     * Durability of armour
     */
    private int durability;

    public Armour(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
        this.durability = 5; 
    }

    public int getDurability() {
        return this.durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void reduceDurability() {
        this.durability -= 1;
    }
}

