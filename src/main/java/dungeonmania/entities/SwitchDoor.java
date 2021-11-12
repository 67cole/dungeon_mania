package dungeonmania.entities;

import dungeonmania.util.Position;

public class SwitchDoor extends StaticEntity{

    private boolean locked;

    public SwitchDoor(Position position, String type, String ID, boolean isInteractable, boolean locked) {
        super(position,type, ID, isInteractable);
        this.locked = locked;
    }
}
