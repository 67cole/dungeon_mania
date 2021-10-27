package dungeonmania.entities;

import dungeonmania.util.Position;

public class Switch extends StaticEntity {
    
    public Switch(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }
}
