package dungeonmania.entities.CollectibleEntities;

import dungeonmania.entities.CollectibleEntity;
import dungeonmania.util.Position;

public class Treasure extends CollectibleEntity{
    public Treasure(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }   
}
