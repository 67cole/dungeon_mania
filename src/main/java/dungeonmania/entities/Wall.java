package dungeonmania.entities;

import dungeonmania.util.Position;

public class Wall extends StaticEntity {

    public Wall(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }   
}
