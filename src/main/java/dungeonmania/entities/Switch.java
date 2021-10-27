package dungeonmania.entities;

import dungeonmania.util.Position;

public class Switch extends StaticEntity {
    
    public Switch(Position position, String type, String ID, boolean isInteractable, String colour) {
        super(position,type, ID, isInteractable, colour);
    }
}
