package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class Key extends CollectableEntity{
    /**
     * Key number of key
     */
    private int keyNum;

    /**
     * Creates the key
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     * @param keyNum - key number
     */
    public Key(Position position, String type, String ID, boolean IsInteractable, int keyNum) {
        super(position,type, ID, IsInteractable);
        this.keyNum = keyNum;
    }   

    /**
     * Setter for Key
     */
    public int getKeyNum() {
        return this.keyNum;
    }
}

