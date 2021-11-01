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
    /**
     * Creates the sword
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Sword(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
        this.durability = 5;
        this.attack = ATTACK;
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
     * Getter for attack
     * @return int
     */
    public int getAttack() {
		return this.attack;
	}
    /**
     * Setter for attack
     * @param attack
     * @return void
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }
    /**
     * Checks durability of sword
     * @return Sword
     */
    public Sword checkDurability() {
        if (this.durability == 0) {
            return this;
        }
        return null;
    }
}
