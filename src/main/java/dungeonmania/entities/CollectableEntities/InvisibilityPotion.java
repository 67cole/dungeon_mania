package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity{
    /**
     * Creates the invisibility potion
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public InvisibilityPotion(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }   
}
