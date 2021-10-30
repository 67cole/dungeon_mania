package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class Treasure extends CollectableEntity{
    public Treasure(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }   
}
