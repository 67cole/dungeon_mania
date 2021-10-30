package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity{
    private boolean activated;

    public Bomb(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
        this.activated = false;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
