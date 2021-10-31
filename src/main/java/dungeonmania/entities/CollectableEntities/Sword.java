package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity{
    public final static int ATTACK = 3;

    /**
     * Durability of sword
     */
    private int durability;

    /**
     * Attack of sword
     */
    private int attack;

    public Sword(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
        this.durability = 5;
        this.attack = ATTACK;
    }

    public int getDurability() {
        return this.durability;
    }

    public void reduceDurability() {
        this.durability -= 1;
    }

    public int getAttack() {
		return this.attack;
	}

    public Sword checkDurability() {
        if (this.durability == 0) {
            return this;
        }
        return null;
    }
}
