package dungeonmania.entities.RareCollectableEntities;

import dungeonmania.entities.CollectableEntities.Sword;
import dungeonmania.util.Position;

public class Anduril extends Sword{
    /**
     * Creates the Anduril
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Anduril(Position position, String type, String ID, boolean IsInteractable) {
        super(position ,type, ID, IsInteractable);
        this.setAttack((this.getAttack() * 5) / 2);
    }
}
