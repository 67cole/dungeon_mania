package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity{
    /**
     * Status of the bomb
     */
    private boolean activated;
    /**
     * Creates the bomb
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Bomb(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
        this.activated = false;
    }
    /**
     * Checks the status of the bomb
     * @return boolean
     */
    public boolean isActivated() {
        return this.activated;
    }
    /**
     * Sets the status of the bomb
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
