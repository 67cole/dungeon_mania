package dungeonmania.entities;

import dungeonmania.util.Position;

public class Door extends StaticEntity {
    public Door(Position position, String type, String ID, boolean isInteractable, String colour) {
        super(position,type, ID, isInteractable, colour);
    } 
}
