package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity{
    /**
     * Creates the invincibility potion
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public InvincibilityPotion(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }   
}