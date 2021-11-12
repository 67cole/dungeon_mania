package dungeonmania.entities.BuildableEntities;

import dungeonmania.entities.BuildableEntity;
import dungeonmania.util.Position;

public class MidnightArmour extends BuildableEntity{
    /**
     * Creates the MidnightArmour
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
     public MidnightArmour(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
    }
}
