package dungeonmania.entities;

import dungeonmania.util.Position;

public class Door extends StaticEntity {
    public Door(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    } 
}
