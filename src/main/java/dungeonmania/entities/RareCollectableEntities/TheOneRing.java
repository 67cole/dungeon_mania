package dungeonmania.entities.RareCollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class TheOneRing extends CollectableEntity{
    /**
     * Creates the TheOneRing
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public TheOneRing(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }
}

