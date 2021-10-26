package dungeonmania.entities;

import dungeonmania.util.Position;

public class Exit extends StaticEntity {

    public Exit(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }
}
